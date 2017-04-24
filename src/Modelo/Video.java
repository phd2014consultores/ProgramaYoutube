/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controlador.conexionAuth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author David Botello
 */
public class Video 
{
        private YouTube youtube;
        private String id;
        private String fecha;
        private String titulo;
        private String descripcion;
        private Map<String,Video> tablaHash = new Hashtable<String,Video>();
        
        
        public Video(String fecha, String titulo, String descripcion) 
        {
            this.fecha = fecha;
            this.titulo = titulo;
            this.descripcion = descripcion;
        }

        public Video(){ }

        
        public JSONArray listarVideoId(String PlayListId)
        {
            PlaylistItemListResponse videoResultado = null;
            try 
            {
                
              youtube = conexionAuth.comandos();
                              
              YouTube.PlaylistItems.List listaVideo = youtube.playlistItems().list("snippet,contentDetails");
              listaVideo.setFields("items");
              listaVideo.setPlaylistId(PlayListId);
                
              //Importante generar condicion de page token debido a que cada pagina tiene una cantidad de videos
              //por lo que quedaria las lista incompleta 
         
              videoResultado = listaVideo.execute();
         
              return new JSONArray(videoResultado.get("items").toString());

            } 
            catch (IOException | JSONException ex)
            {
                Logger.getLogger(PlayLists.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          return null;
        }
        
        
     public Map<String,Video> listarInformacionVideo(String PlayListId)
     {
      JSONArray informacion = listarVideoId(PlayListId);
      JSONObject dato;
      Video lista;
      
       try 
       {      
         for (int i = 0; i < informacion.length(); i++)
         {
             dato = informacion.getJSONObject(i);
             lista = new Video(((JSONObject)dato.get("snippet")).get("publishedAt").toString(),
                                   ((JSONObject)dato.get("snippet")).get("title").toString(),
                                   ((JSONObject)dato.get("snippet")).get("description").toString());
             
                         
             tablaHash.put(((JSONObject)((JSONObject)dato.get("snippet")).get("resourceId")).get("videoId").toString(), lista);
             
         }
         return tablaHash;
       }
       catch (JSONException ex) 
       {
            
       }
     
       return null;
     }

    public String getId() 
    {
        return id;
    }

    public String getFecha() 
    {
        return fecha;
    }

    public String getTitulo() 
    {
        return titulo;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }

    public Map<String,Video> getTablaHash()
    {
        return tablaHash;
    }  
}
