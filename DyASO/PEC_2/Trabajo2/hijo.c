#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>

int msg_queue_id;

void manejar_signal(int signo) {
    if (signo == SIGUSR1) {
        printf("[HIJO %d] Recibida señal de sincronización. Atacando...\n", getpid());
        int estado = rand() % 2;
        struct msg_buffer {
            long mtype;
            int estado;
        } msg;
        msg.mtype = getpid();
        msg.estado = estado;

        if (msgsnd(msg_queue_id, &msg, sizeof(int), 0) < 0) {
            perror("Error al enviar mensaje");
        }
    }
}

int main() {
    msg_queue_id = msgget(91011, 0);
    signal(SIGUSR1, manejar_signal);

    printf("[HIJO %d] Esperando señal...\n", getpid());

    while (1) {
        pause();
    }

    return 0;
}
