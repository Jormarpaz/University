#!/bin/bash

set -e  # Detiene el script en errores
trap 'log_to_bible "Error detectado en Demonio.sh en línea $LINENO"; exit 1' ERR


# --- Función para registrar eventos en Biblia.txt ---
log_to_bible() {
    echo "$(date '+%H:%M:%S') $1" >>Biblia.txt
    echo "Registrado en Biblia: $1" # Mensaje de depuración
} 200>SanPedro

# --- Función para comprobar si un proceso está corriendo ---
is_process_running() {
    local pid=$1
    ps -p $pid >/dev/null 2>&1
    return $?
}

# --- Función para terminar un proceso y sus descendientes ---
kill_process_tree() {
    local pid=$1

    # Obtener todos los procesos hijos (y nietos, etc.) del PID dado
    for child_pid in $(pstree -p $pid | grep -oP '(\(\d+\))' | sed 's/[()]//g'); do
        kill_process_tree $child_pid
    done

    # Terminar el proceso actual
    kill -TERM $pid
    log_to_bible "El proceso $pid ha terminado"
}


handle_apocalypse() {
    log_to_bible "---------------Apocalipsis---------------"

    # Finalizar todos los procesos en las listas
    for file in procesos procesos_servicio procesos_periodicos; do
        if [ -f "$file" ]; then
            while read pid _; do
                if is_process_running $pid; then
                    log_to_bible "Terminando el proceso $pid"
                    kill_process_tree $pid
                fi
            done < "$file"
            rm -f "$file"
        fi
    done

    # Eliminar el directorio Infierno
    if [ -d "Infierno" ]; then
        rm -rf Infierno
        log_to_bible "Directorio Infierno eliminado."
    fi

    # Eliminar el archivo Apocalipsis
    rm -f Apocalipsis
    log_to_bible "Archivo Apocalipsis eliminado."

    # Finalizar el demonio
    log_to_bible "Se acabó el mundo. Recursos limpiados."
    exit 0
}

# --- Manejador de señales ---
handle_signal() {
    log_to_bible "Señal recibida: $1. Terminando el demonio."
    log_to_bible "El demonio ha terminado correctamente tras recibir $1."
    exit 0
}

# Registrar manejadores de señales
trap 'handle_signal SIGHUP' SIGHUP
trap 'handle_signal SIGINT' SIGINT
trap 'handle_signal SIGTERM' SIGTERM

# --- Bucle principal del demonio ---
while true; do

    log_to_bible "Demonio operativo. Monitoreando procesos..."
    sleep 1

    # Comprobar si ha llegado el Apocalipsis
    if [ -f "./Apocalipsis" ]; then
        # Usar flock para asegurar que no haya interferencia al manejar el Apocalipsis
        log_to_bible "Detectado archivo Apocalipsis. Iniciando manejo del Apocalipsis."
        flock 200 -c "handle_apocalypse"
    fi

    # Esperar un segundo
    sleep 1

    # Bloquear acceso a las listas de procesos normales
    flock SanPedro -c '
        while read pid comando; do
            log_to_bible "Revisando proceso $pid en la lista de procesos."
            # Comprobar si el PID está en el directorio infierno
            if [ -f "./Infierno/$pid" ]; then
                # Si el PID está en infierno, matar todo el árbol de procesos
                log_to_bible "El proceso $pid está en el infierno. Terminando el proceso."
                kill_process_tree $pid

                # Eliminar la entrada de la lista
                sed -i "/$pid/d" procesos

                # Eliminar el archivo en infierno
                rm -f ./Infierno/$pid

                # Registrar en la Biblia que el proceso ha terminado
                log_to_bible "El proceso $pid '$comando' ha terminado"
            else
                # Si el PID no está en infierno, verificar si sigue en ejecución
                if ! is_process_running $pid; then
                    # El proceso ha terminado, eliminar de la lista
                    log_to_bible "El proceso $pid no está en ejecución. Eliminando de la lista."
                    sed -i "/$pid/d" procesos

                    # Registrar en la Biblia que el proceso ha terminado
                    log_to_bible "El proceso $pid '$comando' ha terminado"
                fi
            fi
        done < procesos
    '

    # Bloquear acceso a las listas de procesos de servicio
    flock SanPedro -c '
        while read pid comando; do
            log_to_bible "Revisando proceso $pid en la lista de procesos de servicio."
            if [ -f "./Infierno/$pid" ]; then
                # Si el PID está en infierno, matar todo el árbol de procesos
                log_to_bible "El proceso $pid está en el infierno. Terminando el proceso."
                kill_process_tree $pid

                # Eliminar la entrada de la lista
                sed -i "/$pid/d" procesos_servicio

                # Eliminar el archivo en infierno
                rm -f ./Infierno/$pid

                # Registrar en la Biblia que el servicio ha terminado
                log_to_bible "El servicio $pid '$comando' ha terminado"
            else
                # Si el PID no está en infierno, verificar si sigue en ejecución
                if ! is_process_running $pid; then
                    # El proceso ha terminado, resucitarlo
                    log_to_bible "El proceso $pid no está en ejecución. Resucitando el proceso."
                    resurrect_service_process $pid "$comando"
                fi
            fi
        done < procesos_servicio
    '


    # Bloquear acceso a las listas de procesos periódicos
    flock SanPedro -c '
        while read contador periodo pid comando; do
            if is_process_running $pid; then
                log_to_bible "Incrementando contador para el proceso $pid."
                contador=$((contador + 1))
                # Actualizar la entrada en la lista
                sed -i "s/^$pid .*/$contador $periodo $pid '$comando'/" procesos_periodicos
            else
                if [ $contador -ge $periodo ]; then
                    log_to_bible "Reiniciando proceso periódico $pid."
                    nohup bash -c "$comando" >/dev/null 2>&1 &
                    nuevo_pid=$!
                    echo "0 $periodo $nuevo_pid '$comando'" >> procesos_periodicos
                fi
            fi
        done < procesos_periodicos
    '

    sleep 1

done
