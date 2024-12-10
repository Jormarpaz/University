#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <fcntl.h>

#define NUM_HIJOS 10
#define SHM_SIZE sizeof(int) * NUM_HIJOS

// Definiciones de recursos IPC
int shm_id, sem_id, msg_queue_id;
key_t shm_key = 1234, sem_key = 5678, msg_key = 91011;
int *lista; // Apuntador a la memoria compartida

// Función para inicializar recursos IPC
void inicializar_recurso_IPC() {
    shm_id = shmget(shm_key, SHM_SIZE, IPC_CREAT | 0666);
    if (shm_id < 0) {
        perror("Error al crear memoria compartida");
        exit(1);
    }
    lista = (int *)shmat(shm_id, NULL, 0);

    for (int i = 0; i < NUM_HIJOS; i++) {
        lista[i] = 0;
    }

    sem_id = semget(sem_key, 1, IPC_CREAT | 0666);
    if (sem_id < 0) {
        perror("Error al crear semáforo");
        exit(1);
    }
    semctl(sem_id, 0, SETVAL, 1);

    msg_queue_id = msgget(msg_key, IPC_CREAT | 0666);
    if (msg_queue_id < 0) {
        perror("Error al crear cola de mensajes");
        exit(1);
    }
}

void ejecutar_padre() {
    int rondas = 0;
    int hijos_vivos = NUM_HIJOS;

    while (hijos_vivos > 1) {
        rondas++;
        printf("[PADRE] Iniciando ronda %d...\n", rondas);

        // Enviar sincronización a los hijos
        for (int i = 0; i < NUM_HIJOS; i++) {
            if (lista[i] != 0) {
                printf("[PADRE] Enviando sincronización a hijo %d (PID %d)...\n", i + 1, lista[i]);
                kill(lista[i], SIGUSR1);
            }
        }

        sleep(1); // Simular tiempo entre rondas

        // Procesar mensajes de resultados
        struct msg_buffer {
            long mtype;
            int estado;
        } msg;

        for (int i = 0; i < NUM_HIJOS; i++) {
            if (lista[i] != 0) {
                if (msgrcv(msg_queue_id, &msg, sizeof(int), lista[i], 0) > 0) {
                    if (msg.estado == 0) { // KO
                        printf("[PADRE] El hijo %d está KO. Enviando SIGTERM...\n", lista[i]);
                        kill(lista[i], SIGTERM);
                        wait(NULL);
                        lista[i] = 0; // Eliminar PID de la lista
                        hijos_vivos--;
                    } else {
                        printf("[PADRE] El hijo %d sobrevivió a esta ronda.\n", lista[i]);
                    }
                }
            }
        }

        if (hijos_vivos <= 1) break;
    }

    printf("[PADRE] Fin del combate. Procediendo a limpiar recursos...\n");

    for (int i = 0; i < NUM_HIJOS; i++) {
        if (lista[i] != 0) {
            kill(lista[i], SIGTERM);
            wait(NULL);
        }
    }

    printf("[PADRE] Recursos IPC liberados.\n");
}

int main() {
    inicializar_recurso_IPC();
    ejecutar_padre();
    shmdt(lista);
    shmctl(shm_id, IPC_RMID, NULL);
    semctl(sem_id, 0, IPC_RMID);
    msgctl(msg_queue_id, IPC_RMID, NULL);

    printf("[PADRE] Terminando programa...\n");
    return 0;
}
