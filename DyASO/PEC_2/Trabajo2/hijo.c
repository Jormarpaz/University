#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>

int shm_id, sem_id, msg_queue_id;
key_t shm_key = 1234, sem_key = 5678, msg_key = 91011;
int *lista;

struct mensaje {
    long mtype;
    char mtext[100];
};

void enviar_estado(const char *estado) {
    struct mensaje msg;
    msg.mtype = 1; // Tipo genérico para mensajes al padre
    snprintf(msg.mtext, sizeof(msg.mtext), "PID %d: %s", getpid(), estado);
    msgsnd(msg_queue_id, &msg, sizeof(msg.mtext), 0);
}

void manejar_signal(int signo) {
    if (signo == SIGUSR1) {
        printf("[HIJO %d] Recibido ataque. Defendiéndose...\n", getpid());
        enviar_estado("OK");
    } else if (signo == SIGTERM) {
        printf("[HIJO %d] Recibido SIGTERM. Terminando...\n", getpid());
        exit(0);
    }
}

void inicializar_hijo() {
    shm_id = shmget(shm_key, 0, 0);
    lista = (int *)shmat(shm_id, NULL, 0);
    sem_id = semget(sem_key, 0, 0);
    msg_queue_id = msgget(msg_key, 0);
}

int main(int argc, char *argv[]) {
    int barrera_fd = atoi(argv[1]);

    inicializar_hijo();
    signal(SIGUSR1, manejar_signal);
    signal(SIGTERM, manejar_signal);

    printf("[HIJO %d] Inicializado y esperando sincronización...\n", getpid());

    while (1) {
        char dummy;
        read(barrera_fd, &dummy, 1);
        printf("[HIJO %d] Recibida sincronización. Decidiendo acción...\n", getpid());

        if (rand() % 2 == 0) {
            printf("[HIJO %d] Atacando.\n", getpid());
            enviar_estado("KO");    
        } else {
            printf("[HIJO %d] Defendiéndose.\n", getpid());
            enviar_estado("OK");
        }

        sleep(1); // Simulación de ronda
    }

    return 0;
}
