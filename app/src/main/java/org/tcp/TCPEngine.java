package org.tcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.cg.commonclass.CallDispatcher;
import com.cg.commonclass.WebServiceReferences;
import com.cg.hostedconf.AppReference;
import com.main.AppMainActivity;
import com.util.SingleInstance;

import org.core.AESCrypto;
import org.core.ProprietarySignalling;
import org.lib.model.KeepAliveBean;

/**
 * @author GK
 *
 */
public class TCPEngine extends Thread {
    private static String SERVER_IP = AppMainActivity.ip;
    private int SERVER_PORT = AppMainActivity.port;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private TCPListener mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    // private PrintWriter mBufferOut;
    // used to read messages from the server
    // private BufferedReader mBufferIn;

    private OutputStream outStream;

    private InputStream inStream;

    private String userId;
    private ProgressDialog pDialog;
    private Handler handler = new Handler();

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages
     * received from server
     */
    public TCPEngine(String user, String serverIp, int serverPort,
                     TCPListener listener) {
        mMessageListener = listener;
        SERVER_IP = serverIp;
        SERVER_PORT = serverPort;
        userId = user;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message
     *            text entered by client
     */
    public boolean sendMessage(String message) {

        try {
            if (outStream != null) {
                setLogWriter("TCP SENT : "+message, null);
                byte[] encryptedData = AESCrypto.encrypt(ProprietarySignalling.seceretKey, message.getBytes());
                outStream.write(prepareMessage(encryptedData));
                outStream.flush();
                return true;
            }
            // mBufferOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {
        setLogWriter("TCP STOPPED", null);
        Log.i("Debug", "stopClient");
        mRun = false;
        try {
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            cancelDialog();
            Log.i("FEB_13", "Cancel Dialog");
        }catch (Exception e){}
        mMessageListener = null;
        inStream = null;
        outStream = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;
        while (mRun) {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                Socket socket = new Socket(serverAddr, SERVER_PORT);
                socket.setKeepAlive(true);
                setLogWriter("TCP STARTED", null);
                try {
                    outStream = socket.getOutputStream();


                    inStream = socket.getInputStream();
                    try {
                        cancelDialog();
                        Log.i("FEB_13", "Cancel Dialog");
                    }catch (Exception e){}

                    sendMessage("<?xml version=\"1.0\"?><com type=\"20\" userid=\""
                            + userId + "\" key=\"1\"></com>");

                    int len = 0;
                    try {
                        StringBuffer strMsg = new StringBuffer();
                        while ((len = inStream.read()) != -1) {
                            char ca = (char) len;
                            strMsg.append(ca);
                            String originalMessage = new String(strMsg);
                            len = originalMessage.length();
                            if (originalMessage.startsWith("!")) {
                                if (originalMessage.startsWith("!@")) {
                                    if (originalMessage.startsWith("!@#")) {
                                        if (originalMessage.startsWith("!@#")
                                                && originalMessage
                                                .endsWith("#@!")) {
                                            String messageLength = (originalMessage
                                                    .replace("!@#", ""))
                                                    .replace("#@!", "");
                                            int msgLength = Integer
                                                    .parseInt(messageLength);
                                            byte[] bytesReceived = new byte[msgLength];
                                            len = inStream.read(bytesReceived,
                                                    0, msgLength);
                                            mServerMessage = new String(AESCrypto.decrypt(ProprietarySignalling.seceretKey, bytesReceived));
                                            if (mServerMessage != null
                                                    && mMessageListener != null) {
                                                setLogWriter("TCP RECIEVED : "+mServerMessage,null);
                                                mMessageListener
                                                        .tcpMessageReceived(mServerMessage);
                                            }
                                            strMsg = null;
                                            originalMessage = null;
                                            strMsg = new StringBuffer();
                                        } else if (len > 12) {
                                            StringTokenizer st2 = new StringTokenizer(
                                                    originalMessage, "!@#");
                                            int count = 0;
                                            while (st2.hasMoreTokens()) {
                                                count++;
                                                String message = st2
                                                        .nextToken();
                                                if (count == 2) {
                                                    strMsg.delete(0,
                                                            strMsg.length());
                                                    originalMessage = ("!@#" + message)
                                                            .trim();
                                                    strMsg.append(originalMessage);
                                                }
                                            }
                                        }
                                    } else if (len == 3) {
                                        strMsg.delete(0, strMsg.length());
                                    }
                                } else if (len == 2) {
                                    strMsg.delete(0, strMsg.length());
                                }
                            } else {
                                this.setLogWriter(strMsg.toString(), null);
                                strMsg.delete(0, strMsg.length());
                            }
                            len = 0;
                        }
                    }
                    catch (Exception e) {
                        try {
                            showprogress();
                            Log.i("FEB_13", "Show Dialog1");
                        }catch (Exception e1){}
                        CallHeartbeat();
                        this.setLogWriter(e.getMessage(), e);
                        if(inStream != null)
                            inStream.close();
                        if(outStream != null)
                            outStream.close();
                        if(socket != null && socket.isClosed() == false) {
                            socket.close();
                        }
                        try {
                            Thread.sleep(500);
                        }catch (Exception es) {}
                    } finally {
                    }
                } catch (Exception e) {
                    try {
                        showprogress();
                        Log.i("FEB_13", "Show Dialog2");
                    }catch (Exception e1){}
                    Log.e("TCP", "S: Error"+e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (socket.isClosed() == false)
                        socket.close();
                }
            } catch (Exception e) {
                try {
                    showprogress();
                    Log.i("FEB_13", "Show Dialog3");
                }catch (Exception e1){}
                Log.e("TCP", "C: Error-2");
                e.printStackTrace();
            }
        }
    }
    public void setLogWriter(String message, Throwable e) {
        try {
            Log.d("TCP", "TCP : " + message);
            if(e != null)
                e.printStackTrace();
            File e1 = new File(Environment.getExternalStorageDirectory() + "/COMMedia/socketlogTCP.txt");
            if(!e1.exists()) {
                e1.createNewFile();
            }
            FileOutputStream fw = new FileOutputStream(e1, true);
            PrintStream logWriter = new PrintStream(fw, true);
            logWriter.println(this.getCurrentTime() + " :: " + message);
            if(e != null)
                e.printStackTrace(logWriter);
        } catch (Exception var6) {
            Log.d("TCP", "TCP ERROR write log" + var6.toString());
            var6.printStackTrace();
        }
    }

    private String getCurrentTime() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String str = "[" + ts.toString() + "]";
        ts = null;
        return str;
    }

    public static byte[] prepareMessage(byte[] message) {
        int len2 = message.length;
        byte[] messageSize = ("!@#" + len2 + "#@!").getBytes();
        int len1 = messageSize.length;
        byte[] sendData = new byte[len1 + len2];
        System.arraycopy(messageSize, 0, sendData, 0, len1);
        System.arraycopy(message, 0, sendData, len1, len2);
        return sendData;
    }

    public interface TCPListener {
        public void tcpMessageReceived(final String message);
    }

    private void CallHeartbeat(){
        try {
            CallDispatcher callDispatcher = CallDispatcher.getCallDispatcher(SingleInstance.mainContext);
            KeepAliveBean aliveBean = callDispatcher.getKeepAliveBean();
            aliveBean.setKey("1");
            WebServiceReferences.webServiceClient
                    .heartBeat(aliveBean);
        }catch(Exception e){
            SingleInstance.printLog("TCP failure", e.getMessage()
                    + CallDispatcher.LoginUser, "ERROR", e);

            e.printStackTrace();
        }
    }

    //Narayanan FEB_13
    private void showprogress() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(pDialog == null && mRun) {
                        pDialog = new ProgressDialog(SingleInstance.mainContext);
                        pDialog.setCancelable(true);
                        pDialog.setMessage("TCP Disconnected Please Wait...");
                        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pDialog.setProgress(0);
                        pDialog.setMax(100);
                        pDialog.show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancelDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                    pDialog = null;
                }
            }
        });
    }

}
