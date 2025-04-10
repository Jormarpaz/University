/**
 * En la clase Producto se crean los productos que serán la columna vertebral del proyecto, se le añadirán a un productor y en base a sus características se realizarán cálculos en otras clases
 * 
 * @author Jorge
 * @version *17Mayo*
 */
public class Producto
{
    //Declaramos las variables de las caracteristicas de los productos
    private String nombre_producto;
    private double rendimiento;
    private double precio_kg;
    private boolean Perecedero;

    /**
     * Constructor de la clase Producto, se piden las características que incorporará a futuro el producto
     */
    public Producto(String nombre,double rendimiento,double precio,boolean perecedero)
    {
        //Asignamos lo que recibimos a cada caracteristica
        this.nombre_producto = nombre;
        this.rendimiento = rendimiento;
        this.precio_kg = precio;
        this.Perecedero = perecedero;

    }

    /**
     * Un getter para el nombre
     * 
     * @return  nombre_producto   Al ser un getter te devuelve el valor de la funcion 
     */
    public String getNombre()
    {
        //Devuelve el nombre del producto
        return nombre_producto;
    }

    /**
     * Un setter para el nombre
     *
     * @param  nombre   Se pide un nombre para sustituir el actual
     * 
     */
    public void setNombre(String nombre)
    {
        //Le damos un nombre nuevo al producto
        this.nombre_producto=nombre;
    }

    /**
     * Un getter para el Rendimiento
     * 
     * @return  rendimiento   Al ser un getter te devuelve el valor de la funcion 
     */
    public double getRendimiento()
    {
        //Devuelve el rendimiento del producto en el campo
        return rendimiento;
    }

    /**
     * Un setter para el Rendimiento
     *
     * @param  rendimiento   Se pide una cantidad doouble para sustituir el actual
     */
    public void setRendimiento(double rendimiento)
    {
        //Le damos un rendimiento nuevo al producto
        this.rendimiento=rendimiento;
    }

    /**
     * Un getter para el Precio
     * 
     * @return  precio_kg   Al ser un getter te devuelve el valor de la funcion  
     */
    public double getPrecio()
    {
        //Devuelve el precio por kilogramo del producto
        return precio_kg;
    }

    /**
     * Un setter para el precio
     *
     * @param  Precio   Se pide un precio double para sustituir el actual
     */
    public void setPrecio(double Precio)
    {
        //Le damos un nuevo valor al producto
        this.precio_kg = Precio;
    }

    /**
     * Un getter para el estado, saber si es Perecedero o no
     * 
     * @return  Perecedero   Al ser un getter te devuelve el valor de la funcion 
     */
    public boolean getEstado()
    {
        //Devuelve el estado del producto
        return Perecedero;
    }

    /**
     * Un setter para el estado
     *
     * @param  perecedero   Se pide un estado para sustituir el actual
     */
    public void setEstado(boolean perecedero)
    {
        //le otorgamos un estado distinto al producto
        this.Perecedero = perecedero;
    }

    /**
     * En la función toString se crea una muestra de lo que aparecerá al pedir el producto, en este caso, los datos del producto
     *
     * @return     Una oración donde se juntan todos los datos disponibles del producto
     */
    public String toString()
    {
        String perecederoStr = (Perecedero)? "Si" : "No";
        return "Nombre: " + nombre_producto + ", Rendimiento: " + rendimiento + ", Precio: " + precio_kg + ", ¿Es perecedero?: " + perecederoStr;
    }

}
