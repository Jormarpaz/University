
/**
 * Clase Transporte, se crean los diversos transportes bien de pequeña o gran logistica que se asignaran a las empresas
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Transporte
{
    //Se inicializan las variables
    private String nombre;
    private double precio;
    private boolean GranLog;

    /**
     * Constructor para Transporte, se piden las características que incorporará a futuro el transporte logistico
     */
    public Transporte(String nombre,Double precio,Boolean tipo)
    {
        this.nombre=nombre;
        this.precio=precio;
        this.GranLog=tipo;
    }

    /**
     * Funcion getter para obtener el precio del transporte
     * @return precio Se devuelve el precio
     */
    public double getPrecio()
    {
        return precio;
    }

    /**
     * Funcion getter para obtener el tipo de transporte logistico
     * @return GranLog Se devuelve saber el tipo
     */
    public boolean getTipo()
    {
        return GranLog;
    }

    /**
     * Un getter para obtener el nombre del transporte
     *
     * @return nombre Al ser un getter te devuelve el valor de la funcion 
     * 
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * Un setter para darle un nuevo nombre al transporte
     *
     * @param Nombre Para saber el nombre que se le quiere dar al transporte
     * 
     */
    public void SetNombre(String nombre)
    {
        //Se cambia el nombre
        this.nombre = nombre;
    }
    
    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el transporte, en este caso, los datos del mismo
     *
     * @return     Una oración donde se juntan todos los datos disponibles del transporte
     */
    public String toString()
    {
        String s = "";
        if(GranLog){
            s="Gran Logistica : ";
        } else {
            s="Pequeña Logistica : ";
        }

        return s + this.nombre + " Precio : " + precio + " € " ;
    }
}
