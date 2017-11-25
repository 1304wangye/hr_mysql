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
 * 职务service
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
	private JedisPool jedisPool;// redis数据库连接池

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
	 * 带缓存功能
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Job> QueryJob() {

		// jackson转换对象
		ObjectMapper objectMapper = new ObjectMapper();

		// 先查询缓存
		try {
			// 获得redis连接
			Jedis jedis = jedisPool.getResource();

			// 返回职务数据集合的json字符串
			String jsonStr = jedis.get("job_list");

			// 如果没有缓存，直接抛出异常
			if (jsonStr == null || jsonStr.equals("")) {
				throw new RuntimeException("未找到job_list缓存数据");
			}

			// 转换json字符串为List<Job>集合
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Job.class);

			List<Job> list = objectMapper.readValue(jsonStr, javaType);

			// 关闭连接
			jedis.close();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 缓存没有命中，再查询物理数据库
		List<Job> list = mapper.query();

		// 查询的结果同步到缓存中
		try {

			// 获得redis连接
			Jedis jedis = jedisPool.getResource();

			// list转换为json字符串
			String jsonStr = objectMapper.writeValueAsString(list);

			// 设置键值对
			jedis.set("job_list", jsonStr);

			// 关闭连接
			jedis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 删除huanc
	 */
	private void deleteCache() {

		try {
			// 获得redis连接
			Jedis jedis = jedisPool.getResource();
			jedis.del("job_list");
			jedis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
