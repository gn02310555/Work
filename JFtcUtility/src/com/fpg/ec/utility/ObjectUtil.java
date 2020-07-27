package com.fpg.ec.utility;

public class ObjectUtil {
	/**
	 * 複製物件(物件必須implement Serializable)
	 */
	public Object deepCopy(Object obj) throws Exception {
		Object newObj = getObject(getObjectByteArray(obj));
		return newObj;
	}
	/**
	 * 取得物件的ByteArray(物件必須implement Serializable)
	 */
	public byte[] getObjectByteArray(Object obj) throws Exception {
		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		java.io.ObjectOutputStream oout = new java.io.ObjectOutputStream(bout);
		oout.writeObject(obj);
		oout.close();
		return bout.toByteArray();
	}
	/**
	 * 由ByteArray取得物件(物件必須implement Serializable)
	 */
	public Object getObject(byte[] byteArr) throws Exception {
		java.io.ByteArrayInputStream bin = new java.io.ByteArrayInputStream(byteArr);
		java.io.ObjectInputStream oin = new java.io.ObjectInputStream(bin);
		return oin.readObject();
	}
	
}
