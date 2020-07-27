package com.fpg.ec.utility.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS 排除器(Request 包裹器) 
 * 取代特定XSS內容 參考下列網址
 * http://greatwebguy.com/programming/java/simple-cross-site-scripting-xss-servlet-filter/
 * 
 * @author N000101814
 * 
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
	public static final String WYGIWYS_QUERYSTRING = "q=WYGIWYS_QUERYSTRING";
	
	/**
	 * 搜尋RegExp清單
	 */
	private static ArrayList searchRegexpTextListForGet = new ArrayList();
	private static ArrayList searchRegexpTextListForWygiwys = new ArrayList();
	private static ArrayList searchRegexpTextListForNoneGet = new ArrayList();
	private static ArrayList searchRegexpTextListForAll = new ArrayList();

	/**
	 * 取代清單
	 */
	private static ArrayList searchReplaceTextListForGet = new ArrayList();
	private static ArrayList searchReplaceTextListForWygiwys = new ArrayList();
	private static ArrayList searchReplaceTextListForNoneGet = new ArrayList();
	private static ArrayList searchReplaceTextListForAll = new ArrayList();

	private boolean isGet = false;// 是否為GET
	private boolean isWygiwys = false;// 是否為所見即所得編輯器的資料
	private boolean isAllClean = false;// 是否不分GET POST 都擋一樣(購物網前台使用)

	static {
		init();
	}
	
	/**
	 * 初始化
	 */
	public static void init() {
		/**
		 * 清除Get XSS內容
		 * 取代以下資料
		 * 1. & 取代為 &amp;
		 * 2. < 取代為 &lt;
		 * 3. > 取代為 &gt;
		 * 4. " 取代為 &quot;
		 * 5. ' 取代為 &#39;
		 */
		addRegexpReplaceData(searchRegexpTextListForGet, searchReplaceTextListForGet, "&", "＆");
		addRegexpReplaceData(searchRegexpTextListForGet, searchReplaceTextListForGet, "<", "＜");
		addRegexpReplaceData(searchRegexpTextListForGet, searchReplaceTextListForGet, ">", "＞");
		addRegexpReplaceData(searchRegexpTextListForGet, searchReplaceTextListForGet, "\"", "＂");
		addRegexpReplaceData(searchRegexpTextListForGet, searchReplaceTextListForGet, "'", "’");
		
		/**
		 * 清除Wygiwys XSS內容
		 * 取代以下資料(不分大小寫)將  < 取代為 &lt; , > 取代為 &gt; js語法需用空白隔開並轉全型
		 * Tags: 
		 * 1.	<script>
		 * 2.	</script>
		 * 3.	<frameset>
		 * 4.	</frameset>
		 * 5.	<frame>
		 * 6.	</frame>
		 * 7.	<noframes>
		 * 8.	</noframes>
		 * 9.	<iframe>
		 * 10.	</iframe>
		 * 11.	<form>
		 * 12.	</form>
		 * 13.	<body>
		 * 14.	</body>
		 * 15.	<html>
		 * 16.	</html>
		 * 17.	<embed>
		 * 18.	</embed>
		 * 19.	<applet>
		 * 20.	</applet>
		 * 21.	<audio>
		 * 22.	</audio>
		 * 23.	<canvas>
		 * 24.	</canvas>
		 * 25.	<command>
		 * 26.	</command>
		 * 27.	<video>
		 * 28.	</video>
		 * 
		 * Scripts:
		 * 1.	javascript:
		 * 2.	alert(
		 * 3.	document.
		 * 4.	.location
		 * 5.	window.
		 * 6.	jQuery.
		 * 7.	jQuery(
		 * 8.	$.
		 * 9.	$(
		 * 10.	eval(
		 * 11.	onclick=
		 * 12.	ondblclick=
		 * 13.	onmousedown=
		 * 14.	onmouseup=
		 * 15.	onmouseover=
		 * 16.	onmousemove=
		 * 17.	onmouseout=
		 * 18.	onkeydown=
		 * 19.	onkeypress=
		 * 20.	onkeyup=
		 * 21.	onload=
		 * 22.	onunload=
		 * 23.	onabort=
		 * 24.	onerror=
		 * 25.	onresize=
		 * 26.	onscroll=
		 * 27.	onselect=
		 * 28.	onchange=
		 * 29.	onsubmit=
		 * 30.	onreset=
		 * 31.	onfocus=
		 * 32.	onblur=
		 * 33.	.innerHTML
		 * 34.	.outerHTML 
		 * 35.  vbscript:
		 * 36. expression(
		 */
		// Tags
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<script.*?>", "&lt;script&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</script.*?>", "&lt;/script&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<frameset.*?>", "&lt;frameset&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</frameset.*?>", "&lt;/frameset&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<frame.*?>", "&lt;frame&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</frame.*?>", "&lt;/frame&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<noframes.*?>", "&lt;noframes&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</noframes.*?>", "&lt;noframes&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<iframe.*?>", "&lt;iframe&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</iframe.*?>", "&lt;/iframe&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<form.*?>", "&lt;form&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</form.*?>", "&lt;/form&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<body.*?>", "&lt;body&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</body.*?>", "&lt;/body&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<html.*?>", "&lt;html&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</html.*?>", "&lt;/html&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<embed.*?>", "&lt;embed&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</embed.*?>", "&lt;/embed&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<applet.*?>", "&lt;applet&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</applet.*?>", "&lt;/applet&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<audio.*?>", "&lt;audio&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</audio.*?>", "&lt;/audio&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<canvas.*?>", "&lt;canvas&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</canvas.*?>", "&lt;/canvas&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<command.*?>", "&lt;command&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</command.*?>", "&lt;/command&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)<video.*?>", "&lt;video&gt;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)</video.*?>", "&lt;/video&gt;");
		// Scripts
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)javascript\\s*?:", "java script&#58;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)alert\\s*?\\(", "ａｌｅｒｔ &#40;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)document\\s*?\\.", "ｄｏｃｕｍｅｎｔ &#46;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)\\.\\s*?location", "&#46; ｌｏｃａｔｉｏｎ");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)window\\s*?\\.", "ｗｉｎｄｏｗ &#46;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)jQuery\\s*?\\.", "ｊｑｕｅｒｙ &#46;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)jQuery\\s*?\\(", "ｊｑｕｅｒｙ &#40;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "\\$\\s*?\\.", "&#36;&#46;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "\\$\\s*?\\(", "&#36;&#40;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)eval\\s*?\\(", "ｅｖａｌ &#40;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onclick\\s*?\\=", "ｏｎｃｌｉｃｋ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)ondblclick\\s*?\\=", "ｏｎｄｂｌｃｌｉｃｋ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onmousedown\\s*?\\=", "ｏｎｍｏｕｓｅｄｏｗｎ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onmouseup\\s*?\\=", "ｏｎｍｏｕｓｅｕｐ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onmouseover\\s*?\\=", "ｏｎｍｏｕｓｅｏｖｅｒ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onmousemove\\s*?\\=", "ｏｎｍｏｕｓｅｍｏｖｅ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onmouseout\\s*?\\=", "ｏｎｍｏｕｓｅｏｕｔ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onkeydown\\s*?\\=", "ｏｎｋｅｙｄｏｗｎ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onkeypress\\s*?\\=", "ｏｎｋｅｙｐｒｅｓｓ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onkeyup\\s*?\\=", "ｏｎｋｅｙｕｐ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onload\\s*?\\=", "ｏｎｌｏａｄ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onunload\\s*?\\=", "ｏｎｕｎｌｏａｄ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onabort\\s*?\\=", "ｏｎａｂｏｒｔ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onerror\\s*?\\=", "ｏｎｅｒｒｏｒ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onresize\\s*?\\=", "ｏｎｒｅｓｉｚｅ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onscroll\\s*?\\=", "ｏｎｓｃｒｏｌｌ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onselect\\s*?\\=", "ｏｎｓｅｌｅｃｔ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onchange\\s*?\\=", "ｏｎｃｈａｎｇｅ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onsubmit\\s*?\\=", "ｏｎｓｕｂｍｉｔ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onreset\\s*?\\=", "ｏｎｒｅｓｅｔ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onfocus\\s*?\\=", "ｏｎｆｏｃｕｓ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)onblur\\s*?\\=", "ｏｎｂｌｕｒ=");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)\\.\\s*?innerHTML", "&#46; ｉｎｎｅｒＨＴＭＬ");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)\\.\\s*?outerHTML", "&#46; ｏｕｔｅｒＨＴＭＬ");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)vbscript\\s*?:", "vb script&#58;");
		addRegexpReplaceData(searchRegexpTextListForWygiwys, searchReplaceTextListForWygiwys, "(?i)expression\\s*?\\(", "ｅｘｐｒｅｓｓｉｏｎ &#40;");
		/**
		 * 清除非Get XSS內容
		 * 加入清除Wygiwys XSS內容 並增加取代下面資料
		 * 1. <img>
		 */
		searchRegexpTextListForNoneGet.addAll(searchRegexpTextListForWygiwys);
		searchReplaceTextListForNoneGet.addAll(searchReplaceTextListForWygiwys);
		addRegexpReplaceData(searchRegexpTextListForNoneGet, searchReplaceTextListForNoneGet, "(?i)<img.*?>", "&lt;img&gt;");

		/**
		 * 清除所有
		 */
		searchRegexpTextListForAll.addAll(searchRegexpTextListForNoneGet);
		searchReplaceTextListForAll.addAll(searchReplaceTextListForNoneGet);
		searchRegexpTextListForAll.addAll(searchRegexpTextListForGet);
		searchReplaceTextListForAll.addAll(searchReplaceTextListForGet);
	}
	
	
	public XSSRequestWrapper(HttpServletRequest request, boolean i_isAllClean) {
		this(request);
		if (i_isAllClean){
			isAllClean = true;
		}
	}

	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
		// 判別是否為所見即所得編輯器
		if (request.getMethod().equalsIgnoreCase("GET")) {
			isGet = true;
		} else {
			isGet = false;
		}
		// 判別是否為所見即所得編輯器
		if (request.getQueryString() != null
				&& request.getQueryString().indexOf(WYGIWYS_QUERYSTRING) >= 0
				) {
			isWygiwys = true;
		} else {
			isWygiwys = false;
		}
	}
	
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] cleanXSSValues = new String[count];
		for (int i = 0; i < count; i++) {
			if (isAllClean){				
				cleanXSSValues[i] = cleanAllXSS(values[i]);
			}else if (isGet) {
				cleanXSSValues[i] = cleanGetXSS(values[i]);
			} else if (isWygiwys) {
				cleanXSSValues[i] = cleanWygiwysXSS(values[i]);
			} else {
				cleanXSSValues[i] = cleanNoneGetXSS(values[i]);
			}
		}
		return cleanXSSValues;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		String cleanXSSValue = "";
		if (isAllClean){				
			cleanXSSValue = cleanAllXSS(value);
		}else if (isGet) {
			cleanXSSValue = cleanGetXSS(value);
		} else if (isWygiwys) {
			cleanXSSValue = cleanWygiwysXSS(value);
		} else {
			cleanXSSValue = cleanNoneGetXSS(value);
		}
		
		return cleanXSSValue;
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null)
			return null;
		return cleanNoneGetXSS(value);
	}
	
	/**
	 * 清除Get XSS內容
	 */
	private String cleanGetXSS(String value) {
		for (int i = 0; i < searchRegexpTextListForGet.size(); i++) {
			String regexpText = (String) searchRegexpTextListForGet.get(i);
			String ReplaceText = (String) searchReplaceTextListForGet.get(i);
			value = value.replaceAll(regexpText, ReplaceText);
		}
		
		return value;
	}

	/**
	 * 清除Wygiwys XSS內容
	 */
	private String cleanWygiwysXSS(String value) {
		for (int i = 0; i < searchRegexpTextListForWygiwys.size(); i++) {
			String regexpText = (String) searchRegexpTextListForWygiwys.get(i);
			String ReplaceText = (String) searchReplaceTextListForWygiwys.get(i);
			value = value.replaceAll(regexpText, ReplaceText);
		}
		
		return value;
	}
	
	/**
	 * 清除非Get XSS內容
	 */
	private String cleanNoneGetXSS(String value) {
		for (int i = 0; i < searchRegexpTextListForNoneGet.size(); i++) {
			String regexpText = (String) searchRegexpTextListForNoneGet.get(i);
			String ReplaceText = (String) searchReplaceTextListForNoneGet.get(i);
			value = value.replaceAll(regexpText, ReplaceText);
		}

		return value;
	}

	/**
	 * 清除所有 XSS內容
	 */
	private String cleanAllXSS(String value) {
		for (int i = 0; i < searchRegexpTextListForAll.size(); i++) {
			String regexpText = (String) searchRegexpTextListForAll.get(i);
			String ReplaceText = (String) searchReplaceTextListForAll.get(i);
			value = value.replaceAll(regexpText, ReplaceText);
		}

		return value;
	}
	
	/**
	 * 加入參數
	 */
	private static void addRegexpReplaceData(ArrayList regexpList, ArrayList replaceList, String regexpText, String ReplaceText) {
		regexpList.add(regexpText);
		replaceList.add(ReplaceText);
	}

}
