package com.fpg.ec.group.model.base;

import java.util.Hashtable;
import com.fpg.ec.utility.db.SKDataAccessor;

/**
 * EmployeeModel Base物件(Employee)
 * @author Jason
 */
public class EmployeeBase extends SKDataAccessor {
	private static final long serialVersionUID = -2481658690308079917L;
	
	public EmployeeBase() {
        
    }

    public EmployeeBase(Hashtable i_data) {
        super(i_data);
    }

    
    public String getXuid(){
        return getValue("xuid");
    }
    public void setXuid(String iVal){
        setValue("xuid", iVal);
    }

    public String getEmpid(){
        return getValue("empid");
    }
    public void setEmpid(String iVal){
        setValue("empid", iVal);
    }

    public String getEmpnm(){
        return getValue("empnm");
    }
    public void setEmpnm(String iVal){
        setValue("empnm", iVal);
    }

    public String getDpid(){
        return getValue("dpid");
    }
    public void setDpid(String iVal){
        setValue("dpid", iVal);
    }

    public String getGender(){
        return getValue("gender");
    }
    public void setGender(String iVal){
        setValue("gender", iVal);
    }

    public String getTel(){
        return getValue("tel");
    }
    public void setTel(String iVal){
        setValue("tel", iVal);
    }

    public String getAddr(){
        return getValue("addr");
    }
    public void setAddr(String iVal){
        setValue("addr", iVal);
    }

    public String getAge(){
        return getValue("age");
    }
    public void setAge(String iVal){
        setValue("age", iVal);
    }

    public String getGfemp(){
        return getValue("gfemp");
    }
    public void setGfemp(String iVal){
        setValue("gfemp", iVal);
    }

    public String getGftm(){
        return getValue("gftm");
    }
    public void setGftm(String iVal){
        setValue("gftm", iVal);
    }

    public String getTxemp(){
        return getValue("txemp");
    }
    public void setTxemp(String iVal){
        setValue("txemp", iVal);
    }

    public String getTxtm(){
        return getValue("txtm");
    }
    public void setTxtm(String iVal){
        setValue("txtm", iVal);
    }

}
