/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bhogal;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 *
 * @author Mayank
 */
public class Chain {
   public  String path=this.getClass().getClassLoader().getResource("").getPath();
    
    public String insertUser(HashMap db,String username,String session_id,String refer_code,String parent_refer_code)
    {
     
      //  System.out.println("Inside chain code :"+username+": Refer code :"+refer_code+": Parent Refer code :"+parent_refer_code);
      
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
          //Class.forName("com.mysql.jdbc.Driver");  
Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Login login=new Login();
data=login.IsvalidSession(db, username, session_id);  // api to check the USERNAME and SESSION_ID
if(data!=true)
{
    System.out.println("Login fail huya");
       
   json.put("result","false");
   json.put("message","Auhentication Fail");
}
else {
    
    int out = 0;
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("SELECT * FROM structure WHERE username='"+username+"'");
   ResultSet rs=preparedStmt.executeQuery();
  if (!rs.isBeforeFirst()) {     // insert the user profile

    // customization of chain
    Chain obj=new Chain();
  
    // end of customization
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
            
              String changed_parent_code=obj.chain_custom(db, username, refer_code, parent_refer_code);
    //  System.out.println("Changed Parent :"+ changed_parent_code);
             ps.setString(1, username);
    ps.setString(2,refer_code);
     
            // ps.setNull(2, java.sql.Types.VARCHAR);
             ps.setString(3,changed_parent_code);
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
     
          public String retrievechain(String username,HashMap db,String session_id)
     {
         String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
        
        
      java.sql.Connection con=null;
             JSONObject json = new JSONObject();
             boolean data;
             try
             { 
                 Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
Login login=new Login();
data=login.IsvalidSession(db, username, session_id);  // api to check the USERNAME and SESSION_ID
if(data!=true)
{
       
   json.put("result","false");
   json.put("message","Auhentication Fail");
}else
{

//get chain
    int out = 0;
    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement("SELECT * FROM structure WHERE username='"+username+"'");
   ResultSet rs=preparedStmt.executeQuery();
    if (!rs.isBeforeFirst())     // insert the user profile
    {
        json.put("result","false");
        json.put("message","No Chain Found");
    }  
    else{ 
    String refer_code=null;
    String parent_refer_code=null;
        while (rs.next()) {
           refer_code=rs.getString("refer_code");
        parent_refer_code=rs.getString("parent_refer_code"); 
        }
       
   if(isNullOrEmpty(parent_refer_code))
   {
       // parent refer is null and user chains is root
       
       PreparedStatement preparedStmtchain = (PreparedStatement) con.prepareStatement("WITH RECURSIVE chain_list (username, refer_code,parent_refer_code, level, path) AS\n" +
"(\n" +
"  SELECT username, refer_code,parent_refer_code, 0 level, username as path\n" +
"    FROM structure\n" +
"    WHERE parent_refer_code IS NULL AND\n" +
"    username=\'"+username+"'\n" +
"  UNION ALL\n" +
"  SELECT c.username, c.refer_code,c.parent_refer_code,cp.level + 1, CONCAT(cp.path, ' > ', c.username)\n" +
"    FROM chain_list AS cp JOIN structure AS c\n" +
"      ON cp.refer_code = c.parent_refer_code\n" +
")\n" +
"SELECT * FROM chain_list\n" +
"ORDER BY path;");
   ResultSet rst=preparedStmtchain.executeQuery();
    json.put("result","true");
         json.put("name",username);
       json.put("refer_code",refer_code);
       json.put("parent_refer_code",parent_refer_code);
             
JSONArray ja = new JSONArray();

 String temp="0";
 ArrayList<Integer> arrli = new ArrayList<Integer>();
       while(rst.next())
       {
          
         //int convert = Integer.parseInt(rst.getString(3));
      //arrli.add(convert);
      //json_arr.add(convert);
       //String a=rst.getString(3);
       JSONObject jb=new JSONObject();
       jb.put("name",rst.getString(1));
       jb.put("refer_code",rst.getString(2));
       jb.put("parent_refer_code",rst.getString(3));
              jb.put("level",rst.getString(4));
       // jb.put("level",rst.getString(3));
       ja.put(jb);
       
       }
       
       //json.put("order", json_arr);
        //json.put("depth",temp);
      json.put("child",ja);
       
   }
   else
   {
       // parent refer is  Not null and user chains have subchild
       
       
             PreparedStatement preparedStmtchain = (PreparedStatement) con.prepareStatement("WITH RECURSIVE chain_list (username, refer_code,parent_refer_code, level, path) AS\n" +
"(\n" +
"  SELECT username, refer_code,parent_refer_code, 0 level, username as path\n" +
"    FROM structure\n" +
"    WHERE parent_refer_code='"+refer_code+"'\n" +
"  UNION ALL\n" +
"  SELECT c.username, c.refer_code,c.parent_refer_code,cp.level + 1, CONCAT(cp.path, ' > ', c.username)\n" +
"    FROM chain_list AS cp JOIN structure AS c\n" +
"      ON cp.refer_code = c.parent_refer_code\n" +
")\n" +
"SELECT * FROM chain_list\n" +
"ORDER BY path;");
   ResultSet rst=preparedStmtchain.executeQuery();
    json.put("result","true");
         json.put("name",username);
       json.put("refer_code",refer_code);
       json.put("parent_refer_code",parent_refer_code);
             
JSONArray ja = new JSONArray();

 String temp="0";
 ArrayList<Integer> arrli = new ArrayList<Integer>();
       while(rst.next())
       {
          
         //int convert = Integer.parseInt(rst.getString(3));
      //arrli.add(convert);
      //json_arr.add(convert);
       //String a=rst.getString(3);
       JSONObject jb=new JSONObject();
       jb.put("name",rst.getString(1));
       jb.put("refer_code",rst.getString(2));
       jb.put("parent_refer_code",rst.getString(3));
              jb.put("level",rst.getString(4));
       // jb.put("level",rst.getString(3));
        ja.put(jb);
       }
       
       //json.put("order", json_arr);
        //json.put("depth",temp);
      json.put("child",ja);
        
       
       
       
       
       
       
       
       
   }
        
    }  
    
    
    
    
    
    
    
    
    
}
                 
                 
                 
             }catch(Exception e)
             {
                 
              System.out.println(e);
                 
             }
    
             
             return json.toString();
     }
    public int removeDuplicates(int arr[], int n) 
    { 
        // Return, if array is empty 
        // or contains a single element 
        if (n==0 || n==1) 
            return n; 
       
        int[] temp = new int[n]; 
          
        // Start traversing elements 
        int j = 0; 
        for (int i=0; i<n-1; i++) 
            // If current element is not equal 
            // to next element then store that 
            // current element 
            if (arr[i] != arr[i+1]) 
                temp[j++] = arr[i]; 
          
        // Store the last element as whether 
        // it is unique or repeated, it hasn't 
        // stored previously 
        temp[j++] = arr[n-1];    
          
        // Modify original array 
        for (int i=0; i<j; i++) 
            arr[i] = temp[i]; 
       
        return j; 
    } 
       
   public String chain_custom(HashMap db,String username,String refer_code,String parent_refer_code)
   {
       Chain ch=new Chain();
        boolean data;
   
   String result=null;
       //String session=generateSession();
        String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
    
      java.sql.Connection con=null;
      try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
           PreparedStatement preparedStmtchain = (PreparedStatement) con.prepareStatement("WITH RECURSIVE chain_list (username, refer_code,parent_refer_code, level, path) AS\n" +
"(\n" +
"  SELECT username, refer_code,parent_refer_code, 0 level, username as path\n" +
"    FROM structure\n" +
"    WHERE parent_refer_code='"+parent_refer_code+"'\n" +
"  UNION ALL\n" +
"  SELECT c.username, c.refer_code,c.parent_refer_code,cp.level + 1, CONCAT(cp.path, ' > ', c.username)\n" +
"    FROM chain_list AS cp JOIN structure AS c\n" +
"      ON cp.refer_code = c.parent_refer_code\n" +
")\n" +
"SELECT * FROM chain_list\n" +
"ORDER BY path;");
   ResultSet rst=preparedStmtchain.executeQuery(); 
   JSONArray jsonArr=new JSONArray();

   
   int count=0;
   ArrayList<String> al=new ArrayList<String>();
   JSONObject jsonObj = null;
      while(rst.next())
      {
          if(rst.getString(4).equals("0"))
          {
             // System.out.println("iske andar aya");
          count=count+1;
          }else
          {
              
          }
            jsonObj=new JSONObject();
        jsonObj.put("level",rst.getString(4));
        jsonObj.put("refer_code",rst.getString(2));
        
         al.add(rst.getString(4));
              
           jsonArr.put(jsonObj);        
      }
           
      
      if(count<4)
      {
          
          //System.out.println("can insert");
       result=parent_refer_code;   
      }else
      {
          
         //System.out.println("cannot insert");
        JSONObject ob = new JSONObject();
        ob.put("data",jsonArr);
        
     Set uniqueValues = new HashSet(al);
      int depth=uniqueValues.size();
  //System.out.println("depth"+depth);
    
      JSONObject obj = new JSONObject(ob.toString());
      
        boolean b=false;
        int t=0;
for(int i=0;i<depth;i++)
{
    
    //System.out.println("Current depth"+i);
    for(int j=0;j<al.size();j++)
    { 
        String r_c = null;
        JSONArray arr = obj.getJSONArray("data");
       
            if(i==Integer.parseInt(al.get(j)))
            {
               // System.out.println("aaaa");
            String level = arr.getJSONObject(j).getString("level");
             r_c = arr.getJSONObject(j).getString("refer_code");
             
             b=ch.check_subchild(db, r_c);
             
            }else{}
            if(b==true)
             {
                 
                //  System.out.println("r_c"+r_c);
                  t=1;
                 result=r_c;
                 break;
             }else
            {
                // this is the case when a parent has 4  number of child and hench a new refer code is needed
                // this is the time when earning service with the full child needs to be inserted 
               
             
                
                
              
            }
          
           
        
    }//
 
    
    if(t==1)
    {
        break;
    }
}// external loop ends
        
      }
      
    
        
      }catch(Exception e)
      {
          System.out.println(e);
      }
       // System.out.println("Result :"+result);  
    return result;   
       
   }
   public boolean check_subchild(HashMap db,String parent_refer_code)
   {
     String dbusername = (String)db.get(1);
        String dbname = (String)db.get(2);
        String dbpass=(String)db.get(3);
        String databaseurl=(String)db.get(4);
      // System.out.println("Parent :"+parent_refer_code);
       
      java.sql.Connection con=null;
      try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
con=DriverManager.getConnection("jdbc:mysql://"+databaseurl+"/"+dbname,dbusername,dbpass);  
           PreparedStatement preparedStmtchain = (PreparedStatement) con.prepareStatement("WITH RECURSIVE chain_list (username, refer_code,parent_refer_code, level, path) AS\n" +
"(\n" +
"  SELECT username, refer_code,parent_refer_code, 0 level, username as path\n" +
"    FROM structure\n" +
"    WHERE parent_refer_code='"+parent_refer_code+"'\n" +
"  UNION ALL\n" +
"  SELECT c.username, c.refer_code,c.parent_refer_code,cp.level + 1, CONCAT(cp.path, ' > ', c.username)\n" +
"    FROM chain_list AS cp JOIN structure AS c\n" +
"      ON cp.refer_code = c.parent_refer_code\n" +
")\n" +
"SELECT * FROM chain_list\n" +
"ORDER BY path;");
   ResultSet rst=preparedStmtchain.executeQuery();
   int count=0;
   while(rst.next())
   {
       count=count+1;
       
   }
   if(count<4)
   {
       // System.out.println("Full nhe hai");
      
     return true;  
    
   }
   else
   {
       // System.out.println("Full hai");
       
     // this is the code to execute the earning service
       
    return false;     
   }
      }catch(Exception e)
      {
          System.out.println(e);
      }
      return false;
   }
   
  /* public static void main(String...k)
   {
       
       HashMap newmap = new HashMap();
     newmap.put(1,"root");
     newmap.put(2,"clientchain?autoReconnect=true&useSSL=false");
     newmap.put(3,"root");
     newmap.put(4,"localhost:3306");
       Chain c=new Chain();
     //  c.insertUser(newmap, username, session_id, refer_code, parent_refer_code)
       c.chain_custom(newmap,"Vishal","VIS0987WER","AKV8364JQN");
   }  */
   
  
}
