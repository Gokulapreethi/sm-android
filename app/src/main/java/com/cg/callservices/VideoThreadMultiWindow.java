package com.cg.callservices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.util.Queue;

/**
 * Created by Amuthan on 26/04/2016.
 */
public class VideoThreadMultiWindow extends Thread {
    private Handler handler = null;
    private Queue videoQueue = null;
    private boolean running = true;

    /**
     * Constructor method of the VideoThread. Here we are going to initialize
     * the {@link Queue}instance
     *
     * @param videoQueue
     */
    public VideoThreadMultiWindow(Queue videoQueue) {
        this.videoQueue = videoQueue;
    }

    /**
     * To return the handler instance
     *
     * @return
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * To initialize the Handler instance
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * To retrieve the data from the Queue and send that to the handler to
     * update the video frames
     */
    public void run() {

        try {
            while (running) {
                 Log.i("NotesVideo","videoQueue :" + videoQueue.getSize());

//                byte data[] = (byte[]) videoQueue.getMsg();

                VideoThreadBean videoThreadBean = (VideoThreadBean) videoQueue.getMsg();
                byte data[] = videoThreadBean.getData();
                // System.out.println(data.length);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putByteArray("img", data);
                bundle.putInt("windowid", videoThreadBean.getWindow());
                bundle.putInt("videossrc", videoThreadBean.getVideoSssrc());
                bundle.putString("membername",videoThreadBean.getMember_name());
                msg.obj = bundle;
                handler.sendMessage(msg);
                msg = null;
                bundle = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To stop the video thread running
     */
    public void stopVideoThread() {
        running = false;
    }
}
