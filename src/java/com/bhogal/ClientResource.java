/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author Vishal
 */
@Path("/user")
public class ClientResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ClientResource
     */
    public ClientResource() {
    }

    /**
     * Retrieves representation of an instance of com.vishal.ClientResource
     * @return an instance of java.lang.String
     */

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
   public String login(@FormParam("username") String username,
           @FormParam("password") String password,
           @FormParam("account_type") String account)
   {
               Properties prop = new Properties();
       JSONObject json = new JSONObject();
       String result="";
       try
       {
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
     Login obj=new Login();
     result=obj.login(newmap,username,password,account);
          // System.out.println("-----------------"+session);
       }catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
       
       return result;
   }
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
   public String register(@FormParam("username") String username,
                   @FormParam("password") String password,
                   @FormParam("email") String email,
                   @FormParam("mobile_no") String mobile_no,
                   @FormParam("account_type") String account,
                   @FormParam("refer_code") String refer_code){
       
       
       
        Properties prop = new Properties();
       JSONObject json = new JSONObject();
       String result="";
       try
       {
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
      String otp_url=prop.getProperty("otp_url");
      String api_key=prop.getProperty("api_key");
      String tranx_url=prop.getProperty("tranx_url");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
     newmap.put(5, api_key);
     newmap.put(6, otp_url);
     newmap.put(7, tranx_url);
     
Register ob=new Register();
Referral obj=new Referral();
     if(refer_code==null|| refer_code.isEmpty())
       {
           refer_code=null;
           
    result=ob.register(newmap, username, password, email,mobile_no, account,refer_code);

       }else
       {
           
           boolean b=obj.validaterferral(newmap, refer_code);
           if(b==true)
           {
    result=ob.register(newmap, username, password, email,mobile_no, account,refer_code);
    obj.update_refer_count(newmap, refer_code);
    
   
           }else
           {
               JSONObject j = new JSONObject();
               j.put("result","false");
               j.put("message","Invalid Refer Code");
               
               result=j.toString();
               
               
               
           }
           
       }
     
   
      // code to generate and insert referal code
      
     
      
       }catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
      
       
   return result;
   }
   @POST
   @Path("/updateprofile")
   @Produces(MediaType.APPLICATION_JSON)
           public String update(@FormParam("username")String username,
                                @FormParam("session_id")String session,
                                @FormParam("firstname")String firstname,
                                @FormParam("lastname")String lastname,
                                @FormParam("address")String address,
                                @FormParam("city")String city,
                                @FormParam("state")String state,
                                @FormParam("country")String country,
                                @FormParam("pincode")String pin,
                                @FormParam("kyc_type")String kyc_type,
                                @FormParam("kyc_id")String kyc_id){
       String result=null;
        Properties prop = new Properties();
       if(username == null && username.isEmpty()) {  }
           if(session == null && session.isEmpty()) { }
       if(firstname == null && firstname.isEmpty()) { firstname=null; }
       if(lastname == null && lastname.isEmpty()) { lastname=null; }
       if(address== null && address.isEmpty()) { address=null; }
       if(city == null && city.isEmpty()) { city=null; }
       if(state == null && state.isEmpty()) { state=null; }
       if(country == null && country.isEmpty()) { country=null; }
       if(pin == null && pin.isEmpty()) { pin=null; }
      // if(mob == null && mob.isEmpty()) { mob=null; }
         if(kyc_type == null && kyc_type.isEmpty()) { kyc_type=null; }
             if(kyc_id == null && kyc_id.isEmpty()) { kyc_id=null; }


       
       try
       {
           // System.out.print("pin :"+pin+" mobile:"+mob);
      //int pincode=Integer.parseInt(pin);	
      //int mobile=Integer.parseInt(mob);
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
     Profile prof=new Profile();
    result= prof.updateprofile(newmap, username, session, firstname, lastname, address, city, state, country, pin,kyc_type,kyc_id);
     
       }catch(Exception e)
       {
           
       }
       
       
       
    return result;   
   }
    
           
           
    @POST
    @Path("/getprofile")
    @Produces(MediaType.APPLICATION_JSON)
     public String getProfile(@FormParam("username")String username,
                              @FormParam("session_id")String session)
    {
          Properties prop = new Properties();
          String result=null;
        try
        {
              String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
        Profile p=new Profile();
       
        result=p.getProfile(newmap, username, session);
        }catch(Exception c)
        {
            
        }
        return result;
    }
    
     @POST
     @Path("/addbankdetails")
     @Produces(MediaType.APPLICATION_JSON)
     public String addBankDetails(@FormParam("username")String username,
                                  @FormParam("session_id")String session,
                                  @FormParam("bank_name")String bankname,
                                  @FormParam("account_no")String account_no,
                                  @FormParam("ifsc_code")String ifsc_code,
                                  @FormParam("branch")String branch,
                                  @FormParam("branch_code")String branch_code,
                                  @FormParam("bank_account_type")String bank_account_type
                                  )
     {
         String result=null;
        Properties prop = new Properties();
       
            if(username == null && username.isEmpty()) {  }
           if(session == null && session.isEmpty()) { }
       if(bankname == null && bankname.isEmpty()) { bankname=null; }
       if(account_no == null && account_no.isEmpty()) { account_no=null; }
       if(ifsc_code== null && ifsc_code.isEmpty()) { ifsc_code=null; }
       if(branch == null && branch.isEmpty()) { branch=null; }
       if(branch_code == null && branch_code.isEmpty()) { branch_code=null; }
       if(bank_account_type == null && bank_account_type.isEmpty()) { bank_account_type=null; }
      
       try
       {
           
     
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
     BankDetail bank=new BankDetail();
    result=bank.addBankDetails(newmap, username, session, bankname, account_no, ifsc_code, branch, branch_code, bank_account_type);
     
       }catch(Exception e)
       {
           
       }
       
         return result;
     }
    
     @POST
     @Path("/getbankdetails")
     @Produces(MediaType.APPLICATION_JSON)
     public String gettBankDetails(@FormParam("username")String username,
                                  @FormParam("session_id")String session
                                  )
     {
         String result=null;
        Properties prop = new Properties();
       
       
       try
       {
           
     
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
     BankDetail bank=new BankDetail();
    result=bank.getBankDetails(newmap, username, session);
       }catch(Exception e)
       {
           
       }
       
         return result;
     }
    
     
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    @POST
    @Path("/send_otp")
    @Produces(MediaType.APPLICATION_JSON)
    public String otp(@FormParam("mobile_no")String mobile
                                  )
    {
        
     String result=null;
        Properties prop = new Properties();
       
       
       try
       {
           
     
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     String otp_url=prop.getProperty("otp_url");
      String api_key=prop.getProperty("api_key");
      String tranx_url=prop.getProperty("tranx_url");
     
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
      newmap.put(5, api_key);
     newmap.put(6, otp_url);
     newmap.put(7, tranx_url);
     Mobile m=new Mobile();
     String otp=m.generate_otp(newmap, mobile);
     result=m.sendOTP(newmap, mobile, otp);
     
     
     
     
     
       }catch(Exception e)
       {
           System.err.println(e);
       }
        
        
        return result;
        
    }
    @POST
    @Path("/validate_otp")
    @Produces(MediaType.APPLICATION_JSON)
    public String validotp(@FormParam("mobile_no")String mobile,
                           @FormParam("otp")String otp
                                  )
    {
        
     String result=null;
        Properties prop = new Properties();
       
       
       try
       {
           
     
     String path=this.getClass().getClassLoader().getResource("").getPath();
     InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
     String databaseurl=prop.getProperty("databaseurl");
     String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
     String dbpass=prop.getProperty("dbpassword");
     String otp_url=prop.getProperty("otp_url");
      String api_key=prop.getProperty("api_key");
      String tranx_url=prop.getProperty("tranx_url");
     
     HashMap newmap = new HashMap();
     newmap.put(1, dbusername);
     newmap.put(2, dbname);
     newmap.put(3, dbpass);
     newmap.put(4, databaseurl);
      newmap.put(5, api_key);
     newmap.put(6, otp_url);
     newmap.put(7, tranx_url);
     Mobile m=new Mobile();
     result=m.validate_otp(newmap, mobile, otp);

     
     
     
     
     
       }catch(Exception e)
       {
           System.err.println(e);
       }
        
        
        return result;
        
    }
//     @POST
//    @Path("/getchain")
//    @Produces(MediaType.APPLICATION_JSON)
//   public String getchain(@FormParam("username") String username,
//           @FormParam("session_id") String session)
//   {
//               Properties prop = new Properties();
//       JSONObject json = new JSONObject();
//       String result="";
//       try
//       {
//     String path=this.getClass().getClassLoader().getResource("").getPath();
//     InputStream stream = new FileInputStream(path+"DBConnect.properties");
//     prop.load(stream);
//     String databaseurl=prop.getProperty("databaseurl");
//     String dbusername=prop.getProperty("dbusername");
//     String dbname=prop.getProperty("dbname");
//     String dbpass=prop.getProperty("dbpassword");
//     HashMap newmap = new HashMap();
//     newmap.put(1, dbusername);
//     newmap.put(2, dbname);
//     newmap.put(3, dbpass);
//     newmap.put(4, databaseurl);
//     
//     Chain c=new Chain();
//     result=c.getChain(username, newmap, session);
//       }catch(Exception e)
//               {
//                   
//               }
//       return result;
//}
    @POST
    @Path("/getchain")
   @Produces(MediaType.APPLICATION_JSON)
  public String getchain(@FormParam("username") String username,
          @FormParam("session_id") String session)
  {
      String user="Vishal";
      Properties prop = new Properties();
       JSONObject json = new JSONObject();
      String result="";
              try
      {
    String path=this.getClass().getClassLoader().getResource("").getPath();
    InputStream stream = new FileInputStream(path+"DBConnect.properties");
     prop.load(stream);
    String databaseurl=prop.getProperty("databaseurl");
    String dbusername=prop.getProperty("dbusername");
     String dbname=prop.getProperty("dbname");
    String dbpass=prop.getProperty("dbpassword");
    HashMap newmap = new HashMap();
      newmap.put(1, dbusername);
     newmap.put(2, dbname);
    newmap.put(3, dbpass);
    newmap.put(4, databaseurl);
    
       NewChain obj=new NewChain();
       result=obj.retrievechain(username, newmap, session);
      }catch(Exception e)
      {
          
      }
      
   
      
     return result;
  
  }
}
