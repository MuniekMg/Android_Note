package com.butch.notesrotte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase 
{
    private static final String url = "jdbc:mysql://mysql3.webio.pl:3306/8306_traffii1";
    private static final String user = "8306_traffii";
    private static final String pass = "terra2002!";
    private final static String DBDRIVER = "com.mysql.jdbc.Driver";
    Statement st;
    Connection con;
    String moje_id;
    String znajomi_id="";
    
	public void connect()
	{	
		try
		{
			Class.forName(DBDRIVER).newInstance();
		    System.out.print("OK!");
		    con = DriverManager.getConnection(url, user, pass);
		    System.out.print("SUCCESS!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void friend(String numer)
	{
		try
		{
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	
			///Moje id///
		    String query = " select pk_users from users where number = " + numer;
		    ResultSet rs = st.executeQuery(query);
	    	moje_id = Integer.toString(rs.getInt(1));
	    	
	    	///id znajomych///
		    String query2 = " select fk_friend from relations where fk_user = " + moje_id;
		    ResultSet rs2 = st.executeQuery(query2);
		    while(rs2.next())
		    {
		    	znajomi_id += Integer.toString(rs2.getInt(1));
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
