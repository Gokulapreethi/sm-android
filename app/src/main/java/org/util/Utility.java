package org.util;

import java.security.MessageDigest;
import java.util.Date;
/**
 * Used to generate Random Id for Application like SessionId,SignalId etc. 
 * 
 *
 */
public class Utility {

/**
 * Used to generate Random Id using Date Time.
 * @return SessionId
 */
	
	public static String getSessionID(){
		String sessid=null;
		try{
		
		String time =new Long(new Date().getTime()).toString();
		sessid= time;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		 
		return sessid;
	}
	
	
	
//	public String getSessionIDx(){
//		String sessid=null;
//		try{
//		
//		String time =new Long(new Date().getTime()).toString();
//		sessid= time;
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		 
//		return sessid;
//	}
	public String getSessionID(String macAddress)
	{
		//String sessid=null;
		try{
		MessageDigest md = MessageDigest.getInstance("MD5");
		String time =new Long(new Date().getTime()).toString();
		
		String input=time+macAddress;
		
		  byte[] dataBytes = input.getBytes();
	
          md.update(dataBytes, 0, dataBytes.length);
    
        byte[] mdbytes = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
		
		return sb.toString();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		 
		return "123abcfh";
	}
	
	
	/**
	 * Used to generate Random MediaId using java.lang.Math.
	 * @return MediaId
	 */
	public long getRandomMediaID(){
		long aNumber=0;
		try{
			aNumber = (long) (Math.random() * 100000000);
			//System.out.println(aNumber);	
			}catch(Exception e){
				e.printStackTrace();
			}
			return aNumber;
	}

	/**
	 * Used to generate Random PortNo between 1024 to 65535 using java.lang.Math.
	 * @return PortNo
	 */
	public int getPortNo(){
		int value =0;

		while(true)
		{
			value = (int)(Math.random() * 8847);
			if(value<1024 || value>65535)
			{
				continue;

			}else{
				break;
			}
		}

		return value;

	}






}
