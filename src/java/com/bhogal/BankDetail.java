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
public class BankDetail {
    
    public String addBankDetails(HashMap db,String username,String session_id,String bankname,String account_no,String ifsc_code,String branch,String branch_code,String bank_account_type)
    {
        
         boolean check;
         JSONObject json=new JSONObject();
      Login login=new Login();
      check=login.IsvalidSession(db, username, session_id);
     
      String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
       
      java.sql.Connection con=null;
      if(!check)
      {
         
          json.put("result","false");
          json.put("message","Auhentication Fail");
      }else
      {
           try
      {
          Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass); 
PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("SELECT * FROM bankdetails WHERE username='"+username+"'");
   ResultSet rs=preparedStmt.executeQuery();
  if (!rs.isBeforeFirst() ) {
    
      PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO bankdetails(username, bank_name, account_no, ifsc_code, branch,branch_code,account_type) VALUES (?,?,?,?,?,?,?)");
      ps.setString (1,username);
      ps.setString (2,bankname);
      ps.setString(3,account_no);
      ps.setString(4,ifsc_code);
        ps.setString(5,branch);
          ps.setString(6,branch_code);
            ps.setString(7,bank_account_type);
   
      int o=ps.executeUpdate(); 
    
      con.close();
      //System.out.println("INSERTED ");
      json.put("result","true");
      json.put("message","Bank Details Inserted");
      json.put("username",username);
      json.put("bank_name",bankname);
      json.put("account_no",account_no);
      json.put("ifsc_code",ifsc_code);
      json.put("branch",branch);
      json.put("branch_code",branch_code);
      json.put("bank_account_type",bank_account_type);
     
      
  }  else
  {
      //update 
      String updateTableSQL = "UPDATE bankdetails SET bank_name ='"+bankname+"',account_no='"+account_no+"',ifsc_code='"+ifsc_code+"',branch='"+branch+"',branch_code='"+branch_code+"',account_type='"+bank_account_type+"' WHERE username = '"+username+"'";
PreparedStatement preparedStatement = (PreparedStatement) con.prepareStatement(updateTableSQL);
preparedStatement.executeUpdate();
  json.put("result","true");
      json.put("message","Bank Details Updated");
      json.put("username",username);
      json.put("bank_name",bankname);
      json.put("account_no",account_no);
      json.put("ifsc_code",ifsc_code);
      json.put("branch",branch);
      json.put("branch_code",branch_code);
      json.put("bank_account_type",bank_account_type);
   

  }
   
   



        
      }catch(Exception e)
      {
          
      }
      }
        
        
        return json.toString();
    }
    
    public String getBankDetails(HashMap db,String username,String session_id)
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
              PreparedStatement pre_stat = (PreparedStatement) con.prepareStatement("SELECT * FROM bankdetails WHERE username='"+username+"'");
              ResultSet rs=pre_stat.executeQuery();
              if(rs.isBeforeFirst())
              {
                  while(rs.next())
                  {
                      
                      js.put("result","true");
                      js.put("message","Bank Details");
                      js.put("username",rs.getString(2));
                      js.put("bank_name",rs.getString(3));
                      js.put("account_no",rs.getString(4));
                      js.put("ifsc_code",rs.getString(5));
                      js.put("branch",rs.getString(6));
                      js.put("branch_code",rs.getString(7));
                      js.put("bank_account_type",rs.getString(8));
                      
                  }
              }else{
                      
                  
                  js.put("result","true");
                      js.put("message","Bank Details");
                      js.put("username",rs.getString(2));
                      js.put("bank_name",rs.getString(3));
                      js.put("account_no",rs.getString(4));
                      js.put("ifsc_code",rs.getString(5));
                      js.put("branch",rs.getString(6));
                      js.put("branch_code",rs.getString(7));
                      js.put("bank_account_type",rs.getString(8));
                      
              }
              
          }catch(Exception e)
          {
              
          }
      }
      return js.toString();
        
        
        
        
    }

    
}
