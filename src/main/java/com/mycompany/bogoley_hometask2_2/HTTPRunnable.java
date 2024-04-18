/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bogoley_hometask2_2;

import java.io.*;
import java.net.*;

import java.util.HashMap;

/**
 *
 * @author beb19
 */
public class HTTPRunnable implements Runnable {
    private String address;
    private HashMap<String,String> requestBody;
    private String responseBody;
    
    public HTTPRunnable(String address, HashMap<String, String> requestBody){
        this.address = address;
        this.requestBody = requestBody;
    }
     private String generateStringBody(){
        StringBuilder sbParams = new StringBuilder();
        if(this.requestBody != null && !requestBody.isEmpty()){
            int i = 0;
            for(String key: this.requestBody.keySet()){
                try{
                    if (i !=0){
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(this.requestBody.get(key),"UTF-8"));
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            i++;
            }
        }
        return sbParams.toString();
}
    
    public String getResponseBody(){
        return responseBody;
    }
    
    @Override
    public void run(){
        if(this.address != null && !this.address.isEmpty()){
            try{
                URL url = new URL(this.address);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnecton = (HttpURLConnection)connection;
                httpConnecton.setRequestMethod("POST");
                httpConnecton.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(httpConnecton.getOutputStream());
                osw.write(generateStringBody());
                osw.flush();
                int responseCode = httpConnecton.getResponseCode();
                System.out.println("Response Code : " + responseCode);
                if (responseCode == 200){
                    InputStreamReader isr = new InputStreamReader(httpConnecton.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String currentLine;
                    StringBuilder sbResponse = new StringBuilder();
                    while((currentLine = br.readLine()) != null){
                        sbResponse.append(currentLine);
                    }
                    responseBody = sbResponse.toString();
                }else{
                    System.out.println("Error! Bad response code!");
                }  
                }catch(IOException ex){
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }