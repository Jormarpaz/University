/**
 * La clase Grandes Productores es una clase hija de Productores y aqui no hay limite de productos o hectareas
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Grandes_Prod extends Productores
{
    //No existen variables unicas de esta clase

    /**
     * Constructor para Grandes Productores, se piden las características que incorporará a futuro el productor
     */
    public Grandes_Prod(String nombre)
    {
        //Se piden los datos que recibe el padre
         super(nombre);
    }

    /**
     * Un getter para obtener la cantidad maxima de productos del productor
     *
     * @return cant_max Al ser un getter te devuelve el valor de la funcion 
     * 
     */
    public int getCantidadProductosMax()
    {
         //Se devuelve el valor actual de cantidad maxima de productos
         return cant_max;
    }

    /**
     * Un setter para darle un nuevo valor a la cantidad maxima de productos
     *
     * @param hectareas Para cambiarle la cantidad maxima de productos al productor 
     * 
     */
    public void setCantidadProductosMax(int cantidad_productos)
    {
        //Se introduce la cantidad nueva de productos
        this.cant_max = cantidad_productos;
    }

    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el productor pequeño, en este caso, los datos del productor más el tipo al que pertenece
     *
     * @return     Una oración donde se juntan todos los datos disponibles del productor
     */
    public String toString() {

        return super.toString() + " Tipo : Gran Productor";
    }
}
