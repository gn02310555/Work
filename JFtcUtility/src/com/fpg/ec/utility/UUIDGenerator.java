package com.fpg.ec.utility;

import java.util.UUID;

public class UUIDGenerator {

	public UUIDGenerator() {
	}

	 /** 
     * 獲得一個UUID -去掉“-”符號
     * digit:位數
     * @return String UUID 
     */ 
    public static String getUUID(int digit){ 
        String strUUID = UUID.randomUUID().toString(); 
         
        return strUUID.replaceAll("-", "").substring(0, digit); 
    }
    
	 /** 
     * 獲得一個UUID -去掉“-”符號
     * @return String UUID 
     */ 
    public static String getUUID(){ 
        String strUUID = UUID.randomUUID().toString(); 
         
        return strUUID.replaceAll("-", ""); 
    } 

    
    /** 
     * 獲得指定數目的UUID 
     * @param number int 需要獲得的UUID數量 
     * @return String[] UUID陣列 
     */ 
    public String[] getUUIDList(int number, int digit){ 
        if(number < 1){ 
            return null; 
        } 
        String[] ss = new String[number]; 
        for(int i=0;i<number;i++){ 
            ss[i] = getUUID(digit); 
        } 
        return ss; 
    } 

}
