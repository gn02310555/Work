package com.fpg.ec.group.model.base;

import java.util.Hashtable;
import com.fpg.ec.utility.db.SKDataAccessor;

/**
 * DpModel Base物件(Dp)
 * @author Jason
 */
public class DpBase extends SKDataAccessor {
	private static final long serialVersionUID = -2481658690308079917L;
	
	public DpBase() {
        
    }

    public DpBase(Hashtable i_data) {
        super(i_data);
    }

    
    public String getXuid(){
        return getValue("xuid");
    }
    public void setXuid(String iVal){
        setValue("xuid", iVal);
    }

    public String getDpid(){
        return getValue("dpid");
    }
    public void setDpid(String iVal){
        setValue("dpid", iVal);
    }

    public String getDpnm(){
        return getValue("dpnm");
    }
    public void setDpnm(String iVal){
        setValue("dpnm", iVal);
    }

}
