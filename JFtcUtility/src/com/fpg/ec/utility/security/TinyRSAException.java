package com.fpg.ec.utility.security;

/**
 * 請於此處加入類型的說明。
 * 建立日期：(2003/7/10 下午 02:05:19)
 * @作者：pc27/文啟能
 */
class TinyRSAException extends Exception {
	/**
	 * Constructs an IOException with null  as its error detail message.
	 */
	public TinyRSAException(){
		super();
	}
/**
 * TinyRSAException 建構子註解。
 * @param s java.lang.String
 */
public TinyRSAException(String s) {
	super(s);
}
}
