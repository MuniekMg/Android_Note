package com.butch.notesrotte;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection 
{
	String Address;
	int Port;
	Socket socket = null;
	DataOutputStream dout;
	DataInputStream din;
	BufferedReader br;
	
	Connection(String adress, int port)
	{
		Address = adress;
		Port = port;
	}
	
	public void connect()
	{			
		try
		{
			socket = new Socket(Address, Port);
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("ERROR: Unknown problem with sockt connection: " + e);
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR: IO problem with socket connection" + e);
		}			
		if(socket.isConnected())
		{	
			try 
			{
				din = new DataInputStream(socket.getInputStream());
			} 
			catch (IOException e) 
			{
				System.out.println("ERROR: IO problem with DataInputStream" + e);
			}
		}
		else
		{
			System.out.println("ERROR: socket is NOT connected! (method: connect)");
		}
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}
	
	public void disconnect()
	{
		if(socket != null)
		{
			try 
			{
				socket.close();
			}
			catch (IOException e) 
			{
				System.out.println("ERROR: IO problem with closing of socket" + e);
			}
		}
	}
	
	public void send(String number, String message)
	{
		String fullMessage = number+"/"+message;

		if(socket.isConnected())
		{
			try 
			{
				dout = new DataOutputStream(socket.getOutputStream());
			} 
			catch (IOException e) 
			{
				System.out.println("ERROR: IO problem with DataOutputStream" + e);
			}
			
		    try 
		    {
				dout.writeUTF(fullMessage);
			} 
		    catch (IOException e) 
		    {
		    	System.out.println("ERROR: IO problem with 	writeUTF" + e);
			}
		    /*try 
		    {
				dout.close();
			} 
		    catch (IOException e) 
		    {
		    	System.out.println("ERROR: IO problem with closing of DataOutputStream" + e);
			}*/
		}
		else
		{
			System.out.println("ERROR: socket is NOT connected! (method: send)");
		}
	}
	
	public String receive()
	{	
		String message="";
		
		if(socket.isConnected())
		{
			try 
			{
				message = din.readUTF();
			} 
			catch (IOException e) 
			{
		    	System.out.println("ERROR: IO problem with readUTF" + e);
			}
			/*try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				message = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		else
		{
			System.out.println("ERROR: socket is NOT connected! (method: receive)");
		}
	    
		return message;
	}
}








/*String Address;
int Port;
Socket socket = null;

Connection(String adress, int port)
{
	Address=adress;
	Port=port;
}

public void connect()
{			
	try
	{
		socket = new Socket(Address, Port);
	} 
	catch (UnknownHostException e) 
	{
		e.printStackTrace();
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}			
}

public boolean isConnected()
{
	return socket.isConnected();
}

public void disconnect()
{
	if(socket != null)
	{
		try 
		{
			socket.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

public void sent(String number, String message)
{
	String fullMessage=number+"/"+message+"/"+"©";
	DataOutputStream outputStream=null;
	try 
	{
		outputStream = new DataOutputStream(socket.getOutputStream());
	} 
	catch (IOException e) 
	{
		disconnect();
		return;
	}

    try {
		outputStream.writeUTF(fullMessage);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    try {
		outputStream.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public String receive()
{
	connect();
	
	String message="";
	int bytesRead;
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
	byte[] buffer = new byte[1024];
	InputStream inputStream=null;
	
	try 
	{
		inputStream = socket.getInputStream();
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}

    try 
    {
		while((bytesRead = inputStream.read(buffer)) != -1)
		{
		    byteArrayOutputStream.write(buffer, 0, bytesRead);
		    message += byteArrayOutputStream.toString("UTF-8");
		}
	} 
    catch (UnsupportedEncodingException e) 
    {
		e.printStackTrace();
	} 
    catch (IOException e) 
    {
		e.printStackTrace();
	}
    finally
    {
    	disconnect();
    }
    
return message;
}*/