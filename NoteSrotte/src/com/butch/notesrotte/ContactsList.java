package com.butch.notesrotte;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactsList 
{
	public static String getName(int resultCode, Intent data, ContentResolver Cr)
	{
		String name=null;
	      if(resultCode == Activity.RESULT_OK) 
	      {
	        Uri contactData = data.getData();
	        Cursor cursor =  Cr.query(contactData, null, null, null, null);
	        
	        if(cursor.moveToFirst()) 
	        {
	          name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        }
	      cursor.close();
	      }
		return name;
	}
	
	public static String getNumber(String name, ContentResolver Cr)
	{
		String number=null;
        Cursor cursor = Cr.query(ContactsContract.Contacts.CONTENT_URI, null,"DISPLAY_NAME = '" + name + "'", null, null);
        if (cursor.moveToFirst())
        {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = Cr.query(Phone.CONTENT_URI, null,Phone.CONTACT_ID + " = " + contactId, null, null);
           
            while (phones.moveToNext()) 
            {
                number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
            }
            phones.close();
        }
        cursor.close();
		return number;
	}
	
}
