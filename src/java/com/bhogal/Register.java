/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author Vishal
 */
public class Register {
    public String register(HashMap hm,String username,String password,String email,String account_type)
    {
         //System.out.println("Paramaeter"+username+password+email+account_type);
        HashMap db=hm;
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
      java.sql.Connection con=null;
      String session="";
       JSONObject json = new JSONObject();
        try
        {
           // System.out.println("Andar aya");
Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("SELECT * FROM  credentials WHERE username='"+username+"'");
int flag=0;
//System.out.println("Printing flag "+rs.wasNull());
while(rs.next()){
        flag++;
        }

if(flag==0)
{
    String encryptPass=getMd5(password);
    boolean b=matching(encryptPass, password);
    System.out.println(b);
    if(b=true)
    {
        PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("INSERT INTO credentials( username, password, email_id, account_type) VALUES (?,?,?,?)");
      preparedStmt.setString (1, username);
      preparedStmt.setString (2, encryptPass);
      preparedStmt.setString(3, email);
      preparedStmt.setString(4, account_type);
      preparedStmt.execute();   
      con.close();
   session=login(db, username);
   json.put("result","true");
        json.put("message","Registration Successful");
        json.put("username", username);
        json.put("session",session);
   
    }
    
}else
    {
    json.put("result","false");
        json.put("message","Already Registered");    
        //System.out.println(generateSession());
    }
        }
        catch(Exception e)
        {
           // System.out.println(e.getMessage());
        }
       // System.out.println(dbpass+ dbusername+databaseurl);
    return json.toString();
      
    }
    public static String login(HashMap db,String username ) throws SQLException
    {
        //System.out.println("hhhhhh");
        String session=generateSession();
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
      java.sql.Connection con=null;
      try
      {
          Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("SELECT * FROM  login WHERE username='"+username+"'");
int flag=0;        
while(rs.next()){
        flag++;
        }
         // System.out.println("Flag"+flag);
if(flag==0)
{
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("INSERT INTO login( username, sessionid) VALUES (?,?)");
       preparedStmt.setString (1, username);
      preparedStmt.setString (2, session);
      preparedStmt.execute(); 
      return session;
}
else
{
session="";
     
}
      }
      catch(Exception e)
      {
         // System.out.println(e.getMessage());
      }
      finally{
          con.close();
      }
      return session;
    }
    public static String getMd5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    }
    public static boolean matching(String orig, String compare){
    String md5 = null;
    try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(compare.getBytes());
        byte[] digest = md.digest();
        md5 = new BigInteger(1, digest).toString(16);

        return md5.equals(orig);

    } catch (NoSuchAlgorithmException e) {
        return false;
    }

   
}
    public static  String generateSession()
    {
         String s = "";
        double d;
        for (int i = 1; i <= 16; i++) {
            d = Math.random() * 10;
            s = s + ((int)d);
            if (i % 4 == 0 && i != 16) {
                s = s + "";
            }
        }

        //System.out.println(s);
    return s;
    }
}
