package com.fpg.ec.group.dao;

import java.util.List;

import com.fpg.ec.group.model.Employee;

/**
 * EmployeeDAO 介面(Employee)
 * @author Jason
 */
public interface EmployeeDAO {

	// ### Base Method  ##################################################
	
    public void insert(Employee key);

    public void insertSelective(Employee key);

    public int deleteBySelective(Employee key);

    public List selectListBySelective(Employee key);

    
    public Employee selectByPrimaryKey(String xuid);
   
 	public int updateByPrimaryKey(Employee key);
 
    public int updateByPrimaryKeySelective(Employee key);
    
    public int deleteByPrimaryKey(String xuid);
      
}
