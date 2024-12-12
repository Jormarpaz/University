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

#define SHM_KEY 9012
#define MSG_KEY 5678

struct mensaje {
    long mtype;
    char estado[3]; // "OK" o "KO"
};

// IPC variables
int shm_id, msg_id, sem_id;
int *lista;
int barrera[2]; // Tubería sin nombre para la sincronización
char fifo_path[256]; // Ruta del FIFO
key_t ipc_key;
int num_hijos;

void inicializar_IPC() {
    // Crear clave IPC usando ftok()
    ipc_key = ftok(".", 'A');
    if (ipc_key == -1) {
        perror("[PADRE] Error al generar la clave IPC");
        exit(1);
    }

    // Crear memoria compartida
    shm_id = shmget(ipc_key, sizeof(int) * num_hijos, IPC_CREAT | 0666);
    if (shm_id == -1) {
        perror("[PADRE] Error al crear memoria compartida");
        exit(1);
    }
    lista = shmat(shm_id, NULL, 0);

    // Crear cola de mensajes
    msg_id = msgget(ipc_key, IPC_CREAT | 0666);
    if (msg_id == -1) {
        perror("[PADRE] Error al crear cola de mensajes");
        exit(1);
    }

    // Crear semáforo
    sem_id = semget(ipc_key, 1, IPC_CREAT | 0666);
    if (sem_id == -1) {
        perror("[PADRE] Error al crear semáforo");
        exit(1);
    }

    // Crear la tubería sin nombre
    if (pipe(barrera) == -1) {
        perror("[PADRE] Error al crear la tubería");
        exit(1);
    }
}

void limpiar_cola_mensajes() {
    struct mensaje msg;
    while (msgrcv(msg_id, &msg, sizeof(msg.estado), 0, IPC_NOWAIT) != -1) {
        printf("[PADRE] Eliminando mensaje residual del hijo con PID %ld.\n", msg.mtype);
    }
    if (msgctl(msg_id, IPC_RMID, NULL) == -1) {
        perror("[PADRE] Error al eliminar la cola de mensajes");
    } else {
        printf("[PADRE] Cola de mensajes eliminada.\n");
    }
}

void esperar_hijos() {
    int status;
    pid_t pid;
    while ((pid = waitpid(-1, &status, WNOHANG)) > 0) {
        printf("[PADRE] Proceso hijo con PID %d finalizó.\n", pid);
    }
}

void liberar_IPC() {
    esperar_hijos();
    limpiar_cola_mensajes();
    shmdt(lista);
    shmctl(shm_id, IPC_RMID, NULL);
    msgctl(msg_id, IPC_RMID, NULL);
    semctl(sem_id, 0, IPC_RMID);
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
    for (int i = 0; i < num_hijos; i++) {
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
    int hijos_vivos = num_hijos;

    // Lanzar hijos
    for (int i = 0; i < num_hijos; i++) {
        pid_t pid = fork();
        if (pid == 0) {
            // Código del hijo
            char barrera_str[10];
            char ipc_key_str[20];
            sprintf(barrera_str, "%d", barrera[0]); // Enviar descriptor de lectura
            sprintf(ipc_key_str, "%d", ipc_key); // Enviar clave IPC
            execl("./HIJO", "./HIJO", barrera_str, ipc_key_str, NULL);
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
        for (int i = 0; i < num_hijos; i++) {
            if (lista[i] != 0) {
                kill(lista[i], SIGUSR1);
            }
        }

        recibir_resultados(&hijos_vivos);
        printf("[PADRE] Hijos vivos tras la ronda: %d\n", hijos_vivos);
    }

    // Determinar ganador
    if (hijos_vivos == 1) {
        for (int i = 0; i < num_hijos; i++) {
            if (lista[i] != 0) {
                printf("[PADRE] El hijo %d (PID %d) ha ganado.\n", i + 1, lista[i]);
                // Escribir en el FIFO el resultado
                FILE *fifo = fopen(fifo_path, "w");
                if (fifo != NULL) {
                    fprintf(fifo, "El hijo %d (PID %d) ha ganado.\n", i + 1, lista[i]);
                    fclose(fifo);
                } else {
                    perror("[PADRE] Error al escribir en el FIFO");
                }

                // Finalizar al ganador
                kill(lista[i], SIGTERM);
                waitpid(lista[i], NULL, 0);
                lista[i] = 0;
                break;
            }
        }
    } else {
        printf("[PADRE] Empate. No quedan hijos vivos.\n");
        FILE *fifo = fopen(fifo_path, "w");
        if (fifo != NULL) {
            fprintf(fifo, "Empate. No quedan hijos vivos.\n");
            fclose(fifo);
        } else {
            perror("[PADRE] Error al escribir en el FIFO");
        }
    }

    liberar_IPC();
    fflush(stdout);
}

int main(int argc, char *argv[]) {
    if (argc < 3) {
        fprintf(stderr, "Uso: %s <ruta_FIFO> <num_hijos>\n", argv[0]);
        exit(1);
    }

    strncpy(fifo_path, argv[1], sizeof(fifo_path));
    num_hijos = atoi(argv[2]);

    inicializar_IPC();
    ejecutar_padre();

    system("ipcs -q -s -m");
    
    return 0;
}