package org.lib.xml;
/**
 * This class is used to Generate Xml format String.
 * 
 *
 */
public class GenerateXml {
	public String startTag(String name)
	{
		return "<"+name+">";
	}
	public String startTag(String name,String attr,String value)
	{
		return "<"+name+" "+attr+"=\""+value+"\">";
	}
	public String startTag(String name,String attr,int value)
	{
		return "<"+name+" "+attr+"=\""+value+"\">";
	}
	public String setAttribute(String name,String attr,String value)
	{
		return "<"+name+" "+attr+"=\""+value+"\">";
	}
	public String endTag(String name)
	{
		return "</"+name+">";
	}
	public String startAndendTags(String name, String attr, String value)
	{
		return "<"+name+" "+attr+"=\""+value+"\"/>";
	}
	public String startAndendTags(String name, String attr, boolean value)
	{
		return "<"+name+" "+attr+"=\""+value+"\"/>";
	}
	public String startAndendTags(String name, String attr, int value)
	{
		return "<"+name+" "+attr+"=\""+value+"\"/>";
	}
	public String startAndendTags(String name, String[] attr, String[] value)
	{
		String result = "<"+name+" ";
		for (int i=0 ; i<attr.length ; i++)
		{
			result = result+attr[i]+ "=\""+value[i]+"\" ";
		}
		result = result+"/>";
		return result;
	}
	
	
	/**
	 * Returns the Input String with XML format
	 * @param text Input String
	 * @return The XML format String
	 */
	
	 public String GetInputMessage(String text)
     {
         String ret = "";
         try
         {
             String message = "";
             int len = 0, emplen = 0;
             message = "<?xml version=\"1.0\"?>" +
                      "<protostart size=\"\"></protostart>";
             message = message + text;
             len = message.length();
             emplen = Integer.toString(len).length();
             len = len + emplen;
             ret = "<?xml version=\"1.0\"?>" +
                      "<protostart size=\"" + len + "\">";
             ret = ret + text + "</protostart>";
         }
         catch(Exception e) {e.printStackTrace(); }
         return ret;
     }
	
	


}
