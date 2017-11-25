package com.icss.hr.job.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icss.hr.job.dao.JobMapper;
import com.icss.hr.job.pojo.Job;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * ְ��service
 * 
 * @author DLETC
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {

	@Autowired
	private JobMapper mapper;

	@Autowired
	private JedisPool jedisPool;// redis���ݿ����ӳ�

	public void addJob(Job job) {
		mapper.insert(job);
		deleteCache();
	}

	public void updateJob(Job job) {
		mapper.update(job);
		deleteCache();
	}

	public void deleteJob(Integer jobId) {
		mapper.delete(jobId);
		deleteCache();
	}

	@Transactional(readOnly = true)
	public Job QueryJobById(Integer jobId) {
		return mapper.queryById(jobId);
	}

	/**
	 * �����湦��
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Job> QueryJob() {

		// jacksonת������
		ObjectMapper objectMapper = new ObjectMapper();

		// �Ȳ�ѯ����
		try {
			// ���redis����
			Jedis jedis = jedisPool.getResource();

			// ����ְ�����ݼ��ϵ�json�ַ���
			String jsonStr = jedis.get("job_list");

			// ���û�л��棬ֱ���׳��쳣
			if (jsonStr == null || jsonStr.equals("")) {
				throw new RuntimeException("δ�ҵ�job_list��������");
			}

			// ת��json�ַ���ΪList<Job>����
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Job.class);

			List<Job> list = objectMapper.readValue(jsonStr, javaType);

			// �ر�����
			jedis.close();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// ����û�����У��ٲ�ѯ�������ݿ�
		List<Job> list = mapper.query();

		// ��ѯ�Ľ��ͬ����������
		try {

			// ���redis����
			Jedis jedis = jedisPool.getResource();

			// listת��Ϊjson�ַ���
			String jsonStr = objectMapper.writeValueAsString(list);

			// ���ü�ֵ��
			jedis.set("job_list", jsonStr);

			// �ر�����
			jedis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * ɾ��huanc
	 */
	private void deleteCache() {

		try {
			// ���redis����
			Jedis jedis = jedisPool.getResource();
			jedis.del("job_list");
			jedis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
