
/**
 * Clase hija de Cliente, principalmente calcula el precio total de los distribuidores
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Distribuidores extends Cliente
{

    /**
     * Constructor para Distribuidores, donde se recogen los datos del constructor padre
     */
    public Distribuidores(String nombre, double distancia)
    {
        super(nombre,distancia);
    }
    
    /**
     * Funcion para calcular el precio total a pagar por el distribuidor
     *
     * @return  Preciototal   El resultado de la fórmula
     */
    public void calcularPrecioTotal() //El peso tiene que ser mayor a 1 tonelada
    {
        Preciototal =  (productoComprado.get(0).getPrecio() + productoComprado.get(0).getPrecio()*0.05) * this.kg_comprados;
    }
    
    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el cliente, en este caso, los datos del cliente
     *
     * @return     Una oración donde se juntan todos los datos disponibles del cliente
     */
    public String toString() {
        return super.toString() + " Distribuidor " + nombreCliente;
    }
}
