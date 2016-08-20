package com.group.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bean.GroupChatBean;
import com.bean.ProfileBean;
import com.cg.snazmed.R;
import com.image.utils.ImageLoader;
import com.main.FilesFragment;
import com.util.FullScreenImage;
import com.util.SingleInstance;
import com.util.VideoPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by on 07-03-2016.
 */
public class SendChatListAdapter extends ArrayAdapter<SendListUIBean> {

    /**
     * ******** Declare Used Variables ********
     */
    private Context context;
    private Vector<SendListUIBean> userList;
    private LayoutInflater inflater = null;
    private static int checkBoxCounter = 0;
    private int checkboxcount;
    ImageLoader imageLoader;
    GroupChatActivity groupChatActivity;
    private int mPlayingPosition = -1;


    /**
     * ********** CustomAdapter Constructor ****************
     */
    public SendChatListAdapter(Context context, Vector<SendListUIBean> userList) {

        super(context, R.layout.chatsendlistui_row, userList);
        /********** Take passed values **********/
        this.context = context;
        this.userList = userList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(context.getApplicationContext());
        groupChatActivity=(GroupChatActivity) SingleInstance.contextTable
                .get("groupchat");

        /*********** Layout inflator to call external xml layout () ***********/

    }


    /**
     * ***
     * Depends upon data size called for each row , Create each ListView row
     * ***
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            final ViewHolder holder;


            if (convertView == null) {
                holder = new ViewHolder();
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.chatsendlistui_row, null);
                holder.filexbutton = (ImageView) convertView.findViewById(R.id.filexbutton);
                holder.xbutton = (ImageView) convertView.findViewById(R.id.xbutton);
                holder.buddyName = (TextView) convertView.findViewById(R.id.txt_time);
                holder.play_button = (ImageView) convertView.findViewById(R.id.play_button);
                holder.play_button.setTag(position);
                holder.seekBar = (SeekBar)convertView.findViewById(R.id.seekBar1);
                holder.seekBar.setTag(position);
                holder.filelayout = (LinearLayout)convertView.findViewById(R.id.filelayout);
                holder.ad_play = (RelativeLayout)convertView.findViewById(R.id.ad_play);
                holder.tv_fname = (TextView) convertView.findViewById(R.id.tv_fname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.xbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupChatActivity.tempSendList(position);
                    stopPlayback();
                }
            });
            holder.filexbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupChatActivity.tempSendList(position);
                    stopPlayback();
                }
            });

            final SendListUIBean gcBean = userList.get(position);
            holder.seekBar.setTag(gcBean);
            holder.play_button.setTag(gcBean);
            if (gcBean.isPlaying()) {
                Log.i("Audioplay","pusebtn");
                holder.play_button.setBackgroundResource(R.drawable.audiopause);
            } else {
                Log.i("Audioplay","playbtn");
                holder.play_button.setBackgroundResource(R.drawable.play);
            }
            holder.play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gcBean.setType("audio");
                    File newfile=new File(gcBean.getPath());
                    Log.d("Audioplay1","pathname"+newfile);
                    Log.d("Audioplay1","positionavlue"+position);

                    if(mPlayer.isPlaying()) {
                        gcBean.setPlaying(false);
                        holder.seekBar.setTag(position);
                        mPlayer.pause();
                        Log.i("Audioplay1", "pusebtn");
                        holder.play_button.setBackgroundResource(R.drawable.play);

                    }else {
                        gcBean.setPlaying(true);
                        Log.i("Audioplay1", "playbtn");
                        holder.seekBar.setTag(position);
                        holder.play_button.setBackgroundResource(R.drawable.audiopause);
                        playAudio(gcBean.getPath(), position);
                    }
                }
            });
            if (gcBean != null) {

                holder.filelayout.setVisibility(View.GONE);
                holder.ad_play.setVisibility(View.VISIBLE);
                if (gcBean.getType() != null &&gcBean.getType().equalsIgnoreCase("audio"))
                {
                    Log.d("Audio","type"+gcBean.getType());
                    Log.d("Audio","typep"+position);
                    Log.d("Audio","typem"+mPlayingPosition);

                    if (position == mPlayingPosition) {
                        Log.i("AAAA","enters into same position ");

                        Log.d("Audio","seekbar");
                        mProgressUpdater.mBarToUpdate = holder.seekBar;
                        mProgressUpdater.tvToUpdate = holder.buddyName;
                        mHandler.postDelayed(mProgressUpdater, 100);
                    } else {
                        Log.i("AAAA", "enters into update else part ");
                        //pb.setVisibility(View.GONE);
//                        if (gcBean.getType() != null &&gcBean.getType().equalsIgnoreCase("audio")) {
                        try {
                            holder.seekBar.setProgress(0);
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(gcBean.getPath());
                            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            mmr.release();
                            String min, sec;
                            min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
                            sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
                            if (Integer.parseInt(min) < 10) {
                                min = 0 + String.valueOf(min);
                            }
                            if (Integer.parseInt(sec) < 10) {
                                sec = 0 + String.valueOf(sec);
                            }
                            holder.buddyName.setText(min + ":" + sec);
//                            audio_tv.setText(duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        holder.seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                        holder.seekBar.setProgress(0);
                        if (mProgressUpdater.mBarToUpdate == holder.seekBar) {
                            //this progress would be updated, but this is the wrong position
                            mProgressUpdater.mBarToUpdate = null;
                        }
//                        }
                    }
                }else{
                    holder.filelayout.setVisibility(View.VISIBLE);
                    holder.ad_play.setVisibility(View.GONE);
                    holder.tv_fname.setText(gcBean.getPath().split("COMMedia/")[1]);
                    holder.tv_fname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (gcBean.getType()
                                    .equalsIgnoreCase("video")) {
                                Log.i("group123", "icon clicked video");
                                if (mPlayer != null && mPlayer.isPlaying())
                                    mPlayer.stop();
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", gcBean.getPath());
                                context.startActivity(intent);
                            } else if (gcBean.getType()
                                    .equalsIgnoreCase("image")) {
                                Log.i("group123", "icon clicked image");
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", gcBean.getPath());
                                context.startActivity(intent);
                            }else {
                                Log.i("AAAA","openFilesinExternalApp");
                                FilesFragment.openFilesinExternalApp(gcBean.getPath());
                            }
                        }
                    });
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
        ImageView xbutton;
        ImageView play_button;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout cancel_lay;
        SeekBar seekBar;
        ImageView filexbutton;
        TextView tv_fname;
        LinearLayout filelayout;
        RelativeLayout ad_play;
    }
    private final MediaPlayer mPlayer = new MediaPlayer();

    private Handler mHandler = new Handler();
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    private class PlaybackUpdater implements Runnable {
        public SeekBar mBarToUpdate = null;
        public TextView tvToUpdate = null;
        @Override
        public void run() {
            if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
                Log.i("AAAA","enters into update ");
                double tElapsed = mPlayer.getCurrentPosition();
                int fTime = mPlayer.getDuration();
                double timeRemaining = fTime - tElapsed;
                double sTime = mPlayer.getCurrentPosition();

                String min, sec;
                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
                if (Integer.parseInt(min) < 10) {
                    min = 0 + String.valueOf(min);
                }
                if (Integer.parseInt(sec) < 10) {
                    sec = 0 + String.valueOf(sec);
                }
                tvToUpdate.setText(min + ":" + sec);
                mBarToUpdate.setProgress((100 * mPlayer.getCurrentPosition() / mPlayer.getDuration()));
                mHandler.postDelayed(this, 500);

            } else {
                //not playing so stop updating
            }
        }
    }
    public void stopPlayback() {
        mPlayingPosition = -1;
        mProgressUpdater.mBarToUpdate = null;
        mProgressUpdater.tvToUpdate = null;
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
    }
    private void playAudio(String fname, int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fname);
            mPlayer.prepare();
            mPlayer.start();
            mPlayingPosition = position;
            mHandler.postDelayed(mProgressUpdater, 500);
            //trigger list refresh, this will make progressbar start updating if visible
           notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
            stopPlayback();
        }
    }
}