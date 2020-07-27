package com.fpg.ec.group.dao;

import java.util.List;

import com.fpg.ec.group.model.list.*;
import com.fpg.ec.group.model.condition.*;

/**
 * GroupUtilDAO 介面
 * @author Jason
 */
public interface GroupUtilDAO {

	// ### Sequence Method  ##################################################

    /**
	 * 取得Group模組序號
	 */
    public String selectSequenceNumber();
    
    // ### Search Method  ##################################################
    
    
	//===================================================================
	// DpDAO Search Method Start-Dp
	//===================================================================
	/**
	 * 取得Dp清單筆數(Dp)
	 * @param DpCondition iDpCondition
	 * @return int
	 */
	public int selectDpListCount (DpCondition iDpCondition);
	
	
	/**
	 * 取Dp清單(Dp)
	 * @param DpCondition iDpCondition
	 * @return DpList
	 */
	public List selectDpList (DpCondition iDpCondition);

	//===================================================================
	// DpDAO Search Method End-Dp
	//===================================================================
	
	//===================================================================
	// EmployeeDAO Search Method Start-Employee
	//===================================================================
	/**
	 * 取得Employee清單筆數(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return int
	 */
	public int selectEmployeeListCount (EmployeeCondition iEmployeeCondition);
	
	
	/**
	 * 取Employee清單(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return EmployeeList
	 */
	public List selectEmployeeList (EmployeeCondition iEmployeeCondition);

	//===================================================================
	// EmployeeDAO Search Method End-Employee
	//===================================================================
	

}
