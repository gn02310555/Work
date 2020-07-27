package com.fpg.ec.utility.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarker 樣板引擎程式Utility
 * @author N000100937
 *
 */
public class FreeMarkerUtil {	
	private FreeMarkerConfig freeMarkerConfig;

	/**
	 * 取得套完參數的結果
	 */
	public String getAssembledResult(FreeMarkerTemplate freeMarkerTemplate, Map arguments) {
		String strReturn = "";

		try {
			Template template = createTemplate(freeMarkerTemplate);
			StringWriter stringWriter = new StringWriter();
			template.process(arguments, stringWriter);
			strReturn = stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return strReturn;
	}
	
	/**
	 * 取得套完參數的結果
	 */
	public String getAssembledResult(String strContent, Map arguments) {
		String strReturn = "";

		try {
			Configuration configuration = getFreeMarkerConfig().getConfiguration();
			Template template = new Template("name", new StringReader(strContent), configuration);
			StringWriter stringWriter = new StringWriter();
			template.process(arguments, stringWriter);
			strReturn = stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return strReturn;
	}

	/**
	 * 取得套完參數的結果-For PDF
	 */
	public String getAssembledResultForPDF(String iPDFTemplateName, Map arguments) {
		String strReturn = "";
		try {
			FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate(FreeMarkerTemplate.TYPE_PATH);
			freeMarkerTemplate.setPath(FreeMarkerTemplate.PDFTmp_Path + iPDFTemplateName + ".ftl");
			Template template = createTemplate(freeMarkerTemplate);
			StringWriter stringWriter = new StringWriter();
			template.process(arguments, stringWriter);
			strReturn = stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return strReturn;
	}
	
	/**
	 * 取得套完參數的結果-For EMail
	 */
	public String getAssembledResultForEmail(String iEmailTemplateName, Map arguments) {
		String strReturn = "";
		try {
			FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate(FreeMarkerTemplate.TYPE_PATH);
			freeMarkerTemplate.setPath(FreeMarkerTemplate.EmailTmp_Path + iEmailTemplateName + ".ftl");
			
			Template template = createTemplate(freeMarkerTemplate);
			StringWriter stringWriter = new StringWriter();
			template.process(arguments, stringWriter);
			strReturn = stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return strReturn;
	}

	
	/**
	 * 產生Template
	 */
	private Template createTemplate(FreeMarkerTemplate freeMarkerTemplate) throws IOException {
		Configuration configuration = getFreeMarkerConfig().getConfiguration();
		
		Template template = null;
		if (FreeMarkerTemplate.TYPE_PATH.equals(freeMarkerTemplate.getType())) {
			template = configuration.getTemplate(freeMarkerTemplate.getPath());
		} else if (FreeMarkerTemplate.TYPE_CLOB.equals(freeMarkerTemplate.getType())) {
			template = new Template("name", new StringReader(freeMarkerTemplate.getClobcontent()), configuration);
		} else {
			throw new RuntimeException("FreeMarkerTemplate is wrong. (" + freeMarkerTemplate + ")");
		}

		return template;
	}


	// ########### getter && setter ##########################################
	public FreeMarkerConfig getFreeMarkerConfig() {
		return freeMarkerConfig;
	}

	public void setFreeMarkerConfig(FreeMarkerConfig freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}
}
