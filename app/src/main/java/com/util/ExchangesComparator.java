package com.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import org.lib.model.GroupBean;

import android.util.Log;

public class ExchangesComparator implements Comparator<GroupBean> {

	@Override
	public int compare(GroupBean oldChat, GroupBean newChat) {
		// TODO Auto-generated method stub
		String oldDateString = oldChat.getRecentDate();
		String newDateString = newChat.getRecentDate();
		Log.i("exchanges123", "old group name : " + oldChat.getGroupName()
				+ " and recent date : " + oldChat.getRecentDate()
				+ "new group name : " + newChat.getGroupName()
				+ " and recent date : " + oldChat.getRecentDate());
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		try {
			Date oldDate = dateFormat.parse(oldDateString);
			Date newDate = dateFormat.parse(newDateString);
			return (newDate.compareTo(oldDate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

}
