import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class ServerThread extends Thread 
{
	private Server server;
	private Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	String myNumber = "";
	String key;
	String content;
	String[] subStrings = new String[2];
	boolean unknownClientNumber = true;
	
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
			System.out.println("ERROR: IO Problem with DataInputStream: " + e);
		}
		try 
		{
			dout =new DataOutputStream(socket.getOutputStream());
			Thread senderThread = new Thread(new SenderThread(dout));
			senderThread.start();
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: IO Problem with DataOutputStream: " + e);
		}
		
			while(true)
			{
					try 
					{
						if(din.available() > 0)
						{
							String message="";
							try 
							{
								message = din.readUTF();
								System.out.println(message);
							} 
							catch (IOException e) 
							{
								System.out.println("ERROR: IO problem with readUTF: " + e);
							}
							
							split(message); //splitting "numberOfRecipient/message"
							
							if(unknownClientNumber)
							{
								myNumber = key;
								unknownClientNumber = false;
							}
							else
							{
								Server.PutToHashtable(key, content);
							}
						}
					} 
					catch (IOException e) 
					{
						System.out.println("ERROR: IO Problem DataInputStream.available: " + e);
					}
					
					try 
					{
						sleep(10);
					}
					catch (InterruptedException e)
					{
						System.out.println("ERROR: SenderThread's sleep interrupted" + e);
					}
			} 
	}

	private class SenderThread extends Thread
	{
		DataOutputStream dout;
		SenderThread(DataOutputStream dOut)
		{
			dout = dOut;
		}
		
		public void run()
		{
			while(true)
			{
				if(Server.GetHashtable().containsKey(myNumber))
				{
					String message = (String) Server.GetHashtable().get(myNumber);
	
					try 
					{
						System.out.println("wysyłam!");
						dout.writeUTF(message);
						System.out.println("wyslałem!");
					} 
					catch (IOException ie) 
					{
						System.out.println("ERROR: IO problem with writeUTF: " + ie);
					}
					
					Server.GetHashtable().remove(myNumber);
					
					try 
					{
						sleep(10);
					}
					catch (InterruptedException e)
					{
						System.out.println("ERROR: SenderThread's sleep interrupted" + e);
					}
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