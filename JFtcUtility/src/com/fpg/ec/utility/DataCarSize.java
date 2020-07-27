package com.fpg.ec.utility;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;
public class DataCarSize {
	private Hashtable hstSize;
	private String strCurrIndex = "";
	/**
	 * Constructor for DataCarSizeTree
	 */
	public DataCarSize() {
		super();
	}

	public DataCarSize(DataCar dc) {
		super();
		ArrayList alCurrent = dc.getAllData();
		ArrayList alChild = null;
		hstSize = new Hashtable();

		// init Stack
		Stack stkPos = new Stack();
		Stack stkLevel = new Stack();
		int pos = 0;
		Hashtable hstTmp = new Hashtable();
		String strHashKey = "";

		for (;;) {
			try {
				hstTmp = (Hashtable) alCurrent.get(pos);
			} catch (IndexOutOfBoundsException e) {
				//System.out.println(strHashKey+","+String.valueOf(alCurrent.size()));
				hstSize.put(strHashKey, String.valueOf(alCurrent.size()));

				if (strHashKey.length() >= 3) {
					strHashKey = strHashKey.substring(0, strHashKey.length() - 2);
				} else {
					strHashKey = "";
				}

				if (stkLevel.empty())
					break;
				alCurrent = (ArrayList) stkLevel.pop();
				pos = ((Integer) stkPos.pop()).intValue();
				pos++;
				continue;
			}

			alChild = (ArrayList) hstTmp.get("$detail$");
			if (alChild != null && alChild.size() > 0) {
				if (strHashKey.length() == 0)
					strHashKey = "" + pos;
				else
					strHashKey += "." + pos;
				
				stkPos.push(new Integer(pos));
				stkLevel.push(alCurrent);
				alCurrent = alChild;
				pos = 0;
			} else {
			    pos++;
			}
		}
		
	}
	
	/**
	 * 取得某一index之size
	 * @param strIndexs : index
	 * @return int : size
	 */
	public int getSize(String strIndexs){
		if( strIndexs == null ) strIndexs = "";
		String strReturn = (String)hstSize.get(strIndexs);
		if( strReturn == null ) return -1;
		return Integer.parseInt(strReturn);
	}
	
	/**
	 * 取得現行index下之size
	 * @return int : size
	 */
	public int getSize(){
		return getSize(getCurrIndex());
	}
	
	/**
	 * 取得一新DataCarSize並設定其最後一層index
	 * @param intIndex : 欲設定最後一層之index
	 * @return DataCarSize 
	 */
	public DataCarSize get(int intIndex){
		DataCarSize dcsReturn = new DataCarSize();
		dcsReturn.setHstSize(getHstSize());
		if( getCurrIndex().length() == 0 ){
			dcsReturn.setCurrIndex(""+intIndex);
		}else{
			dcsReturn.setCurrIndex(getCurrIndex()+"."+intIndex);
		}
		return dcsReturn;
	}
	
	/**
	 * Gets the strCurrIndex
	 * @return Returns a String
	 */
	public String getCurrIndex() {
		return strCurrIndex;
	}
	/**
	 * Sets the strCurrIndex
	 * @param strCurrIndex The strCurrIndex to set
	 */
	public void setCurrIndex(String strCurrIndex) {
		this.strCurrIndex = strCurrIndex;
	}
	
	/* 暫時不用
	public void root(){
		setCurrIndex("");
	}
	
	public void next(){
		String strTmp = getCurrIndex();
		int intLastIndex = -1;
		int pos = strTmp.lastIndexOf(".");
		if( pos >= 0 ){
		    intLastIndex = Integer.parseInt(strTmp.substring(pos+1,strTmp.length()));
		    setCurrIndex(strTmp.substring(0,pos+1)+(intLastIndex+1));
		}else{
			intLastIndex = Integer.parseInt(strTmp);
			setCurrIndex(""+(intLastIndex+1));
		}
	}*/

	/**
	 * Gets the hstSize
	 * @return Returns a Hashtable
	 */
	public Hashtable getHstSize() {
		return hstSize;
	}
	/**
	 * Sets the hstSize
	 * @param hstSize The hstSize to set
	 */
	public void setHstSize(Hashtable hstSize) {
		this.hstSize = hstSize;
	}

}