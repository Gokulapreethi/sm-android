/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cg.commonclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {
	
	private String logType = "console";
	private boolean INFO = false;
	private boolean DEBUG = false;
	private boolean TRACE = false;
	private boolean WARN = false;
	private boolean ERROR = false;
	private String logFileName;
	private PrintStream logWriter;
	public boolean needsLogging = false;
	private FileOutputStream fw = null;
	private String fileNameAlone;

	public Logger(String logingType, String logFile, boolean info,
			boolean debug, boolean trace, boolean warn, boolean error) {
		this.logType = logingType;
		this.logFileName = logFile;
		this.INFO = info;
		this.DEBUG = debug;
		this.TRACE = trace;
		this.WARN = warn;
		this.ERROR = error;
		setLogWriter();
	}

	public void setLogType(String lType) {
		info("New Log Type : " + lType + "   -  Old Log type : " + logType);
		if (!lType.equals(logType)) {
			logType = lType;
			setLogWriter();
			info("Logs redirected to " + logType);
		}
	}

	public void setNewLogType(String lType) {
		info("New Log Type 1 : " + lType + "   -  Old Log type 1 : " + logType);
		logType = lType;
		setLogWriter();
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public void setTrace(boolean trace) {
		TRACE = trace;
	}

	public void setError(boolean error) {
		ERROR = error;
	}

	public void setInfo(boolean info) {
		INFO = info;
	}

	public void setWarn(boolean warn) {
		WARN = warn;
	}

	public void setDebug(boolean debug) {
		DEBUG = debug;
	}

	public void setLogWriter() {
		if (logWriter != null) {
			try {
				fw.close();
			} catch (IOException ex) {
				info("Old File stream is closed");
			}
			logWriter = null;
		}
		if (logType.equalsIgnoreCase("Console")) {
			logWriter = System.out;
			needsLogging = true;
		} else if (logType.equalsIgnoreCase("File")) {
			try {
				File logFile = new File(logFileName);
				fileNameAlone = logFile.getName();
				if (!logFile.exists()) {
					logFile.createNewFile();
				}

				fw = new FileOutputStream(logFile, true);
				logWriter = new PrintStream(fw, true);
				needsLogging = true;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		} else {
			needsLogging = false;
		}
	}

	public synchronized void info(String message) {
		if (INFO) {
			logWriter.println(getCurrentTime() + " :: " + "INFO : " + message);
		}
	}

	public synchronized void debug(String message) {
		if (DEBUG) {
			logWriter.println(getCurrentTime() + " :: " + "DEBUG : " + message);
		}
	}

	public synchronized void warn(String message) {
		if (WARN) {
			logWriter.println(getCurrentTime() + " :: " + "WARN : " + message);
		}
	}

	public synchronized void trace(String message) {
		if (TRACE) {
			logWriter.println(getCurrentTime() + " :: " + "TRACE : " + message);
		}
	}

	public synchronized void trace(String message, Throwable ex) {
		if (TRACE) {
			logWriter.println(getCurrentTime() + " :: " + "TRACE : " + message);
			ex.printStackTrace(logWriter);
		}
	}

	public synchronized void error(String message) {
		if (ERROR) {
			logWriter.println(getCurrentTime() + " :: " + "ERROR : " + message);
		}
	}

	public synchronized void error(String message, Throwable e) {
		if (ERROR) {
			try {
//				if (e != null && SingleInstance.crashlyticsLog)
//					Crashlytics.logException(e);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			logWriter.println(getCurrentTime() + " :: " + "ERROR : " + message);
			e.printStackTrace(logWriter);
		}
	}

	// For getting the current time
	private String getCurrentTime() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String str = "[" + ts.toString() + "]";
		ts = null;
		return str;
	}

	public String getFileName() {
		return fileNameAlone;
	}

	// Added new logger file for Logging using this date
	public static String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String srvDate = dateFormat.format(new Date());
		String ddmmyy = srvDate.substring(0, 2) + "-" + srvDate.substring(3, 5)
				+ "-" + srvDate.substring(6, 10);
		return ddmmyy;
	}

	public boolean isDebugEnabled() {
		return DEBUG;
	}

	public boolean isInfoEnabled() {
		return INFO;
	}

	public boolean isTraceEnabled() {
		return TRACE;
	}

	public boolean isWarnEnabled() {
		return WARN;
	}

	public boolean isErrorEnabled() {
		return ERROR;
	}
}