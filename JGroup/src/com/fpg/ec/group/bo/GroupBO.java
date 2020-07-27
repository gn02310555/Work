package com.fpg.ec.group.bo;

import com.fpg.ec.group.model.Dp;
import com.fpg.ec.group.model.Employee;
import com.fpg.ec.group.model.condition.DpCondition;
import com.fpg.ec.group.model.condition.EmployeeCondition;
import com.fpg.ec.group.model.list.DpList;
import com.fpg.ec.group.model.list.EmployeeList;

/**
 * Group Inner BO 介面
 * @author Jason
 */
public interface GroupBO {
   
   // ### Exceptions ###############################################	
   
   	/**
	 * 資料不存在Exception
	 */
	static class DataNotExistsException extends RuntimeException {
		private static final long serialVersionUID = -1363772139201021015L;

		DataNotExistsException(String msg) {
			super(msg);
		}
	}
	
	/**
	 * 資料狀態已變更Exception
	 */
	static class DataStsChangeException extends RuntimeException {
		private static final long serialVersionUID = -1363772139201021015L;

		DataStsChangeException(String msg) {
			super(msg);
		}
	}
 	
	/**
	 * 資料已存在Exception
	 */
	static class DataExistedException extends RuntimeException {
		private static final long serialVersionUID = 5866205776044027244L;
		
		DataExistedException(String msg) {
			super(msg);
		}
	}
	
	// ### BO Method #############################################
	
	  	
	//===================================================================
	// DpBO Method(findAObject, findList, save, remove) Start-Dp
	//===================================================================
   
	/**
	 * 取一Dp(Dp)
	 * @param String iXuid, int iInclude
	 * @return Dp
	 */
	public Dp findADp(final String iXuid, final int iInclude);
	
	/**
	 * 取得Dp清單筆數(Dp)
	 * @param DpCondition iDpCondition
	 * @return int
	 */
	public int findDpListCount (final DpCondition iDpCondition);
	
	/**
	 * 取得Dp清單筆數，判斷是否有資料存在(Dp)
	 * @param DpCondition iDpCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findDpListCountHasRec (final DpCondition iDpCondition);
	
	/**
	 * 取Dp清單(Dp)
	 * @param String iStrStartCount, String iStrEndCount, <ObjectName>>Condition iDpCondition
	 * @return DpList
	 */
	public DpList findDpList (final String iStrStartCount, final String iStrEndCount, final DpCondition iDpCondition);
	
    /**
	 * 儲存Dp(Dp)
	 * @param Dp iDp  	
	 */
	public void saveADp(final Dp iDp);
	
	/**
	 * 儲存Dp清單(Dp)
	 * @param Dp iDp  	
	 */
	public void saveDpList(final DpList iDpList);
	
	/**
	 * 刪除Dp(Dp)
	 * @param String iXuid
	 */
	public void removeADp(final String iXuid);
 
 	/**
	 * 刪除Dp清單(Dp)
	 * @param DpList iDpList
	 */
	public void removeDpList(final DpList iDpList);

	/**
	 * 刪除Dp(Dp)BySelective
	 * @param Dp iDp
	 */
	public void removeDpBySelective(final Dp iDp);

	//===================================================================
	// DpBO Method(findAObject, findList, save, remove) End-Dp 
	//===================================================================
	
	
  	
	//===================================================================
	// EmployeeBO Method(findAObject, findList, save, remove) Start-Employee
	//===================================================================
   
	/**
	 * 取一Employee(Employee)
	 * @param String iXuid, int iInclude
	 * @return Employee
	 */
	public Employee findAEmployee(final String iXuid, final int iInclude);
	
	/**
	 * 取得Employee清單筆數(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return int
	 */
	public int findEmployeeListCount (final EmployeeCondition iEmployeeCondition);
	
	/**
	 * 取得Employee清單筆數，判斷是否有資料存在(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findEmployeeListCountHasRec (final EmployeeCondition iEmployeeCondition);
	
	/**
	 * 取Employee清單(Employee)
	 * @param String iStrStartCount, String iStrEndCount, <ObjectName>>Condition iEmployeeCondition
	 * @return EmployeeList
	 */
	public EmployeeList findEmployeeList (final String iStrStartCount, final String iStrEndCount, final EmployeeCondition iEmployeeCondition);
	
    /**
	 * 儲存Employee(Employee)
	 * @param Employee iEmployee  	
	 */
	public void saveAEmployee(final Employee iEmployee);
	
	/**
	 * 儲存Employee清單(Employee)
	 * @param Employee iEmployee  	
	 */
	public void saveEmployeeList(final EmployeeList iEmployeeList);
	
	/**
	 * 刪除Employee(Employee)
	 * @param String iXuid
	 */
	public void removeAEmployee(final String iXuid);
 
 	/**
	 * 刪除Employee清單(Employee)
	 * @param EmployeeList iEmployeeList
	 */
	public void removeEmployeeList(final EmployeeList iEmployeeList);

	/**
	 * 刪除Employee(Employee)BySelective
	 * @param Employee iEmployee
	 */
	public void removeEmployeeBySelective(final Employee iEmployee);

	//===================================================================
	// EmployeeBO Method(findAObject, findList, save, remove) End-Employee 
	//===================================================================
	
	

	
	// ### Sequence ##############################################
	
    /**
	 * 取得Group模組序號
	 */
	public String findGroupModelSeqNum();
	    
}
