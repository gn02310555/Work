package com.fpg.ec.group.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpg.ec.group.dao.DpDAO;
import com.fpg.ec.group.dao.EmployeeDAO;
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
 * Group Inner BO實作物件
 * @author Jason
 */
@Service 
public class GroupBOImpl implements GroupBO {
	
	// ### 宣告資料  ##################################################
	StringUtil strUtil = new StringUtil();
	
	// DAO Autowired
	@Autowired
	private GroupUtilDAO groupUtilDAO;
    
    @Autowired
    private DpDAO dpDAO;

    @Autowired
    private EmployeeDAO employeeDAO;


    // BO
	
	// include data index
	public static int INCLUDE_NONE = 0;
	public static int INCLUDE_ALL = 1023;

    
    // ### BO Method #############################################
    
        
    
	//===================================================================
	// DpBO Method(findAObject, findList, save, remove) Start-Dp
	//===================================================================
   	
	/**
	 * 取一Dp(Dp)
	 * @param String iXuid, int iInclude
	 * @return Dp
	 */
	public Dp findADp(final String iXuid, final int iInclude) {
				
		return getDp(iXuid, iInclude);
	}
		
	/**
	 * 取得Dp清單筆數(Dp)
	 * @param DpCondition iDpCondition
	 * @return int
	 */
	public int findDpListCount (final DpCondition iDpCondition){
		DpCondition key = iDpCondition;
		if (key == null) {
			key = new DpCondition();
		}
		int intCount = groupUtilDAO.selectDpListCount(key);
		
		return Integer.valueOf(intCount);
	}
	
	/**
	 * 取得Dp清單筆數，判斷是否有資料存在(Dp)
	 * @param DpCondition iDpCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findDpListCountHasRec (final DpCondition iDpCondition){
		DpCondition key = iDpCondition;
		if (key == null) {
			key = new DpCondition();
		}
		int intCount = groupUtilDAO.selectDpListCount(key);
		
		if(intCount > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 取Dp清單(Dp)
	 * @param String iStrStartCount, String iStrEndCount, DpCondition iDpCondition
	 * @return DpList
	 */
	public DpList findDpList (final String iStrStartCount, final String iStrEndCount, final DpCondition iDpCondition){
		DpCondition key = iDpCondition;
        if(key == null){
            key = new DpCondition();
        }
        key.setStartCount(iStrStartCount);
        key.setEndCount(iStrEndCount);

        List list = groupUtilDAO.selectDpList(key);
        DpList objReturn = new DpList();
        objReturn.addAll(list);
        
		return objReturn;
	}
		
    /**
	 * 儲存Dp(Dp)
	 * @param Dp iDp  	
	 */
	public void saveADp(final Dp iDp){
	
		storeDp(iDp);
	}
	
	/**
	 * 儲存Dp清單(Dp)
	 * @param DpList iDpList  	
	 */
	public void saveDpList(final DpList iDpList){
		Dp theDp = null;
		for(int i = 0; i < iDpList.size(); i++){
			theDp = iDpList.getDp(i);
			storeDp(theDp);
		}
	}

	/**
	 * 刪除Dp(Dp)
	 * @param String iXuid
	 */
	public void removeADp(final String iXuid){
	    Dp objInDB = dpDAO.selectByPrimaryKey(iXuid);
		if (objInDB == null) {
			throw new DataNotExistsException("資料不存在，刪除失敗!(Xuid:" + iXuid + ")");
		}else{
			dpDAO.deleteByPrimaryKey(iXuid);
		}	
	}
	
	/**
	 * 刪除Dp清單(Dp)
	 * @param DpList iDpList
	 */
	public void removeDpList(final DpList iDpList){
		Dp theDp = null;
		for(int i = 0; i < iDpList.size(); i++){
			theDp = iDpList.getDp(i);
			removeADp(theDp.getXuid());
		}
	}
	
	/**
	 * 刪除Dp(Dp)BySelective
	 * @param Dp iDp
	 */
	public void removeDpBySelective(final Dp iDp){
		dpDAO.deleteBySelective(iDp);
	}

	// ############# Private ###################################################
		
	/**
	 * 取一Dp(含明細)(Dp)
	 * @param String iXuid, int iInclude
	 * @return Dp
	 */
	private Dp getDp(final String iXuid, final int iInclude) {
		Dp objReturn = dpDAO.selectByPrimaryKey(iXuid);

		if (objReturn == null || objReturn.getXuid().length() == 0) {
			return null;
		}

		// Include Mehod
		
		
		return objReturn;
	}
   
    /**
	 * 儲存Dp(Dp)
	 * @param Dp iDp
	 */
    private void storeDp(final Dp iDp) {
		String strNow = new Ystd().utime();
		
		Dp objInDB = dpDAO.selectByPrimaryKey(iDp.getXuid());
			
		if (objInDB == null || objInDB.getXuid().length() == 0) {
			//新增
			int retryCount = 0;
			boolean retry = false;
			do {
				retry = false;
				try {
					iDp.setXuid(StringUtil.genXUID() + "_" + findGroupModelSeqNum());
					iDp.setReturnNullNoThisField(true);
					dpDAO.insertSelective(iDp);
					iDp.setReturnNullNoThisField(false);
				} catch (Exception e) {
					e.printStackTrace();
					if (retryCount < 3) {
						retry = true;
						retryCount++;
						try {
							Thread.sleep(10);
							// 休息0.01 sec,再試一次
						} catch (Exception e3) {
						}
					} else {
						throw new RuntimeException(e);
					}
				}
			} while (retry);
		} else {
			//更新
			iDp.setReturnNullNoThisField(true);
			dpDAO.updateByPrimaryKeySelective(iDp);
			iDp.setReturnNullNoThisField(false);
		}
	}

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
	public Employee findAEmployee(final String iXuid, final int iInclude) {
				
		return getEmployee(iXuid, iInclude);
	}
		
	/**
	 * 取得Employee清單筆數(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return int
	 */
	public int findEmployeeListCount (final EmployeeCondition iEmployeeCondition){
		EmployeeCondition key = iEmployeeCondition;
		if (key == null) {
			key = new EmployeeCondition();
		}
		int intCount = groupUtilDAO.selectEmployeeListCount(key);
		
		return Integer.valueOf(intCount);
	}
	
	/**
	 * 取得Employee清單筆數，判斷是否有資料存在(Employee)
	 * @param EmployeeCondition iEmployeeCondition
	 * @return boolean(true:有存在資料, false:未存在資料)
	 */
	public boolean findEmployeeListCountHasRec (final EmployeeCondition iEmployeeCondition){
		EmployeeCondition key = iEmployeeCondition;
		if (key == null) {
			key = new EmployeeCondition();
		}
		int intCount = groupUtilDAO.selectEmployeeListCount(key);
		
		if(intCount > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 取Employee清單(Employee)
	 * @param String iStrStartCount, String iStrEndCount, EmployeeCondition iEmployeeCondition
	 * @return EmployeeList
	 */
	public EmployeeList findEmployeeList (final String iStrStartCount, final String iStrEndCount, final EmployeeCondition iEmployeeCondition){
		EmployeeCondition key = iEmployeeCondition;
        if(key == null){
            key = new EmployeeCondition();
        }
        key.setStartCount(iStrStartCount);
        key.setEndCount(iStrEndCount);

        List list = groupUtilDAO.selectEmployeeList(key);
        EmployeeList objReturn = new EmployeeList();
        objReturn.addAll(list);
        
		return objReturn;
	}
		
    /**
	 * 儲存Employee(Employee)
	 * @param Employee iEmployee  	
	 */
	public void saveAEmployee(final Employee iEmployee){
	
		storeEmployee(iEmployee);
	}
	
	/**
	 * 儲存Employee清單(Employee)
	 * @param EmployeeList iEmployeeList  	
	 */
	public void saveEmployeeList(final EmployeeList iEmployeeList){
		Employee theEmployee = null;
		for(int i = 0; i < iEmployeeList.size(); i++){
			theEmployee = iEmployeeList.getEmployee(i);
			storeEmployee(theEmployee);
		}
	}

	/**
	 * 刪除Employee(Employee)
	 * @param String iXuid
	 */
	public void removeAEmployee(final String iXuid){
	    Employee objInDB = employeeDAO.selectByPrimaryKey(iXuid);
		if (objInDB == null) {
			throw new DataNotExistsException("資料不存在，刪除失敗!(Xuid:" + iXuid + ")");
		}else{
			employeeDAO.deleteByPrimaryKey(iXuid);
		}	
	}
	
	/**
	 * 刪除Employee清單(Employee)
	 * @param EmployeeList iEmployeeList
	 */
	public void removeEmployeeList(final EmployeeList iEmployeeList){
		Employee theEmployee = null;
		for(int i = 0; i < iEmployeeList.size(); i++){
			theEmployee = iEmployeeList.getEmployee(i);
			removeAEmployee(theEmployee.getXuid());
		}
	}
	
	/**
	 * 刪除Employee(Employee)BySelective
	 * @param Employee iEmployee
	 */
	public void removeEmployeeBySelective(final Employee iEmployee){
		employeeDAO.deleteBySelective(iEmployee);
	}

	// ############# Private ###################################################
		
	/**
	 * 取一Employee(含明細)(Employee)
	 * @param String iXuid, int iInclude
	 * @return Employee
	 */
	private Employee getEmployee(final String iXuid, final int iInclude) {
		Employee objReturn = employeeDAO.selectByPrimaryKey(iXuid);

		if (objReturn == null || objReturn.getXuid().length() == 0) {
			return null;
		}

		// Include Mehod
		
		
		return objReturn;
	}
   
    /**
	 * 儲存Employee(Employee)
	 * @param Employee iEmployee
	 */
    private void storeEmployee(final Employee iEmployee) {
		String strNow = new Ystd().utime();
		
		Employee objInDB = employeeDAO.selectByPrimaryKey(iEmployee.getXuid());
			
		if (objInDB == null || objInDB.getXuid().length() == 0) {
			//新增
			int retryCount = 0;
			boolean retry = false;
			do {
				retry = false;
				try {
					iEmployee.setXuid(StringUtil.genXUID() + "_" + findGroupModelSeqNum());
					iEmployee.setGftm(strNow);
					iEmployee.setReturnNullNoThisField(true);
					employeeDAO.insertSelective(iEmployee);
					iEmployee.setReturnNullNoThisField(false);
				} catch (Exception e) {
					e.printStackTrace();
					if (retryCount < 3) {
						retry = true;
						retryCount++;
						try {
							Thread.sleep(10);
							// 休息0.01 sec,再試一次
						} catch (Exception e3) {
						}
					} else {
						throw new RuntimeException(e);
					}
				}
			} while (retry);
		} else {
			//更新
			iEmployee.setTxtm(strNow);
			iEmployee.setReturnNullNoThisField(true);
			employeeDAO.updateByPrimaryKeySelective(iEmployee);
			iEmployee.setReturnNullNoThisField(false);
		}
	}

	//===================================================================
	// EmployeeBO Method(findAObject, findList, save, remove) End-Employee 
	//===================================================================
	

	
	// ### Sequence ##############################################
	
	/**
	 * 取得Group模組序號
	 */
	public String findGroupModelSeqNum() {
		String objReturn = groupUtilDAO.selectSequenceNumber();
		if (objReturn.length() < 4) {
			objReturn = strUtil.lpad(objReturn, 4, '0');
		} else {
			// 避免Sequence過長  只取Seqence最後四碼  同時也避免重覆
			int len = objReturn.length();
			objReturn = objReturn.substring(len - 4);
		}
		
		return objReturn;
	}
	
}
