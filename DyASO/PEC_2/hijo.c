#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

#define MSG_KEY 5678

struct mensaje {
    long mtype;
    char estado[3];
};

int msg_id;

void enviar_resultado(const char *estado) {
    struct mensaje msg;
    msg.mtype = getpid();
    strncpy(msg.estado, estado, sizeof(msg.estado));
    if (msgsnd(msg_id, &msg, sizeof(msg.estado), 0) == -1) {
        perror("[HIJO] Error al enviar mensaje");
    }
}

void manejar_signal(int sig) {
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
        printf("[HIJO %d] Recibido SIGTERM. Terminando...\n", getpid());
        exit(0);
    }
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "[HIJO] Error: No se pasó el descriptor de la barrera.\n");
        exit(1);
    }

    int barrera_fd = atoi(argv[1]);
    msg_id = msgget(MSG_KEY, 0);
    if (msg_id == -1) {
        perror("[HIJO] Error al conectarse a la cola de mensajes");
        exit(1);
    }

    srand(getpid());
    signal(SIGUSR1, manejar_signal);
    signal(SIGTERM, manejar_signal);

    printf("[HIJO %d] Inicializado y esperando sincronización...\n", getpid());
    while (1) {
        char dummy;
        if (read(barrera_fd, &dummy, 1) > 0) {
            printf("[HIJO %d] Sincronización recibida. Decidiendo acción...\n", getpid());
        } else {
            perror("[HIJO] Error al leer de la barrera");
            exit(1);
        }
    }
    return 0;
}
