/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.cg.stickygridheaders;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.snazmed.R;
import com.cg.forms.formItem;



/**
 * @author Tonic Artos
 * @param <T>
 */
public class StickyGridHeadersSimpleArrayAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {
    protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class.getSimpleName();

    private int mHeaderResId;

    private LayoutInflater mInflater;
    Context context;
    private int mItemResId;

    ArrayList<formItem> values;

    public StickyGridHeadersSimpleArrayAdapter(Context context,ArrayList<formItem> arrayList,int headerResId,
            int itemResId) {
        init(context, arrayList, headerResId, itemResId);
    }
  

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public String getHeaderId(int position) {
        String item = getItem(position);
      

        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        Log.i("welcome", "=====header value");

        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder)convertView.getTag();
        }

        String item = getItem(position);
       
        // set header text as first char in string
        
        Log.i("welcome", "=====header value"+item);
        holder.textView.setText(item);

        return convertView;
    }

    @Override
    public String getItem(int position) {
    	formItem value= values.get(position);
    	
    	Log.i("IOS","Owner===>"+value.getOwner());
        return value.getOwner();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View row, ViewGroup parent) {
    	
        Log.i("welcome", "=====sub value");

        ViewHolder holder;
       formItem item = values.get(position);
        
                	if (row == null) {
            			row = mInflater.inflate(mItemResId, parent, false);

            			holder = new ViewHolder();
            			holder.owner = (TextView) row.findViewById(R.id.ownerTXT);
            			holder.txtTitle = (TextView) row.findViewById(R.id.appnameTXT);

            			holder.action = (TextView) row.findViewById(R.id.actionTXT);
            			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            			row.setTag(holder);
            		} else {
                        holder = (ViewHolder)row.getTag();

            		}
                	  	
    	holder.txtTitle.setText(item.getTitle());
    	if (item.getAction() != null) {
    		if (item.getAction().trim().length() == 0)
    			holder.action.setText("Record Count: 0");
    		else
    			holder.action.setText("Record Count:" + item.getAction());
    	} else
    		holder.action.setText("Record Count: 0");
    	holder.imageItem.setImageBitmap(item.getImage());
    	holder.imageItem.setContentDescription(item.getId());
    	holder.owner.setText(item.getOwner());
    	

		
		
        	
			
	
		return row;
    }

    private void init(Context context, ArrayList<formItem> items1, int headerResId, int itemResId) {
        this.values=items1;
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        this.context=context;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
    	TextView txtTitle;
		TextView action;
		TextView owner;

		ImageView imageItem;    }
}
