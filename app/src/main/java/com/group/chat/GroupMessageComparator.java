package com.group.chat;

import java.util.Comparator;

import com.bean.GroupChatBean;

public class GroupMessageComparator implements Comparator<GroupChatBean> {

	@Override
	public int compare(GroupChatBean gcBean1, GroupChatBean gcBean2) {
		// TODO Auto-generated method stub
		return gcBean1.getSenttime().compareTo(gcBean2.getSenttime());
	}

}
