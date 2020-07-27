package com.fpg.ec.utility.servlet;

import java.sql.Connection;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.fpg.ec.utility.Ystd;

public class SessionBindingListener implements HttpSessionBindingListener {

	// Save a ServletContext to be used for its log() method
	ServletContext context;

	public SessionBindingListener(ServletContext context) {
		this.context = context;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		Hashtable hst = new Hashtable();

		String strId = "";
		String strIP = "";

		if (session.getAttribute("user") != null) {
			com.fpg.ec.utility.User user =
				(com.fpg.ec.utility.User) session.getAttribute("user");

			strId = user.getAcno();
			strIP = user.getIP();
		}
		if (session.getAttribute("acno") != null) {
			strId = (String) session.getAttribute("acno");
		}
		if (session.getAttribute("ip") != null) {
			strIP = (String) session.getAttribute("ip");
		}

		String strNowTime = new Ystd().utime();
		hst.put("ip", strIP);
		context.setAttribute(strId, hst);

		context.log(
			"BOUND as to "
				+ event.getSession().getId()
				+ ",user ip:"
				+ strIP
				+ ",user id:"
				+ strId
				+ ",logontime:"
				+ strNowTime);
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		Connection conn = null;
		HttpSession session = event.getSession();

		String strId = "";
		String strIP = "";

		if (session.getAttribute("user") != null) {
			com.fpg.ec.utility.User user =
				(com.fpg.ec.utility.User) session.getAttribute("user");

			strId = user.getAcno();
		}
		if (session.getAttribute("acno") != null) {
			strId = (String) session.getAttribute("acno");
		}

		String strNowTime = new Ystd().utime();
		context.removeAttribute(strId);

		context.log(
			"UNBOUND as "
				+ event.getSession().getId()
				+ ",user id:"
				+ strId
				+ ",logoff time:"
				+ strNowTime);
	}
}
