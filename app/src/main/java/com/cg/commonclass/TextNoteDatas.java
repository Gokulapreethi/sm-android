package com.cg.commonclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import android.util.Log;

/**
 * Instance of this TextNoteDatas is used to create,write and retrieve the data
 * from the text file
 * 
 * 
 * 
 */
public class TextNoteDatas {

	public TextNoteDatas() {

	}

	/**
	 * Check the file name if it is available append the data otherwise create
	 * the file and write the data. Result is true when the file writing is
	 * success otherwise returned result will be false
	 * 
	 * @param filename
	 *            - name of the text file
	 * @param datas
	 *            - data to write on the file
	 * @return - boolean
	 */
	public boolean checkAndCreate(String filename, String datas) {
	try {
		File folder = new File("/sdcard/COMMedia/");
		if (!folder.exists()) {
			folder.mkdir();
		}
	
			Log.d("lg","file content"+datas);
			File fi = new File(filename);
			FileWriter writer = new FileWriter(fi);
			Log.d("create", "callmethod");
			writer.write(datas);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getInformation(String filename) {
		/*File fi = new File(filename);
		try {
			FileReader fr = new FileReader(fi);
			BufferedReader reader = new BufferedReader(fr);
			String st = "";

			while ((st = reader.readLine()) != null) {
				
				return st;
			}
		} catch (Exception e) {

		}

		return "";*/
		File file=new File(filename);
		String st = "";
		StringBuilder bldr=new StringBuilder();
		
		try {
			FileInputStream fis=new FileInputStream(file);
			BufferedReader rdr=new  BufferedReader(new InputStreamReader(fis));
			
			try {
				while((st=rdr.readLine())!=null)
				{
					Log.d("File", "#######################"+st);
					bldr.append(st);
					bldr.append('\n');
					st="";
				}
				
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return bldr.toString();
	}
	
	
	public  String readFile(String path) throws IOException {
		  FileInputStream stream = new FileInputStream(new File(path));
		  try {
		      FileChannel fc = stream.getChannel();
		      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		      /* Instead of using default, pass in a decoder. */
		      Log.d("File", "#######################"+Charset.defaultCharset().decode(bb).toString());
		      return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		}

}
