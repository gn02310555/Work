package com.fpg.ec.utility.db;

import java.util.Hashtable;

/**
 * 作業機能：公用物件<br>
 * 程式名稱：Key<br>  
 * 程式功能：存放主鍵(Primary Key)資料<br>
 * 建 立 者：pc24/余東耿<br>
 * 建立日期：2004/11/14<br>
 * 參考物件：無<br>
 * 附加紀錄：無<br>  
 * 歷史修改資訊：無<br>
 */
public class Key {
	
	private Hashtable hstKey = new Hashtable();   
	
	/** 
	 * 方法名稱：Key<br>
	 * 方法說明：建構子<br>
	 * @return：無<br>
	 * @exception：無<br>
	 * 建 立 者：PC24/余東耿 <br> 
	 * 建立日期：2004/11/14<br> 
	 * 歷史修改資訊：無<br>
	 **/   
	public Key() {
		super();    
	}  
	
	/** 
	 * 方法名稱：setKey<br>
	 * 方法說明：設定一鍵值<br>
	 * @param i_fn：欄位名稱<br>
	 * @param i_value：欄位值<br>
	 * @return：無<br>
	 * @exception：無<br>
	 * 建 立 者：PC24/余東耿 <br> 
	 * 建立日期：2004/11/14<br> 
	 * 歷史修改資訊：無<br>
	 **/
	public void setKey(String i_fn, String i_value) {    
		
		hstKey.put(i_fn, i_value);
	}  

	/** 
	 * 方法名稱：getKey<br>
	 * 方法說明：取得一鍵值<br>
	 * @param i_fn：欄位名稱<br>	 
	 * @return：欄位值<br>
	 * @exception：無<br>
	 * 建 立 者：PC24/余東耿 <br> 
	 * 建立日期：2004/11/14<br> 
	 * 歷史修改資訊：無<br>
	 **/
	public String getKey(String i_fn) {
   	
   		return (hstKey.get(i_fn)==null)?"":(String)hstKey.get(i_fn);
	}
	
	public Hashtable getAllKeys(){
	    return (Hashtable)this.hstKey.clone();
	}
}
