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
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

/**
 *
 * @author Vishal
 */
public class Register {
    public String register(HashMap hm,String username,String password,String email,String mobile_no,String account_type,String refer_code)
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
           
Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("SELECT * FROM  credentials WHERE username='"+username+"'");
int flag=0;

while(rs.next()){
        flag++;
        }

if(flag==0)
{
    String encryptPass=getBase64(password);
    boolean b=matching(encryptPass, password);
    //System.out.println(b);
    if(b=true)
    {
      PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("INSERT INTO credentials( username, password, email_id, account_type,refer_code,mobile_no) VALUES (?,?,?,?,?,?)");
      preparedStmt.setString (1, username);
      preparedStmt.setString (2, encryptPass);
      preparedStmt.setString(3, email);
      preparedStmt.setString(4, account_type);
      preparedStmt.setString(5, refer_code);
           preparedStmt.setString(6,mobile_no);
      preparedStmt.execute();  
       Referral rf=new Referral();
      String code=rf.generate_referral(username);
      rf.insertrefer(db, username, code);
      con.close();
   session=login(db, username);
   json.put("result","true");
        json.put("message","Registration Successful");
        json.put("username", username);
        json.put("session",session);
        json.put("refer_code",code);
   
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
            System.out.println(e);
        }
       // System.out.println(dbpass+ dbusername+databaseurl);
    return json.toString();
      
    }
    public static String login(HashMap db,String username ) throws SQLException
    {
        
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
         System.out.println(e);
      }
      finally{
          con.close();
      }
      return session;
    }
    public static String getBase64(String input) 
    { 
String encoded = DatatypeConverter.printBase64Binary(input.getBytes());

return  encoded;

  
          
    }
    public static boolean matching(String orig, String compare){
    
    try{
        
        
      String decoded = new String(DatatypeConverter.parseBase64Binary(orig));
        return decoded.equals(compare);

    } catch (Exception e) {
        System.out.println(e);
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

        
    return s;
    }
}
