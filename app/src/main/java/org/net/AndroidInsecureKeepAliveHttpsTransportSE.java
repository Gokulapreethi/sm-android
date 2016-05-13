package org.net;

import java.io.IOException;

import org.ksoap2.transport.HttpsTransportSE;
import org.ksoap2.transport.ServiceConnection;


/**
 * 
 * This class  deals with the problems with the Android ssl libraries having trouble with certificates and
 * certificate authorities somehow messing up connecting/needing reconnects. Added as generic class for SE since it
 * might be useful in SE environments as well and can be used as an example to create your own transport
 * implementations.
 *
 *
 */
public class AndroidInsecureKeepAliveHttpsTransportSE extends HttpsTransportSE {

    private AndroidInsecureHttpsServiceConnectionSE conn = null;
    private final String host;
    private final int port;
    private final String file;
    private final int timeout;

    
    /**
	 * Overloaded class constructor .Used to initialize the  connection parameters
	 * @param host Remote host or domain
	 * @param port Port on which the host is listening
	 * @param file relative path on the server excluding host and port 
	 * @param timeout time out
	 * @throws IOException
	 */
    public AndroidInsecureKeepAliveHttpsTransportSE(String host, int port, String file, int timeout) {
        super(host, port, file, timeout);
        this.host = host;
        this.port = port;
        this.file = file;
        this.timeout = timeout;
    }

    /**
     * Get a service connection. 
     * @return Returns service connectiob ,that
     * ignores "Connection: close" request property setting and has "Connection: keep-alive" always set and is uses
     * a https connection.
     * 
     */
    protected ServiceConnection getServiceConnection() throws IOException {
        conn = new AndroidInsecureHttpsServiceConnectionSE(host, port, file, timeout);
        conn.setRequestProperty("Connection", "keep-alive");
        return conn;
	}
}