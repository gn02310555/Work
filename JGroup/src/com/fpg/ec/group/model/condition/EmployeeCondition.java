package com.fpg.ec.group.model.condition;

import com.fpg.ec.group.model.base.EmployeeBase;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * EmployeeModel List物件(Employee)
 * @author Jason
 */
public class EmployeeCondition extends EmployeeBase {  	
  	private static final long serialVersionUID = 995783700932170182L;
  	
   	public EmployeeCondition() {
        
    }

    public EmployeeCondition(Hashtable i_data) {
        super(i_data);
    }
  	
  	// ###分頁使用#############################################
  	
	public String getStartCount() {
		return getValue("startcount");
	}

	public void setStartCount(String i_val) {
		setValue("startcount", i_val);
	}

	public String getEndCount() {
		return getValue("endcount");
	}

	public void setEndCount(String i_val) {
		setValue("endcount", i_val);
	}

	public String getCurrentPage() {
		return getValue("currentpage");
	}

	public void setCurrentPage(String i_val) {
		setValue("currentpage", i_val);
	}
	
	public String getPageSize() {
		return getValue("pagesize");
	}

	public void setPageSize(String i_val) {
		setValue("pagesize", i_val);
	}

	// ###日期區間#############################################
	
    public String getGftmStartDate(){
        return getValue("gftmstartdate");
    }
    public void setGftmStartDate(String iVal){
        setValue("gftmstartdate", iVal);
    }
    public String getGftmEndDate(){
        return getValue("gftmenddate");
    }
    public void setGftmEndDate(String iVal){
        setValue("gftmenddate", iVal);
    }

    public String getTxtmStartDate(){
        return getValue("txtmstartdate");
    }
    public void setTxtmStartDate(String iVal){
        setValue("txtmstartdate", iVal);
    }
    public String getTxtmEndDate(){
        return getValue("txtmenddate");
    }
    public void setTxtmEndDate(String iVal){
        setValue("txtmenddate", iVal);
    }


	//主機時間(年月日時共十位)
	public String getServerTime() {
		return getValue("servertime");
	}
	
	public void setServerTime(String i_val) {
		setValue("servertime", i_val);
	}
	
	// ###查詢陣列條件#############################################
	
	
	// ###排序欄位設定#############################################
	
	private ArrayList<String> orderByList = new ArrayList<String>();
	
	public ArrayList<String> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(ArrayList<String> orderByList) {
		this.orderByList = orderByList;
	}

	/**
	 * Order by field(僅自動產生前十個欄位)
	 */
	
    public static final String ORDERFIELD_Xuid_Desc = "Xuid Desc";//遞減排序
    public static final String ORDERFIELD_Xuid_Asc = "Xuid Asc";//遞增排序

    public static final String ORDERFIELD_EmpId_Desc = "EmpId Desc";//遞減排序
    public static final String ORDERFIELD_EmpId_Asc = "EmpId Asc";//遞增排序

    public static final String ORDERFIELD_EmpNm_Desc = "EmpNm Desc";//遞減排序
    public static final String ORDERFIELD_EmpNm_Asc = "EmpNm Asc";//遞增排序

    public static final String ORDERFIELD_DpId_Desc = "DpId Desc";//遞減排序
    public static final String ORDERFIELD_DpId_Asc = "DpId Asc";//遞增排序

    public static final String ORDERFIELD_Gender_Desc = "Gender Desc";//遞減排序
    public static final String ORDERFIELD_Gender_Asc = "Gender Asc";//遞增排序

    public static final String ORDERFIELD_Tel_Desc = "Tel Desc";//遞減排序
    public static final String ORDERFIELD_Tel_Asc = "Tel Asc";//遞增排序

    public static final String ORDERFIELD_Addr_Desc = "Addr Desc";//遞減排序
    public static final String ORDERFIELD_Addr_Asc = "Addr Asc";//遞增排序

    public static final String ORDERFIELD_Age_Desc = "Age Desc";//遞減排序
    public static final String ORDERFIELD_Age_Asc = "Age Asc";//遞增排序

    public static final String ORDERFIELD_GfEmp_Desc = "GfEmp Desc";//遞減排序
    public static final String ORDERFIELD_GfEmp_Asc = "GfEmp Asc";//遞增排序

    public static final String ORDERFIELD_GfTm_Desc = "GfTm Desc";//遞減排序
    public static final String ORDERFIELD_GfTm_Asc = "GfTm Asc";//遞增排序

	
	// ###自訂欄位(僅自動產生前十個欄位)#############################################
	
	//Like-field like '%'+fieldCond+'%'
	//LikeIgnCase-UPPER(field) like '%'+UPPER(fieldCond)+'%'
	//IgnCase-UPPER(field) = UPPER(fieldCond)
	//IsNotEmpty-(field is not null and field <![CDATA[ <> ]]> '')
	//IsEmpty-(field is null or field = '')
	
    public String getEmpidLikeSrh(){
        return getValue("empidlikesrh");
    }
    public void setEmpidLikeSrh(String iVal){
        setValue("empidlikesrh", iVal);
    }
    public String getEmpidLikeIgnCaseSrh(){
        return getValue("empidlikeigncasesrh");
    }
    public void setEmpidLikeIgnCaseSrh(String iVal){
        setValue("empidlikeigncasesrh", iVal);
    }
    public String getEmpidIgnCaseSrh(){
        return getValue("empidigncasesrh");
    }
    public void setEmpidIgnCaseSrh(String iVal){
        setValue("empidigncasesrh", iVal);
    }
    public String getEmpidIsNotEmptySrh(){
        return getValue("empidisnotemptysrh");
    }
    public void setEmpidIsNotEmptySrh(String iVal){
        setValue("empidisnotemptysrh", iVal);
    }
    public String getEmpidIsEmptySrh(){
        return getValue("empidisemptysrh");
    }
    public void setEmpidIsEmptySrh(String iVal){
        setValue("empidisemptysrh", iVal);
    }

    public String getEmpnmLikeSrh(){
        return getValue("empnmlikesrh");
    }
    public void setEmpnmLikeSrh(String iVal){
        setValue("empnmlikesrh", iVal);
    }
    public String getEmpnmLikeIgnCaseSrh(){
        return getValue("empnmlikeigncasesrh");
    }
    public void setEmpnmLikeIgnCaseSrh(String iVal){
        setValue("empnmlikeigncasesrh", iVal);
    }
    public String getEmpnmIgnCaseSrh(){
        return getValue("empnmigncasesrh");
    }
    public void setEmpnmIgnCaseSrh(String iVal){
        setValue("empnmigncasesrh", iVal);
    }
    public String getEmpnmIsNotEmptySrh(){
        return getValue("empnmisnotemptysrh");
    }
    public void setEmpnmIsNotEmptySrh(String iVal){
        setValue("empnmisnotemptysrh", iVal);
    }
    public String getEmpnmIsEmptySrh(){
        return getValue("empnmisemptysrh");
    }
    public void setEmpnmIsEmptySrh(String iVal){
        setValue("empnmisemptysrh", iVal);
    }

    public String getDpidLikeSrh(){
        return getValue("dpidlikesrh");
    }
    public void setDpidLikeSrh(String iVal){
        setValue("dpidlikesrh", iVal);
    }
    public String getDpidLikeIgnCaseSrh(){
        return getValue("dpidlikeigncasesrh");
    }
    public void setDpidLikeIgnCaseSrh(String iVal){
        setValue("dpidlikeigncasesrh", iVal);
    }
    public String getDpidIgnCaseSrh(){
        return getValue("dpidigncasesrh");
    }
    public void setDpidIgnCaseSrh(String iVal){
        setValue("dpidigncasesrh", iVal);
    }
    public String getDpidIsNotEmptySrh(){
        return getValue("dpidisnotemptysrh");
    }
    public void setDpidIsNotEmptySrh(String iVal){
        setValue("dpidisnotemptysrh", iVal);
    }
    public String getDpidIsEmptySrh(){
        return getValue("dpidisemptysrh");
    }
    public void setDpidIsEmptySrh(String iVal){
        setValue("dpidisemptysrh", iVal);
    }

    public String getGenderLikeSrh(){
        return getValue("genderlikesrh");
    }
    public void setGenderLikeSrh(String iVal){
        setValue("genderlikesrh", iVal);
    }
    public String getGenderLikeIgnCaseSrh(){
        return getValue("genderlikeigncasesrh");
    }
    public void setGenderLikeIgnCaseSrh(String iVal){
        setValue("genderlikeigncasesrh", iVal);
    }
    public String getGenderIgnCaseSrh(){
        return getValue("genderigncasesrh");
    }
    public void setGenderIgnCaseSrh(String iVal){
        setValue("genderigncasesrh", iVal);
    }
    public String getGenderIsNotEmptySrh(){
        return getValue("genderisnotemptysrh");
    }
    public void setGenderIsNotEmptySrh(String iVal){
        setValue("genderisnotemptysrh", iVal);
    }
    public String getGenderIsEmptySrh(){
        return getValue("genderisemptysrh");
    }
    public void setGenderIsEmptySrh(String iVal){
        setValue("genderisemptysrh", iVal);
    }

    public String getTelLikeSrh(){
        return getValue("tellikesrh");
    }
    public void setTelLikeSrh(String iVal){
        setValue("tellikesrh", iVal);
    }
    public String getTelLikeIgnCaseSrh(){
        return getValue("tellikeigncasesrh");
    }
    public void setTelLikeIgnCaseSrh(String iVal){
        setValue("tellikeigncasesrh", iVal);
    }
    public String getTelIgnCaseSrh(){
        return getValue("teligncasesrh");
    }
    public void setTelIgnCaseSrh(String iVal){
        setValue("teligncasesrh", iVal);
    }
    public String getTelIsNotEmptySrh(){
        return getValue("telisnotemptysrh");
    }
    public void setTelIsNotEmptySrh(String iVal){
        setValue("telisnotemptysrh", iVal);
    }
    public String getTelIsEmptySrh(){
        return getValue("telisemptysrh");
    }
    public void setTelIsEmptySrh(String iVal){
        setValue("telisemptysrh", iVal);
    }

    public String getAddrLikeSrh(){
        return getValue("addrlikesrh");
    }
    public void setAddrLikeSrh(String iVal){
        setValue("addrlikesrh", iVal);
    }
    public String getAddrLikeIgnCaseSrh(){
        return getValue("addrlikeigncasesrh");
    }
    public void setAddrLikeIgnCaseSrh(String iVal){
        setValue("addrlikeigncasesrh", iVal);
    }
    public String getAddrIgnCaseSrh(){
        return getValue("addrigncasesrh");
    }
    public void setAddrIgnCaseSrh(String iVal){
        setValue("addrigncasesrh", iVal);
    }
    public String getAddrIsNotEmptySrh(){
        return getValue("addrisnotemptysrh");
    }
    public void setAddrIsNotEmptySrh(String iVal){
        setValue("addrisnotemptysrh", iVal);
    }
    public String getAddrIsEmptySrh(){
        return getValue("addrisemptysrh");
    }
    public void setAddrIsEmptySrh(String iVal){
        setValue("addrisemptysrh", iVal);
    }

    public String getAgeLikeSrh(){
        return getValue("agelikesrh");
    }
    public void setAgeLikeSrh(String iVal){
        setValue("agelikesrh", iVal);
    }
    public String getAgeLikeIgnCaseSrh(){
        return getValue("agelikeigncasesrh");
    }
    public void setAgeLikeIgnCaseSrh(String iVal){
        setValue("agelikeigncasesrh", iVal);
    }
    public String getAgeIgnCaseSrh(){
        return getValue("ageigncasesrh");
    }
    public void setAgeIgnCaseSrh(String iVal){
        setValue("ageigncasesrh", iVal);
    }
    public String getAgeIsNotEmptySrh(){
        return getValue("ageisnotemptysrh");
    }
    public void setAgeIsNotEmptySrh(String iVal){
        setValue("ageisnotemptysrh", iVal);
    }
    public String getAgeIsEmptySrh(){
        return getValue("ageisemptysrh");
    }
    public void setAgeIsEmptySrh(String iVal){
        setValue("ageisemptysrh", iVal);
    }

    public String getGfempLikeSrh(){
        return getValue("gfemplikesrh");
    }
    public void setGfempLikeSrh(String iVal){
        setValue("gfemplikesrh", iVal);
    }
    public String getGfempLikeIgnCaseSrh(){
        return getValue("gfemplikeigncasesrh");
    }
    public void setGfempLikeIgnCaseSrh(String iVal){
        setValue("gfemplikeigncasesrh", iVal);
    }
    public String getGfempIgnCaseSrh(){
        return getValue("gfempigncasesrh");
    }
    public void setGfempIgnCaseSrh(String iVal){
        setValue("gfempigncasesrh", iVal);
    }
    public String getGfempIsNotEmptySrh(){
        return getValue("gfempisnotemptysrh");
    }
    public void setGfempIsNotEmptySrh(String iVal){
        setValue("gfempisnotemptysrh", iVal);
    }
    public String getGfempIsEmptySrh(){
        return getValue("gfempisemptysrh");
    }
    public void setGfempIsEmptySrh(String iVal){
        setValue("gfempisemptysrh", iVal);
    }

}
