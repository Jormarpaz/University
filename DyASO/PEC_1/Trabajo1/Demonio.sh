#!/bin/bash

set -e # Detiene el script en errores
trap 'log_to_bible "Error detectado en Demonio.sh en línea $LINENO"; exit 1' ERR

# --- Función para registrar eventos en Biblia.txt ---
log_to_bible() {
    echo "$(date '+%H:%M:%S') $1" >>Biblia.txt
    echo "Registrado en Biblia: $1" # Mensaje de depuración
}

# --- Manejador de señales ---
handle_signal() {
    log_to_bible "Señal recibida: $1. Terminando el demonio."
    log_to_bible "El demonio ha terminado correctamente tras recibir $1."
    exit 0
}

# Definir la función
handle_lists() {
    # Manejar la lista de procesos
    while read pid comando; do
        log_to_bible "Revisando proceso $pid en la lista de procesos."

        if [ -f "./Infierno/$pid" ]; then
            log_to_bible "El proceso $pid está en el infierno. Terminando el proceso."
            kill_process_tree $pid

            # Eliminar el PID de la lista de procesos
            if sed -i "/^$pid /d" procesos; then
                log_to_bible "El proceso $pid fue eliminado de la lista de procesos."
            else
                log_to_bible "Error al eliminar el proceso $pid de la lista de procesos."
            fi

            # Eliminar el archivo en Infierno
            if rm -f "./Infierno/$pid"; then
                log_to_bible "Archivo ./Infierno/$pid eliminado correctamente."
            else
                log_to_bible "Error al eliminar archivo ./Infierno/$pid."
            fi
        else
            if ! is_process_running $pid; then
                log_to_bible "El proceso $pid no está en ejecución. Eliminando de la lista."
                sed -i "/^$pid /d" procesos
                log_to_bible "El proceso $pid ha sido eliminado de la lista de procesos."
            fi
        fi
    done < procesos

        # Manejar la lista de procesos de servicio
        while read pid comando; do
            log_to_bible \"Revisando proceso \$pid en la lista de procesos de servicio.\"
            if [ -f \"./Infierno/\$pid\" ]; then
                log_to_bible \"El proceso \$pid está en el infierno. Terminando el proceso.\"
                kill_process_tree \$pid
                sed -i \"/\${pid}/d\" procesos_servicio
                rm -f ./Infierno/\$pid
                log_to_bible \"$(date +'%H:%M:%S'): El servicio \$pid '\$comando' ha terminado\"
            else
                if ! is_process_running \$pid; then
                    log_to_bible \"El proceso \$pid no está en ejecución. Resucitando el proceso.\"
                    nohup bash -c \"\$comando\" >/dev/null 2>&1 &
                    new_pid=\$!
                    sed -i \"s/^\$pid /\$new_pid /\" procesos_servicio
                    log_to_bible \"$(date +'%H:%M:%S'): El proceso \$pid '\$comando' resucita con pid \$new_pid\"
                fi
            fi
        done < procesos_servicio

        # Manejar la lista de procesos periódicos
        while read contador periodo pid comando; do
            log_to_bible \"Revisando proceso \$pid en la lista de procesos periódicos.\"
            if [ -f \"./Infierno/\$pid\" ]; then
                log_to_bible \"El proceso \$pid está en el infierno. Terminando el proceso.\"
                kill_process_tree \$pid
                sed -i \"/\${pid}/d\" procesos_periodicos
                rm -f ./Infierno/\$pid
                log_to_bible \"$(date +'%H:%M:%S'): El proceso \$pid '\$comando' ha terminado\"
            else
                contador=$((contador + 1))
                if ! is_process_running \$pid; then
                    if [ \$contador -ge \$periodo ]; then
                        log_to_bible \"El proceso \$pid no está en ejecución y ha alcanzado su periodo. Reencarnando el proceso.\"
                        nohup bash -c \"\$comando\" >/dev/null 2>&1 &
                        new_pid=\$!
                        sed -i \"s/^\$contador \$periodo \$pid /0 \$periodo \$new_pid /\" procesos_periodicos
                        log_to_bible \"$(date +'%H:%M:%S'): El proceso \$pid '\$comando' se ha reencarnado en el pid \$new_pid\"
                    else
                        sed -i \"s/^\$contador \$periodo \$pid /\$contador \$periodo \$pid /\" procesos_periodicos
                    fi
                else
                    sed -i \"s/^\$contador \$periodo \$pid /\$contador \$periodo \$pid /\" procesos_periodicos
                fi
            fi
        done < procesos_periodicos
}

# --- Función para comprobar si un proceso está corriendo ---
is_process_running() {
    local pid=$1
    kill -0 $pid 2>/dev/null
    return $?
}

# --- Función para terminar un proceso y sus descendientes ---
kill_process_tree() {
    local pid=$1
    
    # Obtener todos los PIDs del árbol de procesos
    local pids=$(pstree -p $pid | grep -o '([0-9]\+)' | grep -o '[0-9]\+')

    # Enviar la señal SIGTERM a todos los PIDs
    kill -SIGTERM $pids
    log_to_bible "El proceso $pid ha terminado"
}

# --- Función para manejar el Apocalipsis ---
Apocalipsis() {
    
    log_to_bible "$(date +'%H:%M:%S') ---------------Apocalipsis---------------"
    log_to_bible "$(date +'%H:%M:%S') Continuamos."

        # Terminar todos los procesos de todas las listas
        for pid in $(awk '{print $1}' procesos procesos_servicio procesos_periodicos 2>/dev/null); do
            if kill -0 $pid 2>/dev/null; then
                kill $pid
                log_to_bible "$(date +'%H:%M:%S') El proceso $pid ha terminado"
            fi
        done

        log_to_bible "$(date +'%H:%M:%S') Se eliminaron los procesos."

        # Borrar las listas y el fichero Apocalipsis
        rm -f procesos procesos_servicio procesos_periodicos SanPedro
        rm -f ./Apocalipsis
        rm -rf ./Infierno

        log_to_bible "$(date +'%H:%M:%S') Se acabo el mundo."
        exit 0
    
}

# Registrar manejadores de señales
trap 'handle_signal SIGHUP' SIGHUP
trap 'handle_signal SIGINT' SIGINT
trap 'handle_signal SIGTERM' SIGTERM

# --- Bucle principal del demonio ---
while true; do

    sleep 1

    # Comprobar si ha llegado el Apocalipsis
    if [ -f "./Apocalipsis" ]; then
        export -f Apocalipsis
        export -f log_to_bible
        flock SanPedro -c "Apocalipsis"
    fi

    if ! flock -w 10 SanPedro -c "handle_lists()"; then
            log_to_bible "Error: No se pudo adquirir el bloqueo para handle_lists dentro del tiempo límite."
            exit 1
    fi

    # Bloquear acceso a las listas de procesos
    export -f handle_lists
    export -f log_to_bible
    flock SanPedro -c "handle_lists"

    log_to_bible "El demonio está operativo."
    sleep 5

done
