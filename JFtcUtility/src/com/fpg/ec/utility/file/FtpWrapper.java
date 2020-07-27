package com.fpg.ec.utility.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * This is a simple wrapper around the Jakarta Commons FTP library. This Java
 * class requires both the Jakarta Commons Net library and the Jakarta ORO
 * library . Make sure you have both of the jar files in your path to compile.
 */
public class FtpWrapper extends FTPClient {

	/** A convenience method for connecting and logging in */
	public boolean connectAndLogin(String host, String userName, String password) throws IOException, UnknownHostException, FTPConnectionClosedException {
		boolean success = false;
		connect(host);
		int reply = getReplyCode();
		if (FTPReply.isPositiveCompletion(reply))
			success = login(userName, password);
		if (!success)
			disconnect();
		return success;
	}

	public void setPassiveMode(boolean setPassive) {
		if (setPassive)
			enterLocalPassiveMode();//有防火牆
		else
			enterLocalActiveMode();//無防火牆
	}

	/** Use ASCII mode for file transfers */
	public boolean ascii() throws IOException {
		return setFileType(FTP.ASCII_FILE_TYPE);
	}

	/** Use Binary mode for file transfers */
	public boolean binary() throws IOException {
		return setFileType(FTP.BINARY_FILE_TYPE);
	}

	/** Download a file from the server, and save it to the specified local file */
	public boolean downloadFile(String serverFile, String localFile) throws IOException, FTPConnectionClosedException {
		System.out.println("localFile:"+localFile);
		FileOutputStream out = new FileOutputStream(localFile);
		boolean result = retrieveFile(serverFile, out);
		if (result) {
		} else {
			// 假如 無此目錄 建立目錄
			File file = new File(localFile);
			file.mkdir();
			
			out = new FileOutputStream(localFile);
		}
		out.close();
		return true;
	}

	/** Upload a file to the server */
	public boolean uploadFile(String localFile, String serverFile) throws IOException, FTPConnectionClosedException {
		FileInputStream in = new FileInputStream(localFile);
		boolean result = storeFile(serverFile, in);
		in.close();
		return result;
	}

	/**
	 * Get the list of files in the current directory as a Vector of Strings
	 * (excludes subdirectories)
	 */
	public Vector listFileNames() throws IOException, FTPConnectionClosedException {
		FTPFile[] files = listFiles();
		Vector v = new Vector();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/**
	 * Get the list of files in the current directory as a single Strings,
	 * delimited by \n (char '10') (excludes subdirectories)
	 */
	public String listFileNamesString() throws IOException, FTPConnectionClosedException {
		return vectorToString(listFileNames(), "\n");
	}

	/**
	 * Get the list of subdirectories in the current directory as a Vector of
	 * Strings (excludes files)
	 */
	public Vector listSubdirNames() throws IOException, FTPConnectionClosedException {
		FTPFile[] files = listFiles();
		Vector v = new Vector();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/**
	 * Get the list of subdirectories in the current directory as a single
	 * Strings, delimited by \n (char '10') (excludes files)
	 */
	public String listSubdirNamesString() throws IOException, FTPConnectionClosedException {
		return vectorToString(listSubdirNames(), "\n");
	}

	/** Convert a Vector to a delimited String */
	private String vectorToString(Vector v, String delim) {
		StringBuffer sb = new StringBuffer();
		String s = "";
		for (int i = 0; i < v.size(); i++) {
			sb.append(s).append((String) v.elementAt(i));
			s = delim;
		}
		return sb.toString();
	}

}
