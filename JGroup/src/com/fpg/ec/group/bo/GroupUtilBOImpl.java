package com.fpg.ec.group.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpg.ec.group.dao.GroupUtilDAO;
import com.fpg.ec.group.model.Dp;
import com.fpg.ec.group.model.Employee;
import com.fpg.ec.group.model.condition.DpCondition;
import com.fpg.ec.group.model.condition.EmployeeCondition;
import com.fpg.ec.group.model.list.DpList;
import com.fpg.ec.group.model.list.EmployeeList;
import com.fpg.ec.utility.StringUtil;
import com.fpg.ec.utility.Ystd;

/**
 * Group UtilBO實作物件
 * @author Jason
 */
@Service 
public class GroupUtilBOImpl implements GroupUtilBO {
	
	// ### 宣告資料  ##################################################
	
	StringUtil strUtil = new StringUtil();
	
    // BO Autowired
	@Autowired
	private GroupBO groupBO;
	
	// include data index
	private static int INCLUDE_NONE = 0;
	private static int INCLUDE_ALL = 1023; 
    
    // ### BO Method #############################################
    
        
    
	//===================================================================
	// DpUtilBO Method(findAObject, findList, save, remove) Start-Dp
	//===================================================================
   	
	/**
	 * 取一Dp主檔(Dp)
	 * @param String iXuid
	 * @return Dp
	 */
	public Dp findADpOnlyMaster(final String iXuid) {
				
		return groupBO.findADp(iXuid, GroupBOImpl.INCLUDE_NONE);
	}
	
	/**
	 * 取一Dp主檔含明細(Dp)
	 * @param String iXuid
	 * @return Dp
	 */
	public Dp findADpWithDetail(final String iXuid) {
				
		return groupBO.findADp(iXuid, GroupBOImpl.INCLUDE_ALL);
	}
		
	/**
	 * 取得Dp清單筆數(Dp)
	 * @param DpCondition iDpCondition
	 * @return int
	 */
	public int findDpListCount (final DpCondition iDpCondition){
		DpCondition searchDpCondition = iDpCondition;
		
		return groupBO.findDpListCount(searchDpCondition);
	}
	
	/**
	 * 取得Dp清單筆數，判斷是否有資料存在(Dp)
	 * @param DpCondition iDpCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findDpListCountHasRec (final DpCondition iDpCondition){
		DpCondition searchDpCondition = iDpCondition;
		System.out.println("groupBO:"+groupBO);
		return groupBO.findDpListCountHasRec(searchDpCondition);
	}
	
	/**
	 * 取Dp清單(Dp)
	 * @param String iStrStartCount, String iStrEndCount, DpCondition iDpCondition
	 * @return DpList
	 */
	public DpList findDpList (final String iStrStartCount, final String iStrEndCount, final DpCondition iDpCondition){
		DpCondition searchDpCondition = iDpCondition;

		DpList theDpList = groupBO.findDpList(iStrStartCount, iStrEndCount, searchDpCondition);
		
		if (theDpList == null || theDpList.size() == 0) {
			return new DpList();
		}else{
			return theDpList;
		}
	}
		
    /**
	 * 儲存Dp(Dp)
	 * @param Dp iDp, Accont iMgr 	
	 */
	public void saveADp(final Dp iDp){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.saveADp(iDp);
	}
	
	/**
	 * 儲存Dp清單(Dp)
	 * @param DpList iDpList, Accont iMgr  	
	 */
	public void saveDpList(final DpList iDpList){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Dp theDp = null;
		for(int i = 0; i < iDpList.size(); i++){
			theDp = iDpList.getDp(i);
			
			groupBO.saveADp(theDp);
		}
	}

	/**
	 * 刪除Dp(Dp)
	 * @param String iXuid
	 */
	public void removeADp(final String iXuid){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.removeADp(iXuid);
	}
	
	/**
	 * 刪除Dp清單(Dp)
	 * @param DpList iDpList
	 */
	public void removeDpList(final DpList iDpList){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Dp theDp = null;
		for(int i = 0; i < iDpList.size(); i++){
			theDp = iDpList.getDp(i);
			
			groupBO.removeADp(theDp.getXuid());
		}
	}
	
	/**
	 * 刪除Dp(Dp)BySelective
	 * @param Dp iDp
	 */
	public void removeDpBySelective(final Dp iDp){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.removeDpBySelective(iDp);
	}

	/**
	 * 更新Dp(Dp)
	 * @param Dp iDp, String updLimitField
	 */
	public void updateADp(final Dp iDp, final String updLimitField){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Dp updDp = iDp;
		updDp.setLimitAccessibleFields(updLimitField);
		groupBO.saveADp(updDp);
		updDp.clearLimitAccessibleFields();
	}
	
	/**
	 * 更新Dp清單(Dp)
	 * @param DpList iDpList, String updLimitField
	 */
	public void updateDpList(final DpList iDpList, final String updLimitField){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		Dp updDp = null;
		for(int i = 0; i < iDpList.size(); i++){
			updDp = iDpList.getDp(i);
			
			updDp.setLimitAccessibleFields(updLimitField);
			groupBO.saveADp(updDp);
			updDp.clearLimitAccessibleFields();
		}
	}
	
	//===================================================================
	// DpUtilBO Method(findAObject, findList, save, remove) End-Dp 
	//===================================================================
	
    
    
	//===================================================================
	// EmployeeUtilBO Method(findAObject, findList, save, remove) Start-Employee
	//===================================================================
   	
	/**
	 * 取一Employee主檔(Employee)
	 * @param String iXuid
	 * @return Employee
	 */
	public Employee findAEmployeeOnlyMaster(final String iXuid) {
				
		return groupBO.findAEmployee(iXuid, GroupBOImpl.INCLUDE_NONE);
	}
	
	/**
	 * 取一Employee主檔含明細(Employee)
	 * @param String iXuid
	 * @return Employee
	 */
	public Employee findAEmployeeWithDetail(final String iXuid) {
				
		return groupBO.findAEmployee(iXuid, GroupBOImpl.INCLUDE_ALL);
	}
		
	/**
	 * 取得Employee清單筆數(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return int
	 */
	public int findEmployeeListCount (final EmployeeCondition iEmployeeCondition){
		EmployeeCondition searchEmployeeCondition = iEmployeeCondition;
		
		return groupBO.findEmployeeListCount(searchEmployeeCondition);
	}
	
	/**
	 * 取得Employee清單筆數，判斷是否有資料存在(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findEmployeeListCountHasRec (final EmployeeCondition iEmployeeCondition){
		EmployeeCondition searchEmployeeCondition = iEmployeeCondition;
		
		return groupBO.findEmployeeListCountHasRec(searchEmployeeCondition);
	}
	
	/**
	 * 取Employee清單(Employee)
	 * @param String iStrStartCount, String iStrEndCount, EmployeeCondition iEmployeeCondition
	 * @return EmployeeList
	 */
	public EmployeeList findEmployeeList (final String iStrStartCount, final String iStrEndCount, final EmployeeCondition iEmployeeCondition){
		EmployeeCondition searchEmployeeCondition = iEmployeeCondition;

		EmployeeList theEmployeeList = groupBO.findEmployeeList(iStrStartCount, iStrEndCount, searchEmployeeCondition);
		
		if (theEmployeeList == null || theEmployeeList.size() == 0) {
			return new EmployeeList();
		}else{
			return theEmployeeList;
		}
	}
		
    /**
	 * 儲存Employee(Employee)
	 * @param Employee iEmployee, Accont iMgr 	
	 */
	public void saveAEmployee(final Employee iEmployee){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.saveAEmployee(iEmployee);
	}
	
	/**
	 * 儲存Employee清單(Employee)
	 * @param EmployeeList iEmployeeList, Accont iMgr  	
	 */
	public void saveEmployeeList(final EmployeeList iEmployeeList){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Employee theEmployee = null;
		for(int i = 0; i < iEmployeeList.size(); i++){
			theEmployee = iEmployeeList.getEmployee(i);
			
			groupBO.saveAEmployee(theEmployee);
		}
	}

	/**
	 * 刪除Employee(Employee)
	 * @param String iXuid
	 */
	public void removeAEmployee(final String iXuid){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.removeAEmployee(iXuid);
	}
	
	/**
	 * 刪除Employee清單(Employee)
	 * @param EmployeeList iEmployeeList
	 */
	public void removeEmployeeList(final EmployeeList iEmployeeList){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Employee theEmployee = null;
		for(int i = 0; i < iEmployeeList.size(); i++){
			theEmployee = iEmployeeList.getEmployee(i);
			
			groupBO.removeAEmployee(theEmployee.getXuid());
		}
	}
	
	/**
	 * 刪除Employee(Employee)BySelective
	 * @param Employee iEmployee
	 */
	public void removeEmployeeBySelective(final Employee iEmployee){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		groupBO.removeEmployeeBySelective(iEmployee);
	}

	/**
	 * 更新Employee(Employee)
	 * @param Employee iEmployee, String updLimitField
	 */
	public void updateAEmployee(final Employee iEmployee, final String updLimitField){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		
		Employee updEmployee = iEmployee;
		updEmployee.setLimitAccessibleFields(updLimitField);
		groupBO.saveAEmployee(updEmployee);
		updEmployee.clearLimitAccessibleFields();
	}
	
	/**
	 * 更新Employee清單(Employee)
	 * @param EmployeeList iEmployeeList, String updLimitField
	 */
	public void updateEmployeeList(final EmployeeList iEmployeeList, final String updLimitField){
		String strNowDat = new Ystd().udate();
		String strNowTime = new Ystd().utime();
		Employee updEmployee = null;
		for(int i = 0; i < iEmployeeList.size(); i++){
			updEmployee = iEmployeeList.getEmployee(i);
			
			updEmployee.setLimitAccessibleFields(updLimitField);
			groupBO.saveAEmployee(updEmployee);
			updEmployee.clearLimitAccessibleFields();
		}
	}
	
	//===================================================================
	// EmployeeUtilBO Method(findAObject, findList, save, remove) End-Employee 
	//===================================================================
	

	
	// ### 自訂 Method #############################################
	
	
	// ### Private #############################################
	
	
}
