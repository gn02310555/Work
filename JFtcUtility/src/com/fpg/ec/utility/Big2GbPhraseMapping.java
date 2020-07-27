package com.fpg.ec.utility;

import java.io.InputStream;
import java.util.Hashtable;

import com.fpg.ec.utility.xml.Big2GbPhraseParser;
/**
 * Big5 轉為 Gb2312之字詞對照singleton
 * @see Big2GbPhraseParser
 * @author pc18
 */
public class Big2GbPhraseMapping extends Hashtable {
	private static Big2GbPhraseMapping singleton = new Big2GbPhraseMapping();

	/**
	 * Constructor for Big2Gb.
	 */
	private Big2GbPhraseMapping() {
		super();
		InputStream in = null;
		System.out.println("Big2GbPhraseMapping init....");
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("big2gbphrase.xml");
			this.putAll(new Big2GbPhraseParser().parseXML(in));
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("create Big2GbPhraseMapping failure ! : "+in);
		}
	}

	public static Big2GbPhraseMapping getInstance() {
		return singleton;
	}

}
