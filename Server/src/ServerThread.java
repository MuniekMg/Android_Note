import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class ServerThread extends Thread 
{
	private Server server;
	private Socket socket;
	DataInputStream din;
	//DataOutputStream dout;
	BufferedWriter dout;
	String myNumber = "48783685337";
	String key;
	String content;
	String[] subStrings = new String[2];
	
	public ServerThread(Server server, Socket socket) 
	{
		this.server = server;
		this.socket = socket;
		start();
	}
	


	public void run() 
	{

		try 
		{
			din = new DataInputStream(socket.getInputStream());
		} 
		catch (IOException e) 
		{
			System.out.println("IO Problem with DataInputStream: " + e);
		}
		try 
		{
			//dout = new DataOutputStream(socket.getOutputStream());
			dout =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			Thread senderThread = new Thread(new SenderThread(dout));
			senderThread.start();
		} 
		catch (IOException e) 
		{
			System.out.println("IO Problem with DataOutputStream: " + e);
		}
		
			while(true)
			{
					try 
					{
						if(din.available() > 0)
						{
							System.out.println("jestem! odbieram!");
							String message="";
							try 
							{
								System.out.println("odbieram!");
								message = din.readUTF();
								System.out.println(message);
							} 
							catch (IOException e) 
							{
								System.out.println("IO problem with readUTF: " + e);
							}
							
							split(message); //splitting "numberOfRecipient/message"
							
							Server.PutToHashtable(key, content);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
					}
			} 
	}

	private class SenderThread extends Thread
	{
		BufferedWriter dout;
		SenderThread(BufferedWriter d_out)
		{
			dout = d_out;
		}
		
		
		
		public void run()
		{
			/*for(int i=0; i<1; i++)
			{
				Server.GetHashtable().put("48783685337", "j:"+i);
			}*/
			while(true)
			{
				if(Server.GetHashtable().containsKey(myNumber))
				{
				String message = (String) Server.GetHashtable().get(myNumber);

				/*try 
				{
					System.out.println("wysyłam!");
					dout.writeUTF(message);
					System.out.println("wyslałem!");
				} 
				catch (IOException ie) 
				{
					System.out.println("IO problem with writeUTF: " + ie);
				}*/
				try {
					dout.write(message);
					System.out.println("wysyłam!!!!!!!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Server.GetHashtable().remove(myNumber);
			}
		}
		}
	}
	
	public void split (String message)
	{
		subStrings = message.split("/");
		key = subStrings[0];
		content = subStrings[1];
	}
}