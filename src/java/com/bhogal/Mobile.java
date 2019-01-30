/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

/**
 *
 * @author Mayank
 */
import com.mysql.jdbc.PreparedStatement;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Mobile {
    

	private static final String USER_AGENT = "Mozilla/5.0";
         
        public static void main(String...k){
       
             try{
           
   
     HashMap newmap = new HashMap();
     
     newmap.put(1, "c3e72263-1da0-11e9-9ee8-0200cd936042");
   
     newmap.put(2, "https://2factor.in/API/R1");
           Mobile m=new Mobile();
            m.sendTransactionMessage(newmap,"8755309939", "MANITX", "MANI", "Vishal");
             }catch(Exception e)
             {
                 
             }
      
        }

	public String sendTransactionMessage(HashMap hm,String to, String from,String templatename,String var) throws IOException {
	 JSONObject json=new JSONObject();
             HashMap db=hm;
     
          String api_key=(String)db.get(5);
        //String otp_url=(String)db.get(6);
        String tranx_url=(String)db.get(7);
            try{	
            
            String fullpath=tranx_url+"?module=TRANS_SMS&apikey="+api_key+"&to="+to+"&from="+from+"&templatename="+templatename+"&var1="+var;
            URL obj = new URL(fullpath);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
                
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
	System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
                       
 Object ob = new JSONParser().parse(response.toString()); 
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) ob; 
        String status=(String)jo.get("Status");
        if(status.equals("Success"))
        {
            json.put("result","true");
            json.put("message","Message Send");
        }else
        {
            json.put("result","false");
            json.put("message","Message Sending Failed");
        }
			
		} else {
			 json.put("result","false");
            json.put("message","Message Sending Failed");
		}
        }catch(Exception e)
        {
            System.out.println(e);
        }
return json.toString();
	}
        
  //otp message method
        public String sendOTP(HashMap hm,String mobile, String otp)
        {
            
          JSONObject json=new JSONObject();
                HashMap db=hm;
        
          String api_key=(String)db.get(5);
        String otp_url=(String)db.get(6);
        String tranx_url=(String)db.get(7);
            try{	
            
            String fullpath=otp_url+"/"+api_key+"/SMS/+91"+mobile+"/"+otp;
            //System.out.println(fullpath);
            URL obj = new URL(fullpath);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
                
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		//System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
                       
 Object ob = new JSONParser().parse(response.toString()); 
          
        // typecasting obj to JSONObject 
        JSONObject jo = (JSONObject) ob; 
        String status=(String)jo.get("Status");
        if(status.equals("Success"))
        {
            json.put("result","true");
            json.put("message","Message Send");
        }else
        {
            json.put("result","false");
            json.put("message","Message Sending Failed");
        }
			
		} else {
			 json.put("result","false");
            json.put("message","Message Sending Failed");
		}
        }catch(Exception e)
        {
            System.out.println(e);
        }
    return json.toString();   
            
            
            
        }
        
        public String generate_otp(HashMap hm,String mobile_no)
        {
           String otp = ""+((int)(Math.random()*9000)+1000);
           System.out.println(otp);
           HashMap db=hm;
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
      
       
        
      java.sql.Connection con=null;
      
       JSONObject json = new JSONObject();
        try
        {
           
Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("SELECT * FROM  mobile_otp WHERE mobile_no='"+mobile_no+"'");
if(rs.next())
{
   PreparedStatement ps = (PreparedStatement) con.prepareStatement("UPDATE mobile_otp SET gen_otp='"+otp+"' WHERE mobile_no='"+mobile_no+"'");
  ps.executeUpdate();
  
}
else
{
     PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("INSERT INTO mobile_otp(mobile_no,gen_otp) VALUES (?,?)");
   preparedStmt.setString(1,mobile_no);
   preparedStmt.setString(2,otp);
   int i=preparedStmt.executeUpdate();
   if(i!=0)
   {
       System.out.println("Inserted otp");
   }else
   {
        System.out.println("Insertion fail");
   }
   
}



        }catch(Exception e)
        {
            
            System.err.println(e);
            
        }
            
            
            
            return otp;
        }
        public String validate_otp(HashMap hm,String mobile_no,String input_otp)
        {
            
         HashMap db=hm;
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
      java.sql.Connection con=null;
      
       JSONObject json = new JSONObject();
        try
        {
           
Class.forName("com.mysql.jdbc.Driver");  
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Statement stmt=con.createStatement();  
ResultSet rs=stmt.executeQuery("SELECT * FROM  mobile_otp WHERE mobile_no='"+mobile_no+"'");
if(rs.next())
{
    String save_otp=rs.getString("gen_otp");
    if(save_otp.equals(input_otp))
    {
        json.put("result","true");
        json.put("message","Otp is validated");
        
    }else
    {
        json.put("result","false");
        json.put("message","Otp is not validated");
        
    }
    
    
}else
{
    
    json.put("result","false");
        json.put("message","Otp is not validated");
        
    
}
        }catch(Exception e)
        {
            System.err.println(e);
        }
            
            
            
            return json.toString();
            
            
        }
        


}