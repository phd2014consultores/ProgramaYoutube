/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controlador.conexionAuth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
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
public class Comentarios 
{
      private YouTube youtube;
        private String id;
        private String fecha;
        private String nombre;
        private String textoOriginal;
        private Map<String,Comentarios> tablaHash = new Hashtable<String,Comentarios>();
        
        
        public Comentarios(String fecha, String titulo, String descripcion) 
        {
            this.fecha = fecha;
            this.nombre = titulo;
            this.textoOriginal = descripcion;
        }

        public Comentarios(){ }

        
        public JSONArray listarComentariosId(String VideoId)
        {
            CommentThreadListResponse comentariosResultado = null;
            try 
            {
                
              youtube = conexionAuth.comandos();
                              
              YouTube.CommentThreads.List listaComentario = youtube.commentThreads().list("snippet");
              listaComentario.setFields("items");
              listaComentario.setVideoId(VideoId);
              listaComentario.setOrder("time");
              listaComentario.setMaxResults(Long.valueOf(100));
              
                
              //Importante generar condicion de page token debido a que cada pagina tiene una cantidad de videos
              //por lo que quedaria las lista incompleta 
         
              comentariosResultado = listaComentario.execute();
         
              return new JSONArray(comentariosResultado.get("items").toString());

            } 
            catch (IOException | JSONException ex)
            {
                Logger.getLogger(PlayLists.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          return null;
        }
        
        
     public Map<String,Comentarios> listarInformacionComentarios(String VideoId)
     {
      JSONArray informacion = listarComentariosId(VideoId);
      JSONObject dato;
      Comentarios lista;
      
       try 
       {      
         for (int i = 0; i < informacion.length(); i++)
         {
             dato = informacion.getJSONObject(i);
             lista = new Comentarios(((JSONObject)dato.get("snippet")).get("publishedAt").toString(),
                                   ((JSONObject)dato.get("snippet")).get("authorDisplayName").toString(),
                                   ((JSONObject)dato.get("snippet")).get("textOriginal").toString());
             
                         
             tablaHash.put(((JSONObject)dato.get("snippet")).get("id").toString(), lista);
             
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
        return nombre;
    }

    public String getDescripcion() 
    {
        return textoOriginal;
    }

    public Map<String,Comentarios> getTablaHash()
    {
        return tablaHash;
    }  
    
    
    
}//fin de la clase
