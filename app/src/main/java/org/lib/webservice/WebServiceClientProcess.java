package org.lib.webservice;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.model.WebServiceBean;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;

import android.util.Log;

/**
 * WebServiceClientProcess which extends Thread. This class is used to call
 * WebService methods for SignIn,Subscribe etc. The result of WebService Request
 * is Send as Notification.
 * 
 * 
 */
public class WebServiceClientProcess extends Thread {

	/**
	 * Queue used to Retrieve WebService Request datas.
	 */
	Queue queue = null;

	private AndroidInsecureKeepAliveHttpsTransportSE androidHttpTransport = null;
	private SoapSerializationEnvelope envelope = null;
	private SoapObject request = null;
	private SoapPrimitive sp = null;
	private String quotes = "\"";
	/**
	 * Set the URL of the Server as the input String.
	 */
	private String mWsdlURL = null;
	/**
	 * NameSpace of the Server.
	 */
	private String mNamespace = null;
	/**
	 * Used to Parse the XML.
	 */
	private XmlParser parser = new XmlParser();
	/**
	 * This interface is used to notify the WebService Response message like
	 * SIGNIN,SUBSCRIBE etc. This is also used to Notify Error(failure).
	 */
	private WebServiceCallback webServiceCallback = null;
	/**
	 * Used to maintain WebServiceClientProcess Thread.
	 */
	private boolean running = true;
	boolean chk = false;

	/**
	 * This bean class is used to set and get datas related to WebService
	 * response. This class contains information like Result and Text.
	 */
	private WebServiceBean webServiceBean = null;
	/**
	 * Server IP address
	 */
	private String ipaddress = null;
	/**
	 * Port of the Server.
	 */
	private int port;

	/**
	 * constructor which assigns the Queue
	 * 
	 * @param queue
	 *            message stack for WebService Request.
	 */
	WebServiceClientProcess(Queue queue, String ipaddress, int port) {

		this.queue = queue;
		this.ipaddress = ipaddress;
		this.port = port;

	}

	/**
	 * Get the URL which the Server is running.
	 * 
	 * @return The URL of Server
	 */
	public String getmWsdlURL() {
		return mWsdlURL;
	}

	/**
	 * Check the Status of Thread.
	 * 
	 * @return Thread State.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Used to stop the Thread.
	 * 
	 * @param running
	 *            Thread State.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Set the URL of the Server as the input String.
	 * 
	 * @param mWsdlURL
	 *            The URL of Server.
	 */
	public void setmWsdlURL(String mWsdlURL) {
		this.mWsdlURL = mWsdlURL;
	}

	/**
	 * Get the NameSpace of the server.
	 * 
	 * @return The NameSpace.
	 */
	public String getmNamespace() {
		return mNamespace;
	}

	/**
	 * Set the NameSpace of the Server as the input String.
	 * 
	 * @param mNamespace
	 *            The NameSpace of Server
	 */
	public void setmNamespace(String mNamespace) {
		this.mNamespace = mNamespace;
	}

	/**
	 * Register the implemented class which is used to notify Results.
	 * 
	 * @param webServiceCallback
	 *            implemented class
	 */
	public void setWebServiceCallback(WebServiceCallback webServiceCallback) {
		this.webServiceCallback = webServiceCallback;
	}

	/**
	 * Used to Request webservice methods for
	 * SUBSCRIBE,SIGNIN,SIGNOUT,ACCEPT,REJECT etc. The Received response is
	 * Notify to the Registered class.
	 */

	public void run() {
		// TODO Auto-generated method stub

		Log.d("zxz", "Call webser thread");
		Log.d("HB", "Timer executed on webser");
		// while (running) {
		// Log.d("zxz", "Call webser thread111");
		// Log.d("HB", "Timer executed on webser1111");
		// try {
		// Log.d("zxz", "Call webser thread");
		// Log.d("HB", "Timer executed on webser");
		// Servicebean servicebean = (Servicebean) queue.getMsg();
		//
		// String parse = "";
		//
		// try {
		// parse = mWsdlURL.substring(mWsdlURL.indexOf("://") + 3);
		// // System.out.println("sds 1"+parse);
		// parse = parse.substring(parse.indexOf(":") + 1);
		// // System.out.println("sds2 "+parse);
		// parse = parse.substring(parse.indexOf("/"),
		// parse.indexOf("?"));
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }
		//
		// // System.out.println("Parse "+parse);
		//
		// androidHttpTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
		// ipaddress, port, parse, 7000);
		// // Get the message from the Queue and process appropriate
		// // Request.
		// Log.d("zxz",
		// "Call webser thread req "
		// + servicebean.getServiceMethods());
		// switch (servicebean.getServiceMethods()) {
		// // Request for SUBSCRIBE
		// case SUBSCRIBE:
		// Log.d("zxz", "subscribe request");
		// // System.out.println("case SUBSCRIBE:");
		// request = new SoapObject(mNamespace, "Subscribe");
		// System.out.println("Input : "
		// + (String) servicebean.getObj());
		// request.addProperty("regInputXml",
		// (String) servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// // System.out.println("?"+mNamespace+"Subscribe");
		// androidHttpTransport.call(quotes + mNamespace + "Subscribe"
		// + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("Response : "+sp.toString());
		// webServiceBean = parser.parseSubscribe(sp.toString());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// // Request for SIGNIN
		// case SIGNIN:
		// Log.d("zxz", "Call signin");
		// request = new SoapObject(mNamespace, "Signin");
		// System.out.println("###### LOGIN REQUSET :"
		// + (String) servicebean.getObj());
		// request.addProperty("loginxml",
		// (String) servicebean.getObj());
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"Signin");
		// androidHttpTransport.call(quotes + mNamespace + "Signin"
		// + quotes, envelope);
		// Log.d("zxz", "received login befor soap pri "
		// + (String) servicebean.getObj());
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("zxz", "After soap pri");
		//
		// Log.d("zxz", "login response " + sp.toString());
		//
		// chk = parser.getResult(sp.toString());
		// // System.out.println("# chk"+chk);
		// if (chk) {
		// ArrayList<BuddyInformationBean> buddyList = parser
		// .parseSignin(sp.toString());
		// // System.out.println("## Size"+buddyList.size());
		// servicebean.setObj(buddyList);
		//
		// Log.d("zxz", "Call on login if");
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		//
		// Log.d("zxz", "Call on login else");
		// }
		//
		// if (sp.toString().contains("<update type=")) {
		// Log.d("zxz", "update tag available");
		// servicebean.setIsUpdateavailable(true);
		// servicebean.setUpdateObject(parser.ParseUpdateTag(sp
		// .toString()));
		// }
		// Log.d("zxz", "Call on notify");
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// Log.d("zxz", "Call on notified");
		// break;
		//
		// // Request for SIGNOUT
		// case SIGNOUT:
		// Log.d("zxz", "called signout");
		// request = new SoapObject(mNamespace, "Signout");
		// // System.out.println("REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("uname", (String) servicebean.getObj());
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace + "Signout"
		// + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("## Logout Response : "+sp.toString());
		// webServiceBean = parser.parseSubscribe(sp.toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "finished signout");
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// case CALLHISTORY:
		//
		// //
		// // Log.d("zxz", "callHistory request");
		// // Log.d("zxz", "Call accept request " + (String)
		// // servicebean.getObj());
		// request = new SoapObject(mNamespace, "RecordTransaction");
		// // System.out.println("REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("historyxml",
		// (String) servicebean.getObj());
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "RecordTransaction" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// //
		// chk = parser.getResult(sp.toString());
		// // System.out.println("# chk"+chk);
		// // Log.d("zxz",
		// // "Check valueeeeeeeeeeeeeeeeeeeeeeeeeeee "+chk);
		// if (parser.getResult(sp.toString())) {
		// // Log.d("zxz", "Call on login ifcdcd  "+sp.toString());
		// ArrayList<Object> buddyList = parser
		// .parseCallhistoryResponse(sp.toString());
		//
		// servicebean.setObj(buddyList);
		//
		// // Log.d("zxz", "Call on login if "+buddyList.size());
		//
		// } else {
		// // Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// // Log.d("zxz", "Call on login else");
		//
		// }
		// // Log.d("zxz", "Call on notify");
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// // Request for Accept Buddy Request
		// case ACCEPT:
		// Log.d("zxz", "accept request");
		// Log.d("zxz",
		// "Call accept request "
		// + (String) servicebean.getObj());
		// request = new SoapObject(mNamespace, "AcceptReject");
		// // System.out.println("REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("uname", (String) servicebean.getObj());
		// request.addProperty("bname", servicebean.getOption()[0]);
		// request.addProperty("response", servicebean.getOption()[1]);
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "AcceptReject" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// //
		// chk = parser.getResult(sp.toString());
		// // System.out.println("# chk"+chk);
		// if (chk) {
		// ArrayList<BuddyInformationBean> buddyList = parser
		// .parseAcceptResponse(sp.toString());
		//
		// servicebean.setObj(buddyList);
		// Log.d("zxz", "Call on login if");
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// // webServiceBean = parser.parseResultFromXml(sp
		// // .toString());
		// webServiceBean = new WebServiceBean();
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		// }
		// Log.d("zxz", "Call on notify");
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// //
		// Log.d("zxz", "Accept response " + sp.toString());
		//
		// break;
		//
		// // Request for REJECT Buddy Request.
		// case REJECT:
		// Log.d("zxz", "reject request");
		// request = new SoapObject(mNamespace, "AcceptReject");
		// // System.out.println("REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("uname", (String) servicebean.getObj());
		// request.addProperty("bname", servicebean.getOption()[0]);
		// request.addProperty("response", servicebean.getOption()[1]);
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"HeartBeat");
		// androidHttpTransport.call(quotes + mNamespace
		// + "AcceptReject" + quotes, envelope);
		//
		// sp = (SoapPrimitive) envelope.getResponse();
		// //
		// Log.d("res", "Reject response is----->" + sp.toString());
		// chk = parser.getResult(sp.toString());
		// // System.out.println("# chk"+chk);
		// if (chk) {
		// webServiceBean = new WebServiceBean();
		// webServiceBean.setResult("1");
		// webServiceBean.setText(servicebean.getOption()[0]);
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login if");
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// // webServiceBean = parser.parseResultFromXml(sp
		// // .toString());
		// webServiceBean = new WebServiceBean();
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		// }
		// Log.d("zxz", "Call on notify");
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// // System.out.println("###### REJECT "+sp.toString());
		// break;
		// // Request for HEARTBEAT.
		// case HEARTBEAT:
		// Log.d("zxz", "heartbeat request");
		// // System.out.println("case HEARTBEAT:");
		// request = new SoapObject(mNamespace, "HeartBeat");
		// Log.d("zxz", "heartbeat soap 1");
		// System.out.println("KeepAlive REQUSET :"
		// + (String) servicebean.getObj());
		// request.addProperty("keepAliveXML",
		// (String) servicebean.getObj());
		// Log.d("zxz", "heartbeat soap 2");
		// Log.d("zxz", "heartbeat key..." + servicebean.getKey());
		// request.addProperty("key", servicebean.getKey());
		// Log.d("zxz", "heartbeat soap 3");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// Log.d("zxz", "heartbeat soap 4");
		// envelope.setOutputSoapObject(request);
		// Log.d("zxz", "heartbeat soap 5");
		// // System.out.println("?"+mNamespace+"HeartBeat");
		// androidHttpTransport.call(quotes + mNamespace + "HeartBeat"
		// + quotes, envelope);
		// Log.d("zxz", "heartbeat soap 6");
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("\n\n\n Keepalive Response : "+sp.toString());
		// Log.d("zxz", "heartbeat response " + sp.toString());
		// chk = parser.getResult(sp.toString());
		// if (chk) {
		// ArrayList<BuddyInformationBean> buddyList = parser
		// .parseKeepAlive(sp.toString());
		// servicebean.setObj(buddyList);
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case SAVEMYLOCATION:
		//
		// request = new SoapObject(mNamespace, "SaveMyLocation");
		// Log.d("zxz", "SaveMyLocation soap 1");
		// //
		// System.out.println("KeepAlive REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("locationXml",
		// (String) servicebean.getObj());
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//
		// envelope.setOutputSoapObject(request);
		// Log.d("zxz", "heartbeat " + (String) servicebean.getObj());
		// // System.out.println("?"+mNamespace+"HeartBeat");
		// androidHttpTransport.call(quotes + mNamespace
		// + "SaveMyLocation" + quotes, envelope);
		// Log.d("zxz", "heartbeat soap 6");
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("\n\n\n Keepalive Response : "+sp.toString());
		// Log.d("zxz", "SaveMyLocation response " + sp.toString());
		// chk = parser.getResult(sp.toString());
		// if (chk) {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// case CreateVirtualBuddy:
		//
		// request = new SoapObject(mNamespace, "CreateVirtualBuddy");
		// Log.d("zxz", "CreateVirtualBuddy soap 1");
		// //
		// System.out.println("KeepAlive REQUSET :"+(String)servicebean.getObj());
		// request.addProperty("virtualbuddyXml",
		// (String) servicebean.getObj());
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		//
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "CreateVirtualBuddy" + quotes, envelope);
		// Log.d("zxz", "heartbeat soap 6");
		// sp = (SoapPrimitive) envelope.getResponse();
		//
		// Log.d("zxz", "SaveMyLocation response " + sp.toString());
		// chk = parser.getResult(sp.toString());
		// if (chk) {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// // To add existing virtual buddy
		// // Name : AddVirtualBuddy
		// // Input Parameter : String username
		// // String virtualbuddyid
		// // Output : Well formatted XML String
		//
		// case ADDVIRTUALBUDDY:
		// Log.d("zxz", "add request");
		// request = new SoapObject(mNamespace, "AddVirtualBuddy");
		// //
		// System.out.println("REQUSET INPUT :"+(String)servicebean.getObj());
		// request.addProperty("username",
		// (String) servicebean.getObj());
		// request.addProperty("virtualbuddyid",
		// servicebean.getOption()[0]);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"AddPeople");
		// androidHttpTransport.call(quotes + mNamespace
		// + "AddVirtualBuddy" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("ADD PEOPLE Result :"+sp.toString());
		// Log.d("zxz", "AddVirtualBuddy response " + sp.toString());
		// chk = parser.getResult(sp.toString());
		// // System.out.println("CHK?"+chk);
		// if (chk) {
		// ArrayList<VirtualBuddyBean> peopleList = parser
		// .parseAddVirtualBuddy(sp.toString());
		// // System.out.println(peopleList);
		// servicebean.setObj(peopleList);
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// // Request for Delete Virtual buddy.
		// case DELETEVIRTUALBUDDY:
		// Log.d("zxz", "deleet request");
		// request = new SoapObject(mNamespace, "DeleteVirtualBuddy");
		// //
		// System.out.println("REQUSET INPUT :"+(String)servicebean.getObj()+" "+servicebean.getOption()[0]);
		// request.addProperty("username",
		// (String) servicebean.getObj());
		// request.addProperty("virtualbuddyid",
		// servicebean.getOption()[0]);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"DeletePeople");
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteVirtualBuddy" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("Result :"+sp.toString());
		// Log.d("zxz",
		// "Delete VirtualBuddy response " + sp.toString());
		// webServiceBean = parser.parseResultFromXml(sp.toString());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// // Request for DeletePeople.
		// case DELETEPEOPLE:
		// Log.d("zxz", "deleet request");
		// request = new SoapObject(mNamespace, "DeletePeople");
		// //
		// System.out.println("REQUSET INPUT :"+(String)servicebean.getObj()+" "+servicebean.getOption()[0]);
		// request.addProperty("uname", (String) servicebean.getObj());
		// request.addProperty("bname", servicebean.getOption()[0]);
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"DeletePeople");
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeletePeople" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("Result :"+sp.toString());
		// webServiceBean = parser.parseResultFromXml(sp.toString());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// // Request for FINDPEOPLE.
		// case FINDPEOPLE:
		// Log.d("zxz", "find request");
		// request = new SoapObject(mNamespace, "FindPeople");
		// //
		// System.out.println("REQUSET INPUT :"+(String)servicebean.getObj());
		// request.addProperty("searchStr",
		// (String) servicebean.getObj());
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"FindPeople");
		// androidHttpTransport.call(quotes + mNamespace
		// + "FindPeople" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("zxz", "find request " + sp.toString());
		// // System.out.println("Result :"+sp.toString());
		// chk = parser.getResult(sp.toString());
		// if (chk) {
		// ArrayList<FindPeopleBean> peopleList = parser
		// .parseFindPeople(sp.toString());
		// servicebean.setObj(peopleList);
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// // Request for SHARE_REMAINDER.
		// case SHARE_REMAINDER:
		// request = new SoapObject(mNamespace, "ShareReminder");
		// request.addProperty("sharereminderXml",
		// (String) servicebean.getObj());
		// Log.d("zxz", "share " + (String) servicebean.getObj());
		// System.out.println("XML :" + (String) servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace + "AddPeople"
		// + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // <?xml version="1.0"?><com result="1"><sharereminder
		// // fileid="7770940"
		// // text="sharereminder accepted successfully" /></com>
		// Log.d("zxz", "share respo " + sp);
		// //
		// System.out.println("#################### Share Reminder  Response :"+sp.toString());
		//
		// chk = parser.getResult(sp.toString());
		// if (chk) {
		// ShareSendBean sbean = parser.getShareSendResult(sp
		// .toString());
		// servicebean.setObj(sbean);
		// } else {
		//
		// }
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// // Request for BuddyRequest.
		// case ADDPEOPLE:
		// Log.d("zxz", "add request");
		// request = new SoapObject(mNamespace, "AddPeople");
		// //
		// System.out.println("REQUSET INPUT :"+(String)servicebean.getObj());
		// request.addProperty("uname", (String) servicebean.getObj());
		// request.addProperty("bname", servicebean.getOption()[0]);
		// request.addProperty("key", "0");
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// // System.out.println("?"+mNamespace+"AddPeople");
		// androidHttpTransport.call(quotes + mNamespace + "AddPeople"
		// + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// // System.out.println("ADD PEOPLE Result :"+sp.toString());
		// Log.d("zxz", "Addpeople response " + sp.toString());
		// chk = parser.getResult(sp.toString());
		// Log.d("thread", "Addpeople response " + chk);
		//
		// // System.out.println("CHK?"+chk);
		// if (chk) {
		// ArrayList<BuddyInformationBean> peopleList = parser
		// .parseAddPeople(sp.toString());
		// // System.out.println(peopleList);
		// servicebean.setObj(peopleList);
		// Log.d("thread", "Addpeople response 111111"
		// + servicebean.getObj());
		//
		// } else {
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// }
		// if (servicebean.getObj() instanceof BuddyInformationBean) {
		// Log.d("thread",
		// "Addpeople response 123" + servicebean.getObj());
		// } else if (servicebean.getObj() instanceof ArrayList) {
		// Log.d("thread",
		// "Addpeople response 456" + servicebean.getObj());
		// } else {
		// Log.d("thread",
		// "Addpeople response 78" + servicebean.getObj());
		// }
		//
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case CHANGEPASSWORD:
		//
		// Log.d("zxz", "change request");
		// request = new SoapObject(mNamespace, "ChangePassword");
		// System.out.println("REQUSET INPUT :"
		// + (String) servicebean.getObj());
		// request.addProperty("userName", servicebean.getOption()[0]);
		// Log.d("requestvalue",
		// "" + servicebean.getOption()[0].toString());
		// request.addProperty("oldPassword",
		// servicebean.getOption()[1]);
		// Log.d("requestvalue",
		// "" + servicebean.getOption()[1].toString());
		// request.addProperty("newPassword",
		// servicebean.getOption()[2]);
		// Log.d("requestvalue",
		// "" + servicebean.getOption()[2].toString());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// System.out.println("?" + mNamespace + "ChangePassword");
		// androidHttpTransport.call(quotes + mNamespace
		// + "ChangePassword" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("ChangePassword Result :"
		// + sp.toString());
		//
		// webServiceBean = parser.parseResultFromXml(sp.toString());
		// Log.d("zxz", "change requestuuuuuuuu  " + sp.toString());
		// Log.e("chk", webServiceBean.getText());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case FORGOTPASSWORD:
		//
		// // ChangePassword(userid,oldpassword, newpassword)
		// // ForgetPassword(userid,emailid);
		//
		// Log.d("zxz", "change request");
		// request = new SoapObject(mNamespace, "ForgetPassword");
		// System.out.println("REQUSET INPUT :"
		// + servicebean.getOption()[0]);
		// System.out.println("REQUSET INPUT :"
		// + servicebean.getOption()[1]);
		// request.addProperty("username", servicebean.getOption()[0]);
		// Log.d("username",
		// "" + servicebean.getOption()[0].toString());
		// request.addProperty("emailid", servicebean.getOption()[1]);
		// Log.d("email_id",
		// "" + servicebean.getOption()[1].toString());
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// System.out.println("request url :" + request.toString());
		//
		// System.out.println("?" + mNamespace + "ForgetPassword");
		// androidHttpTransport.call(quotes + mNamespace
		// + "ForgetPassword" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("Forget Password Result :"
		// + sp.toString());
		//
		// webServiceBean = parser.parseForgetPasswordResultFromXml(sp
		// .toString());
		// Log.d("zxz", "change request" + sp.toString());
		// Log.e("chk", webServiceBean.getText());
		// Log.e("chk", webServiceBean.getResult());
		// servicebean.setObj(webServiceBean);
		// // Log.d("response", ""+webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case SECRETANSWER:
		//
		// Log.d("zxz", "subscribe request");
		//
		// request = new SoapObject(mNamespace, "SecretAnswer");
		// System.out.println("Input : "
		// + (String) servicebean.getObj());
		// request.addProperty("secretXml",
		// (String) servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// // System.out.println("?"+mNamespace+"Subscribe");
		// androidHttpTransport.call(quotes + mNamespace
		// + "SecretAnswer" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("Response : " + sp.toString());
		// webServiceBean = parser.parseSubscribe(sp.toString());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// case CONFIGUREFB:
		// Log.d("FBB", "facebook configuration");
		//
		// request = new SoapObject(mNamespace, "LinkFbAccount");
		// request.addProperty("linkfbxml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "LinkFbAccount" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("configureFB Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call on login if ");
		// ArrayList<BuddyInformationBean> buddyList = parser
		// .parseLink(sp.toString());
		//
		// servicebean.setObj(buddyList);
		//
		// Log.d("zxz", "Call on login if " + buddyList.size());
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		//
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case DELINKFB:
		// Log.d("FBB", "Facebook disconnect");
		// request = new SoapObject(mNamespace, "DelinkFbAccount");
		//
		// System.out.println("Reuest:"
		// + (String) servicebean.getOption()[0]);
		// System.out.println("Reuest:"
		// + (String) servicebean.getOption()[1]);
		//
		// request.addProperty("userid", servicebean.getOption()[0]);
		// request.addProperty("fbaccid", servicebean.getOption()[1]);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "DelinkFbAccount" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("delink Result :" + sp.toString());
		//
		// webServiceBean = parser.parseSubscribe(sp.toString());
		// Log.d("zxz", "delink request" + sp.toString());
		// Log.e("chk", webServiceBean.getText());
		// servicebean.setObj(webServiceBean);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case CREATEFORM:
		// Log.d("log", "Came to createforms");
		//
		// request = new SoapObject(mNamespace, "CreateForm");
		// request.addProperty("formXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "CreateForm" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("create form Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// ArrayList<String[]> result = parser
		// .parseCreateFormResult(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case ACCESSFORM:
		// Log.d("log", "Came to createforms");
		//
		// request = new SoapObject(mNamespace, "SetFormSettings");
		// request.addProperty("formsettingsXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetFormSettings" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("create form Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser.parseAccessFormResult(sp
		// .toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case DELETEACCESSFORM:
		// Log.d("log", "Came to createforms");
		//
		// request = new SoapObject(mNamespace, "SetFormSettings");
		// request.addProperty("formsettingsXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetFormSettings" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("create form Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser.parseDeleteAccessFormResult(sp
		// .toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case GETFORMSETTINGS:
		// Log.d("log", "Came to getsetting");
		//
		// request = new SoapObject(mNamespace, "GetFormSettings");
		// request.addProperty("formsettingsXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetFormSettings" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("create form Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser
		// .parsesettingAccessFormResult(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case GETATTRIBUTE:
		// Log.d("log", "Came to getsetting");
		//
		// request = new SoapObject(mNamespace, "SetFormAttributes");
		// request.addProperty("attributeXML", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetFormAttributes" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("create form Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser
		// .parsesettingAttributeFormResult(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case GETFORMATTRIBUTE:
		//
		// Log.i("bug", "WEBSERVICE CLIENT PROCESS INSIDE--->");
		// Log.d("log", "Came to getsetting");
		//
		// request = new SoapObject(mNamespace, "GetFormAttributes");
		// request.addProperty("attributeXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetFormAttributes" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		//
		// Log.i("bug",
		// "WEBSERVICE CLIENT PROCESS INSIDE--->"
		// + sp.toString());
		//
		// System.out.println("create attribute Result :"
		// + sp.toString());
		//
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// ArrayList<FormAttributeBean> result = parser
		// .parseGetAttribute(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case ADDFORMRECORDS:
		// Log.d("forms", "Came to addforms");
		//
		// request = new SoapObject(mNamespace, "AddFormData");
		// request.addProperty("formdataXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "AddFormData" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("Add records Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser.parseAddFormXml(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case GETFORMRECORDS:
		// Log.d("forms", "Came to getforms");
		// request = new SoapObject(mNamespace, "GetFormContent");
		// request.addProperty("formId", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetFormContent" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("get records Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// Formsinfocontainer fbean = parser.parseGetFormResult(sp
		// .toString());
		// servicebean.setObj(fbean);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		// case UPDATEFORMRECORDS:
		// Log.d("forms", "Came to update foem");
		//
		// request = new SoapObject(mNamespace, "UpdateForm");
		// request.addProperty("updateXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "UpdateForm" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("Add records Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser
		// .parseEditFormXml(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case SHAREBYPROFILE:
		// Log.d("forms", "Came to update foem");
		//
		// request = new SoapObject(mNamespace,
		// "SearchPeopleByProfile");
		// request.addProperty("profileXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "SearchPeopleByProfile" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("Add records Result :" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// ArrayList<String> result = parser
		// .parseSharebyprofileSearchXml(sp.toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case DELETEFORM:
		//
		// Log.d("FBB", "Delete Form");
		// request = new SoapObject(mNamespace, "DeleteForm");
		//
		// System.out.println("Reuest:"
		// + (String) servicebean.getOption()[0]);
		// System.out.println("Reuest:"
		// + (String) servicebean.getOption()[1]);
		//
		// request.addProperty("userid", servicebean.getOption()[0]);
		// request.addProperty("formid", servicebean.getOption()[1]);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteForm" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// System.out.println("delete Result :" + sp.toString());
		//
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// FormsBean fbean = parser.parseDeleteFormResult(sp
		// .toString());
		// servicebean.setObj(fbean);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		// }
		//
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		//
		// case DELETEFORMRECORD:
		// Log.d("forms", "Came to delete form");
		//
		// Log.d("FBB", "Delete row Form");
		// request = new SoapObject(mNamespace, "DeleteRowFromForm");
		//
		// Log.i("BLOB", (String) servicebean.getOption()[0]);
		// Log.i("BLOB", (String) servicebean.getOption()[1]);
		// Log.i("BLOB", (String) servicebean.getOption()[2]);
		//
		// request.addProperty("userid", servicebean.getOption()[0]);
		// request.addProperty("formid", servicebean.getOption()[1]);
		// request.addProperty("tableid", servicebean.getOption()[2]);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteRowFromForm" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.i("BLOB", "responce" + sp.toString());
		//
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// FormsBean fbean = parser.parseDeleteFormRecordResult(sp
		// .toString());
		// servicebean.setObj(fbean);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		// }
		//
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case OFFLINECONFIGURATION:
		// Log.d("forms", "Came to OFFLINECONFIGURATION");
		//
		// request = new SoapObject(mNamespace, "offlineconfiguration");
		// request.addProperty("configurationXml",
		// servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "offlineconfiguration" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// OfflineConfigurationBean result1 = parser
		// .responseForCallConfigurationInfo(sp.toString());
		// servicebean.setObj(result1);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case GETMYCONFIGURATIONDETAILS:
		// Log.d("Avataar", "Came to addforms" + servicebean.getObj());
		//
		// request = new SoapObject(mNamespace,
		// "GetMyConfigurationDetails");
		// request.addProperty("userId", servicebean.getOption()[0]);
		// request.addProperty("dateTime", servicebean.getOption()[1]);
		//
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetMyConfigurationDetails" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("Avataar", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("Avataar", "Call configuration if ");
		// ArrayList<Object> result1 = parser
		// .parseGetMyConfigurationDetails(sp.toString());
		//
		// servicebean.setObj(result1);
		// } else {
		// Log.d("Avataar", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("Avataar", "Call on login else");
		// }
		// Log.d("Avataar", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case OFFLINECALLRESPONSE:
		// Log.d("forms", "Came to addforms");
		//
		// request = new SoapObject(mNamespace, "OfflineCallResponse");
		// request.addProperty("responseXml", servicebean.getObj());
		// System.out.println(request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "OfflineCallResponse" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// ArrayList<Object> result1 = parser
		// .parseOfflineCallResponse(sp.toString());
		// servicebean.setObj(result1);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case DELETEALLSHARES:
		// Log.d("forms", "Came to addforms");
		//
		// request = new SoapObject(mNamespace, "DeleteAllShares");
		// request.addProperty("deleteXml", servicebean.getObj());
		// System.out.println("buddyId:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteAllShares" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result1 = parser
		// .parseWithDrawnPermissionXML(sp.toString());
		//
		// servicebean.setObj(result1);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case RESPONSEFORCALLCONFIGURATION:
		// Log.d("forms", "Came to addforms");
		//
		// request = new SoapObject(mNamespace,
		// "ResponseForCallConfiguration");
		// request.addProperty("configurationXml",
		// servicebean.getObj());
		// System.out.println("buddyId:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport
		// .call(quotes + mNamespace
		// + "ResponseForCallConfiguration" + quotes,
		// envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result1 = parser
		// .parseResponseForCallConfiguration(sp
		// .toString());
		//
		// servicebean.setObj(result1);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case GETCONFIGURATIONRESPONSEDETAILS:
		// Log.d("forms", "Came to GETCONFIGURATIONRESPONSEDETAILS");
		//
		// request = new SoapObject(mNamespace,
		// "GetConfigurationResponDetails");
		// request.addProperty("configurationXml",
		// servicebean.getObj());
		// System.out.println("buddyId:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetConfigurationResponDetails" + quotes,
		// envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// ArrayList<Object> response_list = parser
		// .parseGetConfigurationResponDetails(sp
		// .toString());
		// servicebean.setObj(response_list);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case DELETEMYRESPONSEDETAILS:
		// Log.d("forms", "Came to GETCONFIGURATIONRESPONSEDETAILS");
		// request = new SoapObject(mNamespace,
		// "DeleteMyResponseDetail");
		// request.addProperty("responseXml", servicebean.getObj());
		// System.out.println("buddyId:" + request);
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteMyResponseDetail" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES", "Service response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] response = parser.parseDeletemyResponse(sp
		// .toString());
		// servicebean.setObj(response);
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case SETPERMISSION:
		// Log.d("forms", "Came to SETPERMISSION");
		// Log.d("forms", "Composed xml--->" + servicebean.getObj());
		// request = new SoapObject(mNamespace, "SetPermission");
		// request.addProperty("permissionXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetPermission" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("NOTES",
		// "set permission  response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] response = parser.parseSetPermissionXML(sp
		// .toString());
		// servicebean.setObj(response);
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case GETALLPERMISSION:
		// Log.d("permissions", "Came to GETALLPERMISSION");
		// request = new SoapObject(mNamespace, "GetAllPermission");
		// Log.d("permissions",
		// "Composed xml--->" + servicebean.getObj());
		// request.addProperty("permissionXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetAllPermission" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("permissions",
		// "get all permission  response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		//
		// ArrayList<PermissionBean> permission_list = parser
		// .parseGetallPermissionResult(sp.toString());
		// servicebean.setObj(permission_list);
		//
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		// case GETPROFILETEMPLATE:
		// Log.d("permissions", "Came to GETPROFILETEMPLATE");
		// request = new SoapObject(mNamespace,
		// "GetStandardProfileTemplate");
		// Log.d("profile", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("profileTemplateModifiedTime",
		// servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetStandardProfileTemplate" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("profiletemplate", "get profiletemplate  response-->"
		// + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "profiletemplate if ");
		//
		// ArrayList<Object> getProfileTemplateList = parser
		// .parseGetProfileTemplateResponse(sp.toString());
		// servicebean.setObj(getProfileTemplateList);
		//
		// } else {
		// Log.d("zxz", "profiletemplate else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		// case SETSTANDARDPROFILEFIELDVALUES:
		// Log.d("permissions",
		// "Came to SETSTANDARDPROFILEFIELDVALUES");
		// request = new SoapObject(mNamespace,
		// "SetStandardProfileFieldValues");
		// Log.d("profile", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("profileXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetStandardProfileFieldValues" + quotes,
		// envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("setprofilefieldvalues",
		// "get profiletemplate  response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "setprofilefieldvalues if ");
		//
		// ArrayList<Object> profileResponseList = parser
		// .parseSetProfileResponse(sp.toString());
		// servicebean.setObj(profileResponseList);
		//
		// } else {
		// Log.d("zxz", "setprofilefieldvalues else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		//
		// break;
		// case GETPROFILEFIELDVALUES:
		// Log.d("permissions", "Came to GETPROFILEFIELDVALUES");
		// request = new SoapObject(mNamespace,
		// "GetStandardProfileFieldValues");
		// Log.d("profile", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("profileXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetStandardProfileFieldValues" + quotes,
		// envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("setprofilefieldvalues",
		// "GETPROFILEFIELDVALUES  response-->"
		// + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "setprofilefieldvalues if ");
		// ArrayList<Object> profileResponseList = parser
		// .parseGetStandaradfieldValues(sp.toString());
		// servicebean.setObj(profileResponseList);
		//
		// } else {
		// Log.d("zxz", "GETPROFILEFIELDVALUES else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case DELETEPROFILE:
		//
		// Log.d("forms", "Came to addforms");
		//
		// request = new SoapObject(mNamespace, "DeleteProfile");
		// request.addProperty("ProfileXml", servicebean.getObj());
		// System.out.println("Reuest:" + request);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		//
		// androidHttpTransport.call(quotes + mNamespace
		// + "DeleteProfile" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "Call configuration if ");
		// String[] result = parser.parseDeleteProfile(sp
		// .toString());
		// servicebean.setObj(result);
		// } else {
		// Log.d("zxz", "Call on login else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case GETSETUTILITY:
		// Log.d("permissions", "Came to GETSETUTILITY");
		// request = new SoapObject(mNamespace,
		// "SetAndGetUtilityItems");
		// Log.d("utility", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("utilityXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "SetAndGetUtilityItems" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("utility",
		// "GETSETUTILITY  response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "GETSETUTILITY if ");
		// UtilityResponse response = parser
		// .parseUtilityresponse(sp.toString());
		// servicebean.setObj(response);
		//
		// } else {
		// Log.d("zxz", "GETSETUTILITY else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		//
		// case BLOCKUNBLOCKBUDDY:
		// request = new SoapObject(mNamespace, "BlockUnBlockBuddy");
		// Log.d("block", "" + (String) servicebean.getObj());
		// request.addProperty("userId", servicebean.getOption()[0]);
		// Log.d("block", "" + servicebean.getOption()[0].toString());
		// request.addProperty("buddyId", servicebean.getOption()[1]);
		// Log.d("block", "" + servicebean.getOption()[1].toString());
		// request.addProperty("key", servicebean.getOption()[2]);
		// Log.d("block", "" + servicebean.getOption()[2].toString());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// System.out.println("?" + mNamespace + "BlockUnBlockBuddy");
		// androidHttpTransport.call(quotes + mNamespace
		// + "BlockUnBlockBuddy" + quotes, envelope);
		//
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("utility", "My Result---->" + sp.toString());
		// String[] block_result = parser.parseBlockUnblockresult(sp
		// .toString());
		// Log.d("block", "change requestuuuuuuuu  " + sp.toString());
		// Log.e("chk", webServiceBean.getText());
		// servicebean.setObj(block_result);
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case GETBLOCKBUDDYLIST:
		// Log.d("permissions", "Came to GetBlockedBuddyList");
		// request = new SoapObject(mNamespace, "GetBlockedBuddyList");
		// Log.d("profile", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("userId", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "GetBlockedBuddyList" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("GetBlockedBuddyList",
		// "get GetBlockedBuddyList  response-->"
		// + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "GetBlockedBuddyList if ");
		//
		// ArrayList<Object> getBlockedBuddyList = parser
		// .parseGetBlockBuddyList(sp.toString());
		// servicebean.setObj(getBlockedBuddyList);
		//
		// } else {
		// Log.d("zxz", "profiletemplate else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case SYNCUTILITYITEMS:
		// Log.d("permissions", "Came to GetSyncUtilities");
		// request = new SoapObject(mNamespace, "SyncUtilityItems");
		// Log.d("utility", "Composed xml--->" + servicebean.getObj());
		// request.addProperty("utilityXml", servicebean.getObj());
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "SyncUtilityItems" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("GetSyncUtilities",
		// "get GetSyncUtilities  response-->" + sp.toString());
		// if (parser.getResult(sp.toString())) {
		// Log.d("zxz", "GetSyncUtilities if ");
		//
		// Syncutilitybean bean = parser.parseGetUtilityItems(sp
		// .toString());
		// servicebean.setObj(bean);
		//
		// } else {
		// Log.d("zxz", "profiletemplate else");
		// webServiceBean = parser.parseResultFromXml(sp
		// .toString());
		// servicebean.setObj(webServiceBean);
		// Log.d("zxz", "Call on login else");
		//
		// }
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// case SHOWHIDEMYLOCATION:
		// Log.d("permissions", "Came to ShowOrHideMyDistance");
		// request = new SoapObject(mNamespace, "ShowOrHideMyDistance");
		// String[] input = (String[]) servicebean.getObj();
		// request.addProperty("userId", input[0]);
		// request.addProperty("key", input[1]);
		// request.addProperty("latitude", input[2]);
		// request.addProperty("longitude", input[3]);
		// request.addProperty("publicIp", input[4]);
		//
		// envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		// envelope.setOutputSoapObject(request);
		// androidHttpTransport.call(quotes + mNamespace
		// + "ShowOrHideMyDistance" + quotes, envelope);
		// sp = (SoapPrimitive) envelope.getResponse();
		// Log.d("ShowOrHideMyDistance",
		// " ShowOrHideMyDistance  response-->"
		// + sp.toString());
		// String[] result = parser.parseshowhidelocResult(sp
		// .toString());
		// servicebean.setObj(result);
		// Log.d("ser", "...." + servicebean.getServiceMethods());
		// webServiceCallback.notifyWebServiceResponse(servicebean);
		// break;
		// default:
		// break;
		// }
		// } catch (Exception e) {
		// Log.d("zxz", "Exception response " + e.getMessage());
		//
		// String errorMsg = "Unable To Connect Server.Please try again Later";
		// String exception = e.getMessage();
		// // System.out.println("Exception :"+exception);
		// // System.out.println("Exception :"+e.printStackTrace());
		// e.printStackTrace();
		//
		// if (exception != null) {
		// if (exception
		// .contains("expected: END_TAG {http://schemas.xmlsoap.org/soap/envelope/}"))
		// {
		// errorMsg = "Server Unavailable";
		// } else if (exception.contains("The operation timed out")) {
		// errorMsg = exception;
		// } else if (exception.contains("Connection refused")) {
		// errorMsg = "Connection refused";
		// } else if (exception.contains("No route to host")) {
		// errorMsg = "No route to host";
		// } else if (exception.contains("Malformed ipv4 address")) {
		// errorMsg = "Malformed ip address";
		// } else if (exception.contains("Network is unreachable")) {
		// errorMsg = "Network unreachable";
		// }
		// }
		//
		// webServiceCallback.notifyError(errorMsg);
		//
		// e.printStackTrace();
		// }
		//
		// }

		Log.d("lgg", "###############out of thread...");

	}

}
