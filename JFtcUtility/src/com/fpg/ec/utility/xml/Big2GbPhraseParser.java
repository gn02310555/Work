package com.fpg.ec.utility.xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <code>MySAXParser</code>可以從命令列下讀入一個XML文件並且
 *使用SAX標準的機制來剖析該文件。
 *
 * @author <a href="mailto:james@eknow.com.tw">王景浩</a>
 * @version 0.3
 */

public class Big2GbPhraseParser{
    
	/**
	 *<p>
	 *我們將在這裡使用標準的SAX的處理機制(handler)來處理XML
	 *文件
	 *</p>
	 *
	 * @param uri 欲剖析的XML檔案之URI
	 */

	public Hashtable parseXML(String uri) {
		Hashtable hstReturn = null;
		try{
			//System.out.println("正在剖析中的XML檔案: " + uri + "\n");
			
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(uri);
			hstReturn = parseXML(in);
			in.close();
			return hstReturn;

		} catch(IOException ioe){
			System.out.println("檔案讀取錯誤："+ioe.getMessage());
			
		} 
		return null;
	}
	
	/**
	 *<p>
	 *我們將在這裡使用標準的SAX的處理機制(handler)來處理XML
	 *文件
	 *</p>
	 *
	 * @param uri 欲剖析的XML檔案之URI
	 */

	public Hashtable parseXML(InputStream in) {
		try{
			//System.out.println("正在剖析中的XML檔案: \n");
			
			XMLReader parser =
                		XMLReaderFactory.createXMLReader(
                    		"org.apache.xerces.parsers.SAXParser");

			
			//XMLReader parser = new SAXParser();
			//註冊我們所設計的內容處理器
			Big2GbPhraseContentHandler myContentHandler = new Big2GbPhraseContentHandler();
			parser.setContentHandler(myContentHandler);
			//註冊我們的錯誤處理器
			parser.setErrorHandler(new Big2GbPhraseErrorHandler());
			//剖析文件
			parser.parse(new InputSource(in));
			return myContentHandler.getMappingHashtable();

		} catch(IOException ioe){
			System.out.println("檔案讀取錯誤："+ioe.getMessage());
		} catch(SAXException saxe){
			System.out.println("XML剖析錯誤： "+saxe.getMessage());
		}
		return null;
	}

	/**
	 *<p>
	 *主程式使用命令列的參數來指定所要剖析的XML檔案
	 *</p>
	 */

	public static void main(String[] args) {
		//如果參數數目不對，則印出使用說明，並結束程式
		/*
		if( args.length != 1 ){
			System.out.println("請輸入欲剖析的檔案名: " +
					   "java example3_1 [XML URI]");
			System.exit(-1);
		}

		String uri = args[0];*/
		Big2GbPhraseParser myParser = new Big2GbPhraseParser();
		Hashtable hstTemp = myParser.parseXML("big2gbphrase.xml");
	
		for(Enumeration enumTemp = hstTemp.keys(); enumTemp.hasMoreElements();  ){
			
			String s = (String)enumTemp.nextElement();
			System.out.println(s);
			Hashtable hstTemp2 = (Hashtable)hstTemp.get(s);
			for(Enumeration enum2 = hstTemp2.keys(); enum2.hasMoreElements();  ){
				String s2 = (String)enum2.nextElement();
			    System.out.println(s2);
			}	
		}
		
		
		
	}
}

/**
 *<code>MyContentHandler</code>實做<code>org.xml.sax.
 *ContentHandler</code>介面，並且提供提供相關的回叫方法。
 *所有和XML文件內容有關的剖析處理都是在這裡解決。
 */


class Big2GbPhraseContentHandler implements ContentHandler {
    private Hashtable hstReturn = null;
    private boolean isTrans = false;
    private boolean isBig5 = false;
    private boolean isGb = false;
    private String strBig5Phrase = "";
    private String strGbPhrase = "";
	/**保留Locator物件的資訊*/
	private Locator locator;
	
	public Hashtable getMappingHashtable(){
		return hstReturn;
	}

    /**
     *提供一個<code>Locator</code>物件來表示某事件發生在原本
     *文件的何處。
     *<p>
     *所有其他回叫函式在被呼叫時都會更新<code>Locator</code>
     *物件，以表示最新的值。要注意的是，在剖析的過程中，
     *<code>Locator</code>物件一直都會保持著最新的值，但是當
     *剖析的工作(<code>parse()</code>)做完後，它的值就變成沒
     *有意義的東西，千萬不要在剖析以外的時候去存取它。
     *</p>
     *
     * @param locator 一個可以回報任何SAX事件位置的物件。
     *
     * @see org.xml.sax.Locator
     */
    public void setDocumentLocator (Locator locator){
    	//System.out.println("設定Locator物件..");
    	//把SAX的Locator物件放到到我們自己的Locator物件裡
    	this.hstReturn = new Hashtable();
    	this.locator = locator;
    }


    /**
     *接受文件開始的呼叫
     *
     *<p>
     *在使用SAX剖析器時，這個函式是除了
     *{@link #setDocumentLocator setDocumentLocator}以外第一
     *個被呼叫的。在剖析一份XML文件時，只會呼叫本函式一次。
     *和其他定義在<code>ContentHandler</code>的函式一樣，
     *<code>startDocument</code>在遇到錯誤時會丟回一個
     *SAXException物件
     *</p>
     *
     * @exception org.xml.sax.SAXException   任何的SAX例外，
     *有可能是包裝過的其他例外處理
     *
     * @see #endDocument
     */
    public void startDocument ()
	throws SAXException{
		//System.out.println("文件剖析開始->");
	}


    /**
     *接受文件結束呼叫
     *<p>
     * SAX剖析器只會呼叫這個函式一次，同時會是在剖析的過程裡
     *最後被呼叫。一般來說，只有在兩種狀況下剖析器會呼叫本函
     *式，一個是放棄了剖析程序，有可能是是剖析器遇到了它無法
     *回復的錯誤，另一種情況則是遇到輸入資料的結束。
     *</p>
     *
     * @exception org.xml.sax.SAXException   任何的SAX例外，
     *有可能是包裝過的其他例外處理
     *
     * @see #startDocument
     */
    public void endDocument()
	throws SAXException{
		//System.out.println("<-文件剖析結束");
	}


    /**
     *名稱空間宣告的開始
     *
     *<p>
     *其實由這個函式所提供的資訊，對於一般的名稱空間處理來說，
     *是不常用到的，因為只要把<code>http://xml.org/sax/
     *features/namespaces</code>這個功能設定為真(預設為真)，
     *那麼SAX的XML reader就會自動地把元素名稱空間的前置符號
     *替換調。
     *</p>
     *
     * @param prefix 該元素所宣告的名稱空間
     * @param uri 該名稱空間所對應到的統一資源識別符
     * @exception org.xml.sax.SAXException   任何的SAX例外，
     *有可能是包裝過的其他例外處理
     * @see #endPrefixMapping
     * @see #startElement
     */
    public void startPrefixMapping (String prefix, String uri)
	throws SAXException{
		/*
		//System.out.println( "名稱空間對應開始->" +
							"第" + locator.getLineNumber() + "行" +
							"\n\t名稱空間前置字元: " + prefix +
							"\n\t相對應的統一資源識別符: " + uri);
		*/					
	}


    /**
     *名稱空間宣告的結束
     *
     *<p>
     *這個事件永遠都會在元素結束事件發生之後發生。
     *</p>
     *
     * @param prefix 該名稱空間所對應到的前置字串
     * @exception org.xml.sax.SAXException   任何的SAX例外，
     *有可能是包裝過的其他例外處理
     *的其他例外處理
     * @see #startPrefixMapping
     * @see #endElement
     */
    public void endPrefixMapping (String prefix)
	throws SAXException{
		//System.out.println(	"<-名稱空間對應結束" );
	}



    /**
     *接受元素開始的呼叫
     *
     *<p>
     *每一個元素在開始時都會產生這個呼叫，除了該元素的名稱和
     *名稱空間外，它的屬性也會一同地傳入。不過有一些具有特定
     *用途的屬性則不會被傳入，如<code>xmlns:</code>和
     *<code>xsi:</code>
     *</p>
     *
     *<p>
     *一個元素的名稱一共會被分成三個部分來傳入:
     *</p>
     *
     *<ol>
     * <li>名稱空間的統一資源識別符</li>
     *<li>原始名稱</li>
     *<li>加上名稱空間的原始名稱，也就是生名稱</li>
     *</ol>
     *
     * @param uri 名稱空間的統一資源識別符。當該元素沒有名稱
     *空間時或是剖析器沒有啟動名稱空間功能時，此字串就會為空
     * @param localName 沒有前置字串的元素名稱。如果剖析器沒
     *有啟動名稱空間功能時，此字串為空。
     * @param qName 完整的元素名稱。如果生名稱如法取得時，此
     *字串為空。
     * @param atts  附加在這個元素上的全部屬性。如果該元素沒
     *有任何屬性，則會傳回一個空的Attributes物件。注意，不是
     *空(null)。
     *
     * @exception org.xml.sax.SAXException 任何的SAX例外，有
     *可能是包裝過的其他例外處理
     * @see #endElement
     * @see org.xml.sax.Attributes
     */
    public void startElement (String namespaceURI, String localName,
			      String qName, Attributes atts)
	throws SAXException{
		/*
		//列印元素名稱
		System.out.print("元素開始: " + localName);

		if(namespaceURI.equals("")) {
			namespaceURI = "沒有名稱空間";
		}

		//System.out.println(	", 名稱空間: " + namespaceURI +
							", 生名稱: " + qName + ".");

		//列印屬性
		for(int i=0; i<atts.getLength(); i++)
			//System.out.println(	"\t屬性名稱: " + atts.getLocalName(i) +
								" = " + atts.getValue(i) + ".");
		*/
		
		if( localName.equals("phrase")){ 
		    isTrans = atts.getValue(0).equals("yes");
		}else if( localName.equals("big5")){ 
			isBig5 = true;
	    }else if( localName.equals("gb")){ 
	    	isGb = true;
		}	
			
	}


    /**
     *接受元素結束的呼叫
     *
     *<p>
     *SAX剖析器會在XML文件中遇到的每一個元素的結尾呼叫此函式。
     *每一個{@link #startElement startElement}事件一定會有一
     *個相對應的{@link #startElement startElement}事件
     *</p>
     * @param uri 名稱空間的統一資源識別符。當該元素沒有名稱
     *空間時或是剖析器沒有啟動名稱空間功能時，此字串就會為空
     * @param localName 沒有前置字串的元素名稱。如果剖析器沒
     *有啟動名稱空間功能時，此字串為空。
     * @param qName 完整的元素名稱。如果生名稱如法取得時，此
     *字串為空。
     * @param atts  附加在這個元素上的全部屬性。如果該元素沒
     *有任何屬性，則會傳回一個空的Attributes物件。注意，不是
     *空(null)。
     *
     * @exception org.xml.sax.SAXException 任何的SAX例外，有
     *可能是包裝過的其他例外處理
     */
    public void endElement (String namespaceURI, String localName,
			    String qName)
	throws SAXException{
		/*
		//System.out.print("元素結束: " + localName);

		if(namespaceURI.equals("")) {
			namespaceURI = "沒有名稱空間";
		}

		//System.out.println(	", 名稱空間: " + namespaceURI +
							", 生名稱: " + qName + ".");
		*/	
		if( localName.equals("phrase")){ 
			if( isTrans ){
				//依資料長度分類放置Hashtable
				String strLen = strBig5Phrase.length()+"";
				Hashtable hstPhrase = null;
				if( hstReturn.get(strLen) == null ){
					hstPhrase = new Hashtable();
					hstReturn.put(strLen,hstPhrase);
				}else{
					hstPhrase = (Hashtable)hstReturn.get(strLen);
				}
				hstPhrase.put(strBig5Phrase,strGbPhrase);
			}
		    isTrans = false;
		}else if( localName.equals("big5")){ 
			isBig5 = false;
	    }else if( localName.equals("gb")){ 
	    	isGb = false;
		}					
	}


    /**
     *接受字元資料呼叫
     *
     *<p>
     *剖析器會呼叫這個函式來回報在元素裡的一塊塊字元資料。SAX
     *剖析器會傳回在一個在同一區塊裡的連續字串。
     *</p>
     *
     *<p>
     *要注意的是，千萬不要去讀取超過範圍的字串，SAX剖析器對此
     *不做任何保證。
     *</p>
     *
     * @param ch 從XML文件中取得的字元
     * @param start 有效字元在陣列中的開始位置
     * @param length 有效字元的長度
     * @exception org.xml.sax.SAXException 任何的SAX例外，有
     *可能是包裝過的其他例外處理
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     */
    public void characters (char ch[], int start, int length)
	throws SAXException{

		String charData = new String(ch, start, length);
		//System.out.println("字元資料: \"" + charData.trim() + "\"");
		if( isTrans ){
			if( isBig5 ) 
				strBig5Phrase = charData.trim(); 
			else if( isGb ) 
			    strGbPhrase = charData.trim();
		}
	}


    /**
     *接受在元素的內容裡可忽略的空白呼叫
     *<p>
     *一個可驗證的剖析器一定要使用這個函式來回報出現在元素內
     *容中的可忽略空白字元，參見W3C XML 1.0 recommendation,
     *的第2.10小節。至於不具驗證功能的剖析器則不在此限，可以
     *依照其設計的方法來決定是否使用此函式。
     *</p>
     *
     *<p>
     * SAX剖析器可以把連續的空白在一次呼叫傳入程式裡，也可以
     *分成多個呼叫來傳入程式。唯一的限制就是一次的呼叫理所傳
     *回的空白字元一定要位在同一個實體裡，如果要知道目前的空
     *白是屬於哪一個實體的，可以使用Locator物件來提供這些訊息。
     *</p>
     *
     *<p>
     *和characters()一樣，不要讀取超出限定範圍之外的資料
     *</p>
     *
     * @param ch 從XML文件中取得的字元
     * @param start 空白字元在陣列中的開始位置
     * @param length 空白字元的長度
     * @exception org.xml.sax.SAXException 任何的SAX例外，有
     *可能是包裝過的其他例外處理
     * @see #characters
     */
    public void ignorableWhitespace (char ch[], int start, int length)
	throws SAXException{

		String whiteSpace = new String(ch, start, length);
		//System.out.println("空白字元: \"" + whiteSpace + "\"");
	}


    /**
     *接受處理指令(process instruction)的呼叫
     *
     *<p>
     *每次遇到處理指令是，剖析器就會呼叫一次這個函式，而且它
     *們有可能會出現在文件的主要元素（root）之前。
     *</p>
     *
     * @param target 處理指令的目標（target）.
     * @param data  處理指令的資料，如果沒有任何資料就會傳回
     *空(null)，不過這些資料並不包含那些目標和資料間的空白。
     * @exception  org.xml.sax.SAXException  任何的SAX例外，
     *有可能是包裝過的其他例外處理
     */
    public void processingInstruction (String target, String data)
	throws SAXException{
		/*
		//System.out.println("處理指令  目標(target):" + target
		                   + " 和其資料(data):" + data);
		*/                   
	}


    /**
     *接受被忽略的實體參照呼叫
     *
     *<p>
     *每次剖析器忽略了一個實體時，就會呼叫此函數。
     *</p>
     *
     * @param name 被忽略的實體名稱
     * @exception  org.xml.sax.SAXException  任何的SAX例外，
     *有可能是包裝過的其他例外處理
     */
    public void skippedEntity (String name)
	throws SAXException{
		//System.out.println("忽略的實體: " + name);
	}
}// :) MyContentHandler


/**
 *<code>MyErrorHandler</code>實做<code>org.xml.sax.
 *ErrorHandler</code>介面，並且提供提供相關的回叫方法。
 *所有在剖析XML文件時所遇到的問題都會由這些函式傳入。
 */

class Big2GbPhraseErrorHandler implements ErrorHandler {
    /**
     *接受警告呼叫
     *<p>
     *一般來說，SAX剖析器會使用這個函式來回報不屬於XML 1.0
     *Recommendation中的error和fatal error的狀況。這個函式預
     *設的處理方事就是不做任何事。
     *</p>
     * @param exception 用SAXParseExceptoin所包裝起來的警告
     * @exception  org.xml.sax.SAXException  任何的SAX例外，
     *有可能是包裝過的其他例外處理
     * @see org.xml.sax.SAXParseException
     */
    public void warning (SAXParseException e)
	throws SAXException {
		
		System.out.println(	
			"----剖析警告----\n" +
			"    發生位置:\t\t第" + e.getLineNumber() + "行, " +	
			    "第" + e.getColumnNumber() + "字\n" +
			"    系統識別符:\t" + e.getSystemId() + "\n" +
			"    公用識別符:\t" + e.getPublicId() + "\n" +
			"    錯誤訊息:\t\t" + e.getMessage()
		);	
			
		throw new SAXException("遇到警告.", e);
		
	}

    /**
     *接受可回復的錯誤呼叫
     *
     *<p>
     *這個函式負責所有和XML 1.0 Recommendation的section 1.2
     *裡所定義的"error"狀況。通常都是在驗證的過程裡，有某些
     *XML的限制被違反了，但是還是可以繼續剖析下去的情況。
     *</p>
     * @param exception 用SAXParseExceptoin所包裝起來的警告
     * @exception  org.xml.sax.SAXException  任何的SAX例外，
     *有可能是包裝過的其他例外處理
     * @see org.xml.sax.SAXParseException
     */
    public void error (SAXParseException e)
	throws SAXException{
		
		System.out.println(	
			"----可回復的剖析錯誤----\n" +
			"    發生位置:\t\t第" + e.getLineNumber() + "行, " +	
			    "第" + e.getColumnNumber() + "字\n" +
			"    系統識別符:\t" + e.getSystemId() + "\n" +
			"    公用識別符:\t" + e.getPublicId() + "\n" +
			"    錯誤訊息:\t\t" + e.getMessage()
		);		
		
		throw new SAXException("遇到可回復錯誤.", e);
	}

    /**
     *接受不可回復的錯誤呼叫
     *
     *<p>
     *這個函式負責所有和XML 1.0 Recommendation的section 1.2
     *裡所定義的"fatal error" 狀況。一旦這個錯誤發生後，這份
     *文件就不應該再繼續被剖析，所有和SAX 剖析器有關的程式都
     *應該在此時終止。
     *</p>
     *
     * @param exception 用SAXParseExceptoin所包裝起來的警告
     * @exception  org.xml.sax.SAXException  任何的SAX例外，
     *有可能是包裝過的其他例外處理
     * @see org.xml.sax.SAXParseException
     */
    public void fatalError (SAXParseException e)
	throws SAXException{
		
		System.out.println(	
			"----不可回復的剖析錯誤----\n" +
			"    發生位置:\t\t第" + e.getLineNumber() + "行, " +	
			    "第" + e.getColumnNumber() + "字\n" +
			"    系統識別符:\t" + e.getSystemId() + "\n" +
			"    公用識別符:\t" + e.getPublicId() + "\n" +
			"    錯誤訊息:\t\t" + e.getMessage()
		);		
		
		throw new SAXException("遇到不可回復錯誤.", e);
	}

}// :) end of MyErrorHandler