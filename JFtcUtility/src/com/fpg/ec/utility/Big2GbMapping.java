package com.fpg.ec.utility;

import java.io.InputStream;
import java.util.Hashtable;

//import javax.transaction.Synchronization;
/**
 * Big5 轉為 Gb2312之字元對照singleton
 * @author pc18
 */
public class Big2GbMapping extends Hashtable {
	private static Big2GbMapping singleton = new Big2GbMapping();

	/**
	 * Constructor for Big2Gb.
	 */
	private Big2GbMapping() {
		super();
		InputStream in = null;
		System.out.println("Big2GbMapping init....");
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("big2gb.txt");
			int c = -1;
			int count = 0;
			byte[] bigWord = new byte[2];
			byte[] gbWord = new byte[2];
			while ((c = in.read()) >= 0) {
				//System.out.println(Integer.toHexString(i));
				if (count % 4 == 0) {
					bigWord[0] = (byte) c;
				}
				if (count % 4 == 1) {
					bigWord[1] = (byte) c;
				}
				if (count % 4 == 2) {
					gbWord[0] = (byte) c;
				}
				if (count % 4 == 3) {
					gbWord[1] = (byte) c;
					this.put(new String(bigWord), new String(gbWord));
				}
				count++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create Big2GbMapping failure ! : "+in);
		}
	}

	public static Big2GbMapping getInstance() {
		return singleton;
	}

}
