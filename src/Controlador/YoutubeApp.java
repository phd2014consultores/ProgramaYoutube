/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;
import Modelo.Comentarios;
import Modelo.PlayLists;
import Modelo.Video;
import java.util.Map;
import org.json.JSONObject;
/**
 *
 * @author David Botello
 */
public class YoutubeApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
//        Map<String,PlayLists> tablaHash = new PlayLists().listarInformacionPlayList();
//        
//        for (Map.Entry<String, PlayLists> entry:tablaHash.entrySet()) 
//        {
//         String key = entry.getKey();
//         PlayLists value = entry.getValue();
//         System.out.println("id lista de Reproduccion "+key);
//             System.out.println("Fecha de publicacion "+value.getFecha());
//             System.out.println("Titulo "+value.getTitulo());
//             System.out.println("Descripcion "+value.getDescripcion());    
//        }
        
//        Map<String,Video> tablaHash = new Video().listarInformacionVideo("PLfqTGfx-MToOMn_NQvj1oUnrlY36ppupC");
//        
//        for (Map.Entry<String, Video> entry:tablaHash.entrySet()) 
//        {
//         String key = entry.getKey();
//         Video value = entry.getValue();
//         System.out.println("id Video "+key);
//             System.out.println("Fecha de publicacion "+value.getFecha());
//             System.out.println("Titulo "+value.getTitulo());
//             System.out.println("Descripcion "+value.getDescripcion()+"\n");    
//        }
        
        
        Map<String,Comentarios> tablaHash = new Comentarios().listarInformacionComentarios("oXigT_3psPw");
        int n = 0;
        for (Map.Entry<String, Comentarios> entry:tablaHash.entrySet()) 
        {
         String key = entry.getKey();
         Comentarios value = entry.getValue();
        // System.out.println("id "+key);
         System.out.println("Fecha de publicacion "+value.getFecha());
         System.out.println("Nombre "+value.getnombre());
         System.out.println("Comentario "+value.gettextoOriginal()+"\n");    
        
          n++;
        }
        System.out.println(n);
    }
    
}
