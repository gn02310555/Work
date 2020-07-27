package com.fpg.ec.utility;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.fpg.ec.utility.exception.Connexp;

/**
 * <pre>
 * Connection Class。
 * PROPERTIES_FILE =  "ConnPoolStrings.properties";
 * 建立日期：(2000/6/29 下午 04:54:53)
 * 修改日期：(2002/5/24)  Change Bundle file to property for security consider
 * </pre>
 */
public class Conn {
	private Connection conn = null;
	private static DataSource ds = null;
	private static String source = null;

	/**
	 * Connection在一個Session內完成，建立Conn()物件。 建立日期： (2000/9/26 AM 10:53:49)
	 */
	public Conn() {
		super();
	}

	/**
	 * 處理上一次commit/rollback到目前的所有指令。 建立日期： (2000/9/21 AM 08:49:25)
	 */
	public void commit() throws Connexp {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new Connexp("Commit error  [Conn:commit] => " + e.getMessage());
		} catch (Exception e) {
			throw new Connexp("Commit error  [Conn:commit] => " + e.getMessage());
		}
	}

	/**
	 * 中斷資料庫的連接。 建立日期： (2000/9/21 AM 08:18:13)
	 */
	public void disConn() throws Connexp {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new Connexp("DisConnection error  [Conn:disConn] => " + e.getMessage());
		}
	}

	/**
	 * <pre>
	 * 以作業別取得己連接資料庫的資料庫連接物件，或取得Session中的資料庫接物件。
	 * 建立日期： (2000/9/26 AM 10:53:49)
	 * </pre>
	 */
	public Connection getConn(String jobId) throws Connexp {
		try {
			return getConnPre(jobId);
		} catch (Exception e) {
			throw new Connexp("Get Connection Conn.getConn(String jobId) Err => " + e.getMessage());
		}
	}

	/**
	 * <pre>
	 * input jobId & use external bundle file and get user,password & source。
	 * Create the initial naming context.
	 * Perform a naming service lookup to get a DataSource object.
	 * return a Connection object conn using the DataSource factory.	
	 * 建立日期： (2001/4/20 AM 10:53:49)
	 * </pre>
	 */
	private Connection getConnPre(String jobId) throws Connexp {

		DataSource ds = null;
		try {
			// Create the initial naming context.
			Context ctx = new InitialContext();
			source = "java:comp/env/jdbc/" + jobId;

			// Perform a naming service lookup to get a DataSource object.
			ds = (DataSource) ctx.lookup(source);

		} catch (Exception e) {
			throw new Connexp("Naming service exception: " + e.getMessage());
		}

		try {
			// Get a Connection object conn using the DataSource factory.
			conn = ds.getConnection();

			// for trace Oracle DataBase
			if (conn.getMetaData().getDatabaseProductName().toLowerCase().indexOf("oracle") >= 0) { // if
																									// (source.indexOf("db2")
																									// ==
																									// -1)
																									// {
				CallableStatement cs = conn.prepareCall("call DBMS_APPLICATION_INFO.SET_CLIENT_INFO(?)");
				cs.setString(1, "From j2 Conn " + System.getProperty("user.name"));
				cs.execute();
				cs.close();
				// conn.setAutoCommit(false);
			}

		} catch (Exception e) {
			throw new Connexp("Get connection, process, or close statement " + "exception: " + e.getMessage());
		}
		return conn;
	}

	/**
	 * <pre>
	 * 取消上一次commit/rollback到前的所有指令。
	 * 建立日期： (2000/9/21 AM 08:49:25)
	 * </pre>
	 */
	public void rollback() throws Connexp {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new Connexp("rollback [Conn] => " + e.getMessage());
		} catch (Exception e) {
			throw new Connexp("rollback [Conn] => " + e.getMessage());
		}
	}

}
