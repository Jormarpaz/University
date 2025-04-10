package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class RecentlyPlayed implements RecentlyPlayedIF{
    
    private final int Max_t;
    private final ListIF<Integer> Lista_Can;
    
    public RecentlyPlayed(int Max_t) 
    {
        this.Max_t = Max_t;
        this.Lista_Can = new List<Integer>();
    }
    
    /* Devuelve los identificadores de las últimas canciones reproducidas en   */
    /* el orden inverso al que fueron reproducidas                             */
    /* @return  una lista con los identificadores de las últimas canciones     */
    /*          reproducidas (en el orden inverso al que se reprodujeron)      */
    @Override
    public ListIF<Integer> getContent() 
    {
        return Lista_Can;
    }

    /* Añade la última canción reproducida                                     */
    /* @param   -un entero con el identificador de la última canción           */
    /*          reproducida                                                    */
    /* @pos     -se añade el identificador a la estructura que almacena las    */
    /*          últimas canciones reproducidas, garantizándose que no se       */
    /*          almacenan más canciones que las marcadas por el valor máximo   */
    /*          permitido indicado en el constructor                           */
    @Override
    public void addTune(int tuneID) 
    {
        // Si la lista de canciones reproducidas alcanza su tamaño máximo, se elimina la canción más antigua
        if (Lista_Can.size() >= Max_t && Max_t != 0) {
            //if(!Lista_Can.isEmpty()){
              Lista_Can.remove(Lista_Can.size()-1);  
              Lista_Can.insert(1, tuneID);
            //}
        } else if (Max_t != 0) {
            Lista_Can.insert(1, tuneID);
        }
    }
    
}
