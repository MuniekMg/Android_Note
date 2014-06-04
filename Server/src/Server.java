import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server 
{
	private ServerSocket ss;
	private static Hashtable outputStreams = new Hashtable();

	public Server(int port) throws IOException 
	{
		listen(port);
	}

	private void listen(int port) throws IOException 
	{
		ss = new ServerSocket(port);
		System.out.println("Listening on " + ss);
		while (true) 
		{
			Socket s = ss.accept();
			System.out.println("Connection from " + s);
			new ServerThread(this, s);
		}
	}

	static Enumeration getOutputStreams() 
	{
		return outputStreams.elements();
	}
	
	static void PutToHashtable(String key, String message)
	{
		outputStreams.put(key, message);
	}
	
	static Hashtable GetHashtable()
	{
		return outputStreams;
	}

	// Send a message to all clients (utility routine)
	void sendToAll(String message) 
	{
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		synchronized (outputStreams) 
		{
			// For each client ...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) 
			{
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				// ... and send the message
				try {
					dout.writeUTF(message);
				} 
				catch (IOException ie) 
				{
					System.out.println(ie);
				}
			}
		}
	}

	// Remove a socket, and it's corresponding output stream, from our
	// list. This is usually called by a connection thread that has
	// discovered that the connectin to the client is dead.
	void removeConnection(Socket s) 
	{
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streamsa
		synchronized (outputStreams) 
		{
			// Tell the world
			System.out.println("Removing connection to " + s);
			// Remove it from our hashtable/list
			outputStreams.remove(s);
			// Make sure it's closed
			try 
			{
				s.close();
			} 
			catch (IOException ie) 
			{
				System.out.println("Error closing " + s);
				ie.printStackTrace();
			}
		}
	}

	static public void main(String args[]) throws Exception 
	{
		int port = 8080;
		new Server(port);
	}
}
