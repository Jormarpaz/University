#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <string.h>
#include <sys/wait.h>

#define NUM_HIJOS 10
#define SHM_KEY 9012
#define MSG_KEY 5678

struct mensaje {
    long mtype;
    char estado[3]; // "OK" o "KO"
};

// IPC variables
int shm_id, msg_id;
int *lista;
int barrera[2]; // Tubería sin nombre para la sincronización

void inicializar_IPC() {
    // Crear memoria compartida
    shm_id = shmget(SHM_KEY, sizeof(int) * NUM_HIJOS, IPC_CREAT | 0666);
    if (shm_id == -1) {
        perror("[PADRE] Error al crear memoria compartida");
        exit(1);
    }
    lista = shmat(shm_id, NULL, 0);

    // Crear cola de mensajes
    msg_id = msgget(MSG_KEY, IPC_CREAT | 0666);
    if (msg_id == -1) {
        perror("[PADRE] Error al crear cola de mensajes");
        exit(1);
    }

    // Crear la tubería sin nombre
    if (pipe(barrera) == -1) {
        perror("[PADRE] Error al crear la tubería");
        exit(1);
    }
}

void liberar_IPC() {
    shmdt(lista);
    shmctl(shm_id, IPC_RMID, NULL);
    msgctl(msg_id, IPC_RMID, NULL);
    close(barrera[0]);
    close(barrera[1]);
}

void sincronizar_hijos(int hijos_vivos) {
    printf("[PADRE] Sincronizando hijos vivos...\n");
    for (int i = 0; i < hijos_vivos; i++) {
        write(barrera[1], "1", 1); // Enviar un byte por cada hijo vivo
    }
}

void recibir_resultados(int *hijos_vivos) {
    struct mensaje msg;
    for (int i = 0; i < NUM_HIJOS; i++) {
        if (lista[i] != 0) {
            if (msgrcv(msg_id, &msg, sizeof(msg.estado), lista[i], 0) != -1) {
                if (strcmp(msg.estado, "KO") == 0) {
                    printf("[PADRE] Hijo %d (PID %d) está KO.\n", i + 1, lista[i]);
                    kill(lista[i], SIGTERM);
                    waitpid(lista[i], NULL, 0);
                    lista[i] = 0;
                    (*hijos_vivos)--;
                }
            }
        }
    }
}

void ejecutar_padre() {
    int hijos_vivos = NUM_HIJOS;

    // Lanzar hijos
    for (int i = 0; i < NUM_HIJOS; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            // Código del hijo
            char barrera_str[10];
            sprintf(barrera_str, "%d", barrera[0]); // Enviar descriptor de lectura
            execl("./HIJO", "./HIJO", barrera_str, NULL);
            perror("[HIJO] Error al ejecutar execl.");
            exit(1);
        } else if (pid > 0) {
            lista[i] = pid;
            printf("[PADRE] Hijo %d lanzado.\n", pid);
        } else {
            perror("[PADRE] Error al crear hijo");
        }
    }

    // Rondas de combate
    while (hijos_vivos > 1) {
        sincronizar_hijos(hijos_vivos);
        sleep(1); // Pausa para evitar condiciones de carrera

        printf("[PADRE] Enviando SIGUSR1 a los hijos...\n");
        for (int i = 0; i < NUM_HIJOS; i++) {
            if (lista[i] != 0) {
                kill(lista[i], SIGUSR1);
            }
        }

        recibir_resultados(&hijos_vivos);
        printf("[PADRE] Hijos vivos tras la ronda: %d\n", hijos_vivos);
    }

    // Determinar ganador
    if (hijos_vivos == 1) {
        for (int i = 0; i < NUM_HIJOS; i++) {
            if (lista[i] != 0) {
                printf("[PADRE] El hijo %d (PID %d) ha ganado.\n", i + 1, lista[i]);
                kill(lista[i], SIGTERM);
                waitpid(lista[i], NULL, 0);
                lista[i] = 0;
                break;
            }
        }
    } else {
        printf("[PADRE] Empate. No quedan hijos vivos.\n");
    }

    liberar_IPC();
}

int main() {
    inicializar_IPC();
    ejecutar_padre();
    return 0;
}
