package com.butch.notesrotte;

import java.io.FileOutputStream;
import java.io.IOException;


public class MessagesStorage
{
	public MessagesStorage(FileOutputStream fileOutputStream)
	{	String wiad = "212222222222222222222222222222";
		
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
	}
}
