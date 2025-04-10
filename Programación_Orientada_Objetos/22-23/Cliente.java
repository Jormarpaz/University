import java.util.*;
import java.time.*;

/**
 * Clase Cliente, alberga algunas funciones importantes como la de comprarProducto y múltiples getters o setters
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public abstract class Cliente
{
    //Se inicializan las variables y ArrayList
    protected ArrayList<Producto> productoComprado;
    protected ArrayList<LocalDate> fechacompra;

    protected String nombreCliente;
    public double distancia_cooperativa,kg_comprados,Preciototal; 
    protected boolean compra;
    

    /**
     * Constructor de Cliente, donde adquiere sus propiedades
     */
    public Cliente(String nombre, double distancia)
    {
        this.nombreCliente = nombre;
        this.distancia_cooperativa = distancia;
        this.productoComprado = new ArrayList<Producto>();
        this.fechacompra = new ArrayList<LocalDate>();

    }

    /**
     * Inicializacion de la funcion calcularPrecio introducida en sus herederas
     */
    public abstract void calcularPrecioTotal();

    /**
     * Funcion comprar Producto que añade los productos a la ArrayList de productos de Clientes
     *
     * @param  producto,kg   El producto referencia y la cantidad en kg
     */
    public void ComprarProducto(Producto producto,double kg)
    {
        //Si cojo producto (0) muestro fecha(0), crear array para la fecha, productoComprado.get(productoComprado.getsize()-1)
        productoComprado.add(producto);
        this.kg_comprados = kg;
        fechacompra.add(LocalDate.now());
        compra = true;
    }

    /**
     * Funcion getter para obtener el nombre
     * @return nombreCliente Se devuelve el nombre del cliente
     */
    public String getNombre()
    {
        return this.nombreCliente;
    }

    /**
     * Funcion getter para obtener la distancia hasta la cooperativa
     * @return distancia_cooperativa Se devuelve la distancia hasta la cooperativa
     */
    public double getDistancia()
    {
        return this.distancia_cooperativa;
    }

    /**
     * Un setter para darle un nuevo nombre al cliente
     *
     * @param nombreCliente Para saber el nombre que se le quiere dar al cliente
     * 
     */
    public void setNombre(String nombre)
    {
        this.nombreCliente = nombre;
    }

    /**
     * Un setter para darle una nueva distancia hasta la cooperativa
     *
     * @param distancia Para saber la distancia hasta la cooperativa nombre que se le quiere dar a la empresa
     * 
     */
    public void setDistanciaCooperativa(double distancia)
    {
        // put your code here
        this.distancia_cooperativa = distancia;
    }

    /**
     * Funcion getter para obtener los kilogramos comprados
     * 
     * @return kg_comprados Se devuelve la cantidad comprada en kg
     */
    public double getKilogramos()
    {
        // put your code here
        return this.kg_comprados;
    }

    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el cliente, en este caso, los datos del cliente
     *
     * @return     Una oración donde se juntan todos los datos disponibles del cliente
     */
    public String toString() {
        
        return " Cliente : ";
    }
}
