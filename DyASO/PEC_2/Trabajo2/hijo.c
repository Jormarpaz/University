#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

void manejar_senal(int sig) {
    if (sig == SIGUSR1) {
        int accion = rand() % 2; // 0: atacar, 1: defenderse
        if (accion == 0) {
            printf("[HIJO %d] Atacando.\n", getpid());
        } else {
            printf("[HIJO %d] Defendiéndose.\n", getpid());
            sleep(1);
        }
        exit(accion); // 0: atacar, 1: defenderse
    } else if (sig == SIGTERM) {
        printf("[HIJO %d] Recibido SIGTERM. Terminando...\n", getpid());
        exit(0);
    }
}

int main() {
    signal(SIGUSR1, manejar_senal);
    signal(SIGTERM, manejar_senal);
    printf("[HIJO %d] Inicializado y esperando sincronización...\n", getpid());
    while (1) pause(); // Esperar señales
    return 0;
}