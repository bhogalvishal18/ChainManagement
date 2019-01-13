/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import static com.bhogal.Register.generateSession;
import com.mysql.jdbc.PreparedStatement;
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
public class Login  {
     public String login(HashMap db,String username,String password,String account) throws SQLException
    {
        JSONObject json = new JSONObject();
       // System.out.println("hhhhhh"+username+password+account);
       String session=generateSession();
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
        Register obj=new Register();
      java.sql.Connection con=null;
      try
      {
 Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet res=stmt.executeQuery("SELECT * FROM  credentials WHERE username='"+username+"' AND account_type='"+account+"'");

int flag=0;
String temp_base64="";
String refer_code="";
while(res.next())
{
    temp_base64=res.getString("password");
       
}
if(obj.matching(temp_base64, password))
{
 
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("UPDATE login SET sessionid='"+session+"' WHERE username='"+username+"'");
    preparedStmt.executeUpdate();
    ResultSet result=stmt.executeQuery("SELECT * FROM  referral WHERE username='"+username+"'");
while(result.next())
        {
          refer_code=result.getString("refer_code");
        }
    json.put("result","true");
        json.put("message","Login Successful");
        json.put("username", username);
        json.put("session",session);
        json.put("account_type",account);
        json.put("refer_code",refer_code);
}
else
{
    
    json.put("result","false");
    json.put("message","Login Unsuccessful");
     
}
      }catch(Exception e)
      {
          System.out.println(e.getMessage());
      }  finally{
                 con.close();
              }
     return  json.toString();
    }
     
     public boolean IsvalidSession(HashMap db,String username,String session_id)
     {
         boolean result=false;
          String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
        Register obj=new Register();
      java.sql.Connection con=null;
      try
      {
          Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet res=stmt.executeQuery("SELECT * FROM login WHERE username='"+username+"' AND sessionid='"+session_id+"'");

if (!res.isBeforeFirst() ) {    
  result=false;
}else
{
  result=true;  
}
      }catch(Exception e)
      {
          
      }
         
         
      return result;   
         
     }
   
}
