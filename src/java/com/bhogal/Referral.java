/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Vishal
 */
public class Referral {
    
    
    
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 3) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    
public String generate_referral(String username)
{
 StringBuilder sb=new StringBuilder();         
String sub = username.substring(0, 3);
Random rand = new Random();
String val = ""+((int)(Math.random()*9000)+1000);
Referral obj=new Referral();
String lastString=obj.getSaltString();
sub=sub.toUpperCase();
sb.append(sub);
sb.append(val);
sb.append(lastString);
return sb.toString();
}
 public boolean insertrefer(HashMap db,String username,String refercode) throws SQLException
    {
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
ResultSet res=stmt.executeQuery("SELECT * FROM  referral WHERE username='"+username+"'");
int flag=0;

while(res.next())
{
    flag++;
       
}
if(flag==0)
{
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("INSERT INTO referral(username, refer_code) VALUES (?,?)");
       preparedStmt.setString (1, username);
      preparedStmt.setString (2, refercode);
      preparedStmt.execute();
      
      return true;
      
}
else
{
return false;    
}
      }catch(Exception e)
      {
          System.out.println(e); 
      }
      finally
      {
          con.close();
      }
   return  true;
    }
 
 public boolean  validaterferral(HashMap db,String refer_code) throws SQLException
 {
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
ResultSet res=stmt.executeQuery("SELECT * FROM  referral WHERE refer_code='"+refer_code+"'");
 String code="";

while(res.next())
{
   code=res.getString("refer_code");
   
}
int result=code.compareTo(refer_code);
if(result==0)
    return true;
else
    return false;
 }catch(Exception e)
      {
          System.out.println(e);
      } finally
      {
          con.close();
      }
     
   return true;
 }
 public void update_refer_count(HashMap db,String refer_code) throws SQLException
 {
     
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
ResultSet res=stmt.executeQuery("SELECT * FROM  referral WHERE refer_code='"+refer_code+"'");
 String temp="";

while(res.next())
{
   
   temp=res.getString("no_time_use");
    
   int i=Integer.parseInt(temp);
   i=i+1;
   String count=String.valueOf(i);
  
  PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("UPDATE referral SET no_time_use='"+count+"' WHERE refer_code='"+refer_code+"'");
    preparedStmt.executeUpdate(); 
}
      }catch(Exception e)
      {
          System.out.println(e);
      }
      finally{
          con.close();
      }
     
     
     
 }
      
     
     
 
}
