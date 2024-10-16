package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.Queue;
import es.uned.lsi.eped.DataStructures.QueueIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;

public class PlayBackQueue implements PlayBackQueueIF {
   
    private QueueIF<Integer> queue;
    
    public PlayBackQueue() 
    {
        this.queue = new Queue<Integer>();
    }
    
    /* Devuelve una lista con los identificadores de las canciones contenidas  */
    /* en la cola de reproducción                                              */
    /* @return  -una lista de enteros con los identificadores de las canciones */
    /*          que están en la cola de reproducción, conservando el orden en  */
    /*          el que fueron originalmente introducidos                       */
    @Override
    public ListIF<Integer> getContent() 
    {
        ListIF<Integer> lista = new List<>();
        QueueIF<Integer> copia = new Queue((Queue) this.queue); 
        while (!copia.isEmpty()) {
            // Mientras la cola no esté vacía, extraemos el primer elemento de la cola y lo agregamos a la lista
            int element = copia.getFirst();
            lista.insert(lista.size() + 1, element);
            copia.dequeue();
        }
        return lista;
    }
    
    /* Devuelve un booleano indicando si la cola de reproducción es vacía o no */
    /* @return  -devuelve un valor booleano que indica si la cola de           */
    /*          reproducción está vacía o no                                   */
    @Override
    public boolean isEmpty() 
    {
        return this.queue.isEmpty();
    }
    
    /* Devuelve un entero con el identificador de la primera canción que está  */
    /* en la cola de reproducción                                              */
    /* @pre     -la cola de reproducción no está vacía                         */
    /* @return  -devuelve el identificador de la primera canción en la cola de */
    /*          reproducción                                                   */
    @Override
    public int getFirstTune() 
    {
        return this.queue.getFirst();
    }
    
    /* Extrae la primera canción que se encuentre en la cola de reproducción   */
    /* @pre     -la cola de reproducción no está vacía                         */
    /* @pos     -elimina de la cola de reproducción el primer identificador    */
    @Override
    public void extractFirstTune() 
    {
        if (!this.queue.isEmpty()) {
        this.queue.dequeue();
        }
    }
    
    /* Añade una lista de identificadores de canciones a la cola de            */
    /* reproducción                                                            */
    /* @param   -una lista de enteros con los identificadores de las canciones */
    /*          que se desea añadir a la lista de reproducción                 */
    /* @pre     -todos los elementos de la lista son identificadores de        */
    /*          canciones que existen dentro del repositorio                   */
    /* @pos     -añade todos los identificadores presentes en la lista al      */
    /*          final de la cola de reproducción                               */
    @Override
    public void addTunes(ListIF<Integer> lT) 
    {
        IteratorIF<Integer> iterator = lT.iterator();
        while(iterator.hasNext()){
            this.queue.enqueue(iterator.getNext());
        }
    }
    
    /* Vacía el contenido de la cola de reproducción                           */
    /* @pos     -la cola de reproducción queda vacía, sin identificadores      */
    @Override
    public void clear() 
    {
        this.queue.clear();
    }
    
}
