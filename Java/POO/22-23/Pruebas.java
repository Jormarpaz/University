import java.util.*;
/**
 * Clase donde he hecho todas las pruebas pedidas en el ejercicio
 * 
 * @author Jorge 
 * @version *17Mayo*
 */
public class Pruebas
{
    //Inicializo todas las arraylist que necesito
    public ArrayList<Producto> productos;
    public ArrayList<Producto> productoelegido;
    public ArrayList<Productores> productores;
    public ArrayList<Productores> productorelegido;
    public ArrayList<Logistica> logisticas;
    public ArrayList<Logistica> logisticaelegida;
    public ArrayList<Cliente> clientes; 
    public ArrayList<Pedido> pedidos;
    public ArrayList<Transporte> transporte;
    public ArrayList<Double> precioenvio;

    /**
     * Constructor de Pruebas, en este caso simplemente crea las arraylist que necesita
     */
    public Pruebas() {
        this.productos = new ArrayList<>();
        this.productoelegido = new ArrayList<>();
        this.productores = new ArrayList<>();
        this.productorelegido = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.logisticas = new ArrayList<>();
        this.logisticaelegida = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.transporte = new ArrayList<>();
        this.precioenvio = new ArrayList<>();
    }

    /**
     * Funcion para imprimir por pantalla los productores añadidos mediante la funcion CargarDatos()
     */
    public void imprimirProductores()
    {

        System.out.println("**********Lista de Productores**********");
        for(Productores p: productores){
            System.out.println(p);
        }
        System.out.println("");

    }

    /**
     * Funcion para imprimir por pantalla los productos añadidos mediante la funcion CargarDatos()
     */
    public void imprimirProductos()
    {

        System.out.println("**********Lista de Productos**********");
        for(Producto p: productos){
            System.out.println(p);
        }
        System.out.println("");

    }

    /**
     * Funcion para imprimir por pantalla las empresas de Logistica añadidas mediante la funcion CargarDatos()
     */
    public void imprimirLogistica()
    {
        int x=0;
        System.out.println("**********Lista de empresas de Logística**********");
        for(Logistica l: logisticas){
            System.out.println(l + transporte.get(x).toString() + transporte.get(x+1).toString());
            x=x+2;
        }
        System.out.println("");

    }

    /**
     * Funcion para imprimir por pantalla los clientes añadidos mediante la funcion CargarDatos()
     */
    public void imprimirCliente()
    {

        System.out.println("**********Lista de Clientes**********");
        for(Cliente c: clientes){
            System.out.println(c);
        }
        System.out.println("");

    }

    /**
     * Funcion para imprimir por pantalla pedidos realizados a lo largo de las pruebas
     */
    public void imprimirPedidos(){
        int x=1;
        int i=0;
        System.out.println("*************Lista de Pedidos*************");
        for(Pedido p: pedidos){
            System.out.println("Pedido nº" + x);
            System.out.print("[" + clientes.get(i).fechacompra.get(0) + "] ");
            System.out.print(p);
            clientes.get(i).calcularPrecioTotal();
            System.out.println((precioenvio.get(i)+ clientes.get(i).Preciototal)  + " €. \n");
            x++;
            i++;
        }
        System.out.println("");
    }

    /**
     * Primera prueba, producto perecedero a mas de 100km por distribuidor
     */
    public void Prueba1()
    {
        System.out.println("*************Comienza la Prueba 1*************\n");
        System.out.println("El distribuidor " + clientes.get(0).getNombre() + " va a comprar un producto perecedero estando a " + clientes.get(0).getDistancia() + " km de la cooperativa\n");
        System.out.println("Los siguientes productos son perecederos :");
        for (Producto p : productos){
            if(p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        sc.close();
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(0).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(0)) ){
                p.getHectareas(productoelegido.get(0));
                System.out.println(p.toString() + " " + (productoelegido.get(0).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        System.out.println("Cuantos kilogramos quiere? (>1000) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        sc6.close();
        clientes.get(0).ComprarProducto(productoelegido.get(0),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(0).getNombre());
        
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(0)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(0),productorelegido.get(0),clientes.get(0),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(0), clientes.get(0));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(0),productorelegido.get(0),clientes.get(0),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(0), clientes.get(0));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(0),productorelegido.get(0),clientes.get(0),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(0), clientes.get(0));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(0).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(0) + (precioenvio.get(0) + clientes.get(0).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(0).getNombre() + " se embolsa " + clientes.get(0).Preciototal + " euros de la transacción");

        System.out.println("\n*************Finaliza la Prueba 1*************\n");
    }

    /**
     * Segunda prueba, producto perecedero a menos de 100km por distribuidor
     */
    public void Prueba1_1()
    {
        System.out.println("*************Comienza la Prueba 1_1*************\n");
        System.out.println("El distribuidor " + clientes.get(1).getNombre() + " va a comprar un producto perecedero estando a " + clientes.get(1).getDistancia() + " km de la cooperativa");

        System.out.println("Los siguientes productos son perecederos :");
        for (Producto p : productos){
            if(p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(1).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(1)) ){
                p.getHectareas(productoelegido.get(1));
                System.out.println(p.toString() + " " + (productoelegido.get(1).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        sc.close();
        System.out.println("Cuantos kilogramos quiere? (>1000) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        clientes.get(1).ComprarProducto(productoelegido.get(1),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(1).getNombre());
        sc6.close();
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(1)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(1),productorelegido.get(1),clientes.get(1),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(1), clientes.get(1));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(1),productorelegido.get(1),clientes.get(1),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(1), clientes.get(1));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(1),productorelegido.get(1),clientes.get(1),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(1), clientes.get(1));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(1).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(1) + (precioenvio.get(1) + clientes.get(1).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(1).getNombre() + " se embolsa " + clientes.get(1).Preciototal + " euros de la transacción");

        System.out.println("\n*************Finaliza la Prueba 1_1*************\n");
    }

    /**
     * Tercera prueba, producto no perecedero a mas de 50km por distribuidor
     */
    public void Prueba2()
    {        
        System.out.println("*************Comienza la Prueba 2*************\n");
        System.out.println("El distribuidor " + clientes.get(2).getNombre() + " va a comprar un producto no perecedero estando a " + clientes.get(2).getDistancia() + " km de la cooperativa");
        System.out.println("Los siguientes productos son no perecederos :");
        for (Producto p : productos){
            if(!p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(!p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        sc.close();
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(2).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(2)) ){
                p.getHectareas(productoelegido.get(2));
                System.out.println(p.toString() + " " + (productoelegido.get(2).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        System.out.println("Cuantos kilogramos quiere? (>1000) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        clientes.get(2).ComprarProducto(productoelegido.get(2),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(2).getNombre());
        sc6.close();
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(2)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        clientes.get(2).ComprarProducto(productoelegido.get(2),1600.0);

        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(2),productorelegido.get(2),clientes.get(2),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(2), clientes.get(2));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(2),productorelegido.get(2),clientes.get(2),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(2), clientes.get(2));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(2),productorelegido.get(2),clientes.get(2),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(2), clientes.get(2));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(2).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(2) + (precioenvio.get(2) + clientes.get(2).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(2).getNombre() + " se embolsa " + clientes.get(2).Preciototal + " euros de la transacción");
        
        System.out.println("\n*************Finaliza la Prueba 2*************\n");
    }

    /**
     * Cuarta prueba, producto no perecedero a menos de 50km por distribuidor
     */
    public void Prueba2_2()
    {
        System.out.println("*************Comienza la Prueba 2_2*************\n");
        System.out.println("El distribuidor " + clientes.get(3).getNombre() + " va a comprar un producto no perecedero estando a " + clientes.get(3).getDistancia() + " km de la cooperativa");
        System.out.println("Los siguientes productos son no perecederos :");
        for (Producto p : productos){
            if(!p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(!p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        sc.close();
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(3).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(3)) ){
                p.getHectareas(productoelegido.get(3));
                System.out.println(p.toString() + " " + (productoelegido.get(3).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        System.out.println("Cuantos kilogramos quiere? (>1000) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        clientes.get(3).ComprarProducto(productoelegido.get(3),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(3).getNombre());
        sc6.close();
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(3)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        clientes.get(3).ComprarProducto(productoelegido.get(3),1115.0);

        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(3),productorelegido.get(3),clientes.get(3),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(3), clientes.get(3));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(3),productorelegido.get(3),clientes.get(3),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(3), clientes.get(3));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(3),productorelegido.get(3),clientes.get(3),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(3), clientes.get(3));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(3).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(3) + (precioenvio.get(3) + clientes.get(3).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(3).getNombre() + " se embolsa " + clientes.get(3).Preciototal + " euros de la transacción");

        System.out.println("\n*************Finaliza la Prueba 2_2*************\n");
    }

    /**
     * Quinta prueba, producto perecedero por consumidor
     */
    public void Prueba3()
    {
        System.out.println("*************Comienza la Prueba 3*************\n");
        System.out.println("El consumidor " + clientes.get(4).getNombre() + " va a comprar un producto perecedero estando a " + clientes.get(4).getDistancia() + " km de la cooperativa");
        System.out.println("Los siguientes productos son perecederos :");
        for (Producto p : productos){
            if(p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(4).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(4)) ){
                p.getHectareas(productoelegido.get(4));
                System.out.println(p.toString() + " " + (productoelegido.get(4).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        sc.close();
        System.out.println("Cuantos kilogramos quiere? (<100) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        clientes.get(4).ComprarProducto(productoelegido.get(4),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(4).getNombre());
        sc6.close();
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(4)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        clientes.get(4).ComprarProducto(productoelegido.get(4),95.0);

        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(4),productorelegido.get(4),clientes.get(4),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(4), clientes.get(4));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(4),productorelegido.get(4),clientes.get(4),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(4), clientes.get(4));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(4),productorelegido.get(4),clientes.get(4),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(4), clientes.get(4));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(4).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(4) + (precioenvio.get(4) + clientes.get(4).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(4).getNombre() + " se embolsa " + clientes.get(4).Preciototal + " euros de la transacción");

        System.out.println("\n*************Finaliza la Prueba 3*************\n");
    }

    /**
     * Sexta prueba, producto no perecedero por consumidor
     */
    public void Prueba3_3()
    {
        System.out.println("*************Comienza la Prueba 3_3*************\n");
        System.out.println("El consumidor " + clientes.get(5).getNombre() + " va a comprar un producto no perecedero estando a " + clientes.get(5).getDistancia() + " km de la cooperativa");
        System.out.println("Los siguientes productos son no perecederos :");
        for (Producto p : productos){
            if(!p.getEstado()){
                System.out.println(p.getNombre());
            }
        }
        System.out.println("Cual quiere? :");
        Scanner sc = new Scanner(System.in);
        String productoleer = sc.nextLine();
        for (Producto p : productos){
            if(!p.getEstado() && (productoleer.equalsIgnoreCase(p.getNombre()))){
                productoelegido.add(p);
            }
        }
        sc.close();
        System.out.println("Los siguientes productores tienen "+ productoelegido.get(5).getNombre() + " : ");
        for(Productores p : productores){
            if(p.isProducto(productoelegido.get(5)) ){
                p.getHectareas(productoelegido.get(5));
                System.out.println(p.toString() + " " + (productoelegido.get(5).getRendimiento()*p.hectareasobtenidas.get(0)) + " kg. " );
            }
        }
        System.out.println("Cuantos kilogramos quiere? (<100) ");
        Scanner sc6 = new Scanner(System.in);
        String kilogramosleer = sc6.nextLine();
        double cantidad = Integer.parseInt(kilogramosleer);
        clientes.get(5).ComprarProducto(productoelegido.get(5),cantidad);
        System.out.println("Se compra a la cooperativa "+ cantidad + " kg de "+ productoelegido.get(5).getNombre());
        sc6.close();
        System.out.println("Que productor prefiere? :");
        Scanner sc1 = new Scanner(System.in);
        String productorleer = sc1.nextLine();
        for (Productores p : productores){
            if(p.isProducto(productoelegido.get(5)) && (productorleer.equalsIgnoreCase(p.getNombre()))){
                productorelegido.add(p);
            }
        }
        sc1.close();
        clientes.get(5).ComprarProducto(productoelegido.get(5),43.0);

        System.out.println("Que empresa de transporte se utiliza?");
        for (Logistica l:logisticas){
            System.out.println(l.GetNombre());
        }
        Scanner sc2 = new Scanner(System.in);
        String numero = sc2.nextLine();
        if (numero.equals("1") || numero.equalsIgnoreCase("Union")){ 
            Pedido pedido1 = new Pedido(productoelegido.get(5),productorelegido.get(5),clientes.get(5),logisticas.get(0));
            logisticas.get(0).Envio(productoelegido.get(5), clientes.get(5));
            precioenvio.add(logisticas.get(0).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("2") || numero.equalsIgnoreCase("Publico")){
            Pedido pedido1 = new Pedido(productoelegido.get(5),productorelegido.get(5),clientes.get(5),logisticas.get(1));
            logisticas.get(1).Envio(productoelegido.get(5), clientes.get(5));
            precioenvio.add(logisticas.get(1).GetPrecioenvio());
            pedidos.add(pedido1);
        } else if(numero.equals("3") || numero.equalsIgnoreCase("Privado")){
            Pedido pedido1 = new Pedido(productoelegido.get(5),productorelegido.get(5),clientes.get(5),logisticas.get(2));
            logisticas.get(2).Envio(productoelegido.get(3), clientes.get(3));
            precioenvio.add(logisticas.get(2).GetPrecioenvio());
            pedidos.add(pedido1);
        } else {
            System.out.println("*************Error al seleccionar empresa de transporte*************");
        }
        sc2.close();
        clientes.get(5).calcularPrecioTotal();
        System.out.println("Resumen del pedido : " + pedidos.get(5) + (precioenvio.get(5) + clientes.get(5).Preciototal) + " €. \n");
        System.out.println("El productor " + productorelegido.get(5).getNombre() + " se embolsa " + clientes.get(5).Preciototal + " euros de la transacción");

        System.out.println("\n*************Finaliza la Prueba 3_3*************\n");
    }

    /**
     * Funcion para cargar los datos necesarios del programa
     */
    public void cargarDatos(){
        Producto producto1 = new Producto("Almendra",1000,2.0,false);
        productos.add(producto1);
        Producto producto2 = new Producto("Uvas",2000,1.5,true);
        productos.add(producto2);
        Producto producto3 = new Producto("Melon",1250,0.45,false);
        productos.add(producto3);
        Producto producto4 = new Producto("Sandia",975,0.75,true);
        productos.add(producto4);
        Producto producto5 = new Producto("Naranjas",1500,1.2,false);
        productos.add(producto5);
        Producto producto6 = new Producto("Algodon",1200,1.75,true);
        productos.add(producto6);

        Pequeños_Prod peq_prod1 = new Pequeños_Prod("Jorge");
        productores.add(peq_prod1);
        if(((Pequeños_Prod)productores.get(0)).isComprobar(2.5)){
            productores.get(0).AñadirProducto(productos.get(0),2.5);
        }
        if(((Pequeños_Prod)productores.get(0)).isComprobar(1.5)){
            productores.get(0).AñadirProducto(productos.get(1),1.5);
        }
        if(((Pequeños_Prod)productores.get(0)).isComprobar(0.5)){
            productores.get(0).AñadirProducto(productos.get(5),0.5);
        }

        Pequeños_Prod peq_prod2 = new Pequeños_Prod("Quique");
        productores.add(peq_prod2);
        if(((Pequeños_Prod)productores.get(1)).isComprobar(1.5)){
            productores.get(1).AñadirProducto(productos.get(0),1.5);
        }
        if(((Pequeños_Prod)productores.get(1)).isComprobar(1.0)){
            productores.get(1).AñadirProducto(productos.get(2),1.0);
        }
        if(((Pequeños_Prod)productores.get(1)).isComprobar(0.5)){
            productores.get(1).AñadirProducto(productos.get(4),0.5);
        }

        Pequeños_Prod peq_prod3 = new Pequeños_Prod("Carlos");
        productores.add(peq_prod3);
        if(((Pequeños_Prod)productores.get(2)).isComprobar(0.75)){
            productores.get(2).AñadirProducto(productos.get(1),0.75);
        }
        if(((Pequeños_Prod)productores.get(2)).isComprobar(1.0)){
            productores.get(2).AñadirProducto(productos.get(3),1.0);
        }
        if(((Pequeños_Prod)productores.get(2)).isComprobar(2.5)){
            productores.get(2).AñadirProducto(productos.get(5),2.5);
        }

        Grandes_Prod gran_prod4 = new Grandes_Prod("Asun");
        productores.add(gran_prod4);
        for(Producto p : productos){
            productores.get(3).AñadirProducto(p,2.5);
        }
        Grandes_Prod gran_prod5 = new Grandes_Prod("Elsa");
        productores.add(gran_prod5);
        for(Producto p : productos){
            productores.get(4).AñadirProducto(p,1.0);
        }

        Grandes_Prod gran_prod6 = new Grandes_Prod("Mack");
        productores.add(gran_prod6);
        for(Producto p : productos){
            productores.get(5).AñadirProducto(p,3.0);
        }

        Transporte transporte1 = new Transporte("Javier",0.35,true);
        Transporte transporte2 = new Transporte("Juan",0.15,false);
        Logistica logistica1 = new Logistica ("Union", transporte2, transporte1);
        logisticas.add(logistica1);
        transporte.add(transporte1);
        transporte.add(transporte2);

        Transporte transporte3 = new Transporte("Correos",0.55,true);
        Transporte transporte4 = new Transporte("Correos Express",0.05,false);
        Logistica logistica2 = new Logistica ("Publico", transporte4, transporte3);
        logisticas.add(logistica2);
        transporte.add(transporte3);
        transporte.add(transporte4);

        Transporte transporte5 = new Transporte("Seur",0.45,true);
        Transporte transporte6 = new Transporte("Ups",0.20,false);
        Logistica logistica3 = new Logistica ("Privado", transporte6, transporte5);
        logisticas.add(logistica3);
        transporte.add(transporte5);
        transporte.add(transporte6);

        Distribuidores cliente1 = new Distribuidores ("Pedro",125.0); 
        clientes.add(cliente1);
        Distribuidores cliente2 = new Distribuidores ("Julia",90.0);
        clientes.add(cliente2);
        Distribuidores cliente3 = new Distribuidores ("Pablo",75.0);
        clientes.add(cliente3);
        Distribuidores cliente4 = new Distribuidores ("Ernesto",43.0);
        clientes.add(cliente4);
        Consumidor cliente5 = new Consumidor ("JoseMari",150.0); 
        clientes.add(cliente5);
        Consumidor cliente6 = new Consumidor ("Joan",75.0); 
        clientes.add(cliente6);
        Consumidor cliente7 = new Consumidor ("Melisa",55.0); 
        clientes.add(cliente7);

    }
}
