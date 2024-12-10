#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>
#include <unistd.h>

#define NUM_HIJOS 10

int lista[NUM_HIJOS];
int estado_hijos[NUM_HIJOS]; // 0: vivo, 1: KO

void terminar_hijos()
{
    for (int i = 0; i < NUM_HIJOS; i++)
    {
        if (lista[i] != 0)
        {
            printf("[PADRE] Intentando terminar hijo con PID %d...\n", lista[i]);
            if (kill(lista[i], 0) == 0)
            { // Comprobar si el proceso sigue vivo
                if (kill(lista[i], SIGTERM) == 0)
                {
                    printf("[PADRE] Señal SIGTERM enviada a hijo con PID %d.\n", lista[i]);
                }
                else
                {
                    perror("[PADRE] Error al enviar SIGTERM");
                }
            }
            else
            {
                printf("[PADRE] El proceso con PID %d ya no existe.\n", lista[i]);
            }
            lista[i] = 0; // Limpiar la lista de PIDs
        }
    }
}

void ejecutar_padre()
{
    for (int i = 0; i < NUM_HIJOS; i++)
    {
        lista[i] = 0;
        estado_hijos[i] = 0; // Todos los hijos están vivos inicialmente
    }

    for (int i = 0; i < NUM_HIJOS; i++)
    {
        pid_t pid = fork();
        if (pid == 0)
        {
            // Código del hijo
            execl("./HIJO", "./HIJO", (char *)NULL);
            perror("[HIJO] Error al ejecutar execl.");
            exit(1);
        }
        else if (pid > 0)
        {
            lista[i] = pid;
            printf("[PADRE] Hijo %d lanzado.\n", pid);
        }
        else
        {
            perror("[PADRE] Error al crear hijo");
        }
    }

    int hijos_vivos = NUM_HIJOS;
    int ronda = 1;

    while (hijos_vivos >= 2)
    {
        printf("[PADRE] Iniciando ronda %d...\n", ronda);
        for (int i = 0; i < NUM_HIJOS; i++)
        {
            if (lista[i] != 0 && estado_hijos[i] == 0)
            {
                printf("[PADRE] Enviando sincronización a hijo %d (PID %d)...\n", i + 1, lista[i]);
                if (kill(lista[i], SIGUSR1) == -1)
                {
                    perror("[PADRE] Error al enviar señal de sincronización");
                }
            }
        }

        for (int i = 0; i < NUM_HIJOS; i++)
        {
            if (lista[i] != 0 && estado_hijos[i] == 0)
            {
                int status;
                pid_t pid = waitpid(lista[i], &status, WNOHANG);
                if (pid == 0)
                {
                    // El hijo sigue vivo, no hacemos nada
                    continue;
                }
                else if (pid == -1)
                {
                    perror("[PADRE] Error al esperar al hijo");
                }
                else
                {
                    if (WIFEXITED(status))
                    {
                        int resultado = WEXITSTATUS(status);
                        if (resultado == 1)
                        {
                            estado_hijos[i] = 1;
                            printf("[PADRE] Hijo %d (PID %d) está KO.\n", i + 1, lista[i]);
                            hijos_vivos--;
                        }
                        else
                        {
                            printf("[PADRE] Hijo %d (PID %d) ha terminado tras atacar.\n", i + 1, lista[i]);
                            lista[i] = 0;
                            hijos_vivos--;
                        }
                    }
                }
            }
        }

        printf("[PADRE] Hijos vivos tras la ronda %d: %d\n", ronda, hijos_vivos);
        ronda++;
        sleep(2); // Esperar un momento antes de la siguiente ronda
    }

    terminar_hijos();
}

int main()
{
    ejecutar_padre();
    return 0;
}