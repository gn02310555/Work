package com.fpg.ec.group.model.condition;

import com.fpg.ec.group.model.base.DpBase;
import java.util.Hashtable;
import java.util.ArrayList;

/**
 * DpModel List物件(Dp)
 * @author Jason
 */
public class DpCondition extends DpBase {  	
  	private static final long serialVersionUID = 995783700932170182L;
  	
   	public DpCondition() {
        
    }

    public DpCondition(Hashtable i_data) {
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

    public static final String ORDERFIELD_DpId_Desc = "DpId Desc";//遞減排序
    public static final String ORDERFIELD_DpId_Asc = "DpId Asc";//遞增排序

    public static final String ORDERFIELD_DpNm_Desc = "DpNm Desc";//遞減排序
    public static final String ORDERFIELD_DpNm_Asc = "DpNm Asc";//遞增排序

	
	// ###自訂欄位(僅自動產生前十個欄位)#############################################
	
	//Like-field like '%'+fieldCond+'%'
	//LikeIgnCase-UPPER(field) like '%'+UPPER(fieldCond)+'%'
	//IgnCase-UPPER(field) = UPPER(fieldCond)
	//IsNotEmpty-(field is not null and field <![CDATA[ <> ]]> '')
	//IsEmpty-(field is null or field = '')
	
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

    public String getDpnmLikeSrh(){
        return getValue("dpnmlikesrh");
    }
    public void setDpnmLikeSrh(String iVal){
        setValue("dpnmlikesrh", iVal);
    }
    public String getDpnmLikeIgnCaseSrh(){
        return getValue("dpnmlikeigncasesrh");
    }
    public void setDpnmLikeIgnCaseSrh(String iVal){
        setValue("dpnmlikeigncasesrh", iVal);
    }
    public String getDpnmIgnCaseSrh(){
        return getValue("dpnmigncasesrh");
    }
    public void setDpnmIgnCaseSrh(String iVal){
        setValue("dpnmigncasesrh", iVal);
    }
    public String getDpnmIsNotEmptySrh(){
        return getValue("dpnmisnotemptysrh");
    }
    public void setDpnmIsNotEmptySrh(String iVal){
        setValue("dpnmisnotemptysrh", iVal);
    }
    public String getDpnmIsEmptySrh(){
        return getValue("dpnmisemptysrh");
    }
    public void setDpnmIsEmptySrh(String iVal){
        setValue("dpnmisemptysrh", iVal);
    }

}
