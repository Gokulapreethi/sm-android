package com.cg.instancemessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import android.util.Log;

/**
 * User can write the chat history into the file and retrieve the data about the
 * chat history from the file
 * 
 * 
 */
public class RetriveChatHistory {

	public RetriveChatHistory() {

	}

	/**
	 * Write the details about the chat when user send or receive the im. If
	 * data entered in to the file successfully this method will return true
	 * other wise it will return false
	 * 
	 * @param dataToWrite
	 *            - chat history data to write
	 * @param filename
	 *            - name of the file
	 * @return
	 */
	public boolean writeOnFile(String dataToWrite, String filename) {
		// used to check and create file in sdcard and writeData

		Log.d("MIM", "File Write Called");

		File folder = new File("/sdcard/COMMedia/");
		if (!folder.exists()) {
			folder.mkdir();
		}

		File fi = new File("/sdcard/COMMedia/" + filename);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fi, true));

			bw.write(dataToWrite);
			bw.newLine();
			bw.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * To read the data from the file then parse and make the ImBeanUpdated bean
	 * ArrayList. That bean list will be returned by this method
	 * 
	 * @param filename
	 * @return
	 */
	public ArrayList<ImBeanUpdated> readFile(String filename) {

		Log.d("MIM", "File Read Called");
		File fi = new File("/sdcard/COMMedia/" + filename);

		ArrayList<ImBeanUpdated> chatHistory = new ArrayList<ImBeanUpdated>();

		if (!fi.exists() && fi.length() < 0) {
			// System.out.println("The specified file does not exist");
		} else {
			try {
				FileReader fr = new FileReader(fi);
				BufferedReader reader = new BufferedReader(fr);
				String st = "";

				while ((st = reader.readLine()) != null) {

					Log.e("thread", "##########" + st);
					// Integer.toString(fromTo) + ","+from+","+ time()+","+
					// messagedatas;

					String[] strArr = st.split(",");
					Log.e("thread", "##########" + strArr.length);
					ImBeanUpdated imbean = new ImBeanUpdated();
					imbean.putidentifyFromTo(Integer.parseInt(strArr[0]));
					Log.e("thread", "##########" + strArr[0]);
					imbean.putFromUser(strArr[1]);
					Log.e("thread", "##########" + strArr[1]);
					imbean.putTime(strArr[2]);
					Log.e("thread", "##########" + strArr[2]);
					imbean.putmessageText(strArr[3]);
					Log.e("thread", "##########" + strArr[3]);
					chatHistory.add(imbean);
				}
			} catch (Exception e) {

			}

		}
		return chatHistory;
	}
}
