package com.icss.test;

import java.sql.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icss.hr.common.Pager;
import com.icss.hr.dept.pojo.Dept;
import com.icss.hr.emp.pojo.Emp;
import com.icss.hr.emp.service.EmpService;
import com.icss.hr.job.pojo.Job;

/**
 * 测试员工service
 * 
 * @author DLETC
 *
 */
public class TestEmpService {

	private ApplicationContext context;
	private EmpService service;

	// @Before方法在任何@Test方法之前自动执行
	@Before
	public void init() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		service = (EmpService) context.getBean("empService");
	}

	@Test
	public void testUpdate() {

		Dept dept = new Dept();
		dept.setDeptId(3);

		Job job = new Job();
		job.setJobId(3);

		Emp emp = new Emp(2, "jack222", "jack222", "123456", "tom@163.com", "13866665678", 7999.0,
				Date.valueOf("2017-11-20"), dept, job, "擅长Java oracle MySQL web前端");

		service.updateEmp(emp);
	}

	@Test
	public void testDelete() {
		service.deleteEmp(2);
	}

	@Test
	public void testQueryById() {

		Emp emp = service.queryEmpById(3);
		System.out.println(emp);

	}

	@Test
	public void testQueryByPage() {

		Pager pager = new Pager(service.getEmpCount(), 7, 4);
		
		List<Emp> list = service.queryEmpByPage(pager);
		
		for (Emp emp : list) {
			System.out.println(emp);
		}

	}
	
	@Test
	public void testQueryByLoginName() {

		Emp emp = service.queryEmpByLoginName("tom");
		System.out.println(emp);

	}
	
	@Test
	public void testCheckLogin() {

		int result = service.checkLogin("tom", "123456");
		System.out.println("result="+result);

	}
	
	@Test
	public void testUpdateEmpPic() {
		service.updateEmpPic("tom", null);
	}
	
	@Test
	public void testUpdatePwd() {

		Emp emp = new Emp();
		emp.setEmpLoginName("tom");
		emp.setEmpPwd("123456");
		service.updateEmpPwd(emp);

	}
	
	@Test
	public void testQueryByCondition() {
		
		Integer deptId = 1;
		Integer jobId = 7;
		String empName = "";
		
		int recordCount = service.getEmpCountByCondition(deptId, jobId, empName);
		System.out.println("满足条件的总记录数："+recordCount);
		
		Pager pager = new Pager(recordCount, 5, 1);
		
		List<Emp> list = service.queryEmpByCondition(deptId, jobId, empName, pager);
		
		for (Emp emp : list) {
			System.out.println(emp);
		}

	}
	
}
