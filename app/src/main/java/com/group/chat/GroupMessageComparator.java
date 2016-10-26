package com.group.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.bean.GroupChatBean;

public class GroupMessageComparator implements Comparator<GroupChatBean> {

	@Override
	public int compare(GroupChatBean gcBean1, GroupChatBean gcBean2) {
		// TODO Auto-generated method stub
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss a");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		Date date1=null, date2=null;
		try {
			 date1 = dateFormat.parse(gcBean1.getDateandtime());
			 date2 = dateFormat.parse(gcBean2.getDateandtime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		try {
			return date1.compareTo(date2);
//		}catch (Exception e){
//			e.printStackTrace();
//			return 0;
//		}
	}

}
