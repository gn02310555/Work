package com.fpg.ec.utility;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <PRE><CODE>
 * 請於此處插入類別的說明及使用方法。
 * 建立日期： (2001/11/8 下午 03:58:23)
 * @作者： PC01
 * </CODE></PRE>
 */
public class User implements java.io.Serializable {
	private java.lang.String strIP = "";   //登入IP 
    private java.lang.String strCofnm = "";   //公司全銜
    private java.lang.String strRfno = "";      //公司統編
    private java.lang.String strKd = "";         //類別
    private java.lang.String strRole = "";       //角色
    private java.lang.String strUrnm = "";     //使用者名稱
    private java.lang.String strAcno = "";      //帳號
    private java.lang.String strEmail = "";      //帳號
    private java.lang.String strUrtel = "";      //使用者電話
    private java.lang.String strUrtelext = "";  //使用者電話分機   
    private java.lang.String strMk = "";        //註記 "*" 為必須變更密碼( 改用 sts)
    private java.lang.String strSts = "";         //會員狀態  F: 變更密碼    
    private java.lang.String strChkdiv = "";  //核發事業部
    private java.lang.String strServid = "";  //datasource id
    private java.util.Hashtable hstAuthZone = new java.util.Hashtable();  // 專區權限表          
    private java.util.Hashtable hstAuthDiv = new java.util.Hashtable();  // 事業部權限表    
    private java.util.Hashtable hstAuthRole = new java.util.Hashtable();  // 角色權限表

    private java.util.Hashtable hstAuth1 = new java.util.Hashtable();  // 大類權限表
    private java.util.Hashtable hstAuth2 = new java.util.Hashtable();  // 中類權限表
    private java.util.Hashtable hstAuth3 = new java.util.Hashtable();  // 小類權限表        
    private java.util.Hashtable hstAuthPage = new java.util.Hashtable();  // page權限表	        

/**
 * User 建構子註解。
 */
public User() {
	super();
}

/**
 * 取得帳號
 */
public java.lang.String getAcno() {
	return strAcno;
}

/**
 * 取出事業部權限表
 */
public java.util.ArrayList getAuthorityDiv() {
	return new com.fpg.ec.utility.CollectionUtil().toArrayList(hstAuthDiv,"key");
}

/**
 * 取得角色權限
 */
public java.util.ArrayList getAuthorityRole() {
	return new com.fpg.ec.utility.CollectionUtil().toArrayList(hstAuthRole,"key");
}

/**
 * 取得核發事業部
 */
public java.lang.String getChkdiv() {
	return strChkdiv;
}

/**
 * 取得公司名稱
 */
public java.lang.String getCofnm() {
	return strCofnm;
}

/**
 * 取得客戶簡稱
 */
public String getCuabr(String newDlv, String newSalid) {
    String strKey = "";
    String strReturn = "";
    Enumeration enm = null;
    Hashtable hstSalid = null;
    Hashtable hstCuno = null;
    Hashtable hstCunoData = null;    

    hstSalid = (Hashtable) hstAuthDiv.get(newDlv);
    if (hstAuthDiv.get(newDlv) != null && hstSalid.get(newSalid) != null) {
        hstCuno = (Hashtable) hstSalid.get(newSalid);
        enm = hstCuno.keys();
        strKey = (String) enm.nextElement();
        if (strKey != null) {
	        hstCunoData = (Hashtable) hstCuno.get(strKey); 
            strReturn = (String) hstCunoData.get("cuabr");
        }
    } 
    if (strReturn == null) {
        strReturn = "";
    }
    return strReturn;
}

/**
 * 取得客戶編號
 */
public String getCuno(String newDlv, String newSalid) {
    String strReturn = "";
    Enumeration enm = null;
    Hashtable hstSalid = null;
    Hashtable hstCuno = null;
    hstSalid = (Hashtable) hstAuthDiv.get(newDlv);
    if (hstAuthDiv.get(newDlv) != null && hstSalid.get(newSalid) != null) {
        hstCuno = (Hashtable) hstSalid.get(newSalid);
        enm = hstCuno.keys();
        strReturn = (String) enm.nextElement();
    }
    if (strReturn == null) {
        strReturn = "";
    }
    return strReturn;
}

/**
 * 取得電子郵件
 */
public java.lang.String getEmail() {
	return strEmail;
}

/**
 * 取得登入IP
 */
public java.lang.String getIP() {
	return strIP;
}

/**
 * 取得類別
 */
public java.lang.String getKd() {
	return strKd;
}

/**
 * 取得記註
 */
public java.lang.String getMk() {
	return strMk;
}

/**
 * 取得統編
 */
public java.lang.String getRfno() {
	return strRfno;
}

/**
 * 取得角色 txd2user -> Role
 */
public java.lang.String getRole() {
	return strRole;
}

/**
 * 取得資料來源(同系統別 ex:j2xx)
 */
public java.lang.String getServId() {
	return strServid;
}

/**
 * 取得角色權限個數
 */
public int getSizeDiv() {
    if (hstAuthDiv != null) {
        return hstAuthDiv.size();
    } else {
        return -1;
    }
}

/**
 * 取得角色權限個數
 */
public int getSizeRole() {
    if (hstAuthRole != null) {
       return hstAuthRole.size();
    } else {
        return -1;
    }
}

/**
 * 取得專區權限個數
 */
public int getSizeZone() {
    if (hstAuthZone != null) {
      return  hstAuthZone.size();
    } else {
        return -1;
    }
}

/**
 * 取得狀態
 */
public java.lang.String getSts() {
	return strSts;
}
/**
 * 請於此處加入方法的說明。
 * 建立日期： (2002/1/14 上午 11:46:55)
 * @param newDlv java.lang.String
 * @param newSalid java.lang.String
 */
public String getUrgd(String newDlv, String newSalid) {
    String strKey = "";
    String strReturn = "";
    Enumeration enm = null;
    Hashtable hstSalid = null;
    Hashtable hstCuno = null;
    Hashtable hstCunoData = null;    
    hstSalid = (Hashtable) hstAuthDiv.get(newDlv);
    if (hstAuthDiv.get(newDlv) != null && hstSalid.get(newSalid) != null) {
        hstCuno = (Hashtable) hstSalid.get(newSalid);
        enm = hstCuno.keys();
        strKey = (String) enm.nextElement();
        if (strKey != null) {
	        hstCunoData = (Hashtable) hstCuno.get(strKey); 
            strReturn = (String) hstCunoData.get("urgd");
        }
    }
    if (strReturn == null) {
        strReturn = "";
    }
    return strReturn;
}

/**
 * 取得使用者名稱
 */
public java.lang.String getUrnm() {
	return strUrnm;
}

/**
 * 取得設定使用者電話
 */
public java.lang.String getUrtel() {
	return strUrtel;
}

/**
 * 取得使者用電話區碼
 */
public java.lang.String getUrtelext() {
	return strUrtelext;
}

/**
 * 判別是否有大類權限
 */
public boolean isAuthority1(String i_str) {
    if (hstAuth1.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否有中類權限
 */
public boolean isAuthority2(String i_str) {
    if (hstAuth2.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否有小類權限
 */
public boolean isAuthority3(String i_str) {
    if (hstAuth3.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否有事業部權限
 */
public boolean isAuthorityDiv(String i_str) {
    if (hstAuthDiv.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否有頁面權限
 */
public boolean isAuthorityPage(String i_str) {
    if (hstAuthPage.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否有售別權限
 */
public boolean isAuthoritySalid(String newDlv, String newSalid) {
    Hashtable hstSalid = null;

    if (hstAuthDiv.get(newDlv) != null) {
        hstSalid = (Hashtable)hstAuthDiv.get(newDlv);
        if (hstSalid.get(newSalid) != null) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

/**
 * 判別是否有專區權限
 */
public boolean isAuthorityZone(String i_str) {
    if (hstAuthZone.get(i_str) != null) {
        return true;
    } else {
        return false;
    }
}

/**
 * 判別是否強制變更密碼 sts='F'
 */
public boolean isChangePswd() {
	if (strSts.equals("F"))
		return true;
	else		
		return false;
}

/**
 * 判別是否為管理者 zone = '1'
 */
public boolean isManager() {
	if (hstAuthZone.get("1") != null)
		return true;
	else		
		return false;
}

/**
 * 判別是否為一般使用者 zone = '2'
 */
public boolean isGeneralUser() {
	if (hstAuthZone.get("2") != null)
		return true;
	else		
		return false;
		
}

/**
 * 判別是否為會員 zone = '3'
 */
public boolean isMember() {
	if (hstAuthZone.get("3") != null)
		return true;
	else		
		return false;
}

/**
 *設定帳號 
 */
public void setAcno(java.lang.String newId) {
	strAcno = newId;
}

/**
 * 設定大類權限
 */
public void setAuthority1(java.util.Hashtable  i_hst) {
    hstAuth1 = i_hst;
}

/**
 * 設定中類權限
 */
public void setAuthority2(java.util.Hashtable  i_hst) {
    hstAuth2 = i_hst;
}

/**
 * 設定小類權限
 */
public void setAuthority3(java.util.Hashtable  i_hst) {
    hstAuth1 = i_hst;
}

/**
 * 設定事業部權限
 */
public void setAuthorityDiv(
    java.lang.String newDlv,
    java.lang.String newSalid,
    java.lang.String newCuno,
    java.util.Hashtable i_hstCunoData) {

    Hashtable hstSalid = null;
    Hashtable hstCuno = null;

    if (hstAuthDiv.get(newDlv) != null) {
	    // Get Salid hastable from Dlv hashtable
        hstSalid = (Hashtable) hstAuthDiv.get(newDlv);
	    // Cuno            
        hstCuno = new java.util.Hashtable();       
        hstCuno.put(newCuno, i_hstCunoData);
	    // Put Cuno hashtable to  Salid hashtble
        hstSalid.put(newSalid, hstCuno);  
    } else {
	    // Cuno
        hstCuno = new java.util.Hashtable();
        hstCuno.put(newCuno, i_hstCunoData);
        // Salid
        hstSalid = new java.util.Hashtable();
        hstSalid.put(newSalid, hstCuno);
        //Dlv
        hstAuthDiv.put(newDlv, hstSalid);
    }
}

/**
 * 設定Page權限
 */
public void setAuthorityPage(java.util.Hashtable  i_hst) {
    hstAuthPage = i_hst;
}

/**
 * 設定Role權限
 */
public void setAuthorityRole(java.util.Hashtable  i_hst) {
    hstAuthRole = i_hst;
}

/**
 * 設定專區權限
 */
public void setAuthorityZone(java.util.Hashtable  i_hst) {
    hstAuthZone = i_hst;
}

/**
 * 設定核發事業部
 */
public void setChkdiv(java.lang.String newChkdiv) {
	strChkdiv = newChkdiv;
}

/**
 * 設定公司名稱
 */
public void setCofnm(java.lang.String newCofnm) {
	strCofnm = newCofnm;
}

/**
 * 設定電子郵件
 */
public void setEmail(java.lang.String newEmail) {
	strEmail = newEmail;
}

/**
 * 設定登入IP (由登入的Action Bean設定)
 */
public void setIP(java.lang.String newIP) {
	strIP = newIP;
}

/**
 * 設定類別
 */
public void setKd(java.lang.String newKd) {
	strKd = newKd;
}

/**
 * 設定記註
 */
public void setMk(java.lang.String newMk) {
	strMk = newMk;
}

/**
 * 設定統編
 */
public void setRfno(java.lang.String newRfno) {
	strRfno = newRfno;
}

/**
 * 設定角色 txd2user -> Role
 */
public void setRole(java.lang.String newRole) {
	strRole = newRole;
}

/**
 * 設定資料來源(同系統別 ex:j2xx)
 */
public void setServId(java.lang.String newServId) {
	strServid = newServId;
}

/**
 * 設定狀態
 */
public void setSts(java.lang.String newSts) {
	strSts = newSts;
}

/**
 * 設定使用者名稱
 */
public void setUrnm(java.lang.String newUrnm) {
	strUrnm = newUrnm;
}

/**
 * 設定使用者電話
 */
public void setUrtel(java.lang.String newUrtel) {
	strUrtel = newUrtel;
}

/**
 * 設定使者用電話區碼
 */
public void setUrtelext(java.lang.String newUrtelext) {
	strUrtelext = newUrtelext;
}
}
