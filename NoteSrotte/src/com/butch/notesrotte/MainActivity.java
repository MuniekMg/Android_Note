package com.butch.notesrotte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

	public class MainActivity extends ActionBarActivity 
	{
		TextView text;
		EditText editNumber;
		EditText editContent;
		static String message = "";
		static boolean wyslijClicked;
		String content;
		String number;
		String myNumber = "inne";
		Thread listenerThread;
		FileOutputStream fileOutputStream;
		
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			editNumber = (EditText)findViewById(R.id.editNumber);
			editContent = (EditText)findViewById(R.id.editText2);
			text = (TextView)findViewById(R.id.textView1);
			wyslijClicked = false;
			try 
			{
				fileOutputStream = openFileOutput("messages",MODE_PRIVATE);
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("ERROR: Probem with FileOutputSream");
			}
			String wiad = "212222222222222222222222222222";
			
			try {
				System.out.println("22222222222222222222");
				fileOutputStream.write(wiad.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				System.out.println("333333333333333333333333333333");
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listenerThread = new Thread(new ListenerThread());
			getSelfNumber();
		}
		
		public void wyslij(View view)
		{	
			content = editContent.getText().toString();
			number = editNumber.getText().toString();
			wyslijClicked = true;
		}

		private class ListenerThread extends Thread
		{
			   Connection connection;

			   public void run() 
			   {   
				   connection = new Connection("192.168.100.104", 8080);
				   connection.connect();
				   Thread senderThread = new Thread(new SenderThread(connection));
				   senderThread.start();

					while(true)
					{
						try 
						{
							sleep(10);
						} 
						catch (InterruptedException e1) 
						{
							System.out.println("ERROR: istener's sleep interrupted" + e1);
						}
						try 
						{
							if(connection.din.available() > 0)
							{
								MainActivity.message += connection.receive() + " ";
								
								MainActivity.this.runOnUiThread(new Runnable()
								{
									public void run()
									{
										Intent updateWidget = new Intent(getBaseContext(), Widget.class);
										updateWidget.setAction("update_widget");
										PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
										try 
										{
											pending.send();
										} 
										catch (CanceledException e) 
										{
											System.out.println("ERROR: update widget" + e);
										}					
									}
								});
							}
						}
						catch (IOException e) 
						{
							System.out.println("ERROR: DataInputStream.available" + e);
						}
					}
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
				connection.send(myNumber, "hello! i'm online!");
				while(true)
				{
					try 
					{
						sleep(10);
					} 
					catch (InterruptedException e) 
					{
						System.out.println("ERROR: sender's sleep interrupted" + e);
					}
					if(MainActivity.wyslijClicked == true)
					{
						MainActivity.wyslijClicked = false;
						connection.send(number, content);
					}
				}
			}
		}

		public void showDialog()
		{
		   final Dialog gettingNumberDialog = new Dialog(MainActivity.this);
		   gettingNumberDialog.setContentView(R.layout.dialog_layout);
		   gettingNumberDialog.setTitle("Numer u¿ytkownika");
		   final EditText editNumberDialog = (EditText) gettingNumberDialog.findViewById(R.id.editNumberDialog);
		   Button okButton = (Button) gettingNumberDialog.findViewById(R.id.okButton);
			   
		   okButton.setOnClickListener(new OnClickListener()
		   {
				public void onClick(View v) 
				{
					myNumber = editNumberDialog.getText().toString();
					gettingNumberDialog.dismiss();
					listenerThread.start();
				}
		   });
			   
		   gettingNumberDialog.show();
		}
		
		public void getSelfNumber()
		{
			   TelephonyManager telephoneManager = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
			   myNumber = telephoneManager.getLine1Number();
			   if(myNumber.length() < 9)
			   {
				   showDialog();
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
				  
				  editNumber.setText(number);
				  break;
			}
		}
	}