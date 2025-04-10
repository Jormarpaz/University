package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class PlayList implements PlayListIF {

    private ListIF<Integer> IDs;

    // Constructor para crear una nueva lista de reproducción
    public PlayList() {
        // Inicializamos la lista de identificadores de canciones
        this.IDs = new List<Integer>();
    }
    
    /* Devuelve la lista de identificadores de canciones de la lista de        */
    /* reproducción                                                            */
    /* @return  -una lista de enteros con los identificadores de las canciones */
    /*          contenidas en la lista de reproducción                         */
    @Override
    public ListIF<Integer> getPlayList() {
        List<Integer> c = new List<>();
        IteratorIF<Integer> iterator = IDs.iterator();
        while (iterator.hasNext()) {
            c.insert(c.size() + 1, iterator.getNext());
        }
        return c;
    }
    
    /* Añade una lista de identificadores de canciones a la lista de           */
    /* reproducción                                                            */
    /* @param   -una lista de enteros con los identificadores de las canciones */
    /*          que se quiere añadir a la lista de reproducción                */
    /* @pre     -todos los elementos de la lista son identificadores de        */
    /*          canciones que existen dentro del repositorio                   */
    /* @pos     -el contenido de la lista recibida como parámetro se concatena */
    /*          al contenido existente en la lista de reproducción             */
    @Override
    public void addListOfTunes(ListIF<Integer> lT) {
        // Iteramos sobre la lista de identificadores de canciones y los añadimos a la lista de reproducción
        for (int i = 1; i <= lT.size(); i++) {
            this.IDs.insert(this.IDs.size() + 1, lT.get(i));
        }
    }
    /* Elimina todas las apariciones de un identificador de canción de la      */
    /* lista de reproducción                                                   */
    /* @param   -un entero representando el identificador de la canción que se */
    /*          desea eliminar de la lista de reproducción                     */
    /* @pos     -del contenido de la lista de reproducción se han eliminado    */
    /*          todas las apariciones del identificador recibido como          */
    /*          parámetro. El resto de identificadores conserva su orden       */
    /*          relativo                                                       */
    @Override
    public void removeTune(int tuneID) {
        IteratorIF<Integer> iterator = IDs.iterator();
        int pos = 1;
        while (iterator.hasNext()) {
            int id = iterator.getNext();
            if(id==tuneID){
                this.IDs.remove(pos);
                pos--;
            }
            pos++;
        }
    }
}
