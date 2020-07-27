package com.fpg.ec.utility.freemarker;

import java.io.File;
import java.util.Hashtable;

import com.fpg.ec.utility.db.SKDataAccessor;

/**
 * 
 * @author N000100937
 *
 */
public class FreeMarkerTemplate extends SKDataAccessor {
	private static final long serialVersionUID = 1877905452542373902L;
	
	/**
	 * 樣板存放路徑
	 */
	public static final String EmailTmp_Path =  File.separator + "email" + File.separator;// WEB-INF/freemarker/email/
	public static final String PDFTmp_Path =  File.separator + "pdf" + File.separator;// WEB-INF/freemarker/pdf/

	/**
	 * 樣板類型
	 */
	public static final String TYPE_PATH = "P";// 路徑
	public static final String TYPE_CLOB = "C";// CLOB(String)
	
    public FreeMarkerTemplate() {
        
    }

    public FreeMarkerTemplate(String iType) {
    	setType(iType);
    }
    
    public FreeMarkerTemplate(Hashtable i_data) {
        super(i_data);
    }

    public String getXuid() {
        return getValue("xuid");
    }

    public void setXuid(String i_val) {
        setValue("xuid", i_val);
    }
    
    public String getType() {
        return getValue("type");
    }

    public void setType(String i_val) {
        setValue("type", i_val);
    }
    
    public String getPath() {
        return getValue("path");
    }

    public void setPath(String i_val) {
        setValue("path", i_val);
    }
    
    public String getClobcontent() {
        return getValue("clobcontent");
    }

    public void setClobcontent(String i_val) {
        setValue("clobcontent", i_val);
    }
}
