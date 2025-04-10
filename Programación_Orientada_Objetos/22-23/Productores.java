import java.util.*;
/**
 * Clase padre de los productores, alberga algunas funciones importantes como la de asignarle un producto al productor o comprobar si existe el producto en el productor
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public abstract class Productores
{
    //Declaramos las variables de las caracteristicas de los productores asi como el HashMap con el que le asignaremos productos al productor.
    protected HashMap<Producto,Double> productosAsignados;
    public ArrayList<Double> hectareasobtenidas;

    protected int cant_max = 5;
    private String nombre;

    /**
     * Constructor de la clase Productores, se piden las características que incorporará a futuro el productor
     */
    public Productores(String nombre)
    {
        //Asignamos lo que recibimos a cada caracteristica y creamos el hashmap
        this.nombre = nombre;
        this.productosAsignados = new HashMap<>();
        this.hectareasobtenidas = new ArrayList<>();
    }

    /**
     * Funcion void para asignarle un producto al productor junto a la cantidad de hectareas que ocupa
     *
     * @param  producto  El producto que introducimos en el mapa
     * @param  hectareas Para saber las hectareas que ocupará el producto
     * 
     */
    public void AñadirProducto(Producto producto, Double hectareas)
    {   
        //Introducimos en el HashMap los datos
        productosAsignados.put(producto,hectareas);
    }
    
    /**
     * Funcion void para la cantidad de hectareas que ocupa el producto
     */
    public void getHectareas(Producto producto)
    {   
        //Recorremos el Hashmap y extraemos las hectareas
        for(HashMap.Entry<Producto,Double> p : productosAsignados.entrySet()){
            if(p.getKey().equals(producto)){
                hectareasobtenidas.add(p.getValue());
            }
        }
    }
    
    /**
     * Funcion booleana para saber si el productor cuenta con el producto que se le pide
     *
     * @param  producto  El producto que introducimos en el mapa
     * @return boolean Para saber el resultado de la funcion, si hay o no ese producto
     */
    public boolean isProducto(Producto producto)
    {
        boolean demostrar = false;
        //Se le hace un for al Mapa y se introduce un if para comprobar si existe el producto
        for(HashMap.Entry<Producto,Double> p : productosAsignados.entrySet()){
            if (p.getKey().equals(producto)){
                demostrar = true;
            } 
        }

        if (demostrar){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Un getter para obtener el nombre del productor
     *
     * @return nombre Al ser un getter te devuelve el valor de la funcion 
     * 
     */
    public String getNombre()
    {
        //Devuelve el nombre del productor
        return this.nombre;
    }

    /**
     * Un setter para darle un nuevo nombre al productor
     *
     * @param Nombre Para saber el nombre que se le quiere dar al productor 
     * 
     */
    public void setNombre(String Nombre)
    {
        // Nombre a darle al productor
        this.nombre=Nombre;
    }

    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el productor, en este caso, los datos del productor
     *
     * @return     Una oración donde se juntan todos los datos disponibles del productor
     */
    public String toString() {
        return "Nombre : " + nombre ;
    }

}
