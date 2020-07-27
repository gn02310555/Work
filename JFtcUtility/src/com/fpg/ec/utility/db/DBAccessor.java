package com.fpg.ec.utility.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 作業機能：公用物件<br>
 * 程式名稱：Accessor<br>  
 * 程式功能：定義資料物件操作介面<br>
 * 建 立 者：pc24/余東耿<br>
 * 建立日期：2004/11/14<br>
 * 參考物件：無<br>
 * 附加紀錄：無<br>  
 * 歷史修改資訊：無<br>
 */
public abstract class DBAccessor {

    protected Hashtable hstData = new Hashtable();

    public boolean isEmpty() {
        return ((hstData == null) || hstData.isEmpty());
    }

    /** 
     * 方法名稱：getField<br>
     * 方法說明：取得單一欄位值<br>
     * @param i_fn：欄位名稱<br>
     * @return：欄位值<br>
     * @exception：無<br>
     * 建 立 者：PC24/余東耿 <br> 
     * 建立日期：2004/11/14<br> 
     * 歷史修改資訊：無<br>
     **/
    public String getField(String i_fn) {
        String strReturn = (hstData.get(i_fn) == null) ? "" : (String) hstData.get(i_fn);
        return strReturn.trim();
    }

    /** 
     * 方法名稱：getAllField<br>
     * 方法說明：取得所有欄位資料<br>	 
     * @return：所有欄位資料<br>
     * @exception：無<br>
     * 建 立 者：PC24/余東耿 <br> 
     * 建立日期：2004/11/14<br> 
     * 歷史修改資訊：無<br>
     **/
    public Hashtable getAllField() {

        return hstData;
    }

    /** 
     * 方法名稱：setField<br>
     * 方法說明：設定單一欄位值<br>
     * @param i_fn：欄位名稱<br>
     * @param i_value：欄位值<br>
     * @return：無<br>
     * @exception：無<br>
     * 建 立 者：PC24/余東耿 <br> 
     * 建立日期：2004/11/14<br> 
     * 歷史修改資訊：無<br>
     **/
    public void setField(String i_fn, String i_value) {

        hstData.put(i_fn, i_value);
    }

    /** 
     * 方法名稱：setAllField<br>
     * 方法說明：設定主檔資料<br>
     * @param i_data：主檔資料<br>
     * @return：無<br>
     * @exception：無<br>
     * 建 立 者：PC24/余東耿 <br> 
     * 建立日期：2004/11/14<br> 
     * 歷史修改資訊：無<br>
     **/
    public void setAllField(Hashtable i_data) {
        Enumeration enumTemp = i_data.keys();
        while (enumTemp.hasMoreElements()) {
            String strKey = (String) enumTemp.nextElement();
            hstData.put(strKey, i_data.get(strKey));
        }
    }

    public String toString() {
        return hstData.toString();
    }

    abstract protected void create(Connection i_conn) throws SQLException, Exception;

    abstract protected void store(Connection i_conn) throws SQLException, Exception;

    abstract protected void remove(Connection i_conn) throws SQLException, Exception;

    abstract protected void findByPrimaryKey(Connection i_conn, Key i_key) throws SQLException, Exception;
}
