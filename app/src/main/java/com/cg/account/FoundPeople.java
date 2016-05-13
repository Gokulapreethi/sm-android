package com.cg.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.adapter.FoundPeopleAdapter;
import com.cg.snazmed.R;

import org.lib.model.FindPeopleBean;

import java.util.Vector;


public class FoundPeople extends Activity {

    public ListView peopleListView;
    public FoundPeopleAdapter adapter;
    private Context context;
    public Vector<FindPeopleBean> peopleList=new Vector<FindPeopleBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_result);
        context=this;
        peopleListView=(ListView)findViewById(R.id.listview);
        adapter = new FoundPeopleAdapter(context, peopleList);
        peopleListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
