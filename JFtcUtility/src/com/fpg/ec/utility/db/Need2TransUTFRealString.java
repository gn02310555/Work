package com.fpg.ec.utility.db;

import com.fpg.ec.utility.StringUtil;


/**
 * 實作此interface之DataAccessor,
 * 會在getValue時將其所有欄位轉為uncode字符,
 * 若僅需指定某些欄位進行轉換,
 * 請以override DataAccessor 中的 initNeed2TransUTFRealStringField()來進行
 * @see StringUtil
 * @see DataAccessor
 */
public interface Need2TransUTFRealString {
	/**
	 * 需轉換非big5字元為&#x???字符
	 * 例:(方方土) ==> &#x5803
	 */
}
