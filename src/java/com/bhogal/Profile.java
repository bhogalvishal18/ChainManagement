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
 * @author Mayank
 */
public class Profile {
       public String updateprofile(HashMap db,String username,String session_id,String firstname,String lastname,String address, String city,String state,String country,String pincode,String mobile,String kyc_type,String kyc_id) throws SQLException
    {
        JSONObject json = new JSONObject();
  boolean result;
       //String session=generateSession();
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
Login login=new Login();
result=login.IsvalidSession(db, username, session_id);  // api to check the USERNAME and SESSION_ID
//System.out.println("Valid session ? :"+result);
if(result!=true)
{
       
   json.put("result","false");
   json.put("message","Auhentication Fail");
}
else { 
  

  PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("SELECT * FROM userdetails WHERE username='"+username+"'");
   ResultSet rs=preparedStmt.executeQuery();
  if (!rs.isBeforeFirst() ) {
     // insert the user profile
    
      PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO userdetails(username, firstname, lastname, address, city, state, country, pincode, mobile_no,kyc_type,kyc_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
      ps.setString (1,username);
      ps.setString (2,firstname);
      ps.setString(3,lastname);
      ps.setString(4,address);
      ps.setString(5,city);
      ps.setString(6,state);
      ps.setString(7,country);
      ps.setString(8,pincode);
      ps.setString(9,mobile);
      ps.setString(10, kyc_type);
      ps.setString(11, kyc_id);
             
      int o=ps.executeUpdate(); 
 
      con.close();
      
      json.put("result","true");
      json.put("message","Profile Updated");
      json.put("username",username);
      json.put("firstname",firstname);
      json.put("lastname",lastname);
      json.put("address",address);
      json.put("city",city);
      json.put("state",state);
      json.put("country",country);
      json.put("pincode",pincode);
      json.put("mobile_no",mobile);
      json.put("kyc_type", kyc_type);
      json.put("kyc_id", kyc_id);
      
  }  else
  {
      //update 
      String updateTableSQL = "UPDATE userdetails SET firstname ='"+firstname+"',lastname='"+lastname+"',address='"+address+"',city='"+city+"',state='"+state+"',country='"+country+"',pincode='"+pincode+"',mobile_no='"+mobile+"',kyc_type='"+kyc_type+"',kyc_id='"+kyc_id+"' WHERE username = '"+username+"'";
PreparedStatement preparedStatement = (PreparedStatement) con.prepareStatement(updateTableSQL);
preparedStatement.executeUpdate();
  json.put("result","true");
      json.put("message","Profile Updated");
      json.put("username",username);
      json.put("firstname",firstname);
      json.put("lastname",lastname);
      json.put("address",address);
      json.put("city",city);
      json.put("state",state);
      json.put("country",country);
      json.put("pincode",pincode);
      json.put("mobile_no",mobile);
      json.put("kyc_type", kyc_type);
      json.put("kyc_id", kyc_id);

  }
   
   
}
      }catch(Exception e)
      {
          
      }
      
      return json.toString();
    }
       
  public String getProfile(HashMap db,String username,String session_id)
  {
       
      JSONObject js=new JSONObject();
      boolean check;
      Login login=new Login();
      check=login.IsvalidSession(db, username, session_id);
      
      if(!check)
      {
          
          js.put("result","false");
          js.put("message","Auhentication Fail");
      }else
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
              PreparedStatement pre_stat = (PreparedStatement) con.prepareStatement("SELECT * FROM userdetails WHERE username='"+username+"'");
              ResultSet rs=pre_stat.executeQuery();
              if(rs.isBeforeFirst())
              {
                  while(rs.next())
                  {
                      
                      js.put("result","true");
                      js.put("message","Profile Details");
                      js.put("username",rs.getString(2));
                      js.put("firstname",rs.getString(3));
                      js.put("lastname",rs.getString(4));
                      js.put("address",rs.getString(5));
                      js.put("city",rs.getString(6));
                      js.put("state",rs.getString(7));
                      js.put("country",rs.getString(8));
                      String pin=String.valueOf(rs.getInt(9));
                      js.put("pincode",pin);
                      js.put("mobile_no",rs.getString(10));
                      js.put("kyc_type", rs.getString(11));
                      js.put("kyc_id",rs.getString(12));
                  }
              }else{
                       
                  
                  js.put("result","true");
                  js.put("message","Profile Details");
                  js.put("username",username);
                  js.put("firstname",null);
                  js.put("lastname",null);
                  js.put("address",null);
                  js.put("city",null);
                  js.put("state",null);
                  js.put("country",null);
                  //String pin=String.valueOf(rs.getInt(8));
                  js.put("pincode",null);
                  js.put("mobile_no",null);
                  js.put("kyc_type", null);
                  js.put("kyc_id", null);
              }
              
          }catch(Exception e)
          {
              
          }
      }
      return js.toString();
}
}
