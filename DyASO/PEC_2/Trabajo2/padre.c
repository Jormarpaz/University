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

struct mensaje
{
    long mtype;
    char mtext[100];
};

// Inicialización de recursos IPC
void inicializar_recurso_IPC()
{
    // Crear memoria compartida
    shm_id = shmget(shm_key, SHM_SIZE, IPC_CREAT | 0666);
    if (shm_id < 0)
    {
        perror("Error al crear memoria compartida");
        exit(1);
    }
    lista = (int *)shmat(shm_id, NULL, 0);

    // Inicializar el array de PIDs
    for (int i = 0; i < NUM_HIJOS; i++)
    {
        lista[i] = 0;
    }

    // Crear semáforo
    sem_id = semget(sem_key, 1, IPC_CREAT | 0666);
    if (sem_id < 0)
    {
        perror("Error al crear semáforo");
        exit(1);
    }
    semctl(sem_id, 0, SETVAL, 1);

    // Crear cola de mensajes
    msg_queue_id = msgget(msg_key, IPC_CREAT | 0666);
    if (msg_queue_id < 0)
    {
        perror("Error al crear cola de mensajes");
        exit(1);
    }
}

// Procesar resultados al final de cada ronda
void procesar_resultados()
{
    struct mensaje msg;
    int hijos_vivos = 0;

    for (int i = 0; i < NUM_HIJOS; i++)
    {
        if (lista[i] != 0)
        {
            printf("[PADRE] Intentando terminar hijo con PID %d...\n", lista[i]);

            // Verificar si el proceso existe
            if (kill(lista[i], SIGTERM) == 0)
            {
                printf("[PADRE] Señal SIGTERM enviada a hijo con PID %d.\n", lista[i]);
                if (waitpid(lista[i], NULL, 0) == -1)
                {
                    perror("[PADRE] Error al esperar al hijo");
                }
                else
                {
                    printf("[PADRE] Hijo con PID %d terminado correctamente.\n", lista[i]);
                }
            }
            else
            {
                perror("[PADRE] Error al enviar SIGTERM");
            }

            lista[i] = 0; // Limpiar la lista de PIDs
        }
    }

    printf("[PADRE] Hijos vivos tras esta ronda: %d\n", hijos_vivos);
}

// Ejecución del padre
void ejecutar_padre()
{
    int barrera[2];
    if (pipe(barrera) == -1)
    {
        perror("Error al crear la tubería");
        exit(1);
    }

    printf("[PADRE] Tubería creada con éxito.\n");

    for (int i = 0; i < NUM_HIJOS; i++)
    {
        if (fork() == 0)
        {
            char fd_str[10];
            sprintf(fd_str, "%d", barrera[0]);
            execl("./HIJO", "./HIJO", fd_str, (char *)NULL);
            perror("[HIJO] Error al ejecutar execl.");
            exit(1);
        }
        else
        {
            printf("[PADRE] Hijo %d lanzado.\n", i + 1);
            lista[i] = getpid(); // Registrar PID del hijo
        }
    }

    close(barrera[0]); // El padre no lee de la tubería

    int rondas = 0;
    while (1)
    {
        printf("[PADRE] Iniciando ronda %d...\n", ++rondas);

        // Sincronizar con los hijos enviando datos por la tubería
        for (int i = 0; i < NUM_HIJOS; i++)
        {
            if (lista[i] != 0)
            {
                printf("[PADRE] Enviando sincronización a hijo %d (PID %d)...\n", i + 1, lista[i]);
                write(barrera[1], "1", 1);
            }
        }

        sleep(1); // Simulación de una ronda

        procesar_resultados();

        int vivos = 0;
        for (int i = 0; i < NUM_HIJOS; i++)
        {
            if (lista[i] != 0)
            {
                vivos++;
            }
        }

        if (vivos <= 1)
        {
            break;
        }
    }

    printf("[PADRE] Liberando recursos IPC...\n");
    // Liberar recursos IPC
    shmdt(lista);
    shmctl(shm_id, IPC_RMID, NULL);
    semctl(sem_id, 0, IPC_RMID);
    msgctl(msg_queue_id, IPC_RMID, NULL);
    close(barrera[1]);

    printf("[PADRE] Juego terminado.\n");
}

void liberar_recursos_IPC()
{
    // Desvincular y eliminar la memoria compartida
    if (shmdt(lista) == -1)
    {
        perror("[PADRE] Error al desvincular memoria compartida");
    }
    if (shmctl(shm_id, IPC_RMID, NULL) == -1)
    {
        perror("[PADRE] Error al eliminar memoria compartida");
    }

    // Eliminar el semáforo
    if (semctl(sem_id, 0, IPC_RMID) == -1)
    {
        perror("[PADRE] Error al eliminar semáforo");
    }

    // Eliminar la cola de mensajes
    if (msgctl(msg_queue_id, IPC_RMID, NULL) == -1)
    {
        perror("[PADRE] Error al eliminar cola de mensajes");
    }

    printf("[PADRE] Recursos IPC liberados correctamente.\n");
}

int main()
{
    inicializar_recurso_IPC(); // Inicializar los recursos IPC
    ejecutar_padre();          // Ejecutar la lógica principal del padre
    liberar_recursos_IPC();    // Liberar los recursos IPC
    printf("[PADRE] Juego terminado.\n");
    return 0;
}
