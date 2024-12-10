#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>

void manejar_signal(int signo) {
    if (signo == SIGUSR1) {
        printf("Hijo PID %d realizando un ataque.\n", getpid());
    }
}

int main() {
    signal(SIGUSR1, manejar_signal);

    while (1) {
        // Esperar aquí la señal de sincronización para atacar
        pause();
        sleep(0.1);
    }
    return 0;
}