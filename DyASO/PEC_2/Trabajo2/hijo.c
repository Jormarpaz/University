#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <string.h>

#define MSG_KEY 5678
#define SHM_KEY 9012
#define SEM_KEY 1234

struct mensaje {
    long mtype;
    char estado[3]; // "OK" o "KO"
};

int msg_id;

void enviar_resultado(const char *estado) {
    struct mensaje msg;
    msg.mtype = getpid();
    strncpy(msg.estado, estado, sizeof(msg.estado));
    msgsnd(msg_id, &msg, sizeof(msg.estado), 0);
}

void manejar_signal(int sig) {
    if (sig == SIGUSR1) {
        int accion = rand() % 2; // 0: atacar, 1: defenderse
        if (accion == 0) {
            printf("[HIJO %d] Atacando.\n", getpid());
            sleep(1);
            enviar_resultado("KO");
        } else {
            printf("[HIJO %d] Defendiéndose.\n", getpid());
            sleep(1);
            enviar_resultado("OK");
        }
    } else if (sig == SIGTERM) {
        printf("[HIJO %d] Recibido SIGTERM. Terminando...\n", getpid());
        exit(0);
    }
}

int main() {
    srand(getpid());
    // Configurar manejadores de señales
    if (signal(SIGUSR1, manejar_signal) == SIG_ERR) {
        perror("[HIJO] Error al configurar manejador de SIGUSR1");
        exit(1);
    }
    if (signal(SIGTERM, manejar_signal) == SIG_ERR) {
        perror("[HIJO] Error al configurar manejador de SIGTERM");
        exit(1);
    }

    msg_id = msgget(MSG_KEY, 0);
    if (msg_id == -1) {
        perror("[HIJO] Error al conectarse a la cola de mensajes");
        exit(1);
    }

    printf("[HIJO %d] Inicializado y esperando sincronización...\n", getpid());
    while (1)
        pause(); // Esperar señales
    return 0;
}
