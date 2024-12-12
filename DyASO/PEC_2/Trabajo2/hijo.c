#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

struct mensaje {
    long mtype; // Tipo de mensaje (normalmente el PID del emisor).
    char estado[3]; // Estado del proceso: "OK" o "KO".
};

int msg_id; // Identificador de la cola de mensajes, compartido entre el padre y los hijos.

void enviar_resultado(const char *estado) {
    struct mensaje msg;
    msg.mtype = getpid(); // Establecer el tipo de mensaje como el PID del proceso hijo.
    strncpy(msg.estado, estado, sizeof(msg.estado)); // Copiar el estado ("OK" o "KO") en el mensaje.
    if (msgsnd(msg_id, &msg, sizeof(msg.estado), 0) == -1) {
        perror("[HIJO] Error al enviar mensaje");
    }
}

void manejar_signal(int sig) {
    // Si recibe SIGUSR1, decide la acción (atacar o defender).
    if (sig == SIGUSR1) {
        int accion = rand() % 2;
        if (accion == 0) {
            printf("[HIJO %d] Atacando.\n", getpid());
            usleep(100000); // 0.1 segundos
            enviar_resultado("KO");
        } else {
            printf("[HIJO %d] Defendiéndose.\n", getpid());
            usleep(200000); // 0.2 segundos
            enviar_resultado("OK");
        }
    } else if (sig == SIGTERM) {
        // Si recibe SIGTERM, imprime mensaje y finaliza el proceso.
        printf("[HIJO %d] Recibido SIGTERM. Terminando...\n", getpid());
        exit(0);
    }
}

int main(int argc, char *argv[]) {
    if (argc < 3) {
        fprintf(stderr, "[HIJO] Error: No se pasaron los argumentos necesarios.\n");
        exit(1);
    }

    int barrera_fd = atoi(argv[1]); // Leer el descriptor de la barrera de sincronización.
    key_t ipc_key = (key_t)strtoul(argv[2], NULL, 10); // Convertir la clave IPC de cadena a entero.

    msg_id = msgget(ipc_key, 0);
    // Conectarse a la cola de mensajes usando la clave IPC.
    if (msg_id == -1) {
        perror("[HIJO] Error al conectarse a la cola de mensajes");
        exit(1);
    }

    srand(getpid()); // Inicializar la semilla para números aleatorios usando el PID del proceso.
    signal(SIGUSR1, manejar_signal);
    signal(SIGTERM, manejar_signal);

    printf("[HIJO %d] Inicializado y esperando sincronización...\n", getpid());
    while (1) {
        char dummy;
        if (read(barrera_fd, &dummy, 1) > 0) {
            // Leer de la tubería de sincronización. Espera hasta recibir un byte.
            printf("[HIJO %d] Sincronización recibida. Decidiendo acción...\n", getpid());
        } else {
            perror("[HIJO] Error al leer de la barrera");
            exit(1);
        }
    }
    return 0;
}