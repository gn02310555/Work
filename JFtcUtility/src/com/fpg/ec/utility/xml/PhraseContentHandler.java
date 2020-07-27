/*
 * Created on 2009/3/13
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fpg.ec.utility.xml;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class PhraseContentHandler implements ContentHandler {

    /**
     * 
     */
    private StringBuffer buf;
    private Hashtable hstData;

    public PhraseContentHandler() {
        super();
    }

    public void setDocumentLocator(Locator locator) {

    }

    public void startDocument() throws SAXException {
        buf=new StringBuffer();
        hstData = new Hashtable();
    }

    public void endDocument() throws SAXException {

    }

   
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //打印出非空的元素?容并?StringBuffer清空                  
        String nullStr="";
          if (!buf.toString().trim().equals(nullStr)){
             //System.out.println("\t?容是: " + buf.toString().trim());
             hstData.put(qName,buf.toString().trim());
          }
          buf.setLength(0);
          //打印元素解析?束信息
          //System.out.println("元素: "+"["+qName+"]"+" 解析結束!");         

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        buf.append(ch,start,length);


    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    public void processingInstruction(String target, String data) throws SAXException {

    }

    public void skippedEntity(String name) throws SAXException {

    }
    
    public Hashtable getHashtable() {
        return hstData;

    }    

}
