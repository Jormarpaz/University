package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class PlayListManager implements PlayListManagerIF{
    
    private ListIF<PlayListNuevo> PLM;
    
    public PlayListManager() {
        this.PLM = new List<>();
    }
    
    /* Comprueba si existe una lista de reproducción dado su identificador     */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción que queremos saber si existe o no        */
    /* @return  -un valor booleano indicando si existe o no una lista de       */
    /*          reproducción asociada al identificador recibido como parámetro */
    @Override
    public boolean contains(String playListID) 
    {
         IteratorIF<PlayListNuevo> iterator = PLM.iterator();
         while(iterator.hasNext()){
             if(iterator.getNext().getId().equals(playListID)){
                 return true;
             }
         } return false;
    }
    
    /* Devuelve la lista de reproducción asociada a un identificador           */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción que queremos recuperar                   */
    /* @pre     -existe una lista de reproducción asociada al identificador    */
    /*          que se recibe como parámetro                                   */
    /* @return  -la lista de reproducción asociada al identificador recibido   */
    /*          como parámetro                                                 */
    @Override
    public PlayListIF getPlayList(String playListID)
    {
       IteratorIF<PlayListNuevo> iterator = PLM.iterator();
         while(iterator.hasNext()){
             PlayListNuevo playlist = iterator.getNext();
             if(playlist.getId().equals(playListID)){
                 return playlist.getPlayList();
             }
         }
        return new PlayList();
    }
    
    /* Devuelve una lista con todos los identificadores de las listas de       */
    /* reproducción existentes                                                 */
    /* @return  -una lista de cadenas de caracteres (todas no vacías) que son  */
    /*          los identificadores de todas las listas de reproducción        */
    /*          existentes                                                     */
    @Override
    public ListIF<String> getIDs()
    {
        IteratorIF<PlayListNuevo> iterator = PLM.iterator();
        ListIF<String> ids = new List<>();
         while(iterator.hasNext()){
                 ids.insert(ids.size() + 1, iterator.getNext().getId());
         }
        return ids;
        
    }
    
    /* Crea una nueva lista de reproducción vacía y la asocia a un nuevo       */
    /* identificador                                                           */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción que queremos crear                       */
    /* @pre     -no existe ninguna lista de reproducción asociada al           */
    /*          identificador recibido como parámetro                          */
    @Override
    public void createPlayList(String playListID) 
    {
        PlayListNuevo playlist = new PlayListNuevo(playListID,new PlayList());
        PLM.insert(PLM.size() + 1, playlist);
    }
 
    /* Elimina una lista de reproducción asociada a un identificador           */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción que queremos eliminar                    */
    /* @pre     -existe una lista de reproducción asociada al identificador    */
    /*          recibido como parámetro                                        */
    @Override
    public void removePlayList(String playListID) 
    { //Quedaria mejor con un iterador que tendria un coste asintotico menor pero el for me aporta un mayor control
        for(int x = 1; x<=PLM.size();x++){
            PlayListNuevo playlist = PLM.get(x);
            if(playlist.getId().equals(playListID)){
                 PLM.remove(x);
                 return;
             }
        }
    }
    
    /*Clase creada para dar lugar a una estructura similar a un HashMap donde  */
    /*alinear ids con playlists                                                */
    private class PlayListNuevo {
    
    private String id;
    private PlayList playlist;
    
    private PlayListNuevo(String id, PlayList playlist){
        this.id = id;
        this.playlist = playlist;
    }
    
    private String getId(){
        return id;
    }
    
    private PlayList getPlayList(){
        return playlist;
    }
    
    private void setId(String id){
        this.id = id;
    }
    
    private void setPlayList(PlayList playlist){
        this.playlist = playlist;
    }
      
    }    
}
