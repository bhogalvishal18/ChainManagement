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
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        return "hello";
    }
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
                   @FormParam("account_type") String account){
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
     Register ob=new Register();
    result=ob.register(newmap, username, password, email, account);
   System.out.println("result"+result);
      
       }catch(Exception e)
       {
           System.out.println(e.getMessage());
       }
      
       
   return result;
   }
    /**
     * PUT method for updating or creating an instance of ClientResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
