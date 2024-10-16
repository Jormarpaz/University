package es.uned.lsi.eped.pract2023_2024;

public class Tune implements TuneIF {
    
    private String t;
    private String a;
    private String g;
    private String al;
    private int y;
    private int d;
    
    
    /* Constructor de la clase que implementa TuneIF */
    /* @param -una cadena de caracteres con el título de la canción */
    /* -una cadena de caracteres con el autor de la canción */
    /* -una cadena de caracteres con el género de la canción */
    /* -una cadena de caracteres con el álbum al que pertenece */
    /* -un entero con el año de publicación de la canción */
    /* -un entero con la duración en segundos de la canción */
    /* @pre -t != "" && a != "" && g != "" && al != "" && y > 0 && d > 0 */
    public Tune(String t, String a, String g, String al, int y, int d)
    {
        this.t=t;
        this.a=a;
        this.g=g;
        this.al=al;
        this.y=y;
        this.d=d;
    }
    
    /* Dado un objeto QueryIF conteniendo unos criterios de búsqueda, devuelve */
    /* un valor de verdad indicando si la canción los cumple o no los cumple   */
    /* @param   -un objeto QueryIF con unos criterios de búsqueda              */
    /* @return  -si la canción cumple TODOS los criterios, devolverá verdadero */
    /*          -si la canción incumple algún criterio, devolverá falso        */
    public boolean match(QueryIF q)
    {
        if (!q.getTitle().isEmpty() && !t.equalsIgnoreCase((String) q.getTitle())) {
            return false;
        }
        if (!q.getAuthor().isEmpty() && !a.equalsIgnoreCase((String) q.getAuthor())) {
            return false;
        }
        if (!q.getGenre().isEmpty() && !g.equalsIgnoreCase((String) q.getGenre())) {
            return false;
        }
        if (!q.getAlbum().isEmpty() && !al.equalsIgnoreCase((String) q.getAlbum())) {
            return false;
        }
        if (q.getMin_year() != -1 && y < q.getMin_year()) {
            return false;
        }
        if (q.getMax_year() != -1 && y > q.getMax_year()) {
            return false;
        }
        if (q.getMin_duration() != -1 && d < q.getMin_duration()) {
            return false;
        }
        if (q.getMax_duration() != -1 && d > q.getMax_duration()) {
            return false;
        }
        return true;  
    }
    
    
}
