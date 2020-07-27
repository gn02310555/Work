package com.fpg.ec.utility.pdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * HTML --> PDF產生器
 */
public class Html2PdfGenerator {
	private static final HashMap availableTagMap = new HashMap();// 可用的Tag Map
	private String strFont = ""; // 字型名稱
	private BaseFont baseFont = null; // 字型
	private Document document = null; //樣板
	private final static float Default_Font_Size = 10f;// 預設字體大小
	private final static int Default_Font_Style = Font.UNDEFINED;// 預設字體Style
	private final static float Default_Multiplied_Leading = 1.33f;// 預設 Multiplied Leading
	private final static int Column_Count = 100;// 欄位數量
	
	static {
		availableTagMap.put(Tag.U, "*");
		availableTagMap.put(Tag.I, "*");
		availableTagMap.put(Tag.B, "*");
		availableTagMap.put(Tag.TABLE, "*");
		availableTagMap.put(Tag.TD, "*");
		availableTagMap.put(Tag.FONT, "*");
		availableTagMap.put(Tag.BR, "*");
		availableTagMap.put(Tag.IMG, "*");
		availableTagMap.put(Tag.COMMENT, "*");
		availableTagMap.put(Tag.CONTENT, "*");
	}
	
	public byte[] generate(String i_html) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Rectangle r = null;
		if (isRotateDocument(i_html)) {
			r = PageSize.A4.rotate();
		} else {
			r = PageSize.A4;
		}
		
		Document document = null;
		if(this.getDocument() == null){
			document = new Document(r, -60, -60, 30, 20);
		}else{
			document = this.getDocument();
		}
		
		byte[] byteReturn = null;
		
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			
			FtcPdfPageEvent event = new FtcPdfPageEvent(writer);
			// 判別是否有頁碼屬性
			Hashtable PageNumberParamenterHT = getPageNumberParamenter(i_html);
			if (PageNumberParamenterHT != null) {
				String strFormat = (String) PageNumberParamenterHT.get("format");
				String strVAlign = (String) PageNumberParamenterHT.get("valign");
				String strHAlign = (String) PageNumberParamenterHT.get("halign");
				String strFontSize = (String) PageNumberParamenterHT.get("fontsize");
				String strPageFooter = (String) PageNumberParamenterHT.get("pagefooter");
				PageNumber pageNumber = new PageNumber(strFormat, strVAlign, strHAlign, strFontSize, strPageFooter);
				event.setPageNumber(pageNumber);
			}
			writer.setPageEvent(event);
			document.open();
			
			// 解析節點
			HtmlParserChecker parserChecker = this.new HtmlParserChecker();
			this.new HtmlParserCallback(parserChecker).parse(i_html);
			// 產生HTML
			this.new HtmlNodeToPdfGenerator(writer, document, parserChecker).generate();
			
			document.close();
			writer.close();
			baos.flush();
			byteReturn = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return byteReturn;
	}
	
	/**
	 * HtmlParser
	 */
	class HtmlParserCallback extends ParserCallback {
		// Debug使用
		private boolean isTraceMode = false;// 是否為Trace Mode
		
		private HtmlParserChecker parserChecker = null;
		
		public HtmlParserCallback(HtmlParserChecker parserChecker) {
			this.parserChecker = parserChecker;
		}
		
		public void parse(String i_html) {
			try {
				int length = i_html.length();
				char c[] = new char[length];
				i_html.getChars(0, length, c, 0);
				CharArrayReader input = new CharArrayReader(c);
				new ParserDelegator().parse(input, this, true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		/**
		 * 開始Tag
		 */
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			trace("handleStartTag: " + t + ", a:" + a + ", pos:" + pos);
			
			// 判斷是否為可用的Tag
			if (!isAvailableTag(t)) {
				trace("Tag: " + t + ", is not available Tag");
				return;
			}
			
			HtmlNode node = new HtmlNode();
			node.setTagType(t);
			node.setAttributeSet(a.copyAttributes());
			
			// 如果沒有前一個Tag 代表為第一個 Tag
			if (parserChecker.getPreviousTagType() == null) {
				parserChecker.setCurrentDepth(HtmlParserChecker.DEPTH_ZERO);
				node.setDepth(parserChecker.getCurrentDepth());
			} else if (HtmlParserChecker.TAG_TYPE_END.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_END 代表深度不變
				// do nothing
			} else if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_START 代表深度加深
				parserChecker.deepenDepth();
			}
			
			node.setDepth(parserChecker.getCurrentDepth());
			// 如果不為深度 DEPTH_ZERO 取出上一個深度的節點
			if (parserChecker.getCurrentDepth() != HtmlParserChecker.DEPTH_ZERO) {
				int parentDepth = parserChecker.getCurrentDepth() - 1;
				HtmlNode parentNode = (HtmlNode) parserChecker.getDepthLastedNodeMap().get(String.valueOf(parentDepth));
				node.setParentNode(parentNode);
				parentNode.getChildrenNode().add(node);
			}
			
			// 塞入每個深度最後一個Node MAP
			String strDepth = String.valueOf(parserChecker.getCurrentDepth());
			parserChecker.getDepthLastedNodeMap().put(strDepth, node);
			
			// 塞入Html Node List
			parserChecker.getHtmlNodeList().addHtmlNode(node);
			// 塞入節點順序
			node.setNodeIndex(parserChecker.getHtmlNodeList().size() - 1);
			
			parserChecker.setPreviousTagType(HtmlParserChecker.TAG_TYPE_START);			
		}

		/**
		 * 結束Tag
		 */
		public void handleEndTag(Tag t, int pos) {
			trace("handleEndTag: " + t + ", pos:" + pos);
			
			// 判斷是否為可用的Tag
			if (!isAvailableTag(t)) {
				trace("Tag: " + t + ", is not available Tag");
				return;
			}
			
			if (HtmlParserChecker.TAG_TYPE_END.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_END 代表深度變淺
				parserChecker.shallowDepth();
			} else if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_START 代表深度不變
				// do nothing
			}
			
			parserChecker.setPreviousTagType(HtmlParserChecker.TAG_TYPE_END);
		}
		
		/**
		 * 簡單Tag
		 */
		public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
			trace("handleSimpleTag: " + t + ", a:" + a + ", pos:" + pos);
			
			// 判斷是否為可用的Tag
			if (!isAvailableTag(t)) {
				trace("Tag: " + t + ", is not available Tag");
				return;
			}
			
			HtmlNode node = new HtmlNode();
			node.setTagType(t);
			node.setAttributeSet(a.copyAttributes());
			
			// 如果沒有前一個Tag 代表為第一個 Tag
			if (parserChecker.getPreviousTagType() == null) {
				parserChecker.setCurrentDepth(HtmlParserChecker.DEPTH_ZERO);
			} else if (HtmlParserChecker.TAG_TYPE_END.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_END 代表深度不變
				// do nothing
			} else if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_START 代表深度加深
				parserChecker.deepenDepth();
			}
			
			node.setDepth(parserChecker.getCurrentDepth());
			// 如果不為深度 DEPTH_ZERO 取出上一個深度的節點
			if (parserChecker.getCurrentDepth() != HtmlParserChecker.DEPTH_ZERO) {
				int parentDepth = parserChecker.getCurrentDepth() - 1;
				HtmlNode parentNode = (HtmlNode) parserChecker.getDepthLastedNodeMap().get(String.valueOf(parentDepth));
				node.setParentNode(parentNode);
				parentNode.getChildrenNode().add(node);
			}
			
			if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果有加深 要減回來
				parserChecker.shallowDepth();
			}
			
			// 塞入Html Node List
			parserChecker.getHtmlNodeList().addHtmlNode(node);
			// 塞入節點順序
			node.setNodeIndex(parserChecker.getHtmlNodeList().size() - 1);
		}

		/**
		 * 註解
		 */
		public void handleComment(char[] data, int pos) {
			String strText = String.valueOf(data);
			trace("handleComment: " + strText + ", pos:" + pos);
			
			HtmlNode node = new HtmlNode();
			node.setTagType(Tag.COMMENT);
			node.setTextContent(strText);
			
			// 如果沒有前一個Tag 代表為第一個 Tag
			if (parserChecker.getPreviousTagType() == null) {
				parserChecker.setCurrentDepth(HtmlParserChecker.DEPTH_ZERO);
			} else if (HtmlParserChecker.TAG_TYPE_END.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_END 代表深度不變
				// do nothing
			} else if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_START 代表深度加深
				parserChecker.deepenDepth();
			}
			
			node.setDepth(parserChecker.getCurrentDepth());
			// 如果不為深度 DEPTH_ZERO 取出上一個深度的節點
			if (parserChecker.getCurrentDepth() != HtmlParserChecker.DEPTH_ZERO) {
				int parentDepth = parserChecker.getCurrentDepth() - 1;
				HtmlNode parentNode = (HtmlNode) parserChecker.getDepthLastedNodeMap().get(String.valueOf(parentDepth));
				node.setParentNode(parentNode);
				parentNode.getChildrenNode().add(node);
			}
			
			if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果有加深 要減回來
				parserChecker.shallowDepth();
			}
			
			// 塞入Html Node List
			parserChecker.getHtmlNodeList().addHtmlNode(node);
			// 塞入節點順序
			node.setNodeIndex(parserChecker.getHtmlNodeList().size() - 1);
		}

		/**
		 * 文字
		 */
		public void handleText(char[] data, int pos) {
			String strText = String.valueOf(data);
			trace("handleText: " + strText + ", pos:" + pos);
			
			HtmlNode node = new HtmlNode();
			node.setTagType(Tag.CONTENT);
			node.setTextContent(strText);
			
			// 如果沒有前一個Tag 代表為第一個 Tag
			if (parserChecker.getPreviousTagType() == null) {
				parserChecker.setCurrentDepth(HtmlParserChecker.DEPTH_ZERO);
			} else if (HtmlParserChecker.TAG_TYPE_END.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_END 代表深度不變
				// do nothing
			} else if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果前一個Tag Type 是 TAG_TYPE_START 代表深度加深
				parserChecker.deepenDepth();
			}
			
			node.setDepth(parserChecker.getCurrentDepth());
			// 如果不為深度 DEPTH_ZERO 取出上一個深度的節點
			if (parserChecker.getCurrentDepth() != HtmlParserChecker.DEPTH_ZERO) {
				int parentDepth = parserChecker.getCurrentDepth() - 1;
				HtmlNode parentNode = (HtmlNode) parserChecker.getDepthLastedNodeMap().get(String.valueOf(parentDepth));
				node.setParentNode(parentNode);
				parentNode.getChildrenNode().add(node);
			}
			
			if (HtmlParserChecker.TAG_TYPE_START.equals(parserChecker.getPreviousTagType())) { // 如果有加深 要減回來
				parserChecker.shallowDepth();
			}
			
			// 塞入Html Node List
			parserChecker.getHtmlNodeList().addHtmlNode(node);
			// 塞入節點順序
			node.setNodeIndex(parserChecker.getHtmlNodeList().size() - 1);
		}
		
		/**
		 * trace 使用
		 */
		private void trace(String message) {
			if (isTraceMode) {
				System.out.println(message);
			}
		}
		
		/**
		 * 判斷是否為可用的Tag
		 */
		private boolean isAvailableTag(Tag t) {
			if (availableTagMap.containsKey(t)) {
				return true;
			}
			
			return false;
		}
	}
	
	/**
	 * HtmlParser Checker
	 */
	class HtmlParserChecker {
		public static final int DEPTH_ZERO = 0;// 深度0
		public static final String TAG_TYPE_START = "S";// 開頭類型Tag
		public static final String TAG_TYPE_END = "E";// 結尾類型Tag
		
		private HtmlNodeList htmlNodeList = new HtmlNodeList();// Html Node List
		private String previousTagType = null;// 前一個Tag類型
		private int currentDepth = DEPTH_ZERO;// 目前深度
		private HashMap depthLastedNodeMap = new HashMap();// 每個深度最後一個Node MAP
		
		public HtmlParserChecker() {
		}
		
		/**
		 * 增加深度
		 */
		public void deepenDepth() {
			setCurrentDepth(currentDepth + 1);
		}
		
		/**
		 * 減少深度
		 */
		public void shallowDepth() {
			setCurrentDepth(currentDepth - 1);
		}
		
        // ######### getter && setter ############################
		public HtmlNodeList getHtmlNodeList() {
			return htmlNodeList;
		}

		public void setHtmlNodeList(HtmlNodeList htmlNodeList) {
			this.htmlNodeList = htmlNodeList;
		}

		public String getPreviousTagType() {
			return previousTagType;
		}

		public void setPreviousTagType(String previousTagType) {
			this.previousTagType = previousTagType;
		}

		public int getCurrentDepth() {
			return currentDepth;
		}

		public void setCurrentDepth(int currentDepth) {
			this.currentDepth = currentDepth;
		}

		public HashMap getDepthLastedNodeMap() {
			return depthLastedNodeMap;
		}

		public void setDepthLastedNodeMap(HashMap depthLastedNodeMap) {
			this.depthLastedNodeMap = depthLastedNodeMap;
		}

		public String getTAG_TYPE_START() {
			return TAG_TYPE_START;
		}

		public String getTAG_TYPE_END() {
			return TAG_TYPE_END;
		}
	}
	
	/**
	 * HTML節點
	 */
	class HtmlNode {		
		private Tag tagType = null;// 標籤類型
		private AttributeSet attributeSet = new SimpleAttributeSet();// 屬性
		private HtmlNode parentNode = null;// 父節點
		private ArrayList childrenNode = new ArrayList();// 子節點清單
		private String textContent = null;// 文字內容
		private int depth;// 深度
		private int nodeIndex;// 節點index
		private boolean isBarcode = false;// 是否為Barcode
		private int barcodeHeight = -1;// Barcode高度
		private boolean isNewPage = false;// 是否為新開頁面
		private boolean isFont = false;// 是否為Font相關
		private boolean isGetLogo = false;// 要LOGO
		
		public HtmlNode() {
		}
		
		/**
		 * 改寫toString
		 */
		public String toString() {
			String newLine = "\r\n";
			StringBuffer sb = new StringBuffer();
			
			sb.append(getAllFieldValue() + newLine);
			if (parentNode == null) {
				sb.append("parentNode == null" + newLine);
			} else {
				sb.append("parentNode.getTag(): " + parentNode.getAllFieldValue()  + newLine);
			}
			if (childrenNode.size() == 0) {
				sb.append("childrenNode.size() == 0" + newLine);
			} else {
				for (int i = 0; i < childrenNode.size(); i++) {
					HtmlNode childNode = (HtmlNode) childrenNode.get(i);
					sb.append("child " + i + ", childNode.getTag(): " + childNode.getAllFieldValue()  + newLine);
				}
			}
			
			return sb.toString();
		}
		
		/**
		 * 取得成員資料
		 */
		public String getAllFieldValue() {
			StringBuffer sb = new StringBuffer();
			
			sb.append("tagType: " + tagType);
			sb.append(", attributeSet: " + attributeSet);
			sb.append(", depth: " + depth);
			sb.append(", textContent: " + textContent);
			sb.append(", nodeIndex: " + nodeIndex);
			
			return sb.toString();
		}
		
		/**
		 * 是否全部兒子都已經處理
		 */
		public boolean isAllChildrenProcess(HashMap elementMap) {
			if (childrenNode.size() == 0) {
				return true;
			}
			
			for (int i = 0; i < childrenNode.size(); i++) {
				HtmlNode node = (HtmlNode) childrenNode.get(i);
				if (!elementMap.containsKey(node)) {
					return false;
				}
			}
			
			return true;
		}
		
        // ######### getter && setter ############################
		
		public Tag getTagType() {
			return tagType;
		}

		public void setTagType(Tag tagType) {
			this.tagType = tagType;
		}

		public AttributeSet getAttributeSet() {
			return attributeSet;
		}

		public void setAttributeSet(AttributeSet attributeSet) {
			this.attributeSet = attributeSet;
		}

		public HtmlNode getParentNode() {
			return parentNode;
		}

		public void setParentNode(HtmlNode parentNode) {
			this.parentNode = parentNode;
		}

		public ArrayList getChildrenNode() {
			return childrenNode;
		}

		public void setChildrenNode(ArrayList childrenNode) {
			this.childrenNode = childrenNode;
		}

		public String getTextContent() {
			return textContent;
		}

		public void setTextContent(String textContent) {
			this.textContent = textContent;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}
		
		public int getNodeIndex() {
			return nodeIndex;
		}

		public void setNodeIndex(int nodeIndex) {
			this.nodeIndex = nodeIndex;
		}
		
		public boolean isBarcode() {
			return isBarcode;
		}

		public void setBarcode(boolean isBarcode) {
			this.isBarcode = isBarcode;
		}
		
		public int getBarcodeHeight() {
			return barcodeHeight;
		}

		public void setBarcodeHeight(int barcodeHeight) {
			this.barcodeHeight = barcodeHeight;
		}
		
		public boolean isNewPage() {
			return isNewPage;
		}

		public void setNewPage(boolean isNewPage) {
			this.isNewPage = isNewPage;
		}
		
		public boolean isGetLogo() {
			return isGetLogo;
		}

		public void setGetLogo(boolean isGetLogo) {
			this.isGetLogo = isGetLogo;
		}

		public boolean isFont() {
			return isFont;
		}

		public void setFont(boolean isFont) {
			this.isFont = isFont;
		}
	}
	
	/**
	 * HTML節點清單
	 */
	class HtmlNodeList {
		private ArrayList list = new ArrayList();
		private HashMap map = new HashMap();
		
		public HtmlNodeList() {
		}
		
		/**
		 * 加入節點
		 */
		public void addHtmlNode(HtmlNode i_obj) {
			list.add(i_obj);
			map.put(list.size() - 1, i_obj);
		}
		
		/**
		 * 取得節點
		 */
		public HtmlNode getHtmlNode(int i_index) {
			HtmlNode objReturn = (HtmlNode) list.get(i_index);
			return objReturn;
		}
		
		/**
		 * 取得節點
		 */
		public HtmlNode getHtmlNode(String key) {
			HtmlNode objReturn = (HtmlNode) map.get(key);
			return objReturn;
		}
		
		/**
		 * 取得size
		 */
		public int size() {
			return list.size();
		}
		
		/**
		 * 改寫toString
		 */
		public String toString() {
			StringBuffer strbReturn = new StringBuffer();
			
			String strNewLine = "\r\n";
			for (int i = 0; i < list.size(); i++) {
				HtmlNode node = (HtmlNode) list.get(i);
				strbReturn.append(strNewLine + node.toString());
			}
			return strbReturn.toString() + "\n";
		}
	}
	
	/**
	 * HtmlNode To Pdf Generator
	 */
	class HtmlNodeToPdfGenerator {		
		// Debug使用
		private boolean isTraceMode = false;// 是否為Trace Mode
		
		private Document document = null;
		private PdfWriter writer = null;
		private HtmlParserChecker parserChecker = null;
		private HashMap elementMap = new HashMap();// Element Map
		private ArrayList needAddToDocumentNodeList = new ArrayList();// 需要加入Document的 Node
		
		public HtmlNodeToPdfGenerator(PdfWriter writer, Document document, HtmlParserChecker parserChecker) {
			this.writer = writer;
			this.document = document;
			this.parserChecker = parserChecker;
		}
		
		/**
		 * 產生PDF
		 */
		public void generate() throws Exception {
			trace("begin generate...");
			
			// 取得過濾後的節點清單
			HtmlNodeList htmlNodeList = parserChecker.getHtmlNodeList();
			
			for (int i = 0; i < htmlNodeList.size(); i++) {
				HtmlNode node = htmlNodeList.getHtmlNode(i);
				trace("i:" + i + ", htmlNode.toString():" + node.toString());
				processHtmlNode(node);
				
				// 如果沒有父節點 代表需要加入document
				if (node.getParentNode() == null) {
					needAddToDocumentNodeList.add(node);
					trace("node.getParentNode() == null: needAddToDocumentList.add(element);, node:" + node); 
					continue;
				}
				// 判定是否全部子節點已處理
				if (node.isAllChildrenProcess(elementMap)) {
					// 處理元素(顯示使用)
					processElement(node);
					trace("processElement(node);");
					continue;
				}
				trace("not need processElement(node)");
			}
			
			// 將待塞入document node list塞入 將待塞入document(須最後加入 否則不會顯示)
			for (int i = 0; i < needAddToDocumentNodeList.size(); i++) {
				HtmlNode node = (HtmlNode) needAddToDocumentNodeList.get(i);
				trace("i:" + i + ", node:" + node);
				processDocumentNode(node);
			}
			
			trace("end generate...");
		}
		
		/**
		 * 處理節點
		 */
		private void processHtmlNode(HtmlNode htmlNode) {			
			// 處理純文字節點
			if (Tag.CONTENT.equals(htmlNode.getTagType())) {
				trace("processPlainTextNode(htmlNode);");
				processPlainTextNode(htmlNode);
				return;
			}
			
			// 處理註解節點
			if (Tag.COMMENT.equals(htmlNode.getTagType())) {
				trace("processCommentNode(htmlNode);");
				processCommentNode(htmlNode);
				return;
			}
			
			// 處理簡單節點
			if (Tag.BR.equals(htmlNode.getTagType())
					|| Tag.IMG.equals(htmlNode.getTagType())
					) {
				trace("processSimpleNode(htmlNode);");
				processSimpleNode(htmlNode);
				return;
			}
			
			// 處理字體節點
			if (Tag.U.equals(htmlNode.getTagType())
					|| Tag.I.equals(htmlNode.getTagType())
					|| Tag.B.equals(htmlNode.getTagType())
					|| Tag.FONT.equals(htmlNode.getTagType())
					) {
				trace("processFontNode(htmlNode);");
				processFontNode(htmlNode);
				return;
			}
			
			// 處理表格節點
			if (Tag.TABLE.equals(htmlNode.getTagType())
					|| Tag.TD.equals(htmlNode.getTagType())
					) {
				trace("processTableNode(htmlNode);");
				processTableNode(htmlNode);
				return;
			}
			
		}
		
		/**
		 * 處理註解節點
		 */
		private void processCommentNode(HtmlNode htmlNode) {
			try {
				String strTextContent = htmlNode.getTextContent();
				if (strTextContent.indexOf("pdf_newpage") > 0) {
					htmlNode.setNewPage(true);
					trace("processCommentNode: htmlNode.setNewPage(true);");
				}
				
				if(strTextContent.indexOf("remove_logo") > 0){
					htmlNode.setGetLogo(false);
				}else{
					htmlNode.setGetLogo(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理純文字節點
		 */
		private void processPlainTextNode(HtmlNode htmlNode) {
			try {
				Font defaultFont = getFont(Default_Font_Size);
				String strTextContent = htmlNode.getTextContent();
				Phrase phrase = null;
				// 是否為Barcode
				if (htmlNode.isBarcode()) {
					PdfContentByte cb = writer.getDirectContent(); 
					int intBarcodeHeight = htmlNode.getBarcodeHeight();
					if (intBarcodeHeight < 0) {
						intBarcodeHeight = 6; 
					}
					Barcode39 code39 = new Barcode39();
					code39.setCode(strTextContent);
					code39.setStartStopText(true);
					code39.setSize(intBarcodeHeight);// 設定barcode size
					BaseColor barColor = new BaseColor(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue());
					BaseColor textColor = new BaseColor(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue());
					Image image39 =  code39.createImageWithBarcode(cb, barColor, textColor);
					phrase = new Phrase(new Chunk(image39, 0, 0));
					trace("processSimpleNode: phrase = new Phrase(new Chunk(image39, 0, 0));");
				} else {
					phrase = new Phrase(strTextContent, defaultFont);
					trace("processPlainTextNode: phrase = new Phrase(strTextContent, defaultFont);");
				}
				
				elementMap.put(htmlNode, phrase);
				trace("processPlainTextNode: elementMap.put(htmlNode, phrase);");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理簡單節點
		 */
		private void processSimpleNode(HtmlNode htmlNode) {
			try {
				Font defaultFont = getFont(Default_Font_Size);
				Phrase phrase = null;
				if (Tag.BR.equals(htmlNode.getTagType())) {
					phrase = new Phrase(Chunk.NEWLINE);
					trace("processSimpleNode: phrase = new Phrase(Chunk.NEWLINE);"); 
				}
				if (Tag.IMG.equals(htmlNode.getTagType())) {
					String strSrc = "";// 一般路徑
					String strPercent = "";// 縮放百分比
					String strWidth = "";// 寬度
					String strHeight = "";// 高度
					String strImgObjContent = "";// 圖疊文字
					String strClassSrc = "";// ClassPath路徑
					AttributeSet a = htmlNode.getAttributeSet();
					Enumeration e = a.getAttributeNames();
					while (e.hasMoreElements()) {
						Object obj = e.nextElement();
						if (obj.toString().equalsIgnoreCase("src")) {
							strSrc = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("percent")) {
							strPercent = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("width")) {
							strWidth = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("height")) {
							strHeight = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("imgobj")) {
							strImgObjContent = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("classsrc")) {
							strClassSrc = (String) a.getAttribute(obj);
						}
					}
					trace("processSimpleNode: strSrc == " + strSrc);
					trace("processSimpleNode: strPercent == " + strPercent);
					trace("processSimpleNode: strWidth == " + strWidth);
					trace("processSimpleNode: strHeight == " + strHeight);
					trace("processSimpleNode: strImgObjContent == " + strImgObjContent);
					trace("processSimpleNode: strClassSrc == " + strClassSrc);
					Image image = null;
					byte buffer[] = null;
					if (strSrc.trim().length() > 0) {// 一般路徑
						InputStream in = new FileInputStream(strSrc);
						buffer = new byte[in.available()];
						in.read(buffer);
						in.close();
						image = Image.getInstance(buffer);
					} else if (strClassSrc.trim().length() > 0) {// ClassPath路徑
						InputStream in = getClass().getClassLoader().getResourceAsStream(strClassSrc);
						buffer = new byte[in.available()];
						in.read(buffer);
						in.close();
						image = Image.getInstance(buffer);
					} else if (strImgObjContent.trim().length() > 0) {// 圖疊文字
						String[] strContent = strImgObjContent.split(",");
						String strFileSource = "";
						String strPurprw = "";
						String strSignDay = "";
						if (strContent != null && strContent.length > 0) {
							strFileSource = strContent[0];
							if (strContent.length > 1) {
								strPurprw = strContent[1];
							}
							if (strContent.length > 2) {
								strSignDay = strContent[2];
							}
							image = combineImageAndText(strFileSource, strPurprw, strSignDay);
						}
					}
					if (image == null) {
						phrase = new Phrase("", defaultFont);
						trace("processSimpleNode: image == null");
					} else {
						image.setAlignment(Element.ALIGN_CENTER);
						if (strPercent.trim().length() > 0) {
							image.scalePercent(Integer.parseInt(strPercent));
						}
						if (strHeight.trim().length() > 0) {
							image.scaleAbsoluteHeight(Integer.parseInt(strHeight));
						}
						if (strWidth.trim().length() > 0) {
							image.scaleAbsoluteWidth(Integer.parseInt(strWidth));
						}
						phrase = new Phrase(new Chunk(image, 0, 0));
						trace("processSimpleNode: phrase = new Phrase(new Chunk(image, 0, 0));");
					}
				}
				
				elementMap.put(htmlNode, phrase);
				trace("processSimpleNode: elementMap.put(htmlNode, chunk);");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理字體節點
		 */
		private void processFontNode(HtmlNode htmlNode) {
			try {
				htmlNode.setFont(true);// 為字體相關
				float fontsize = Default_Font_Size;// 字體大小
				int intFontStyle = Default_Font_Style;// 字體Style
				String strFontColor = "";// 字體顏色
				Phrase phrase = null;
				if (Tag.U.equals(htmlNode.getTagType())) {
					intFontStyle = Font.UNDERLINE;
					trace("processFontNode: intFontStyle = Font.UNDERLINE;"); 
				}
				if (Tag.I.equals(htmlNode.getTagType())) {
					intFontStyle = Font.ITALIC;
					trace("processFontNode: intFontStyle = Font.ITALIC;"); 
				}
				if (Tag.B.equals(htmlNode.getTagType())) {
					intFontStyle = Font.BOLD;
					trace("processFontNode: intFontStyle = Font.BOLD;");
				}
				if (Tag.FONT.equals(htmlNode.getTagType())) {
					AttributeSet a = htmlNode.getAttributeSet();
					Enumeration e = a.getAttributeNames();
					while (e.hasMoreElements()) {
						Object obj = e.nextElement();
						if (obj.toString().equalsIgnoreCase("udsize")) {
							try {
								fontsize = Float.parseFloat((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
								System.out.println("NumberFormatException:udsize");
							}
						} else if (obj.toString().equalsIgnoreCase("color")) {
							strFontColor = (String) a.getAttribute(obj);
						}
					}
					trace("processFontNode: FONT");
				}
				Font font = getFont(fontsize);
				trace("font = getFont(" + fontsize + ");");
				font.setStyle(intFontStyle);
				if (strFontColor.length() > 0) {
					Color color = (Color) (Class.forName("java.awt.Color").getField(strFontColor).get(new Object()));
					trace("Color color = (Color) (Class.forName(\"java.awt.Color\").getField(strFontColor).get(new Object()));");
					BaseColor textColor = new BaseColor(color.getRed(), color.getGreen(), color.getBlue());
					font.setColor(textColor);
				}
				phrase = new Phrase("", font);
				
				elementMap.put(htmlNode, phrase);
				trace("processFontNode: elementMap.put(htmlNode, phrase);");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理表格節點
		 */
		private void processTableNode(HtmlNode htmlNode) {
			try {
				if (Tag.TABLE.equals(htmlNode.getTagType())) {
					PdfPTable table = new PdfPTable(Column_Count);
					table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_TOP);
					trace("processTableNode: PdfPTable table = new PdfPTable((" + Column_Count + ");");
					trace("processTableNode: table.getDefaultCell().setBorder(Rectangle.NO_BORDER);");
					trace("processTableNode: table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_TOP);");
					
					int padding = 1;
					AttributeSet a = htmlNode.getAttributeSet();
					Enumeration e = a.getAttributeNames();
					while (e.hasMoreElements()) {
						Object obj = e.nextElement();
						if (obj.toString().equalsIgnoreCase("padding")) {
							try {
								padding = Integer.parseInt((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
							}
						}
					}
					table.getDefaultCell().setPadding(padding);
					trace("processTableNode: table.getDefaultCell().setPadding(padding);");
					
					elementMap.put(htmlNode, table);
					trace("processTableNode: elementMap.put(htmlNode, table);");
					return;
				}
				if (Tag.TD.equals(htmlNode.getTagType())) {
					PdfPCell cell = new PdfPCell();
					
					String strColor = "";
					int colspan = -1;
					int rowspan = -1;
					int align = -1;
					int valign = -1;
					int border = -1;
					float leading = 0;
					AttributeSet a = htmlNode.getAttributeSet();
					Enumeration e = a.getAttributeNames();
					while (e.hasMoreElements()) {
						Object obj = e.nextElement();
						if (obj.toString().equalsIgnoreCase("colspan")) {
							try {
								colspan = Integer.parseInt((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
								System.out.println("NumberFormatException:colspan");
							}
						} else if (obj.toString().equalsIgnoreCase("rowspan")) {
							try {
								rowspan = Integer.parseInt((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
								System.out.println("NumberFormatException:rowspan");
							}
						} else if (obj.toString().equalsIgnoreCase("class")) {
							String strClass = (String) a.getAttribute(obj);
							if (strClass.toLowerCase().startsWith("b")) {
								try {
									border = Integer.parseInt(strClass.substring(1));
								} catch (NumberFormatException ex) {
									System.out.println("NumberFormatException:b=>" + strClass.substring(1));
								}
							}
						} else if (obj.toString().equalsIgnoreCase("align")) {
							String strAlign = (String) a.getAttribute(obj);
							if (strAlign.equalsIgnoreCase("center")) {
								align = Element.ALIGN_CENTER;
							} else if (strAlign.equalsIgnoreCase("left")) {
								align = Element.ALIGN_LEFT;
							} else if (strAlign.equalsIgnoreCase("right")) {
								align = Element.ALIGN_RIGHT;
							}
						} else if (obj.toString().equalsIgnoreCase("valign")) {
							String strAlign = (String) a.getAttribute(obj);
							if (strAlign.equalsIgnoreCase("middle")) {
								valign = Element.ALIGN_MIDDLE;
							} else if (strAlign.equalsIgnoreCase("top")) {
								valign = Element.ALIGN_TOP;
							} else if (strAlign.equalsIgnoreCase("bottom")) {
								valign = Element.ALIGN_BOTTOM;
							}
						} else if (obj.toString().equalsIgnoreCase("leading")) {
							try {
								leading = Float.parseFloat((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
								System.out.println("NumberFormatException:leading");
							}
						} else if (obj.toString().equalsIgnoreCase("bgcolor")) {
							strColor = (String) a.getAttribute(obj);
						} else if (obj.toString().equalsIgnoreCase("isBarcode")) {
							boolean isBarcode = true;// 是否為Barcode
							int intBarcodeHeight = -1;// Barcode高度
							try {
								intBarcodeHeight = Integer.parseInt((String) a.getAttribute(obj));
							} catch (NumberFormatException ex) {
								intBarcodeHeight = -1;
							}
							// 將子節點設為Barcode
							HtmlNode childNode = (HtmlNode) htmlNode.getChildrenNode().get(0);
							childNode.setBarcode(isBarcode);
							childNode.setBarcodeHeight(intBarcodeHeight);
						}
					}

					// 判斷背景顏色
					HtmlNode trNode = htmlNode.getParentNode();
					String strBgColor = "";
					AttributeSet trAttr = trNode.getAttributeSet();
					Enumeration trEnum = trAttr.getAttributeNames();
					while (trEnum.hasMoreElements()) {
						Object obj = trEnum.nextElement();
						if (obj.toString().equalsIgnoreCase("bgcolor")) {
							strBgColor = (String) a.getAttribute(obj);
						}
					}
					if (strBgColor.length() > 0) {
						try {
							Color color = (Color) (Class.forName("java.awt.Color").getField(strBgColor).get(new Object()));
							trace("Color color = (Color) (Class.forName(\"java.awt.Color\").getField(strBgColor).get(new Object()));");
							BaseColor bgColor = new BaseColor(color.getRed(), color.getGreen(), color.getBlue());
							cell.setBackgroundColor(bgColor);
						} catch (Exception ex) {
						}
					}
					if (border > 0) {
						BaseColor borderColor = new BaseColor(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue());
						cell.setBorderColor(borderColor);
						trace("cell.setBorderColor(Color.black);");
						cell.setBorder(border);
						trace("cell.setBorder(" + border + ");");
					} else {
						cell.setBorder(Rectangle.NO_BORDER);
						trace("cell.setBorder(Rectangle.NO_BORDER);");
					}
					if (colspan < 0) {
						colspan = 1;
						trace("colspan = 1;");
					}
					if (align < 0) {
						align = Element.ALIGN_LEFT;
						trace("align = Element.ALIGN_LEFT;");
					}
					if (valign < 0) {
						valign = Element.ALIGN_MIDDLE;
						trace("valign = Element.ALIGN_MIDDLE;");
					}
					cell.setColspan(colspan);
					if (rowspan > 0) {
						cell.setRowspan(rowspan);
						trace("cell.setRowspan(" + rowspan + ");");
					}
					cell.setHorizontalAlignment(align);
					cell.setVerticalAlignment(valign);
					cell.setLeading(leading, Default_Multiplied_Leading);
					trace("cell.setColspan(" + colspan + ");");
					trace("cell.setHorizontalAlignment(" + align + ");");
					trace("cell.setVerticalAlignment(" + valign + ");");
					trace("cell.setLeading(" + leading + ", " + Default_Multiplied_Leading + ");");
					trace("table.addCell(cell);");
					
					elementMap.put(htmlNode, cell);
					trace("processTableNode: elementMap.put(htmlNode, cell);");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理元素(顯示使用)
		 */
		public void processElement(HtmlNode node) {
			try {
				HtmlNode parentNode = node.getParentNode();				
				Element elment = (Element) elementMap.get(node);
				Element parentElment = (Element) elementMap.get(parentNode);
				
				if (parentElment instanceof PdfPTable) {
					trace("processElement: parentElment instanceof PdfPTable");
					PdfPTable parentTable = (PdfPTable) parentElment;
					PdfPCell cell = (PdfPCell) elment;
					parentTable.addCell(cell);
				} else if (parentElment instanceof PdfPCell) {
					trace("processElement: parentElment instanceof PdfPCell");
					PdfPCell parentCell = (PdfPCell) parentElment;
					if (elment instanceof Phrase) {
						Phrase phrase = (Phrase) elment;
						trace("processElement: phrase:" + phrase);
						if (parentCell.getPhrase() == null) {
							parentCell.setPhrase(phrase);
							trace("processElement: cell.setPhrase(phrase);");
						} else {
							Phrase previousPhrase = parentCell.getPhrase();
							trace("processElement: previousPhrase:" + previousPhrase);
							previousPhrase.add(phrase);
							parentCell.setPhrase(previousPhrase);
							trace("processElement: parentCell.setPhrase(previousPhrase);");
						}
					} else if (elment instanceof PdfPTable) {
						PdfPTable table = (PdfPTable) elment;
						trace("processElement: table:" + table);
						table.setWidthPercentage(100f);// 設定寬度為100%
						parentCell.addElement(table);
						trace("processElement: parentCell.addElement(table);");
					}
				} else if (parentElment instanceof Phrase) {
					trace("processElement: parentElment instanceof Phrase");
					Phrase parentPhrase = (Phrase) parentElment;
					Phrase phrase = (Phrase) elment;
					Font font = phrase.getFont();
					// 字體style 以有設定的為主
					int fontStyle = font.getStyle();
					HtmlNode upperNode = node;
					while (upperNode.getParentNode() != null
							&& fontStyle == Default_Font_Style
							) {
						upperNode = upperNode.getParentNode();
						trace("fontStyle: upperNode:" + upperNode);
						Element upperElement = (Element) elementMap.get(upperNode);
						// 如果上層不是Phrase跳掉
						if (!(upperElement instanceof Phrase)) {
							break;
						}
						Phrase upperPhrase = (Phrase) upperElement;
						// 如果不是Font相關節點 繼續
						if (!upperNode.isFont) {
							continue;
						}
						int upperFontStyle = upperPhrase.getFont().getStyle();
						// 如果相關設定為預設值 繼續
						if (upperFontStyle == Default_Font_Style) {
							continue;
						} else {
							fontStyle = upperFontStyle;
							trace("fontStyle = upperFontStyle:" + upperFontStyle);
						}
					}
					// 字體大小 以有設定的為主
					float fontSize = font.getSize();
					upperNode = node;
					while (upperNode.getParentNode() != null
							&& fontSize == Default_Font_Size
							) {
						upperNode = upperNode.getParentNode();
						trace("fontSize: upperNode:" + upperNode);
						Element upperElement = (Element) elementMap.get(upperNode);
						// 如果上層不是Phrase跳掉
						if (!(upperElement instanceof Phrase)) {
							break;
						}
						Phrase upperPhrase = (Phrase) upperElement;
						// 如果不是Font相關節點 繼續
						if (!upperNode.isFont) {
							continue;
						}
						float upperFontSize = upperPhrase.getFont().getSize();
						// 如果相關設定為預設值 繼續
						if (upperFontSize == Default_Font_Size) {
							continue;
						} else {
							fontSize = upperFontSize;
							trace("fontSize = upperFontSize:" + upperFontSize);
						}
					}
					// 字體顏色 以有設定的為主
					BaseColor textColor = font.getColor();
					upperNode = node;
					while (upperNode.getParentNode() != null
							&& textColor == null
							) {
						upperNode = upperNode.getParentNode();
						trace("color: upperNode:" + upperNode);
						Element upperElement = (Element) elementMap.get(upperNode);
						// 如果上層不是Phrase跳掉
						if (!(upperElement instanceof Phrase)) {
							break;
						}
						Phrase upperPhrase = (Phrase) upperElement;
						// 如果不是Font相關節點 繼續
						if (!upperNode.isFont) {
							continue;
						}
						BaseColor upperTextColor = upperPhrase.getFont().getColor();
						// 如果相關設定為預設值 繼續
						if (upperTextColor == null) {
							continue;
						} else {
							textColor = upperTextColor;
							trace("color = upperTextColor:" + upperTextColor);
						}
					}
					
					// 設定字體Style
					font.setStyle(fontStyle);
					trace("font.setStyle(fontStyle):" + fontStyle);
					// 設定字體大小
					font.setSize(fontSize);
					trace("font.setSize(fontSize):" + fontSize);
					// 設定字體顏色
					if (textColor != null) {
						font.setColor(textColor);
						trace("font.setColor(textColor):" + textColor);
					}
					// 重新塞入字體設定
					phrase.setFont(font);
					trace("parentPhrase:" + parentPhrase);
					trace("phrase:" + phrase);
					parentPhrase.add(phrase);
					trace("parentPhrase.add(phrase);");
				}
				
				// 判定是否全部子節點已處理
				if (parentNode.isAllChildrenProcess(elementMap)) {
					// 如果parentNode為空跳掉
					if (parentNode.getParentNode() == null) {
						trace("parentNode.getParentNode() == null");
						return;
					}
					trace("processElement: processElement(parentNode);");
					// 處理元素(顯示使用)
					processElement(parentNode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 處理document node
		 */
		public void processDocumentNode(HtmlNode node) {
			try {
				// 註解欄位要另外處理
				if (Tag.COMMENT.equals(node.getTagType())) {
					// 是否為換頁
					if (node.isNewPage()) {
						document.newPage();
						trace("processDocumentNode: document.newPage();");
						
						//設定LOGO，若不需要設定為false
						if(node.isGetLogo()){
							//設定LOGO位置
							String strXPos = "30";
							String strYPos = (document.getPageSize().getHeight() - 30) + "";
							String strScalePercent = "40";
							
							//LOGO存放位置
							String strAttachPictureName = "com/fpg/ec/utility/pdf/pdf_logo.gif";
							int intXPos = Float.valueOf(strXPos).intValue();
							int intYPos = Float.valueOf(strYPos).intValue();
							int intScalePercent = Integer.parseInt(strScalePercent);

							byte buffer[] = null;

							try {
								InputStream in = getClass().getClassLoader().getResourceAsStream(strAttachPictureName);
								buffer = new byte[in.available()];
								in.read(buffer);
								in.close();
							} catch (Exception ee) {
								ee.printStackTrace();
							}

							Image AttachPicture = Image.getInstance(buffer);
							AttachPicture.setAbsolutePosition(intXPos, intYPos);
							AttachPicture.scalePercent(intScalePercent);
							document.add(AttachPicture);
							trace("processDocumentNode: document.add(AttachPicture);");
						}
				
						
						return;
					}
					
					trace("processCommentNode: do nothing, content:" + node.getTextContent());
					return;
				}
				
				Element element = (Element) elementMap.get(node);
				trace("processDocumentNode: element:" + element);
				document.add(element);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * trace 使用
		 */
		private void trace(String message) {
			if (isTraceMode) {
				System.out.println(message);
			}
		}
	}
	
	/**
	 * 取得字型
	 */
	private Font getFont(float i_size) {
		if (baseFont == null) {
			try {
				if (strFont == null || strFont.length() == 0) {
					strFont = "com/fpg/ec/utility/pdf/msming.ttf";
				}
				baseFont = BaseFont.createFont(strFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Font(baseFont, i_size);
	}
	
	/**
	 * 圖疊文字功能 
	 * http://blog.yslifes.com/archives/527
	 * http://www.cnblogs.com/dotjava/archive/2008/09/04/1283963.html
	 */
	private Image combineImageAndText(String strFileSource, String strContent, String strSignDay) {
		Image iTextImage = null;
		BufferedImage bufferedImage = null;
		
		/**
		 * 如果沒有資料 回傳空圖片 避免出錯
		 */
		if (strFileSource.length() == 0
				|| strContent.length() == 0
				|| strSignDay.length() == 0
				) {
			return null;
		}
		
		try {
			File imageFile = new File(strFileSource);
			java.awt.Image awtImage = ImageIO.read(imageFile);
			
			int intImageX = 65;
			int intImageY = 65;

			bufferedImage = new BufferedImage(intImageX, intImageY, BufferedImage.TYPE_INT_RGB);

			Graphics2D graphics2D = bufferedImage.createGraphics();
			
			// 使得背景透明
			bufferedImage = graphics2D.getDeviceConfiguration().createCompatibleImage(intImageX, intImageY, Transparency.TRANSLUCENT);
			graphics2D.dispose();
			graphics2D = bufferedImage.createGraphics();
			graphics2D.drawImage(awtImage, 0, 0, intImageX, intImageY, null); 
			
			
			String strLastName = strContent.substring(0, 1);
			String strFirstName = strContent.substring(1);
			int intYear = Integer.parseInt(strSignDay.substring(0, 4)) - 1911;
			int intMonth = Integer.parseInt(strSignDay.substring(4, 6));
			int intDate = Integer.parseInt(strSignDay.substring(6, 8));
			String strDateStr = "" + intYear + "." + intMonth + "." + intDate;
			
			graphics2D.setColor(Color.BLACK);
			java.awt.Font font = new java.awt.Font("com/fpg/ec/utility/pdf/msming.ttf", java.awt.Font.PLAIN, 11);
			graphics2D.setFont(font);
			
			/**
			 * 疊字
			 */
			// 姓
			graphics2D.drawString(strLastName, (int) (intImageX * 0.4), (int) (intImageY * 0.3));
			// 日期
			if (strDateStr.length() == 9) {
				graphics2D.drawString(strDateStr, (int) (intImageX * 0.14), (int) (intImageY * 0.58));
			} else if (strDateStr.length() == 8) {
				graphics2D.drawString(strDateStr, (int) (intImageX * 0.17), (int) (intImageY * 0.58));
			} else if (strDateStr.length() == 7) {
				graphics2D.drawString(strDateStr, (int) (intImageX * 0.21), (int) (intImageY * 0.58));
			} else if (strDateStr.length() == 6) {
				graphics2D.drawString(strDateStr, (int) (intImageX * 0.24), (int) (intImageY * 0.58));
			}
			// 名
			if (strFirstName.length() == 1) {
				graphics2D.drawString(strFirstName, (int) (intImageX * 0.4), (int) (intImageY * 0.8));
			} else {
				strFirstName = strFirstName.substring(0, 1) + " " + strFirstName.substring(1);
				graphics2D.drawString(strFirstName, (int) (intImageX * 0.27), (int) (intImageY * 0.8));
			}

			ImageIcon imageIcon = new ImageIcon(bufferedImage);
			awtImage = imageIcon.getImage();
			iTextImage = Image.getInstance(awtImage, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return iTextImage;
	}
	
	/**
	 * 取得頁碼參數
	 */
	public Hashtable getPageNumberParamenter(String i_html) {
		Hashtable ParameterHT = null;
		
		String startTag = "<pagenumber>";
		String endTag = "</pagenumber>";
		int start_pos = i_html.indexOf(startTag);
		int end_pos = i_html.indexOf(endTag);
		
		if (start_pos < 0 || end_pos < 0) {
			return null;
		} else {
			ParameterHT = new Hashtable();
			String strParamenter = i_html.substring(start_pos + startTag.length(), end_pos);
			String[] parameters = strParamenter.split(";");
			for (int i = 0; i < parameters.length; i++) {
				String[] args = parameters[i].split("=");
				if (args.length > 1) {
					ParameterHT.put(args[0].trim().toLowerCase(), args[1].trim());
				}
			}
		}
		
		return ParameterHT;
	}
	
	/**
	 * 判斷頁面是否為橫向
	 */
	public boolean isRotateDocument(String i_html) {
		if (i_html.indexOf("<!--###rotate_document###-->") >= 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 1. 頁碼處理程式 iText in Action eBook - Page X of Y
	 * http://itextpdf.com/examples/iia.php?id=104
	 * 2. 浮水印處理 iText in Action eBook - WaterMark
	 * http://itextpdf.com/examples/iia.php?id=105
	 */
	class FtcPdfPageEvent extends PdfPageEventHelper {
		private PageNumber pageNumber;
		private BaseFont watermark_base_font;
		private PdfWriter writer = null;
		
		public FtcPdfPageEvent(PdfWriter writer) {
			this.writer = writer;
		}
		
		/**
		 * 初始化
		 */
		public void init() {
        	// 頁碼
        	if (pageNumber != null) {
            	try {
            		pageNumber.setTotal_page_number(writer.getDirectContent().createTemplate(13, 13));
            		if (strFont == null || strFont.length() == 0) {
    					strFont = "com/fpg/ec/utility/pdf/msming.ttf";
    				}
//    				pageNumber.setPage_base_font(BaseFont.createFont(strFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
        	}
        	
        	// 產生浮水印字體
        	try {
				watermark_base_font = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
		}
        
        /**
         * Creates the PdfTemplate that will hold the total number of pages.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
        	init();
        }
        
        public void onStartPage(PdfWriter writer, Document document) {
        	
        }
 
        /**
         * Adds a header to every page
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
        	// 頁碼
        	if (pageNumber != null) {
            	try {
                	PdfPTable table = new PdfPTable(1);
                    table.setWidths(new int[]{1});
                    table.setTotalWidth(document.getPageSize().getWidth());
                    table.setLockedWidth(true);
                    table.getDefaultCell().setFixedHeight(pageNumber.getPage_number_height());
                    
                    // Main Page Number Cell
                    PdfPCell cell = new PdfPCell();
                    cell.setBorderWidth(PdfPCell.NO_BORDER);
                    // 水平位置
                    cell.setHorizontalAlignment(pageNumber.getPage_number_halign());
                    Phrase phrase = new Phrase();
                    phrase.setFont(new Font(pageNumber.getPage_base_font(), pageNumber.getFont_size()));
                    Image image = Image.getInstance(pageNumber.getTotal_page_number());
                    float widthScale = image.getWidth() / image.getHeight();
                    image.scaleAbsoluteHeight(pageNumber.getFont_size() * pageNumber.getImage_to_font_ratio());
                    image.scaleAbsoluteWidth(pageNumber.getFont_size() * widthScale * pageNumber.getImage_to_font_ratio());
                    
                    // 頁尾文字內容
                    if(pageNumber.getPage_footer_comt().length() > 0) {
                    	phrase.add(pageNumber.getPage_footer_comt() + "\n");
                    }
                    
                    // 處理頁碼顯示問題
                    String[] pageNumberSringFormat = pageNumber.getPageNumberSringFormat();
                    if (pageNumberSringFormat.length > 1) {
                    	phrase.add(new Chunk(pageNumberSringFormat[0].replaceAll(pageNumber.getCurrent_page_mark(), String.valueOf(writer.getPageNumber()))));
                    	phrase.add(new Chunk(image, 0, 0));
                    	phrase.add(new Chunk(pageNumberSringFormat[1].replaceAll(pageNumber.getCurrent_page_mark(), String.valueOf(writer.getPageNumber()))));
                    } else {
                    	phrase.add(new Chunk(pageNumberSringFormat[0].replaceAll(pageNumber.getCurrent_page_mark(), String.valueOf(writer.getPageNumber()))));
                    }
                    cell.setPhrase(phrase);
                    table.addCell(cell);
                    
                    // 垂直位置
                    if (pageNumber.getPage_number_valign() == Element.ALIGN_TOP) {
                    	table.writeSelectedRows(0, -1, 0, document.getPageSize().getHeight(), writer.getDirectContent());
                    } else {
                    	table.writeSelectedRows(0, -1, 0, pageNumber.getPage_number_height(), writer.getDirectContent());
                    }
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
        	}        	
        	
        	// 浮水印
        	try {
				PdfContentByte cb = writer.getDirectContent();
				PdfGState gs = new PdfGState();
				cb.saveState();
				gs.setFillOpacity(0.1f);
				cb.setGState(gs);
				cb.beginText();
				cb.setFontAndSize(watermark_base_font, 128);
				cb.showTextAligned(Element.ALIGN_CENTER, "  FTC", document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2 , 45);
				cb.endText();
				cb.restoreState();
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
 
        /**
         * Fills out the total number of pages before the document is closed.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
        	// 頁碼
        	if (pageNumber != null) {
            	try {
            		Phrase phrase = new Phrase(String.valueOf(writer.getPageNumber() - 1));
                	ColumnText.showTextAligned(pageNumber.getTotal_page_number(), Element.ALIGN_CENTER, phrase, 5, 0, 0);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
        	}
        }
        
        
        public PageNumber getPageNumber(PageNumber pageNumber) {
        	return this.pageNumber;
        }
        
        public void setPageNumber(PageNumber pageNumber) {
        	this.pageNumber = pageNumber;
        }
    }
	
	/**
	 * 頁碼物件
	 * @author N000101814
	 *
	 */
	class PageNumber {
		private String current_page_mark = "X";// 現在頁碼標記
		private String total_page_mark = "Y";// 總頁碼標記
		private String page_number_format = current_page_mark + " / " + total_page_mark;// 頁碼文字
		private PdfTemplate total_page_number;// 總頁碼PdfTemplate
        private float page_number_height = 20f;// 頁碼Cell高度
        private BaseFont page_base_font;// 頁碼的BaseFont
        private float font_size = 8f;// 頁碼的BaseFont的size
        private float image_to_font_ratio = 1.33f;// Image 轉為 文字 的Ratio
        private int page_number_valign = Element.ALIGN_BOTTOM;// 頁碼的水平對齊
        private int page_number_halign = Element.ALIGN_CENTER;// 頁碼的垂直對齊
        private String page_footer_comt = ""; //頁尾文字內容
        
        public PageNumber(String page_number_format, String page_number_valign, String page_number_halign, String font_size, String page_footer_comt) {
        	// 頁碼格式
        	if (page_number_format != null && page_number_format.length() > 0) {
        		if (page_number_format.indexOf(current_page_mark) < 0) {
        			throw new RuntimeException("Page Number Format Must Contains Current Page Mark(X)");
        		}
        		this.page_number_format = page_number_format;
        	}
        	// 垂直對齊
        	if (page_number_valign != null && page_number_valign.length() > 0) {
        		if (page_number_valign.equalsIgnoreCase("top")) {
        			this.page_number_valign = Element.ALIGN_TOP;
        		} else if (page_number_valign.equalsIgnoreCase("bottom")) {
        			this.page_number_valign = Element.ALIGN_BOTTOM;
        		}
        	}
        	// 水平對齊
        	if (page_number_halign != null && page_number_halign.length() > 0) {
        		if (page_number_halign.equalsIgnoreCase("left")) {
        			this.page_number_halign = Element.ALIGN_LEFT;
        		} else if (page_number_halign.equalsIgnoreCase("center")) {
        			this.page_number_halign = Element.ALIGN_CENTER;
        		} else if (page_number_halign.equalsIgnoreCase("right")) {
        			this.page_number_halign = Element.ALIGN_RIGHT;
        		}
        	}
        	// 字體大小
        	if (font_size != null && font_size.length() > 0) {
        		try {
        			float float_font_size = Float.valueOf(font_size).floatValue();
        			if (float_font_size > this.font_size) {
        				float scale = float_font_size / this.font_size;
        				this.page_number_height = page_number_height * scale / image_to_font_ratio;
        			}
        			this.font_size = float_font_size;
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        	
        	//頁尾文字內容
        	if(page_footer_comt != null && page_footer_comt.length() > 0) {
        		this.page_footer_comt = page_footer_comt;
        	}
        }
        
        /**
         * 取得Page Number Format String Array
         */
        public String[] getPageNumberSringFormat() {
        	String[] pageNumberSringFormat = null;
        	
        	int format_length = page_number_format.length();
        	int total_page_pos = page_number_format.indexOf(total_page_mark);
        	if (total_page_pos >= 0) {
        		pageNumberSringFormat = new String[2];
        		if (total_page_pos == 0) {
        			pageNumberSringFormat[0] = "";
        			pageNumberSringFormat[1] = page_number_format.substring(1);
        		} else if (total_page_pos == format_length) {
        			pageNumberSringFormat[0] = page_number_format.substring(0, format_length - 1);
        			pageNumberSringFormat[1] = "";
        		} else {
        			pageNumberSringFormat[0] = page_number_format.substring(0, total_page_pos);
        			pageNumberSringFormat[1] = page_number_format.substring(total_page_pos + 1);
        		}
        	} else {
        		pageNumberSringFormat = new String[]{page_number_format};
        	}
        	
        	return pageNumberSringFormat;
        }
        
        public String getCurrent_page_mark() {
			return current_page_mark;
		}

		public void setCurrent_page_mark(String currentPageMark) {
			current_page_mark = currentPageMark;
		}

		public String getTotal_page_mark() {
			return total_page_mark;
		}

		public void setTotal_page_mark(String totalPageMark) {
			total_page_mark = totalPageMark;
		}

		public String getPage_number_format() {
			return page_number_format;
		}

		public void setPage_number_format(String pageNumberFormat) {
			page_number_format = pageNumberFormat;
		}

		public PdfTemplate getTotal_page_number() {
			return total_page_number;
		}

		public void setTotal_page_number(PdfTemplate totalPageNumber) {
			total_page_number = totalPageNumber;
		}

		public float getPage_number_height() {
			return page_number_height;
		}

		public void setPage_number_height(float pageNumberHeight) {
			page_number_height = pageNumberHeight;
		}

		public BaseFont getPage_base_font() {
			return page_base_font;
		}

		public void setPage_base_font(BaseFont pageBaseFont) {
			page_base_font = pageBaseFont;
		}

		public float getFont_size() {
			return font_size;
		}

		public void setFont_size(float fontSize) {
			font_size = fontSize;
		}

		public float getImage_to_font_ratio() {
			return image_to_font_ratio;
		}

		public void setImage_to_font_ratio(float imageToFontRatio) {
			image_to_font_ratio = imageToFontRatio;
		}

		public int getPage_number_valign() {
			return page_number_valign;
		}

		public void setPage_number_valign(int pageNumberValign) {
			page_number_valign = pageNumberValign;
		}

		public int getPage_number_halign() {
			return page_number_halign;
		}

		public void setPage_number_halign(int pageNumberHalign) {
			page_number_halign = pageNumberHalign;
		}
		
		public String getPage_footer_comt() {
			return page_footer_comt;
		}

		public void setPage_footer_comt(String pagefootercomt) {
			page_footer_comt = pagefootercomt;
		}
	}

	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
