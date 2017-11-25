package com.icss.hr.emp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.hr.common.Pager;
import com.icss.hr.emp.dao.EmpMapper;
import com.icss.hr.emp.pojo.Emp;

/**
 * 员工service
 * @author DLETC
 *
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class EmpService {

	@Autowired
	private EmpMapper mapper;
	
	public void addEmp(Emp emp) {
		mapper.insert(emp);
	}
	
	public void updateEmp(Emp emp) {
		mapper.update(emp);
	}
	
	public void deleteEmp(Integer empId) {
		mapper.delete(empId);
	}
	
	@Transactional(readOnly=true)
	public Emp queryEmpById(Integer empId) {
		return mapper.queryById(empId);
	}
	
	@Transactional(readOnly=true)
	public List<Emp> queryEmpByPage(Pager pager) {
		return mapper.queryByPage(pager);
	}
	
	@Transactional(readOnly=true)
	public int getEmpCount() {
		return mapper.getCount();
	}
	
	@Transactional(readOnly=true)
	public Emp queryEmpByLoginName(String empLoginName) {
		return mapper.queryByLoginName(empLoginName);
	}
	
	/**
	 * 登录业务
	 * 返回1用户名不存在 2密码错误 3登录成功
	 */
	@Transactional(readOnly=true)
	public int checkLogin(String empLoginName,String empPwd) {
		
		Emp emp = mapper.queryByLoginName(empLoginName);
		
		if (emp == null) {
			return 1;
		}else if (!empPwd.equals(emp.getEmpPwd())) {
			return 2;
		}else{
			return 3;
		}
		
	}
	
	public void updateEmpPic(String empLoginName,String empPic) {
		mapper.updateEmpPic(empLoginName, empPic);
	}
	
	public void updateEmpPwd(Emp emp) {
		mapper.updatePwd(emp);
	}
	/**
	 * 条件分页
	 * @param deptId
	 * @param jobId
	 * @param empName
	 * @param pager
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Emp> queryEmpByCondition(Integer deptId,Integer jobId,String empName,Pager pager) {
		return mapper.queryByCondition(deptId, jobId, empName, pager);
	}
	
	/**
	 * 满足条件的总记录数
	 */
	@Transactional(readOnly=true)
	public int getEmpCountByCondition(Integer deptId,Integer jobId,String empName) {
		return mapper.getCountByCondition(deptId, jobId, empName);
	}
	
}
