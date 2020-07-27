package com.fpg.ec.utility;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.fpg.ec.utility.exception.NoResultSetException;
/**
 * <PRE><CODE>
 *  專案代號：j2
 *  類別名稱：SelectData
 *  @author Atlas
 *  </CODE></PRE>
 */
public class SelectData {
    private Hashtable hstData = null;
    private ArrayList alMultiData = null;
    /**
     * SelectData 建構子註解。
     */
    public SelectData() {
        super();
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰getData
     *  方法說明︰取得以 i_key 為名的欄位值
     *  @param i_key : 欄位名稱
     *  @return String 欄位值 
     *  </CODE></PRE>
     */
    public ArrayList getArraylist() {
        return alMultiData;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰getData
     *  方法說明︰取得以 i_key 為名的欄位值
     *  @param i_key : 欄位名稱
     *  @return String 欄位值 
     *  </CODE></PRE>
     */
    public String getData(String i_key) {
        String strReturn = "";
        i_key = i_key.toLowerCase();
        strReturn = (String) hstData.get(i_key);
        return (strReturn == null) ? "" : strReturn;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰getData
     *  方法說明︰取得以 i_key 為名的欄位值
     *  @param i_key : 欄位名稱
     *  @return String 欄位值 
     *  </CODE></PRE>
     */
    public Hashtable getHashtable() {
        return hstData;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰getMultiData
     *  方法說明︰取得第 i_inx筆紀錄之以 i_key 為名的欄位值
     *  @param i_key : 欄位名稱
     *  @param i_inx : 紀錄之index ( >= 0 )
     *  @return String 欄位值 
     *  </CODE></PRE>
     */
    public String getMultiData(String i_key, int i_inx) {
        Hashtable hstTemp = (Hashtable) alMultiData.get(i_inx);
        i_key = i_key.toLowerCase();
        if (hstTemp == null)
            return "";
        else
            return (String) hstTemp.get(i_key);

    }
    /**
     * <PRE><CODE>
     *  方法名稱︰getMultiDataSize
     *  方法說明︰取得紀錄筆數
     *  @return int 紀錄筆數 
     *  </CODE></PRE>
     */
    public int getMultiDataSize() {
        if (alMultiData == null)
            return 0;
        return alMultiData.size();
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰mpiPutHst
     *  方法說明︰將 i_rs 中之 i_key 的值放入 i_hst中
     *  範例︰
     *  @param i_hst 欲放入之Hastable
     *  @param i_rs 相對之ResultSet
     *  @param i_key 欲放入Vector 之fieldname
     *  @return void
     *  </CODE></PRE>
     */
    private void mpiPutHst(Hashtable i_hst, ResultSet i_rs, String i_key) {
        try {
            String strTemp = i_rs.getString(i_key);
            i_hst.put(i_key, (strTemp == null) ? "" : strTemp);
        } catch (Exception ex) {
            // do nothing ...
        }
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰mpiPutHst
     *  方法說明︰將 i_rs 中之 i_key 的值放入 i_hst中
     *  範例︰
     *  @param i_hst 欲放入之Hastable
     *  @param i_rs 相對之ResultSet
     *  @param i_key 欲放入Vector 之fieldname
     *  @return void
     *  </CODE></PRE>
     */
    private void mpiPutValue2Tier(ArrayList i_arlMaster, Hashtable i_hstMaster, Hashtable i_hstDetail) throws Exception {

        int len = i_arlMaster.size();
        Hashtable hstTemp = new Hashtable();

        ArrayList arlDetail = new ArrayList();
        boolean notFind = true;

        for (int i = 0; i < len; i++) {
            hstTemp = (Hashtable) ((Hashtable) i_arlMaster.get(i)).clone();
            hstTemp.remove("DetailData");
            if (hstTemp.equals(i_hstMaster)) {
                ((ArrayList) ((Hashtable) i_arlMaster.get(i)).get("DetailData")).add(i_hstDetail);
                //   System.out.println(String.valueOf( ((ArrayList)((Hashtable)i_arlMaster.get(i)).get("DetailData")).size()));
                notFind = false;
                break;
            }
        }
        if (notFind) {
            arlDetail.add(i_hstDetail);
            i_hstMaster.put("DetailData", arlDetail);
            i_arlMaster.add(i_hstMaster);
        }
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setData
     *  方法說明︰將i_rs中之資料放入 hstData 中
     *  @param i_rs : ResultSet
     *  </CODE></PRE>
     */
    public SelectData setData(ResultSet i_rs) throws NoResultSetException, Exception {
        hstData = new Hashtable();
        try {
            if (i_rs.next()) {
                ResultSetMetaData rsmd = i_rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String strColnm = rsmd.getColumnName(i).toLowerCase();
                    mpiPutHst(hstData, i_rs, strColnm);
                } // end of for
            } else {
                throw new NoResultSetException(" ResultSet have no data !! ");
            }
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setData Error !!");
        }
        return this;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setData
     *  方法說明︰將i_rs中之資料放入 i_hst 中
     *  @param i_rs : 遠端ResultSet
     *  @param i_hst : 遠端Hashtable
     *  </CODE></PRE>
     */
    public void setData(ResultSet i_rs, Hashtable i_hst) throws NoResultSetException, Exception {
        try {
            if (i_rs.next()) {
                ResultSetMetaData rsmd = i_rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String strColnm = rsmd.getColumnName(i).toLowerCase();
                    mpiPutHst(i_hst, i_rs, strColnm);
                } // end of for
            } else {
                throw new NoResultSetException(" ResultSet have no data !! ");
            }
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setData Error !!");
        }
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setMultiData
     *  方法說明︰將i_rs中之資料放入 hashtable 再將 hashtable 放入 vtrMultiData 中
     *  @param i_rs : 遠端ResultSet
     *  </CODE></PRE>
     */

    public SelectData setMultiData(ResultSet i_rs) throws NoResultSetException, Exception {
        Hashtable hstData = null;
        int cnt = 0;
        try {
            ResultSetMetaData rsmd = i_rs.getMetaData();
            int intColnum = rsmd.getColumnCount();
            alMultiData = new ArrayList();
            while (i_rs.next()) {
                cnt++;
                hstData = new Hashtable();
                for (int i = 1; i <= intColnum; i++) {
                    String strColnm = rsmd.getColumnName(i).toLowerCase();
                    mpiPutHst(hstData, i_rs, strColnm);
                } // end of for

                alMultiData.add(hstData);
            } // end of while
            if (cnt == 0)
                throw new NoResultSetException(" no resultset !!");
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setMultiData Error !!");
        }
        return this;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setMulitData
     *  方法說明︰將依i_page 及i_pageitem取得i_rs中之資料,放入hashtable再將hashtable放入 vtrMultiData 中
     *  @param i_rs : 遠端ResultSet
     *  @param i_page : 目前頁次
     *  @param 1_page : 每頁筆數
     *  </CODE></PRE>
     */
    public SelectData setMultiData(ResultSet i_rs, int i_page, int i_pageitem) throws NoResultSetException, Exception {
        Hashtable hstData = null;
        int cnt = 0;
        try {
            int count = 1; //用於分頁
            ResultSetMetaData rsmd = i_rs.getMetaData();
            int intColnum = rsmd.getColumnCount();
            alMultiData = new ArrayList();
            for (; i_rs.next() && count <= i_page * i_pageitem; count++) {
                if (count >= (i_page - 1) * i_pageitem + 1) {
                    cnt++;
                    hstData = new Hashtable();
                    for (int i = 1; i <= intColnum; i++) {
                        String strColnm = rsmd.getColumnName(i).toLowerCase();
                        mpiPutHst(hstData, i_rs, strColnm);
                    } // end of for
                    alMultiData.add(hstData);
                } // end of if
            } // end of for
            if (cnt == 0)
                throw new NoResultSetException(" no resultset !!");
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setMultiData Error !!");
        }
        return this;
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setMultiData
     *  方法說明︰將i_rs中之資料放入 hashtable 再將 hashtable 放入 i_vtr 中
     *  @param i_rs :  遠端ResultSet
     *  @param i_vtr : 遠端vector
     *  </CODE></PRE>
     */

    public void setMultiData(ResultSet i_rs, ArrayList i_al) throws NoResultSetException, Exception {
        int cnt = 0;
        try {
            ResultSetMetaData rsmd = i_rs.getMetaData();
            int intColnum = rsmd.getColumnCount();
            while (i_rs.next()) {
                cnt++;
                hstData = new Hashtable();
                for (int i = 1; i <= intColnum; i++) {
                    String strColnm = rsmd.getColumnName(i).toLowerCase();
                    mpiPutHst(hstData, i_rs, strColnm);
                } // end of for

                i_al.add(hstData);
            } // end of while
            if (cnt == 0)
                throw new NoResultSetException(" no resultset !!");
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setMultiData Error !!");
        }
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setMultiData
     *  方法說明︰將依i_page 及i_pageitem取得i_rs中之資料,放入 hashtable 再將 hashtable 放入 i_vtr 中
     *  @param i_rs :  遠端ResultSet
     *  @param i_vtr : 遠端vector
     *  @param i_page : 目前頁次
     *  @param 1_page : 每頁筆數
     *  </CODE></PRE>
     */
    public void setMultiData(ResultSet i_rs, ArrayList i_al, int i_page, int i_pageitem) throws NoResultSetException, Exception {
        int cnt = 0;
        try {
            int count = 1; //用於分頁
            ResultSetMetaData rsmd = i_rs.getMetaData();
            int intColnum = rsmd.getColumnCount();
            for (; i_rs.next() && count <= i_page * i_pageitem; count++) {
                if (count >= (i_page - 1) * i_pageitem + 1) {
                    cnt++;
                    hstData = new Hashtable();
                    for (int i = 1; i <= intColnum; i++) {
                        String strColnm = rsmd.getColumnName(i).toLowerCase();
                        mpiPutHst(hstData, i_rs, strColnm);
                    } // end of for
                    i_al.add(hstData);
                } // end of if
            } // end of for
            if (cnt == 0)
                throw new NoResultSetException(" no resultset !!");
        } catch (NoResultSetException e) {
            throw e;
        } catch (Exception ex) {
            throw new Exception(" setMultiData Error !!");
        }
    }
    /**
     * <PRE><CODE>
     *  方法名稱︰setMultiData2Tier
     *  方法說明︰將 (Master 及 Detail) Join 的資料 折成 Master - Detail
     *  @param i_rs :  遠端ResultSet
     *  @param i_arl : Master's Field Name
     *  </CODE></PRE>
     */
    public ArrayList setMultiData2Tier(ResultSet i_rs, ArrayList i_arl) throws Exception {
        int len = i_arl.size();
        String strColnm = "";
        Hashtable hstKeyField = new Hashtable();

        for (int i = 0; i < len; i++) {
            hstKeyField.put((String) i_arl.get(i), (String) i_arl.get(i));
        }

        ArrayList arlMaster = new ArrayList();
        Hashtable hstMaster = new Hashtable();
        Hashtable hstDetail = new Hashtable();

        ResultSetMetaData rsmd = i_rs.getMetaData();
        try {
            while (i_rs.next()) {
                hstMaster = new Hashtable();
                hstDetail = new Hashtable();

                // Master Data
                for (int j = 0; j < len; j++) {
                    strColnm = (String) i_arl.get(j);
                    hstMaster.put(strColnm, (i_rs.getString(strColnm) == null) ? "" : i_rs.getString(strColnm));

                }

                // Detail Data
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    strColnm = rsmd.getColumnName(i).toLowerCase();
                    if (hstKeyField.get(strColnm) == null) {
                        hstDetail.put(strColnm, (i_rs.getString(strColnm) == null) ? "" : i_rs.getString(strColnm));
                    }
                }
                //
                mpiPutValue2Tier(arlMaster, hstMaster, hstDetail);

            }
        } catch (Exception ex) {
            throw new Exception(" setData Error !!");
        }

        return arlMaster;

    }
    /**
     * 取得ArrayList中(from SelectData),各欄位distinct後的值群
     * @param i_data : 目標ArrayList
     * @return Hashtable
     */
    public Hashtable distinctField(ArrayList i_data){
    	Hashtable hstReturn = new Hashtable();
    	
    	Hashtable hstARecord = null;
    	for( int i = 0; i<i_data.size(); i++){
    		try{
    			hstARecord = (Hashtable)i_data.get(i);
    		}catch( Exception e ){ //can't convert to Hashtable
    			return null;
    		}
    		if( hstARecord == null ) return null;
    		
    		//start to distinct ....
    	    Enumeration enumKeys = hstARecord.keys();
    	    Hashtable hstValues = null;
    	    while( enumKeys.hasMoreElements() ){
    	    	String strKey = (String)enumKeys.nextElement();
    	    	if( ( hstValues = (Hashtable)hstReturn.get(strKey) ) ==null ){
    	    		hstValues = new Hashtable();
    	    		hstReturn.put(strKey,hstValues);
    	    	}
    	    	String strValue = (String)hstARecord.get(strKey);
    	    	if( !hstValues.containsKey(strValue) ){
    	    		hstValues.put(strValue,"*");
    	    	}
    	    }  	
    		
    	}
    	return hstReturn;
    }
}