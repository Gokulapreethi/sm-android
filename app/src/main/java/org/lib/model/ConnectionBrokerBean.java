package org.lib.model;
/**
 * This bean class is used to set and get data's Related to ConnectionBroker. This class contains information 
 * like cbserver1,cbserver2,router,relayServer.
 * 
 *
 */
public class ConnectionBrokerBean {

	private String cbserver1=null;
	private String cbserver2=null;
	private String router=null;
	private String relayServer=null;
	private String FS=null;

	private int aa1port;
	private int aa2port;
	
	
	public String getRelayServer() {
		return relayServer;
	}
	public void setRelayServer(String relayServer) {
		this.relayServer = relayServer;
	}
	public String getCbserver1() {
		return cbserver1;
	}
	public void setCbserver1(String cbserver1) {
		this.cbserver1 = cbserver1;
	}
	
	public String getFS() {
		return FS;
	}
	public void setFS(String freeswitch) {
		this.FS = freeswitch;
	}
	public String getCbserver2() {
		return cbserver2;
	}
	public void setCbserver2(String cbserver2) {
		this.cbserver2 = cbserver2;
	}
	public String getRouter() {
		return router;
	}
	public void setRouter(String router) {
		this.router = router;
	}
	public void setaa1Port(String prt)
	{
		this.aa1port=Integer.parseInt(prt);
	}
	public int getaa1Port()
	{
		return this.aa1port;
	}
	public void setaa2Port(String port2)
	{
		this.aa2port=Integer.parseInt(port2);
	}
	public int getaa2Port()
	{
		return this.aa2port;
	}
	
	
}
