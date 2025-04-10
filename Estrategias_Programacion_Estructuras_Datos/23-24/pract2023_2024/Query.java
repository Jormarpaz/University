package es.uned.lsi.eped.pract2023_2024;

public class Query implements QueryIF {
    
    private String t;
    private String a;
    private String g;
    private String al;
    private int Min_y;
    private int Max_y;
    private int Min_d;
    private int Max_d;
    
    public Query(String t, String a, String g, String al,int Min_y, int Max_y, int Min_d, int Max_d) 
    {
        this.t = t;
        this.a = a;
        this.g = g;
        this.al = al;
        this.Min_y = Min_y;
        this.Max_y = Max_y;
        this.Min_d = Min_d;
        this.Max_d = Max_d;
    }

  /* Devuelve el criterio título                                             */
  /* @return  -una cadena de caracteres con el título de la canción buscada  */
  public String getTitle()
  {
      return t;
  }

  /* Devuelve el criterio autor                                              */
  /* @return  -una cadena de caracteres con el autor de la canción buscada   */
  public String getAuthor()
  {
      return a;
  }

  /* Devuelve el criterio género                                             */
  /* @return  -una cadena de caracteres con el género de la canción buscada  */
  public String getGenre()
  {
      return g;
  }

  /* Devuelve el criterio álbum                                              */
  /* @return  --una cadena de caracteres con el álbum al que pertenece la    */
  /*          canción buscada                                                */
  public String getAlbum()
  {
      return al;
  }

  /* Devuelve el criterio año mínimo                                         */
  /* @return  -un entero con el primer año del intervalo en el que se grabó  */
  /*          la canción a buscar                                            */
  public int getMin_year()
  {
     return Min_y; 
  }

  /* Devuelve el criterio año máximo                                         */
  /* @return  -un entero con el último año del intervalo en el que se grabó  */
  /*          la canción a buscar                                            */
  public int getMax_year()
  {
      return Max_y;
  }

  /* Devuelve el criterio duración mínima                                    */
  /* @return  -un entero con la duración mínima de la canción a buscar       */
  public int getMin_duration()
  {
      return Min_d;
  }

  /* Devuelve el criterio duración máxima                                    */
  /* @return  -un entero con la duración máxima de la canción a buscar       */
  public int getMax_duration()
  {
      return Max_d;
  }
    
}
