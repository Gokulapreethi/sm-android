package com.cg.quickaction;

public interface QuickActionType {
	public static int CALL = 1;
	public static int BCAST = 2;
	public static int SHARE = 3;
	public static int REPORT = 4;
	public static String[] ACTIONTYPES = { "Call", "Broadcast", "Share",
			"Report" };
	public static int AUDIOCALL = 1;
	public static int VIDEOCALL = 2;
	public static int AUDIOCONF = 3;
	public static int VIDEOCONF = 4;
	public static int HOSTEDCONF = 5;
	public static String AUDIOCALLCODE = "AC";
	public static String VIDEOCALLCODE = "VC";
	public static String AUDIOCONFCODE = "ACF";
	public static String VIDEOCONFCODE = "VCF";
	public static String HOSTEDCONFCODE = "HC";
	public static String AUDIOBCASTCODE = "ABC";
	public static String VIDEOBCASTCODE = "VBC";
	public static String TEXTSHARECODE = "ST";
	public static String AUDIOSHARECODE = "SA";
	public static String VIDEOSHARECODE = "SV";
	public static String IMAGESHARECODE = "SP";
	public static String SKETCHSHARECODE = "SHS";
	public static String REPORTREPORTCODE = "Show Results Form";
	public static int[] CALLSUBTYPES = { AUDIOCALL, VIDEOCALL, AUDIOCONF,
			VIDEOCONF, HOSTEDCONF };
	public static int AUDIOBCAST = 6;
	public static int VIDEOBCAST = 7;
	public static int[] BCASTSUBTYPES = { AUDIOBCAST, VIDEOBCAST };
	public static int TEXTSHARE = 8;
	public static int AUDIOSHARE = 9;
	public static int VIDEOSHARE = 10;
	public static int IMAGESHARE = 11;
	public static int SKETCHSHARE = 12;
	public static int[] SHARESUBTYPES = { TEXTSHARE, AUDIOSHARE, VIDEOSHARE,
			IMAGESHARE, SKETCHSHARE };
	public static int REPORTREPORT = 13;
	public static int[] REPORTSUBTYPES = { REPORTREPORT };
	public static String CALLTEXT = "Call";
	public static String BCASTTEXT = "Broadcast";
	public static String SHARETEXT = "Share";
	public static String REPORTTEXT = "Report";
	public static String AUDIOCALLTEXT = "Audio Call";
	public static String VIDEOCALLTEXT = "Video Call";
	public static String AUDIOCONFTEXT = "Audio Conference";
	public static String VIDEOCONFTEXT = "Video Conference";
	public static String HOSTEDCONFTEXT = "Hosted Conference";
	public static String[] CALLSUBTYPESTEXT = { AUDIOCALLTEXT, VIDEOCALLTEXT,
			AUDIOCONFTEXT, VIDEOCONFTEXT, HOSTEDCONFTEXT };
	public static String AUDIOBCASTTEXT = "Audio Broadcast";
	public static String VIDEOBCASTTEXT = "Video Broadcast";
	public static String[] BCASTSUBTYPESTEXT = { AUDIOBCASTTEXT, VIDEOBCASTTEXT };
	public static String TEXTSHARETEXT = "Share Text Note";
	public static String AUDIOSHARETEXT = "Share Audio Note";
	public static String VIDEOSHARETEXT = "Share Video Note";
	public static String IMAGESHARETEXT = "Share Photo Note";
	public static String SKETCHSHARETEXT = "Share Hand Sketch";
	public static String[] SHARESUBTYPESTEXT = { TEXTSHARETEXT, AUDIOSHARETEXT,
			VIDEOSHARETEXT, IMAGESHARETEXT, SKETCHSHARETEXT };
	public static String REPORTREPORTTEXT = "Report";
	public static String[] REPORTSUBTYPESTEXT = { REPORTREPORTTEXT };
	public static String[] QuickActionAllTypesCode = { AUDIOCALLCODE,
			VIDEOCALLCODE, AUDIOCONFCODE, VIDEOCONFCODE, HOSTEDCONFCODE,
			AUDIOBCASTCODE, VIDEOBCASTCODE, TEXTSHARECODE, AUDIOSHARECODE,
			VIDEOSHARECODE, IMAGESHARECODE, SKETCHSHARECODE, REPORTREPORTCODE };
	public static int[] QuickActionAllTypes = { AUDIOCALL, VIDEOCALL,
			AUDIOCONF, VIDEOCONF, HOSTEDCONF, AUDIOBCAST, VIDEOBCAST,
			TEXTSHARE, AUDIOSHARE, VIDEOSHARE, IMAGESHARE, SKETCHSHARE,
			REPORTREPORT };

	public static String[] QuickActionAllTypesText = { AUDIOCALLTEXT,
			VIDEOCALLTEXT, AUDIOCONFTEXT, VIDEOCONFTEXT, HOSTEDCONFTEXT,
			AUDIOBCASTTEXT, VIDEOBCASTTEXT, TEXTSHARETEXT, AUDIOSHARETEXT,
			VIDEOSHARETEXT, IMAGESHARETEXT, SKETCHSHARETEXT, REPORTREPORTTEXT };
	public static int REPEAT_YEAR = 0;
	public static int REPEAT_MONTH = 1;
	public static int REPEAT_WEEK = 2;
	public static int REPEAT_DAY = 3;
	public static int REPEAT_HOUR = 4;
	public static int REPEAT_MIN = 5;
	public static int REPEAT_SEC = 6;
	public static String[] scheduleModeList = { "Year", "Month", "Week", "Day",
			"Hour", "Minute", "Second" };

}
