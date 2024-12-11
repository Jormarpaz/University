#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <string.h>

#define NUM_HIJOS 10
#define SHM_SIZE sizeof(int) * NUM_HIJOS
#define SEM_KEY 1234
#define MSG_KEY 5678
#define SHM_KEY 9012

// Definición de estructura para mensajes
struct mensaje {
    long mtype;
    char estado[3]; // "OK" o "KO"
};

// IPC variables
int shm_id, sem_id, msg_id;
int *lista; // Memoria compartida

void inicializar_IPC() {
    // Crear memoria compartida
    shm_id = shmget(SHM_KEY, SHM_SIZE, IPC_CREAT | 0666);
    if (shm_id < 0) {
        perror("[PADRE] Error al crear memoria compartida");
        exit(1);
    }
    lista = (int *)shmat(shm_id, NULL, 0);

    // Crear semáforo
    sem_id = semget(SEM_KEY, 1, IPC_CREAT | 0666);
    if (sem_id < 0) {
        perror("[PADRE] Error al crear semáforo");
        exit(1);
    }
    semctl(sem_id, 0, SETVAL, 1);

    // Crear cola de mensajes
    msg_id = msgget(MSG_KEY, IPC_CREAT | 0666);
    if (msg_id < 0) {
        perror("[PADRE] Error al crear cola de mensajes");
        exit(1);
    }
}

void liberar_IPC() {

    for (int i = 0; i < NUM_HIJOS; i++) {
        if (lista[i] != 0) { // Verificar si el hijo tiene un PID válido
            if (kill(lista[i], 0) == 0) { // Verificar si el proceso aún existe
                printf("[PADRE] Enviando SIGTERM a PID %d...\n", lista[i]);
                if (kill(lista[i], SIGTERM) == 0) { // Enviar la señal
                    if (waitpid(lista[i], NULL, 0) == -1) {
                        perror("[PADRE] Error al esperar al hijo");
                    } else {
                        printf("[PADRE] Hijo con PID %d terminado correctamente.\n", lista[i]);
                    }
                } else {
                    perror("[PADRE] Error al enviar SIGTERM");
                }
            } else {
                printf("[PADRE] El proceso con PID %d ya no existe.\n", lista[i]);
            }
            lista[i] = 0; // Limpiar el PID de la lista
        }
    }

    shmdt(lista);
    shmctl(shm_id, IPC_RMID, NULL);
    semctl(sem_id, 0, IPC_RMID);
    msgctl(msg_id, IPC_RMID, NULL);
}

void sincronizar_hijos() {
    printf("[PADRE] Iniciando ronda de ataques...\n");
    for (int i = 0; i < NUM_HIJOS; i++) {
        if (lista[i] != 0) {
            if (kill(lista[i], SIGUSR1) == -1) {
                printf("[PADRE] Hijo %d ya no existe o no responde.\n", lista[i]);
            }
        }
    }
}

void recibir_resultados(int *hijos_vivos) {
    struct mensaje msg;
    for (int i = 0; i < NUM_HIJOS; i++) {
        if (lista[i] != 0) { // Verificar si el PID aún existe
            if (msgrcv(msg_id, &msg, sizeof(msg.estado), lista[i], 0) != -1) {
                if (strcmp(msg.estado, "KO") == 0) { // Si el hijo está KO
                    printf("[PADRE] Hijo %d (PID %d) está KO.\n", i + 1, lista[i]);
                    
                    if (kill(lista[i], SIGTERM) == 0) { 
                        waitpid(lista[i], NULL, 0);
                        lista[i] = 0; 
                        (*hijos_vivos)--; // Actualizar el número de hijos vivos
                    } else {
                        printf("[PADRE] No se pudo enviar SIGTERM a PID %d\n", lista[i]);
                    }
                }
            }
        }
    }
}

void ejecutar_padre() {
    for (int i = 0; i < NUM_HIJOS; i++) {
        lista[i] = 0;
    }

    for (int i = 0; i < NUM_HIJOS; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            // Código del hijo
            execl("./HIJO", "./HIJO", NULL);
            perror("[HIJO] Error al ejecutar execl.");
            exit(1);
        } else if (pid > 0) {
            lista[i] = pid;
            printf("[PADRE] Hijo %d lanzado.\n", pid);
        } else {
            perror("[PADRE] Error al crear hijo");
        }
    }

    int hijos_vivos = NUM_HIJOS;
    while (hijos_vivos > 1) {
        sincronizar_hijos();
        recibir_resultados(&hijos_vivos);
        printf("[PADRE] Hijos vivos: %d\n", hijos_vivos);
        sleep(1);
    }

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
