package com.fpg.ec.utility;


import java.io.ByteArrayOutputStream;

/**
 * ByteArray 與 HexString 互轉工具
 * @author N000101814
 *
 */
public class ByteArrayHexStringUtil {
	private static final String HEX_PADDING = ",";
	
	/**
	 * Byte 轉 Padding Hex(16進位)
	 */
	public static String convertByteArrayToPaddingHex(byte[] byteFile) {
		return convertByteArrayToPaddingHex(byteFile, HEX_PADDING);
	}
	
	/**
	 * Byte 轉 Padding Hex(16進位)(以 strPadding 隔開,建議使用非 RegExp 以及 非 0-9A-F文字)
	 */
	private static String convertByteArrayToPaddingHex(byte[] byteFile, String strPadding) {
		StringBuffer sbHex = new StringBuffer();
		for (int i = 0; i < byteFile.length; i++) {
			if (sbHex.length() > 0) {
				sbHex.append(strPadding);
			}
			sbHex.append(toHex(byteFile[i]));
		}
		return sbHex.toString();
	}

	/**
	 * Padding Hex(16進位) 轉 Byte
	 */
	public static byte[] convertPaddingHexToByteArray(String strHexPadding) {
		return convertPaddingHexToByteArray(strHexPadding, HEX_PADDING);
	}
	
	/**
	 * Padding Hex(16進位) 轉 Byte (以 strPaddingRegExp 切割字串, 注意: strPaddingRegExp 為 RegExp, 建議使用非 RegExp 以及 非 0-9A-F文字))
	 */
	private static byte[] convertPaddingHexToByteArray(String strHexPadding, String strPaddingRegExp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		String[] strHexPaddingArr = strHexPadding.split(strPaddingRegExp);
		for (int i = 0; i < strHexPaddingArr.length; i++) {
			baos.write(Integer.parseInt(strHexPaddingArr[i], 16));
		}

		return baos.toByteArray();
	}
	
	/**
	 * Byte 轉 Hex (16進位)
	 */
	public static String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
	}
}
