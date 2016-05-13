/**
 * 
 */
package com.ws;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.lib.model.Syncutilitybean;
import org.lib.model.WebServiceBean;
import org.lib.xml.XmlParser;
import org.net.AndroidInsecureKeepAliveHttpsTransportSE;

import android.util.Log;

import com.cg.snazmed.R;
import com.process.BGProcessor;
import com.util.Queue;
import com.util.SingleInstance;


public class BackgroundWSProcessor extends Thread {

	private Queue queue;
	private boolean running;

	private static BackgroundWSProcessor bwsProcessor;

	private String url, nameSpace, wsdlUrl, loginIP, serverServiceName;
	private int port;

	private AndroidInsecureKeepAliveHttpsTransportSE androidHttpsTransport = null;
	private SoapObject soapObject = null;
	private SoapSerializationEnvelope soapEnvelope = null;
	private SoapPrimitive soapPrimitive = null;
	private String quotes = "\"";

	private XmlParser xmlParser;

	// private ProgressDialog progressDialog = null;
	//
	// public void showprogress(ProgressDialog psdialog, Context context) {
	//
	// try {
	// progressDialog = psdialog;
	// if (progressDialog != null) {
	// progressDialog.setCancelable(true);
	// progressDialog.setMessage("Progress ...");
	// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// progressDialog.setProgress(0);
	// progressDialog.setMax(100);
	// progressDialog.show();
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static BackgroundWSProcessor getInstance() {
		if (bwsProcessor == null) {
			bwsProcessor = new BackgroundWSProcessor();
			bwsProcessor.setRunning(true);
			bwsProcessor.start();
		}
		return bwsProcessor;
	}

	public BackgroundWSProcessor() {

		queue = new Queue();
		xmlParser = new XmlParser();

		url = SingleInstance.mainContext.getResources().getString(
				R.string.service_url);

		nameSpace = "http://ltws.com/";

		wsdlUrl = url.trim() + "?wsdl";

		loginIP = url.substring(url.indexOf("://") + 3);

		loginIP = loginIP.substring(0, loginIP.indexOf(":"));

		loginIP = loginIP.trim();

		String urlPort = url.substring(url.indexOf("://") + 3);

		urlPort = urlPort.substring(urlPort.indexOf(":") + 1);

		urlPort = urlPort.substring(0, urlPort.indexOf("/"));

		if (urlPort != null)
			port = Integer.parseInt(urlPort);
		else
			port = 80;

		serverServiceName = wsdlUrl.substring(wsdlUrl.indexOf("://") + 3);

		serverServiceName = serverServiceName.substring(serverServiceName
				.indexOf(":") + 1);

		serverServiceName = serverServiceName.substring(
				serverServiceName.indexOf("/"), serverServiceName.indexOf("?"));

	}

	public Queue getQueue() {
		return queue;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (running) {

			try {

				WSBean wsBean = (WSBean) queue.getObject();
				if (wsBean != null) {

					try {

						androidHttpsTransport = new AndroidInsecureKeepAliveHttpsTransportSE(
								loginIP, port, serverServiceName, 30000);

						soapObject = new SoapObject(nameSpace, wsBean
								.getWsMethodName().trim());

						if (wsBean.getProperty_map() != null) {
							for (Entry<String, String> set : wsBean
									.getProperty_map().entrySet()) {
								soapObject.addProperty(set.getKey().trim(), set
										.getValue().trim());
							}
						}
						Log.d("BACKGROUNDWSPROCESSOR",
								"Requset For :" + wsBean.getWsMethodName());
						Log.d("BACKGROUNDWSPROCESSOR", "Server Requset  :"
								+ soapObject);

						soapEnvelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11);

						soapEnvelope.setOutputSoapObject(soapObject);

						androidHttpsTransport.call(
								quotes + nameSpace + wsBean.getWsMethodName()
										+ quotes, soapEnvelope);
						soapPrimitive = (SoapPrimitive) soapEnvelope
								.getResponse();

						switch (wsBean.getServiceMethods()) {

						case GETPROFILETEMPLATE:

							if (xmlParser.getResult(soapPrimitive.toString())) {
								ArrayList<Object> getProfileTemplateList = xmlParser
										.parseGetProfileTemplateResponse(soapPrimitive
												.toString());
								wsBean.setResultObject(getProfileTemplateList);
								Log.d("BACKGROUNDWSPROCESSOR",
										"GETPROFILETEMPLATE-----------if"
												+ soapPrimitive.toString());

							} else {
								WebServiceBean webServiceBean = xmlParser
										.parseResultFromXml(soapPrimitive
												.toString());
								wsBean.setResultObject(webServiceBean);
								Log.d("BACKGROUNDWSPROCESSOR",
										"GETPROFILETEMPLATE------------else"
												+ soapPrimitive.toString());
							}

							if (wsBean.getCallBack() != null) {
								Log.i("BackGround",
										"wsBean.getCallBack() != null"
												+ wsBean.getCallBack());

								((BGProcessor) wsBean.getCallBack())
										.notifyProfileTemplate(wsBean);
							} else if (wsBean.getCallBack() == null) {
								Log.i("BackGround",
										"wsBean.getCallBack() == null"
												+ wsBean.getCallBack());

							}
							break;

						case GETPROFILEFIELDVALUES:
							if (xmlParser.getResult(soapPrimitive.toString())) {

								Log.i("BackGround",
										"GETPROFILEFIELDVALUES ------------if");

								ArrayList<Object> profileResponseList = xmlParser
										.parseGetStandaradfieldValues(soapPrimitive
												.toString());
								wsBean.setResultObject(profileResponseList);

							} else {

								Log.i("BackGround",
										"GETPROFILEFIELDVALUES ------------else");

								WebServiceBean webServiceBean = xmlParser
										.parseResultFromXml(soapPrimitive
												.toString());
								wsBean.setResultObject(webServiceBean);
							}
							// mService_callback.notifyWebServiceResponse(wsBean);
							if (wsBean.getCallBack() != null) {
								Log.i("BackGround",
										"GETPROFILEFIELDVALUES : wsBean.getCallBack() != null");
								if (wsBean.getCallBack() instanceof BGProcessor) {
									Log.i("BackGround",
											"wsBean.getCallBack() instanceof BGProcessor");

									((BGProcessor) wsBean.getCallBack())
											.notifyProfileValues(wsBean);
								}
							}
							break;
						case SYNCUTILITYITEMS:

							if (xmlParser.getResult(soapPrimitive.toString())) {
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: enter");

								Syncutilitybean bean = xmlParser
										.parseGetUtilityItems(soapPrimitive
												.toString());
								wsBean.setResultObject(bean);
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: end");
							} else {
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: enter else");
								WebServiceBean webServiceBean = xmlParser
										.parseResultFromXml(soapPrimitive
												.toString());
								wsBean.setResultObject(webServiceBean);
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: end else part");
							}
							if (wsBean.getCallBack() != null) {
								// mService_callback.notifyWebServiceResponse(wsBean);
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: enter response");
								((BGProcessor) wsBean.getCallBack())
										.notifyUtilityResponse(wsBean);
								Log.d("BACKGROUNDWSPROCESSOR",
										"SYNCUTILITYITEMS: send response");
							}
							break;
						case GETBLOCKBUDDYLIST:

							if (xmlParser.getResult(soapPrimitive.toString())) {
								String[] blockUnblock = xmlParser
										.parseBlockUnblockresult(soapPrimitive
												.toString());
								wsBean.setResultObject(blockUnblock);
							}
							((BGProcessor) wsBean.getCallBack())
									.notifyGETBLOCKBUDDYLIST(wsBean);
							break;

						case GETMYCONFIGURATIONDETAILS:

							if (xmlParser.getResult(soapPrimitive.toString())) {
								ArrayList<Object> result1 = xmlParser
										.parseGetMyConfigurationDetails(soapPrimitive.toString());
		
								wsBean.setResultObject(result1);
							} else {
								WebServiceBean webServiceBean = xmlParser.parseResultFromXml(soapPrimitive
										.toString());
								wsBean.setResultObject(webServiceBean);
							}
	if (wsBean.getCallBack() != null) {
								Log.i("BackGround",
										"GETMYCONFIGURATIONDETAILS: wsBean.getCallBack() != null"
												+ wsBean.getCallBack());

								((BGProcessor) wsBean.getCallBack())
										.notifyGetMyConfigurationDetails(wsBean);
							}
//							mService_callback.notifyWebServiceResponse(wsBean);
						
//							if (wsBean.getCallBack() != null) {
//								((BGProcessor) wsBean.getCallBack())
//										.notifyGetMyConfigurationDetails(wsBean);
//							}
							break;
						default:
							Log.d("BACKGROUNDWSPROCESSOR",
									"Came to default case");
							break;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Log.i("BACKGROUNDWSPROCESSOR",
							"BackgroundWSProcessor going to Exist");
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.i("BACKGROUNDWSPROCESSOR", "BackgroundWSProcessor was stopped");
	}

}
