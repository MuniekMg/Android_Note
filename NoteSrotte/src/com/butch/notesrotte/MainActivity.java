package com.butch.notesrotte;

import java.io.IOException;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

	public class MainActivity extends ActionBarActivity 
	{
		TextView text;
		EditText editNumer;
		EditText editContent;
		static String message = "";
		static boolean wyslijClicked;
		static String content = "";
		String number = "";
		
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			editNumer = (EditText)findViewById(R.id.editNumber);
			editContent = (EditText)findViewById(R.id.editText2);
			text = (TextView)findViewById(R.id.textView1);
			wyslijClicked = false;
			
			Thread listenerThread = new Thread(new ListenerThread());
			listenerThread.start();
		}
		
		public void wyslij(View view)
		{	
			//content = editContent.getText().toString();
			wyslijClicked = true;
		}

		private class ListenerThread extends Thread
		{
			   Connection connection;

			   public void run() 
			   {
				   connection = new Connection("192.168.43.75", 8080);
				   connection.connect();
					Thread senderThread = new Thread(new SenderThread(connection));
					senderThread.start();

					while(true)
					{
						/*if(MainActivity.wyslijClicked == true)
						{
							connection.send("48783685337", "text");
							MainActivity.wyslijClicked = false;
						}*/
						//try {
							//if(connection.din.available() > 0)
							//{
								System.out.println("jestem bo coœ mam do odebrania");
								MainActivity.message += connection.receive() + " ";
								MainActivity.this.runOnUiThread(new Runnable()
								{
									public void run()
									{
										editContent.setText(MainActivity.message);
										
										Intent updateWidget = new Intent(getBaseContext(), Widget.class);
										updateWidget.setAction("update_widget");
										PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
										try 
										{
											pending.send();
										} 
										catch (CanceledException e) 
										{
											e.printStackTrace();
										}
									}
								});
							//}
						//} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						//}
					}
					
					/*if(connection.getSocket().isConnected())
					{
						System.out.println("ERROR: socket is NOT connected! (Class: ClientThread)");
					}*/
			   }
		}
		
		private class SenderThread extends Thread
		{
			Connection connection;
			SenderThread(Connection conn)
			{
				connection = conn;
			}
			public void run()
			{
				while(true)
				{
					try 
					{
						sleep(10);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					if(MainActivity.wyslijClicked == true)
					{
						MainActivity.wyslijClicked = false;
						connection.send("48783685337", "text");
					}
				}
			}
		}

		
		
		
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		public void click_for_number(View view)
		{
			Intent ContactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(ContactsIntent, 1);
		}
		
		public void onActivityResult(int reqCode, int resultCode, Intent data) 
		{	
			switch(reqCode)
			{
				case 1:
				  super.onActivityResult(reqCode, resultCode, data);
				  ContentResolver Cr = getContentResolver();
	
				  String name = ContactsList.getName(resultCode, data, Cr);
				  number = ContactsList.getNumber(name, Cr);
				  
				  editNumer.setText(number);
				  break;
			}
		}
	}