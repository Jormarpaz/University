#!/bin/bash

set -e # Detiene el script en errores
trap 'log_to_bible "Error detectado en Demonio.sh en línea $LINENO"; exit 1' ERR

# --- Función para registrar eventos en Biblia.txt ---
log_to_bible() {
    echo "$(date '+%H:%M:%S') $1" >>Biblia.txt
    echo "Registrado en Biblia: $1" # Mensaje de depuración
}

# Exportar la función para que esté disponible en subprocesos
export -f log_to_bible

# --- Manejador de señales ---
handle_signal() {
    log_to_bible "Señal recibida: $1. Terminando el demonio."
    log_to_bible "El demonio ha terminado correctamente tras recibir $1."
    exit 0
}

# Definir la función
handle_lists() {
    if ! flock -w 5 SanPedro -c "
        # Procesar lista de procesos normales
        if [ -f procesos ]; then
            while read pid comando; do
                if ! ps -p \"$pid\" >/dev/null 2>&1; then
                    log_to_bible \"El proceso $pid '$comando' ha terminado.\"
                    sed -i \"/^$pid /d\" procesos
                fi
            done < procesos
        fi

        # Procesar lista de procesos de servicio
        if [ -f procesos_servicio ]; then
            while read pid comando; do
                if ! ps -p \"$pid\" >/dev/null 2>&1; then
                    log_to_bible \"El proceso $pid ha terminado.\"
                    nohup bash -c \"$comando\" >/dev/null 2>&1 &
                    new_pid=\$!
                    sed -i \"s/^$pid /$new_pid /\" procesos_servicio
                    log_to_bible \"El proceso $pid resucita con pid $new_pid.\"
                fi
            done < procesos_servicio
        fi

        # Procesar lista de procesos periódicos
        if [ -f procesos_periodicos ]; then
            while read time period pid comando; do
                if (( time >= period )); then
                    kill \"$pid\" 2>/dev/null
                    log_to_bible "El proceso $pid '$comando' ha terminado."
                    nohup bash -c \"$comando\" >/dev/null 2>&1 &
                    new_pid=\$!
                    sed -i \"s/^$time $period $pid /0 $period $new_pid /\" procesos_periodicos
                    log_to_bible "El proceso $pid se reencarna con pid $new_pid."
                else
                    sed -i \"s/^$time $period $pid /$((time + 1)) $period $pid /\" procesos_periodicos
                fi
            done < procesos_periodicos
        fi
    "; then
        log_to_bible "Error: No se pudo manejar las listas debido a un fallo en flock."
        return 1
    fi
}

# --- Función para manejar el Apocalipsis ---
Apocalipsis() {
    log_to_bible "$(date +'%H:%M:%S') ---------------Apocalipsis---------------"
    log_to_bible "$(date +'%H:%M:%S') Iniciando la eliminación de procesos y listas."

    # Eliminar todos los procesos en las listas
    for file in procesos procesos_servicio procesos_periodicos; do
        if [ -f "$file" ]; then
            while read pid; do
                if kill -0 $pid 2>/dev/null; then
                    kill $pid
                    log_to_bible "$(date +'%H:%M:%S') El proceso $pid ha sido terminado."
                fi
            done < <(awk '{print $1}' "$file")
            rm -f "$file"
            log_to_bible "$(date +'%H:%M:%S') Lista $file eliminada."
        fi
    done

    # Limpiar el directorio Infierno
    if [ -d "./Infierno" ]; then
        rm -rf "./Infierno"
    fi

    # Eliminar el archivo SanPedro
    rm -f "./SanPedro"

    # Eliminar el archivo Apocalipsis
    rm -f "./Apocalipsis"
    log_to_bible "$(date +'%H:%M:%S') Se acabó el mundo."
    exit 0
}

# Exportar la función Apocalipsis
export -f Apocalipsis

cleanup_and_exit() {
    log_to_bible "Señal recibida: SIGTERM. Terminando el demonio."
    rm -f SanPedro
    log_to_bible "El archivo SanPedro ha sido eliminado."

    > Biblia.txt
    log_to_bible "El demonio ha terminado correctamente tras recibir SIGTERM."
    exit 0
}

# Registrar manejadores de señales
trap 'handle_signal SIGHUP' SIGHUP
trap 'handle_signal SIGINT' SIGINT
trap 'handle_signal SIGTERM' SIGTERM
trap 'cleanup_and_exit' SIGTERM

# --- Bucle principal del demonio ---
while true; do
    # Registrar que el demonio está operativo
    sleep 1

    # Comprobar si ha llegado el Apocalipsis
    if [ -f "./Apocalipsis" ]; then
        Apocalipsis
        exit 0  # Finalizar el bucle después del Apocalipsis
    fi

    # Manejar listas
    if ! handle_lists; then
        log_to_bible "Error: No se pudo manejar las listas."
        # Opcionalmente, podrías realizar una pausa para evitar reinicios rápidos
        sleep 1
    fi
done
