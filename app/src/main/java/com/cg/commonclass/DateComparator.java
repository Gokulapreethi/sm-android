package com.cg.commonclass;

import com.bean.NotifyListBean;

import java.util.Comparator;


public class DateComparator implements Comparator<NotifyListBean> {

    @Override
    public int compare(NotifyListBean oldBean,
                       NotifyListBean newBean) {
        // TODO Auto-generated method stub
        return (newBean.getSortdate().compareTo(oldBean.getSortdate()));
    }
}
