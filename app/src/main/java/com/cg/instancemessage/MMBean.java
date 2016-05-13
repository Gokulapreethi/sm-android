package com.cg.instancemessage;

public class MMBean {
	private String strFileName;

	private String strFilePath;

	private String strSessionId;

	void setImagePath(String strpath) {
		strFilePath = strpath;
		strFileName = strpath.substring(strpath.lastIndexOf("/") + 1);
	}

	void setSessionId(String SessionId) {
		strSessionId = SessionId;
	}

	String getFileName() {
		return strFileName;
	}

	String getFilePath() {
		return strFilePath;
	}

	String getSessionId() {
		return strSessionId;
	}
}
