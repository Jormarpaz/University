package es.uned.lsi.eped.pract2023_2024;

import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class Player implements PlayerIF {

    private final TuneCollectionIF TC;
    private final PlayListManagerIF PLM;
    private final PlayBackQueueIF PBQ;
    private final RecentlyPlayedIF RP;

    
    public Player(TuneCollectionIF T, int maxRecentlyPlayed) {
        this.TC = T;
        this.PLM = new PlayListManager();
        this.PBQ = new PlayBackQueue();
        this.RP = new RecentlyPlayed(maxRecentlyPlayed);
    }
    
    /* Devuelve los identificadores de todas las listas de reproducción        */
    /* existentes                                                              */
    /* @returns -una lista con los identificadores de todas las listas de      */
    /*          reproducción (no importa el orden)                             */
    @Override
    public ListIF<String> getPlayListIDs() {
        return PLM.getIDs();
    }
    
    /* Devuelve el contenido de una lista de reproducción                      */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción de la que se quiere obtener su contenido */
    /* @return  -si en el reproductor existe una lista de reproducción con ese */
    /*          identificador, se devolverá una lista con su contenido         */
    /*          -en caso contrario, se devolverá una lista vacía               */
    @Override
    public ListIF<Integer> getPlayListContent(String playListID) { 
        PlayListIF playlist = PLM.getPlayList(playListID);
        if (!playlist.getPlayList().isEmpty()){
            return playlist.getPlayList();
        } else {
            return new List<Integer>();
        } 
    }
    
    /* Devuelve los identificadores de las canciones contenidas en la cola de  */
    /* reproducción                                                            */
    /* @return  una lista con los identificadores de las canciones que están   */
    /*          en la cola de reproducción (ha de conservar el orden en el que */
    /*          se introdujeron las canciones)                                 */
    @Override
    public ListIF<Integer> getPlayBackQueue() {
        return PBQ.getContent();
    }
    
    /* Devuelve los identificadores de las últimas canciones reproducidas que  */
    /* están almacenadas en RecentlyPlayed                                     */
    /* @return  una lista con los identificadores de las últimas canciones     */
    /*          reproducidas (en el orden inverso al que se reprodujeron)      */
    @Override
    public ListIF<Integer> getRecentlyPlayed() {
        return RP.getContent();
    }

    /* Crea una nueva lista de reproducción a partir de su identificador       */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          nueva lista de reproducción                                    */
    /* @pos     -si no existe una lista de reproducción con ese identificador, */
    /*          se crea                                                        */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void createPlayList(String playListID) {
        if (!PLM.contains(playListID)) {
            PLM.createPlayList(playListID);
        } 
    }

    /* Elimina una lista de reproducción del reproductor a partir de su        */
    /* identificador                                                           */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción a eliminar                               */
    /* @pos     -si existe una lista de reproducción con ese identificador, se */
    /*          elimina                                                        */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void removePlayList(String playListID) {
        if (PLM.contains(playListID)) {
            PLM.removePlayList(playListID);
        } 
    }
    
    /* Añade una lista de identificadores de canciones del repositorio a una   */
    /* lista de reproducción                                                   */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción a la que se van a añadir las canciones   */
    /*          -una lista de identificadores de canciones contenidas en el    */
    /*          repositorio                                                    */
    /* @pre     -todos los elementos de la lista son identificadores de        */
    /*          canciones que existen dentro del repositorio                   */
    /* @pos     -si existe una lista de reproducción con ese identificador, se */
    /*          añaden a ella los identificadores contenidos en la lista       */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void addListOfTunesToPlayList(String playListID, ListIF<Integer> lT) {
        if (PLM.contains(playListID)) {
            PlayListIF PL = PLM.getPlayList(playListID);
            PL.addListOfTunes(lT);
        } 
    }
    
    /* Añade los identificadores de todas las canciones del repositorio que    */
    /* cumplan los criterios indicados a una lista de reproducción             */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción a la que se van a añadir las canciones   */
    /*          -una cadena de caracteres con el título de la canción buscada  */
    /*          -una cadena de caracteres con el autor de la canción buscada   */
    /*          -una cadena de caracteres con el género de la canción buscada  */
    /*          -una cadena de caracteres con el álbum al que pertenece la     */
    /*          canción buscada                                                */
    /*          -un entero con el primer año del intervalo en el que se        */
    /*          publicó la canción a buscar                                    */
    /*          -un entero con el último año del intervalo en el que se        */
    /*          publicó la canción a buscar                                    */
    /*          -un entero con la duración mínima de la canción a buscar       */
    /*          -un entero con la duración máxima de la canción a buscar       */
    /* @pos     -si existe una lista de reproducción con ese identificador, se */
    /*          añaden a ella los identificadores de todas las canciones del   */
    /*          repositorio que cumplan todos los criterios indicados          */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void addSearchToPlayList(String playListID, String t, String a, String g, String al, int min_y, int max_y, int min_d, int max_d) {
        if (PLM.contains(playListID)) {
            QueryIF requisitos = new Query(t, a, g, al, min_y, max_y, min_d, max_d);
            ListIF<Integer> lT = new List<>();
            PlayListIF PL = PLM.getPlayList(playListID);
            for (int i = 0; i < TC.size(); i++) {
                if (TC.getTune(i).match(requisitos)) {
                    lT.insert(lT.size() + 1, i);
                }
            }
            PL.addListOfTunes(lT);
        }
    }

    /* Elimina una canción de una lista de reproducción                        */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción de la que se quiere eliminar la canción  */
    /*          -un entero con el identificador de la canción del repositorio  */
    /*          que se quiere eliminar de dicha lista                          */
    /* @pos     -si existe una lista de reproducción con se identificador, se  */
    /*          elimina de dicha lista todas las apariciones del identificador */
    /*          de la canción del repositorio pasada como parámetro            */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void removeTuneFromPlayList(String playListID, int tuneID) {

        if (PLM.contains(playListID)) {
            PlayListIF PL = PLM.getPlayList(playListID);
                if(PL.getPlayList().contains(tuneID))
                {
                    PL.removeTune(tuneID);
                } 
        }
    }
    
    /* Añade una lista de identificadores de canciones del repositorio a la    */
    /* cola de reproducción                                                    */
    /* @param   -una lista de identificadores de canciones contenidas en el    */
    /*          repositorio
    /* @pre     -todos los elementos de la lista son identificadores de        */
    /*          canciones que existen dentro del repositorio                   */
    /* @pos     se añaden a la cola de reproducción los identificadores de las */
    /*          canciones contenidos en la lista                               */
    @Override
    public void addListOfTunesToPlayBackQueue(ListIF<Integer> lT) {

        PBQ.addTunes(lT);

    }
    
    /* Añade los identificadores de todas las canciones del repositorio que    */
    /* cumplan los criterios indicados a la cola de reproducción               */
    /* @param   -una cadena de caracteres con el título de la canción buscada  */
    /*          -una cadena de caracteres con el autor de la canción buscada   */
    /*          -una cadena de caracteres con el género de la canción buscada  */
    /*          -una cadena de caracteres con el álbum al que pertenece la     */
    /*          canción buscada                                                */
    /*          -un entero con el primer año del intervalo en el que se creó   */
    /*          la canción a buscar                                            */
    /*          -un entero con el último año del intervalo en el que se creó   */
    /*          la canción a buscar                                            */
    /*          -un entero con la duración mínima de la canción a buscar       */
    /*          -un entero con la duración máxima de la canción a buscar       */
    /* @pos     se añaden a la cola de reproducción los identificadores de     */
    /*          todas las canciones del repositorio que cumplan todos los      */
    /*          criterios indicados                                            */
    @Override
    public void addSearchToPlayBackQueue(String t, String a, String g, String al, int min_y, int max_y, int min_d, int max_d) {
        QueryIF requisitos = new Query(t, a, g, al, min_y, max_y, min_d, max_d);
        ListIF<Integer> lT = new List<>();
        for (int i = 0; i < TC.size(); i++) {
            if (TC.getTune(i).match(requisitos)) {
                lT.insert(lT.size() + 1, i);
            }
        }
        PBQ.addTunes(lT);
    }
    
    /* Añade el contenido de una lista de reproducción a la cola de            */
    /* reproducción                                                            */
    /* @param   -una cadena de caracteres no vacía con el identificador de la  */
    /*          lista de reproducción cuyo contenido se desea añadir a la cola */
    /*          de reproducción                                                */
    /* @pos     -si existe una lista de reproducción con se identificador, se  */
    /*          añade su contenido a la cola de reproducción                   */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void addPlayListToPlayBackQueue(String playListID) 
    {
        if (PLM.contains(playListID)) {
            PlayListIF PL = PLM.getPlayList(playListID);
            ListIF<Integer> lT = PL.getPlayList();
            PBQ.addTunes(lT);
        }   
    }
    
    /* Vacía la cola de reproducción                                           */
    /* @pos     -la cola de reproducción se vacía                              */
    @Override
    public void clearPlayBackQueue() {
        
        PBQ.clear();

    }
    
    /* Reproduce la siguiente canción en la cola de reproducción               */
    /* @pos     -si la cola de reproducción no es vacía, se elimina de ella el */
    /*          primer elemento, pasando éste a la estructura que almacena las */
    /*          últimas canciones reproducidas, sin sobrepasar su tamaño       */
    /*          máximo                                                         */
    /*          -en caso contrario, no se hace nada                            */
    @Override
    public void play() {
        
        // Verificar si la cola de reproducción no está vacía
        if (!PBQ.isEmpty()) {
        // Extraer la primera canción de la cola de reproducción y reproducirla
        int siguiente = PBQ.getFirstTune();
        PBQ.extractFirstTune();
        
        // Guardar la canción reproducida en el almacén de últimas canciones reproducidas
        RP.addTune(siguiente);
        } 
    }
}
