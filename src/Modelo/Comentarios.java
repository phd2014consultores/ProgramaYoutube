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
    
    //------------------Atributos de la clase------------------//
        private YouTube youtube;
        private String id;
        private String fecha;
        private String nombre;
        private String textoOriginal;
        private boolean paginacion;
        private String codigoPagina = "";
        private Map<String,Comentarios> tablaHash = new Hashtable<String,Comentarios>();
     //----------------------------------------------------------------//   
        
        
     //----------------------------Contructores de la clase-----------------------//   
        public Comentarios(String fecha, String nombre, String textoOriginal) 
        {
            this.fecha = fecha;
            this.nombre = nombre;
            this.textoOriginal = textoOriginal;
        }

        public Comentarios()
        { 
           try 
            {
              youtube = conexionAuth.comandos();
            } 
              catch (IOException ex) {
               
            }
         }
       //------------------------------------------------//
        
        
         /**
         * Metodo que se encarga de listar los de id de cada comentario creado sobre video publicado
         * se tiene el parametro paginacion ya que youtube divide todos los comentarios del video 
         * en diferentes paginas, el maximo numero de comentarios por pagina son de 100, y el minimo por 
         * defecto es de 20
         * @param String PlayListId
         * @param boolean paginacion
         * @return JSONArray
         */
        
        public JSONArray listarComentariosId(String VideoId,boolean paginacion)
        {
            CommentThreadListResponse comentariosResultado = null;
            try 
            {
              YouTube.CommentThreads.List listaComentario = youtube.commentThreads().list("snippet");
             
              listaComentario.setVideoId(VideoId);
              listaComentario.setOrder("time");
              listaComentario.setMaxResults(Long.valueOf(100));
              
              //condicion que permite insertar el id de las proxima de pagina donde se encuentra los 
              //comentarios del mismo video
              if(this.paginacion)
                listaComentario.setPageToken(this.codigoPagina);
             
              comentariosResultado = listaComentario.execute();
              
              if(comentariosResultado.get("nextPageToken")==null)
              {
                    this.paginacion = false;
                    this.codigoPagina = "";
                    System.out.println("Ya no hay mas paginas.......");
              }
              else
              {
                  this.codigoPagina = comentariosResultado.get("nextPageToken").toString();
                  this.paginacion = true;
             
              }
              
           //   System.out.println(this.codigoPagina);
              return new JSONArray(comentariosResultado.get("items").toString());

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
         * PlayList con los atributos fecha, nombre del usuario, coemtario posteado
         * @param String VideoId
         * @return Map<String,Comentarios> tabla hash
         */
     public Map<String,Comentarios> listarInformacionComentarios(String VideoId)
     {
      JSONArray informacion = listarComentariosId(VideoId,paginacion);
      JSONObject dato;
      Comentarios lista;
      
       try 
       {      
         for (int i = 0; i < informacion.length(); i++)
         {
             dato = informacion.getJSONObject(i);
          //   System.out.println(dato);
             lista = new Comentarios(((JSONObject)((JSONObject)((JSONObject)dato.get("snippet")).get("topLevelComment")).get("snippet")).get("publishedAt").toString(),
                                     ((JSONObject)((JSONObject)((JSONObject)dato.get("snippet")).get("topLevelComment")).get("snippet")).get("authorDisplayName").toString(),
                                     ((JSONObject)((JSONObject)((JSONObject)dato.get("snippet")).get("topLevelComment")).get("snippet")).get("textOriginal").toString());
                               
            tablaHash.put(((JSONObject)((JSONObject)dato.get("snippet")).get("topLevelComment")).get("id").toString(), lista);
              
         }
         
         if(paginacion)
         { 
           //  System.out.println("Veces "+(n++));
             listarInformacionComentarios(VideoId);
         }
         
         return tablaHash;
       }
       catch (JSONException ex) 
       {
            System.out.println("Entro aqui......"+ex);
            if(paginacion)
            { 
           //  System.out.println("Veces "+(n++));
              listarInformacionComentarios(VideoId);
            }
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

    public String getnombre() 
    {
        return nombre;
    }

    public String gettextoOriginal() 
    {
        return textoOriginal;
    }

    public Map<String,Comentarios> getTablaHash()
    {
        return tablaHash;
    }  
   //---------------------------------------------------// 
    
    
}//fin de la clase
