package com.icss.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icss.hr.job.pojo.Job;
import com.icss.hr.job.service.JobService;

/**
 * 测试职务service
 * 
 * @author DLETC
 *
 */
public class TestJobService {

	private ApplicationContext context;
	private JobService service;

	// @Before方法在任何@Test方法之前自动执行
	@Before
	public void init() {
		context = new ClassPathXmlApplicationContext("application*.xml");
		service = (JobService) context.getBean("jobService");
	}

	@Test
	public void testInsert() {
		Job job = new Job("职员", 3999, 7999);
		service.addJob(job);
	}

	@Test
	public void testUpdate() {
		Job job = new Job(1,"科员", 4999, 8999);
		service.updateJob(job);
	}

	@Test
	public void testDelete() {
		service.deleteJob(6);
	}

	@Test
	public void testQueryById() {
		Job job = service.QueryJobById(36);
		System.out.println(job);
	}

	@Test
	public void testQuery() {
		List<Job> list = service.QueryJob();
		
		for (Job job : list) {
			System.out.println(job);
		}
	}
	
	@Test
	public void testJackson() throws JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Job job =new Job(10,"开发部",3999,9999);
		
		String jsonStr = objectMapper.writeValueAsString(job);
				
		System.out.println(jsonStr);
	}

}
