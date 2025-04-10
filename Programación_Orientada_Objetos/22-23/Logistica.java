/**
 * Clase Logistica, alberga algunas funciones importantes como la de calcular el costo del envio logistico y las propias fórmulas para pequeña o gran logística
 * 
 * @author Jorge
 * @version *17Mayo*
 */
public class Logistica
{
    //Se inicializan múltiples variables que se corresponden con las propiedades de la empresa logistica, asi como la formula del envio
    protected double      precios,i,n;
    public double precioenvio;
    protected String      nombreEmpresa;
    protected Transporte  transporteP,transporteG;
    /**
     * Constructor para Logistica, se piden las características que incorporará a futuro la empresa logistica
     */
    public Logistica(String nombreEmpresa,Transporte empresapeq, Transporte empresagran)
    {
        //Asignamos lo que recibimos a cada caracteristica
        this.nombreEmpresa=nombreEmpresa;
        this.transporteP = empresapeq;
        this.transporteG = empresagran;
    }

    /**
     * Constructor sobrecargado para Logistica donde solo se le pide el nombre de la empresa por si se quieren añadir a posteriori los transportes
     */
    public Logistica(String nombreEmpresa)
    {
        //Asignamos lo que recibimos a cada caracteristica
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Funcion void para calcular el valor del envio teniendo en cuenta factores como la distancia, la cantidad, el transporte...
     *
     * @param  producto,cliente  El producto que compra el cliente y este mismo para saber la distancia que lo separa de la cooperativa
     * @return precioenvio El precio que queda después de todos los cálculos
     */
    public void Envio(Producto producto,Cliente cliente)
    {   
        //Este primer if es para producto perecedero
        if(producto.getEstado() == true && cliente.getDistancia()<100){
            precioenvio = GranLogistica(producto,cliente);
        } else if (producto.getEstado() == true && cliente.getDistancia()>100){
            if(cliente.getDistancia()-100 < 100){
                i=cliente.getDistancia()%100;
                precioenvio = GranLogistica(producto,cliente) + (i * PequeñaLogistica(cliente));
            } 
            if(cliente.getDistancia()-100 > 100){
                n=cliente.getDistancia()/100;
                i=cliente.getDistancia()%100;
                precioenvio = ((n * GranLogistica(producto,cliente)) + (i *PequeñaLogistica(cliente)));
            } 
        }

        //Este segundo if es para producto no perecedero
        if(producto.getEstado() == false){
            n=cliente.getDistancia()/50;
            i=cliente.getDistancia()%50;

            precioenvio = ((n * GranLogistica(producto,cliente)) + (i * PequeñaLogistica(cliente)));
        }

        System.out.println("El precio de envio con esta empresa sería de : " + precioenvio + " euros.");
    }

    /**
     * Pequeña función para realizar los cálculos de la pequeña logística, basándose en precios y la cantidad de producto comprado
     *
     * @param  cliente  Quien compra el producto, de ahi se puede obtener cuanto y desde donde compra
     * @return precio Precio resultante de la pequeña logistica
     */
    private double PequeñaLogistica(Cliente cliente)
    {
        double precio = transporteP.getPrecio()*0.01*cliente.getKilogramos();
        return precio;
    }

    /**
     * Pequeña función para realizar los cálculos de la gran logística, basándose en precios y la cantidad de producto comprado
     *
     * @param  cliente  Quien compra el producto, de ahi se puede obtener cuanto y desde donde compra
     * @return precio Precio resultante de la pequeña logistica
     */
    private double GranLogistica(Producto producto,Cliente cliente)
    {
        double precio = transporteG.getPrecio()*0.5*producto.getPrecio()*cliente.getKilogramos();
        return precio;
    }

    /**
     * Funcion getter para obtener el nombre
     * @return nombreEmpresa Se devuelve el nombre de la empresa
     */
    public String GetNombre()
    {
        //Se obtiene el nombre
        return this.nombreEmpresa;
    }

    /**
     * Funcion getter para obtener el precio del envio
     * @return precioenvio Se devuelve el precio del envio 
     */
    public double GetPrecioenvio()
    {
        //Se obtiene el precio del envio
        return this.precioenvio;
    }

    /**
     * Un setter para darle un nuevo nombre a la empresa de logistica
     *
     * @param Nombre Para saber el nombre que se le quiere dar a la empresa
     * 
     */
    public void SetNombre(String nombre)
    {
        //Se cambia el nombre
        this.nombreEmpresa = nombre;
    }

    /**
     * Un setter para darle uan nueva logistica pequeña
     *
     * @param emrpesaP Para saber la logistica que queremos cambiar
     * 
     */
    public void SetLogisticaP(Transporte empresaP)
    {
        //Cambio el transporte
        this.transporteP = empresaP;
    }

    /**
     * Un setter para darle uan nueva logistica grande
     *
     * @param emrpesaP Para saber la logistica que queremos cambiar
     * 
     */
    public void SetLogisticaG(Transporte empresaG)
    {
        //Cambio el tranporte
        this.transporteG = empresaG;
    }

    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir la logistica, en este caso, los datos de la empresa
     *
     * @return     Una oración donde se juntan todos los datos disponibles del productor
     */
    public String toString()
    {
        return "Nombre de la empresa : " + this.nombreEmpresa + " Transportes disponibles = ";
    }
}
