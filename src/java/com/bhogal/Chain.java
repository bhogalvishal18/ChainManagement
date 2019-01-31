/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import org.json.simple.JSONObject;

/**
 *
 * @author Mayank
 */
public class Chain {
   
    
    public String insertUser(HashMap db,String username,String session_id,String refer_code,String parent_refer_code)
    {
     
        System.out.println("Inside chain code :"+username+": Refer code :"+refer_code+": Parent Refer code :"+parent_refer_code);
        String result=null;
        boolean data;
        JSONObject json = new JSONObject();
 
       //String session=generateSession();
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
        
        
      java.sql.Connection con=null;
      try
      {
          Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Login login=new Login();
data=login.IsvalidSession(db, username, session_id);  // api to check the USERNAME and SESSION_ID
if(data!=true)
{
       
   json.put("result","false");
   json.put("message","Auhentication Fail");
}
else {
    
    int out = 0;
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("SELECT * FROM structure WHERE username='"+username+"'");
   ResultSet rs=preparedStmt.executeQuery();
  if (!rs.isBeforeFirst()) {
     // insert the user profile
    
      PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO structure(username, refer_code, parent_refer_code) VALUES (?,?,?)");
        if(isNullOrEmpty(parent_refer_code))
        {
            
              ps.setString(1, username);
    
            ps.setString(2,refer_code);
            ps.setNull(3, java.sql.Types.VARCHAR);
            
             
             out=ps.executeUpdate(); 
             
              con.close();
                json.put("result","true");
      json.put("message","Chain Insertion Successful");
              
              
        }else
        {
            
            
             ps.setString(1, username);
    ps.setString(2,refer_code);
     
            // ps.setNull(2, java.sql.Types.VARCHAR);
             ps.setString(3,parent_refer_code);
             out=ps.executeUpdate();  
              con.close();
                 json.put("result","true");
      json.put("message","Chain Insertion Successful");
              
        }
      
    
     
 
  }else
  {
    json.put("result","false");
      json.put("message","Chain Insertion Fail");   
    
  }
    
} 
  
    
    
      }catch(Exception e)
      {
         
      }
    return json.toString();
}
     public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
