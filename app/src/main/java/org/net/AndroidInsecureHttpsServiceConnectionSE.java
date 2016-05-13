package org.net;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.ksoap2.transport.ServiceConnection;

import android.os.Build;

/**
 * 
 * This class provides secure  communication across internet
 *
 */
public class AndroidInsecureHttpsServiceConnectionSE implements ServiceConnection
{
	private HttpsURLConnection connection;

	/**
	 * Overloaded class constructor .Used to initialize the  connection parameters
	 * @param host Remote host or domain
	 * @param port Port on which the host is listening
	 * @param file relative path on the server excluding host and port 
	 * @param timeout time out
	 * @throws IOException
	 */
	public AndroidInsecureHttpsServiceConnectionSE(String host, int port, String file, int timeout) throws IOException {

		allowAllSSL();
		connection = (HttpsURLConnection) new URL("https", host, port, file).openConnection();
		updateConnectionParameters(timeout);
	}

	/**
	 * The marker interface for JSSE trust managers. The purpose is to group trust managers. The responsibility a trust manager is to handle the trust data used to make trust decisions for deciding whether credentials of a peer should be accepted,
	 */
	private static TrustManager[] trustManagers;

	/**
	 * The trust manager for X509 certificates to be used to perform authentication for secure 
	 *
	 */

	public static class FakeX509TrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		/**
		 * Given the partial or complete certificate chain provided by the peer, build a certificate path to a trusted root and 
		 * return if it can be validated and is trusted for server SSL authentication based on the authentication type. 
		 * The authentication type is the key exchange algorithm portion of the cipher suites represented as a String, 
		 * such as "RSA", "DHE_DSS". Note: for some exportable cipher suites, the key exchange algorithm is determined at run time during the handshake. For instance, for TLS_RSA_EXPORT_WITH_RC4_40_MD5, the authType should be RSA_EXPORT when an ephemeral RSA key is used for the key exchange, and RSA when the key from the server certificate is used. Checking is case-sensitive.
		 * @param arg0 the peer certificate chain
		 * @param arg1 the key exchange algorithm used
		 * 
		 */
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}
		

		/**
		 * Given the partial or complete certificate chain provided by the peer, build a certificate path to a trusted root.
		 * @param chain certificate chain provided by the peer
		 * @return true if it can be validated and is trusted for client SSL authentication.
		 */
		public boolean isClientTrusted(X509Certificate[] chain) {
			return (true);
		}

		/**
		 * Given the partial or complete certificate chain provided by the peer, build a certificate path to a trusted root
		 * @param chain complete certificate chain provided by the peer
		 * @return   true if it can be validated and is trusted for server SSL authentication.
		 */
		public boolean isServerTrusted(X509Certificate[] chain) {
			return (true);
		}

		/**
		 * Get's certificate issuer authorities
		 * @return Returns the list of certificate issuer authorities which are trusted for authentication of peers.
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return (_AcceptedIssuers);
		}
	}





	
	

	/**
	 * Allow all SSL certificates by setting up a host name verifier that passes everything and as well setting up a
	 * SocketFactory with the #FakeX509TrustManager.
	 */
	public static void allowAllSSL() {

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		SSLContext context = null;

		if (trustManagers == null) {
			trustManagers = new TrustManager[]{new FakeX509TrustManager()};
		}

		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {

			//	Log.e("allowAllSSL",""+ e.toString());
		} catch (KeyManagementException e) {
			//Log.e("allowAllSSL", ""+e.toString());
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	/**
	 * update the connection with the timeout parameter as well as allowing SSL if the Android version is 7 or lower
	 * (since these versions have a broken certificate manager, which throws a SSL exception saying "Not trusted
	 * security certificate"
	 *
	 * @param timeout timeout
	 */
	private void updateConnectionParameters(int timeout) {
		connection.setConnectTimeout(timeout); // 20 seconds
		connection.setReadTimeout(timeout); // even if we connect fine we want to time out if we cant read anything..
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setDoInput(true);

		int buildVersion = Build.VERSION.SDK_INT;
		if (buildVersion <= 7) {
		//	System.out.println("Detected old operating system version " + buildVersion + " with SSL certificate problems. Allowing " +
		//	"all certificates.");
			allowAllSSL();
		} else {
		//	System.out.println("Full SSL active on new operating system version " + buildVersion);
			//Log.d("Full SSL active on new operating system version " + buildVersion);
		}
	}

	/**
	 * Opens a connection to the resource. This method will not reconnect to a resource after the initial connection has been closed.
	 */
	public void connect() throws IOException {
		connection.connect();
	}

	/**
	 * Closes the connection to the HTTP server.
	 */
	public void disconnect() {
		connection.disconnect();
	}

	/**
	 * list header properties.
	 * @return list of HeaderProperty  pojo contains  http header properties.
	 */
	public List getResponseProperties() {
		Map properties = connection.getHeaderFields();
		Set keys = properties.keySet();
		List retList = new LinkedList();

		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			List values = (List) properties.get(key);

			for (int j = 0; j < values.size(); j++) {
				retList.add(new HeaderProperty(key, (String) values.get(j)));
			}
		}

		return retList;
	}

	/**
	 * Sets the value of the specified request header field. The value will only be used by the current URLConnection instance. This method can only be called before the connection is established
	 * @param key the request header field to be set.
	 * @param value the new value of the specified property. 
	 */
	public void setRequestProperty(String key, String value) {
		// We want to ignore any setting of "Connection: close" because
		// it is buggy with Android SSL.
		if ("Connection".equalsIgnoreCase(key) && "close".equalsIgnoreCase(value)) {
			// do nothing
		} else {
			connection.setRequestProperty(key, value);
		}
	}

	/**
	 * Sets the request command which will be sent to the remote HTTP server. This method can only be called before the connection is made.
	 * @param requestMethod the string representing the method to be used
	 */
	public void setRequestMethod(String requestMethod) throws IOException {
		connection.setRequestMethod(requestMethod);
	}

	/**
	 * Returns an OutputStream for writing data to this URLConnection. It throws an UnknownServiceException by default. This method must be overridden by its subclasses
	 * @return the OutputStream to write data.
	 */
	public OutputStream openOutputStream() throws IOException {
		return connection.getOutputStream();
	}

	
	/**
	 * Returns an InputStream for reading data from the resource pointed by this URLConnection. It throws an UnknownServiceException by default. This method must be overridden by its subclasses.
	 * @return the InputStream to read data from.
	 */
	public InputStream openInputStream() throws IOException {
		return connection.getInputStream();
	}

	
	
	/**
	 * Returns an input stream from the server in the case of an error such as the requested file has not been found on the remote server. This stream can be used to read the data the server will send back.
	 * @return the error input stream returned by the server. 
	 */
	public InputStream getErrorStream() {
		return connection.getErrorStream();
	}
	

	/**
	 * the port number of this URL. 
	 * @return the host name or IP address of this URL. 
	 */
	public String getHost() {
		return connection.getURL().getHost();
	}
	

	/**
	 * Gets the port number of this URL or -1 if the port is not set.
	 * @return the port number of this URL. 
	 */
	public int getPort() {
		return connection.getURL().getPort();
	}

	/**
	 * Gets the value of the path part of this URL. 
	 * @return the path part of this URL. 
	 */
	public String getPath() {
		return connection.getURL().getPath();
	}

}