/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Controlador.conexionAuth;
import com.google.api.services.youtube.YouTube;
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
public class PlayLists 
{
        //-----Stributos de la clase-----//
        private YouTube youtube;
        private String id;
        private String fecha;
        private String titulo;
        private String descripcion;
        private Map<String,PlayLists> tablaHash = new Hashtable<String,PlayLists>();
        //---------------------------------//
        
        public PlayLists(String fecha, String titulo, String descripcion) 
        {
            this.fecha = fecha;
            this.titulo = titulo;
            this.descripcion = descripcion;
        }

        public PlayLists(){ }

        
        /**
         * Metodo que se encarga de listar los de id de cada playlist creado dentro del canal de youtube
         * @return JSONArray
         */
        public JSONArray listarPlaylistId()
        {
            PlaylistListResponse listaResultado = null;
            try 
            {
                
              youtube = conexionAuth.comandos();
                              
              YouTube.Playlists.List listaReproduccion = youtube.playlists().list("snippet,contentDetails");
              listaReproduccion.setFields("items");
              listaReproduccion.setChannelId(conexionAuth.getCHANNEL_ID());
               
         
              listaResultado = listaReproduccion.execute();
      
              return new JSONArray(listaResultado.get("items").toString());

            } 
            catch (IOException | JSONException ex)
            {
                Logger.getLogger(PlayLists.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          return null;
        }
        
        /**
         * Metodo donde se crea una tabla hash con los datos obtenidos en JSONArray 
         * donde el key son los playlistId y el value viene dado por un objeto de tipo 
         * PlayList con los atributos fecha, titulo, descripcion
         * @return Map<String,PlayLists> tabla hash
         */
     public Map<String,PlayLists> listarInformacionPlayList()
     {
      JSONArray informacion = listarPlaylistId();
      JSONObject dato;
      PlayLists lista;
      
       try 
       {      
         for (int i = 0; i < informacion.length(); i++)
         {
             dato = informacion.getJSONObject(i);
             lista = new PlayLists(((JSONObject)dato.get("snippet")).get("publishedAt").toString(),
                                   ((JSONObject)dato.get("snippet")).get("title").toString(),
                                   ((JSONObject)dato.get("snippet")).get("description").toString());
             
             tablaHash.put(dato.get("id").toString(), lista);
             
         }
         return tablaHash;
       }
       catch (JSONException ex) 
       {
            
       }
     
       return null;
     }

     //---------- metodos get------------------------///
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

    public Map<String,PlayLists> getTablaHash()
    {
        return tablaHash;
    }
    //---------------------------------------------------------//    
}


