/**
 * Clase Cooperativa, desde aqui se cargan todas las funciones para que funcione el programa y se llevan a cabo las pruebas
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Cooperativa
{
    //Todo se inicializa en la clase Pruebas
    public static void main (String[] args){
        Pruebas prueba = new Pruebas();
        //Insertar datos iniciales
        prueba.cargarDatos();
        
        //Se imprimen los datos cargados
        prueba.imprimirCliente();
        prueba.imprimirProductos();
        prueba.imprimirProductores();
        prueba.imprimirLogistica();
        
        //Comienzan las pruebas
        prueba.Prueba1();
        prueba.Prueba1_1();
        prueba.Prueba2();
        prueba.Prueba2_2();
        prueba.Prueba3();
        prueba.Prueba3_3();
        
        //Se cargan los pedidos realizados
        prueba.imprimirPedidos();

    }
}
