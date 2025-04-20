import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Clase principal que representa el router
public class Router {
    private final LineController lineAController;
    private final LineController lineBController;
    private final LineController lineCController;
    private final ProtectedUnit protectedUnit;

    public Router() {
        // Colas para cada línea
        BlockingQueue<Message> queueA = new LinkedBlockingQueue<>();
        BlockingQueue<Message> queueB = new LinkedBlockingQueue<>();
        BlockingQueue<Message> queueC = new LinkedBlockingQueue<>();

        // Controladores de línea
        this.lineAController = new LineController("Linea_A", queueA);
        this.lineBController = new LineController("Linea_B", queueB);
        this.lineCController = new LineController("Linea_C", queueC);

        // Unidad protegida con las tres líneas
        this.protectedUnit = new ProtectedUnit(queueA, queueB, queueC);

        // Iniciar los controladores como threads
        new Thread(lineAController).start();
        new Thread(lineBController).start();
        new Thread(lineCController).start();
    }

    // Método para recibir una llamada entrante
    public void receiveCall(Message message) {
        protectedUnit.routeMessage(message);
    }

    public static void main(String[] args) {
        Router router = new Router();
        
        // Simular varias llamadas entrantes
        for (int i = 1; i <= 17; i++) {
            router.receiveCall(new Message("Mensaje " + i));
        }
    }
}

// Clase que representa la unidad protegida que decide la ruta
class ProtectedUnit {
    private final BlockingQueue<Message> queueA;
    private final BlockingQueue<Message> queueB;
    private final BlockingQueue<Message> queueC;
    private static final int MAX_QUEUE_SIZE = 5;

    public ProtectedUnit(BlockingQueue<Message> queueA, 
                        BlockingQueue<Message> queueB, 
                        BlockingQueue<Message> queueC) {
        this.queueA = queueA;
        this.queueB = queueB;
        this.queueC = queueC;
    }

    public void routeMessage(Message message) {
        synchronized (queueA) {
            if (queueA.size() < MAX_QUEUE_SIZE) {
                queueA.offer(message);
                System.out.println(
                    "Estado colas - A: " + queueA.size() + ", B: " + queueB.size() + ", C: " + queueC.size() + "\n" +
                    message.getId() + " enrutado a Linea_A"
                );
                return;
            }
        }
        synchronized (queueB) {
            if (queueB.size() < MAX_QUEUE_SIZE) {
                queueB.offer(message);
                System.out.println(
                    "Estado colas - A: " + queueA.size() + ", B: " + queueB.size() + ", C: " + queueC.size() + "\n" +
                    message.getId() + " enrutado a Linea_B (Linea_A llena)"
                );
                return;
            }
        }
        synchronized (queueC) {
            if (queueC.offer(message)) {
                System.out.println(
                    "Estado colas - A: " + queueA.size() + ", B: " + queueB.size() + ", C: " + queueC.size() + "\n" +
                    message.getId() + " enrutado a Linea_C (Lineas A y B llenas)"
                );
            } else {
                System.err.println("Todas las líneas están llenas. Mensaje descartado: " + message.getId());
            }
        }
    }
}

// Clase que representa el controlador de una línea
class LineController implements Runnable {
    private final String lineName;
    private final BlockingQueue<Message> queue;

    public LineController(String lineName, BlockingQueue<Message> queue) {
        this.lineName = lineName;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Tomar un mensaje de la cola y procesarlo
                Message message = queue.take();
                System.out.println(lineName + " procesando: " + message.getId());
                
                // Simular tiempo de procesamiento
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(lineName + " interrumpida: " + e.getMessage());
        }
    }
}

// Clase que representa un mensaje
class Message {
    private final String id;

    public Message(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
