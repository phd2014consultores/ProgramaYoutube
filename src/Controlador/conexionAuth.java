/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
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

    //se coloca el cliente id obtenido en la pagina google Api COnsole de la cuenta google, para ello se debe crear 
    //un nuevo proyecto Id de cliente OAuth
    private static final String CLIENT_ID = "91851742346-amlq37mkdgf7aahmibn2vk8ttfe48gb6.apps.googleusercontent.com";
    
    //se coloca el client secret obtenido en el google Api Console
    private static final String CLIENT_SECRET = "gYHnJHeNC-oZJIPb22uAWTru";
    
    
    //se coloca el token obtenido de la pagina OAuth 2.0 Playground que permite actualizar el tiempo de duracion del
    //token, su duracion es de maximo una hora 
    private static final String REFRESH_TOKEN = "1/Zo9IlDxoackzR1Olkw5uSRUQrBk9sWt5ZLp5KicBiosB_TAzKs969uSCI5RpCIKS";
    
    //Id del canal de youtube, por efectos practicos se coloco constante ya que es un solo canal, ahora bien este id puede 
    //obtenerse de la misma forma que se obtienen los id PlayList, id video o id Comentario. Esto es cuando se manejen 
    //de un canal con la misma cuenta de google
    private static final String CHANNEL_ID = "UCSuZpSq2Wyy3ys7B5DxStKw";
    
    /**
     * metodo que se encarga de establecer comunicacion con la cuenta google segun las credenciales previamente autorizadas
     * @return YouTube
     * @throws IOException 
     */
    public static YouTube comandos() throws IOException
    {
      return new YouTube.Builder(conexionAuth.HTTP_TRANSPORT, conexionAuth.JSON_FACTORY, Autorizar()).setApplicationName("Programa de YouTube").build();
    }

    /**
     * Metodo que verifica CLIENT_ID,CLIENT_SECRET y REFRESH_TOKEN, y autoriza las credenciales necesarias para establecer
     * la comunicacion con la cuenta google
     * @return Credential
     * @throws IOException 
     */
    private static Credential Autorizar() throws IOException
    {
        GoogleCredential.Builder builder = new GoogleCredential.Builder();
        builder.setTransport(HTTP_TRANSPORT);
        builder.setJsonFactory(JSON_FACTORY);
        builder.setClientSecrets(CLIENT_ID,CLIENT_SECRET);
             
        Credential credential = builder.build();
        credential.setAccessToken(REFRESH_TOKEN);
        credential.setRefreshToken(getAccessToken());
   
        return credential;
    }
    
    /**
     * Metodo que se encarga de autualizar el token para evitar fallos de comunicacion por 
     * token expirado
     * @return String
     */
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
