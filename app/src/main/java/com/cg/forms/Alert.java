package com.cg.forms;




import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Alert{
	public static final String PREF_NAME = "PEOPLE_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;
	public static final String satus= "status";
	public static final String id= "id";
	public static final String ftppath= "ftppath";
	public static final String touser= "touser";
	public static final String fromuser= "from";
	public static final String content= "content";
	public static final String type= "type";
	public static final String query= "query";
	public static final String Ftpusername= "user";
	public static final String Ftppassword= "password";
	public static final String form_owner= "owner";
	public static final String issync= "check";
	public static final String QuickActionquery= "quickaction";
	public static final String ToUsers= "users";
	public static final String password= "password";
	public static final String ProfileId= "profileid";

	public static final String instruction= "instructions";

	public static final String formid= "formid";
	public static final String heartbeat= "heartbeat";
	public static final String autologin= "autologinsetting";

	public static final String rempassword= "rempassword";
	
	public static final String iscompletelistview= "no";

	public static final String viewbuddyName= "no";

	public static final String viewBuddyStatus= "yes";
	public static final String UserProfile= "profile";

	public static final String deadLine = "deadline";





	public static final Boolean check = false;


	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}
	
	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}
	
	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}
	
	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

}

