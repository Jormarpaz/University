/**
 * Clase Pedido, donde se muestra principalmente el resumen de pedidos
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Pedido
{
    private Producto producto;
    private Productores productores;
    private Cliente cliente;
    private Logistica logistica;
    
    /**
     * Constructor de Pedido, donde adquiere sus propiedades
     */
    public Pedido(Producto producto, Productores productor, Cliente cliente, Logistica logistica){
        this.producto = producto;
        this.productores = productor;
        this.cliente = cliente;
        this.logistica = logistica;
        
    }    
    
    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el pedido, en este caso, los datos de toda la compra
     *
     * @return     Una oración donde se juntan todos los datos disponibles de la compra
     */
    public String toString()
    {
        String tipocliente = (this.cliente instanceof Distribuidores)? " distribuidor " : " consumidor " ;
        String tipoproductor = (this.productores instanceof Pequeños_Prod)? " pequeño productor " : " gran productor " ;
        return "El " + tipocliente + cliente.getNombre() + ", que se encuentra a una distancia de " + cliente.getDistancia() + " km de la cooperativa, ha comprado " + cliente.getKilogramos() + 
        " kg de " + producto.getNombre() + " al" + tipoproductor + productores.getNombre() + " y ha usado la empresa de transportes " + logistica.GetNombre() + 
        ".\nEl precio total del envio se eleva a : ";
    }
    
}
