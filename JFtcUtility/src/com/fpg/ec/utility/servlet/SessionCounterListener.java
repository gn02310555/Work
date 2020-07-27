package com.fpg.ec.utility.servlet;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.fpg.ec.utility.StringUtil;
import com.fpg.ec.utility.Ystd;

/**
 * Session Listener
 * @author N000101814
 *
 */
public class SessionCounterListener implements HttpSessionListener {
	private static HashMap<String, String> sessionMap = new HashMap<String, String>();
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		sessionMap.put(session.getId(), "0");
		System.out.println(new StringUtil().toFormat(Ystd.utime4(), "####/##/## ##:##:## ###") + "  Session Created, session.getId():" + session.getId());
	}
	
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		sessionMap.remove(session.getId());
		System.out.println(new StringUtil().toFormat(Ystd.utime4(), "####/##/## ##:##:## ###") + "  Session Destroyed, session.getId():" + session.getId());
	}

	public static HashMap<String, String> getSessionMap() {
		return sessionMap;
	}

	public static void setSessionMap(HashMap<String, String> sessionMap) {
		SessionCounterListener.sessionMap = sessionMap;
	}

}
