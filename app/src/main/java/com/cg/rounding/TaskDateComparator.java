package com.cg.rounding;

import org.lib.model.TaskDetailsBean;

import java.util.Comparator;

public class TaskDateComparator implements Comparator<TaskDetailsBean> {

    @Override
    public int compare(TaskDetailsBean oldBean,
                       TaskDetailsBean newBean) {
        // TODO Auto-generated method stub
        return (oldBean.getDuedate().compareTo(newBean.getDuedate()));
    }
}