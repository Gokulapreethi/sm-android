package com.group.chat;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bean.GroupChatBean;
import com.bean.ProfileBean;
import com.cg.DB.DBAccess;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.util.SingleInstance;

import org.lib.model.BuddyInformationBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by  on 04-03-2016.
 */
public class ChatInfoAdapter  extends ArrayAdapter<ChatInfoBean> {

    /*********** Declare Used Variables *********/
    private Context context;
    private Vector<ChatInfoBean> userList;
    private LayoutInflater inflater = null;
    private static int checkBoxCounter = 0;
    private int checkboxcount;
    ImageLoader imageLoader;


    /************* CustomAdapter Constructor *****************/
    public ChatInfoAdapter(Context context, Vector<ChatInfoBean> userList) {

        super(context, R.layout.chat_info_row, userList);
        /********** Take passed values **********/
        this.context = context;
        this.userList = userList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context.getApplicationContext());

        /*********** Layout inflator to call external xml layout () ***********/

    }



    /******
     * Depends upon data size called for each row , Create each ListView row
     *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder holder;

            holder = new ViewHolder();
            if(convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.chatinfo_row, null);
                holder.buddyicon = (ImageView) convertView.findViewById(R.id.buddyicon);
                holder.buddyName = (TextView) convertView.findViewById(R.id.buddyName);
                holder.occupation = (TextView) convertView.findViewById(R.id.occupation);
                convertView.setTag(holder);
            }else
                holder = (ViewHolder) convertView.getTag();
            final ChatInfoBean gcBean = userList.get(position);
            if(gcBean!=null) {

                    ProfileBean pbean= DBAccess.getdbHeler().getProfileDetails(gcBean.getName());
                    if(pbean!=null) {
                        if(pbean.getPhoto()!=null)
                            imageLoader.DisplayImage(
                                    Environment.getExternalStorageDirectory().getAbsolutePath()+"/COMMedia/"+
                                            pbean.getPhoto(),
                                    holder.buddyicon, R.drawable.icon_buddy_aoffline);
                    }
                holder.buddyName.setText(gcBean.getName());
//                2016-03-17 12:24:23 PM
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateInString = gcBean.getDate();
                SimpleDateFormat fmt2 = new SimpleDateFormat("MM:dd:yyyy hh:mm a");
                try {
                    Date date = fmt.parse(dateInString);
                    holder.occupation.setText(fmt2.format(date));
                }
                catch(Exception pe) {

                }


            }



            return convertView;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    class ViewHolder {
        CheckBox selectUser;
        ImageView buddyicon;
        ImageView statusIcon;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout cancel_lay;
    }

}