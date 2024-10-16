import java.util.*;
/**
 * La clase Pequeños Productores es una clase hija de Productores y aqui se comprueba si se puede añadir un producto al pequeño productor sin pasarse de las hectareas
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Pequeños_Prod extends Productores
{
    //No hay muchas variables mas alla de las hectareas maximas que puede tener el pequeño productor
    protected double hectareasmax;
    /**
     * Constructor para Pequeños Productores, se piden las características que incorporará a futuro el productor y a mayores se establece el maximo de hectareas
     */
    public Pequeños_Prod(String nombre)
    {
        //Asignamos lo que recibimos a cada caracteristica
        super(nombre);
        this.hectareasmax = 5;
    }

    /**
     * Un getter para obtener las hectareas del productor
     *
     * @return hectareasmax Al ser un getter te devuelve el valor de la funcion 
     * 
     */
    public double getHectareasMax()
    {
        //Devuelve las hectareas maximas del producto
        return hectareasmax;
    }

    /**
     * Un setter para darle un nuevo valor a las hectareas maximas
     *
     * @param hectareas Para cambiarle las hectareas maximas al productor 
     * 
     */
    public void setHectareasMax(double hectareas)
    {
        //Se  da una nueva cantidad de hectareas maximas al productor
        this.hectareasmax = hectareas;
    }

    /**
     * Funcion booleana para saber si el productor puede agregar el producto a su lista sin pasarse de las hectareas o cantidad de productos
     *
     * @param  hectareas  Las hectareas que se tendrán en cuenta al comprobar el maximo
     * @return boolean Para saber el resultado de la funcion, si puede o no añadirse el producto
     */
    public boolean isComprobar(Double hectareas)
    {
        //Funcion para comprobar si puede añadirse el producto dependiendo de las hectareas
        int x = 0;
        double sum = 0.0;
        for (HashMap.Entry<Producto,Double> p : productosAsignados.entrySet()){
            x++;
            sum = sum + p.getValue();
        }
        if (x >= cant_max){
            return false;
        } else {
            if ((sum + hectareas) <= hectareasmax){
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Un setter para darle un nuevo valor a la cantidad maxima de productos
     *
     * @param cantidad_productos Para cambiarle la cantidad maxima de productos al productor 
     * 
     */
    public void setCantidadProductosMax(int cantidad_productos)
    {
        //Se da una nueva cantidad de productos maximos por productor
        this.cant_max = cantidad_productos;
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
     * En la función toString se crea una muestra de lo que aparecerá al pedir el productor pequeño, en este caso, los datos del productor más el tipo al que pertenece
     *
     * @return     Una oración donde se juntan todos los datos disponibles del productor
     */
    public String toString() {
        return super.toString() + " Tipo : Pequeño Productor";
    }
}
