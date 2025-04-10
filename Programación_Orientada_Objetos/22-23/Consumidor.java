
/**
 * Clase hija de Cliente, principalmente calcula el precio total de los consumidores
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Consumidor extends Cliente
{
    // instance variables - replace the example below with your own

    /**
     * Constructor para el Consumidor, donde obtiene las propiedades del constructor padre
     */
    public Consumidor(String nombre, double distancia)
    {
        super(nombre,distancia);
    }
    
    /**
     * Funcion para calcular el precio total a pagar por el consumidor
     *
     * @return  Preciototal   El resultado de la fórmula
     */
    public void calcularPrecioTotal() //El peso tiene que ser como maximo 100 kg
    {
        Preciototal =  (((productoComprado.get(0).getPrecio()*0.15) + productoComprado.get(0).getPrecio()) + (productoComprado.get(0).getPrecio()*0.10)) * this.kg_comprados;
    }
    
    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el cliente, en este caso, los datos del consumidor
     *
     * @return     Una oración donde se juntan todos los datos disponibles del cliente
     */
    public String toString() {
        return super.toString() + " Consumidor " + nombreCliente;
    }
}
