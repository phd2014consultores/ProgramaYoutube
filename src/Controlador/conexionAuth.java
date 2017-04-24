/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author David Botello
 */
public class conexionAuth 
{
     /**
     * Define la instancia global HTTP transport.
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define la instancia global JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final String CLIENT_ID = "91851742346-amlq37mkdgf7aahmibn2vk8ttfe48gb6.apps.googleusercontent.com";
    
    private static final String CLIENT_SECRET = "gYHnJHeNC-oZJIPb22uAWTru";
    
    private static final String REFRESH_TOKEN = "1/Zo9IlDxoackzR1Olkw5uSRUQrBk9sWt5ZLp5KicBiosB_TAzKs969uSCI5RpCIKS";
    
    private static final String CHANNEL_ID = "UCSuZpSq2Wyy3ys7B5DxStKw";
    
    public static YouTube comandos() throws IOException
    {
      return new YouTube.Builder(conexionAuth.HTTP_TRANSPORT, conexionAuth.JSON_FACTORY, Autorizar()).setApplicationName("youtube-cmdline-localizations-sample").build();
    }


    private static Credential Autorizar() throws IOException
    {
        GoogleCredential.Builder builder = new GoogleCredential.Builder();
        builder.setTransport(HTTP_TRANSPORT);
        builder.setJsonFactory(JSON_FACTORY);
        builder.setClientSecrets(CLIENT_ID,CLIENT_SECRET);
             
        Credential credential = builder.build();
        credential.setAccessToken(REFRESH_TOKEN);
        credential.setRefreshToken(getAccessToken());
       // System.out.println(getAccessToken());
        // Authorize.
        return credential;
    }
    
    
    private static String getAccessToken()
    {
       try
       {
           Map<String,String> params = new LinkedHashMap<String,String>();
           params.put("grant_type","refresh_token");
           params.put("client_id",CLIENT_ID);
           params.put("client_secret",CLIENT_SECRET);
           params.put("refresh_token",REFRESH_TOKEN);

           StringBuilder postData = new StringBuilder();
           for(Map.Entry<String,String> param : params.entrySet())
           {
               if(postData.length() != 0)
               {
                   postData.append('&');
               }
               postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
               postData.append('=');
               postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
           }
           byte[] postDataBytes = postData.toString().getBytes("UTF-8");

           URL url = new URL("https://accounts.google.com/o/oauth2/token");
           HttpURLConnection con = (HttpURLConnection)url.openConnection();
           
           con.setDoOutput(true);
           con.setUseCaches(false);
           con.setRequestMethod("POST");
           con.getOutputStream().write(postDataBytes);

           BufferedReader  reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
           StringBuffer buffer = new StringBuffer();
           for (String line = reader.readLine(); line != null; line = reader.readLine())
           {
               buffer.append(line);
           }

           JSONObject json = new JSONObject(buffer.toString());
          
           String accessToken = json.getString("access_token");
           return accessToken;
       }
       catch (Exception ex)
       {
           ex.printStackTrace(); 
       }
       return null;
    }

    public static String getCHANNEL_ID()
    {
        return CHANNEL_ID;
    }
    
    
    
}//Fin de la Clase
