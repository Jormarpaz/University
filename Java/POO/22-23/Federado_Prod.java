import java.util.*;
/**
 * La clase Federados Productores es una clase hija de Productores y aqui se comprueba si se puede añadir un producto al federado productor sin pasarse de las hectareas
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Federado_Prod extends Productores
{   
    //Se inicializan las variables y ArrayList de productores    
    public ArrayList<Pequeños_Prod> productores;
    protected double hectareasmax;

    /**
     * Constructor para Federados Productores, se piden las características que incorporará a futuro a la union de productores y a mayores se establece el maximo de hectareas
     */
    public Federado_Prod(String nombre,int hectareas_trabajadas, Producto producto)
    {
        //Asignamos lo que recibimos a cada caracteristica y se crea el arraylist de productores pqueños
        super(nombre);
        this.productores = new ArrayList<>();
        this.hectareasmax = 5;
    }

    /**
     * Funcion vacia para saber si el productor pequeño puede agregarse a la lista de la federación sin pasarse de las hectareas y sin repetirse
     *
     * @param  Pequeños_Prod,hectareas  El pequeño productor y las hectareas que se tendrán en cuenta al comprobar el maximo o si se repite
     * 
     */
    public void agregarPequenoProd(Pequeños_Prod p,double hectareas) {
        if (isComprobar(hectareas)) {
            if (!this.productores.contains(p)) {
                this.productores.add(p);
            } else {
                System.out.println("El pequeño productor ya está en la agrupación");
            } 
        } else {
            System.out.println("La suma de las hectáreas supera el límite permitido");
        }
    }

    /**
     * Funcion booleana para saber si el productor puede agregar el producto a su lista sin pasarse de las hectareas o cantidad de productos
     *
     * @param  hectareas  Las hectareas que se tendrán en cuenta al comprobar el maximo
     * @return boolean Para saber el resultado de la funcion, si puede o no añadirse el producto
     */
    public boolean isComprobar(double hectareas)
    {
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
     * En la función toString se crea una muestra de lo que aparecerá al pedir el productor federado, en este caso, los datos del productor más el tipo al que pertenece
     *
     * @return     Una oración donde se juntan todos los datos disponibles del productor
     */
    public String toString() {
        return super.toString() + " Tipo : Productora Federada";
    }
}
