#!/bin/bash

set -e # Detiene el script en errores
trap 'log_to_bible "Error detectado en Demonio.sh en línea $LINENO"; exit 1' ERR

# --- Función para registrar eventos en Biblia.txt ---
log_to_bible() {
    local message="$1"
    if [ -n "$message" ]; then
        echo "$(date '+%H:%M:%S') $message" >>Biblia.txt
        echo "Registrado en Biblia: $message" # Mensaje de depuración
    fi
}

# Exportar la función para que esté disponible en subprocesos
export -f log_to_bible

# Definir la función
handle_lists() {

    exec 200>SanPedro.lock
    if flock -w 5 200; then
        # Procesar lista de procesos normales
        if [ -f procesos ]; then
            while read -r pid comando; do
                if ! ps -p "$pid" >/dev/null 2>&1; then
                    log_to_bible \"El proceso $pid '$comando' ha terminado.\"
                    sed -i "/^$(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos
                fi
            done <procesos
        fi

        # Procesar lista de procesos de servicio
        if [ -f procesos_servicio ]; then
            while read -r pid comando; do
                if ! ps -p "$pid" >/dev/null 2>&1; then
                    if [ -f "./Infierno/$pid" ]; then
                        log_to_bible "El proceso $pid ha sido eliminado manualmente."
                        rm -f "./Infierno/$pid"
                        sed -i "/^$(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos_servicio
                    else
                        log_to_bible "El proceso $pid ha terminado."
                        nohup bash -c "$comando" >/dev/null 2>&1 &
                        new_pid=$!
                        escaped_pid=$(printf '%s\n' "$pid" | sed 's/[][\.*^$/]/\\&/g')
                        sed -i "s/^$escaped_pid /$new_pid /" procesos_servicio
                        log_to_bible "El proceso $pid resucita con pid $new_pid."
                    fi
                fi
            done <procesos_servicio
        fi

        # Procesar lista de procesos periódicos
        if [ -f procesos_periodicos ]; then
            while read -r time period pid comando; do
                if ! ps -p "$pid" >/dev/null 2>&1; then
                    log_to_bible "El proceso $pid '$comando' ha terminado."
                    sed -i "/^$time $period $(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos_periodicos
                elif ((time >= period)); then
                    kill \"$pid\" 2>/dev/null
                    log_to_bible "El proceso $pid '$comando' ha terminado."
                    nohup bash -c \"$comando\" >/dev/null 2>&1 &
                    new_pid=\$!
                    sed -i "s/^$time $period $pid /0 $period $new_pid /" procesos_periodicos
                    log_to_bible "El proceso $pid se reencarna con pid $new_pid."
                else
                    sed -i \"s/^$time $period $pid /$((time + 1)) $period $pid /\" procesos_periodicos
                fi
            done <procesos_periodicos
        fi
    fi
    flock -u 200

}

kill_process_tree() {
    local pid=$1
    # Matar todos los procesos en el árbol de procesos
    pkill -TERM -P "$pid" 2>/dev/null
    kill "$pid" 2>/dev/null
}

# --- Función para manejar el Apocalipsis ---
Apocalipsis() {

    log_to_bible "---------------Apocalipsis---------------"
    log_to_bible "Iniciando la eliminación de procesos y listas."

    exec 200>SanPedro.lock
    flock -x 200

    while read -r pid _; do
        kill_process_tree "$pid"
        log_to_bible "El proceso $pid ha terminado."
    done < procesos

    # Eliminar los procesos de servicio
    while read -r pid _; do
        kill_process_tree "$pid"
        log_to_bible "El proceso de servicio $pid ha terminado."
    done < procesos_servicio

    # Eliminar los procesos periódicos
    while read -r time period pid _; do
        kill_process_tree "$pid"
        log_to_bible "El proceso periódico $pid ha terminado."
    done < procesos_periodicos

    # Eliminar todos los procesos en las listas
    # for file in procesos procesos_servicio procesos_periodicos; do
    #     if [ -f "$file" ]; then
    #         while read pid; do
    #             if kill -0 $pid 2>/dev/null; then
    #                 kill $pid
    #                 log_to_bible "$(date +'%H:%M:%S') El proceso $pid ha sido terminado."
    #             fi
    #         done < <(awk '{print $1}' "$file")
    #         rm -f "$file"
    #     else
    #         echo "No se encontró el archivo $file"
    #     fi
    # done

    flock -u 200

    rm -f procesos procesos_servicio procesos_periodicos
    # Eliminar cualquier archivo con terminación .lock
    for lock_file in *.lock; do
        if [ -f "$lock_file" ]; then
            rm -f "$lock_file"
        fi
    done
    rm -rf Infierno
    rm -f SanPedro Apocalipsis

    # Limpiar el directorio Infierno
    if [ -d "./Infierno" ]; then
        rm -rf "./Infierno"
    fi

    # Eliminar el archivo SanPedro
    rm -f "./SanPedro"

    # Eliminar el archivo Apocalipsis
    rm -f "./Apocalipsis"
    log_to_bible "Se acabó el mundo."
    exit 0
}

# Exportar la función Apocalipsis
export -f Apocalipsis

# --- Bucle principal del demonio ---
while true; do
    # Registrar que el demonio está operativo
    sleep 1

    # Comprobar si ha llegado el Apocalipsis
    if [ -f "./Apocalipsis" ]; then
        Apocalipsis
        exit 0 # Finalizar el bucle después del Apocalipsis
    fi

    # Manejar listas
    if ! handle_lists; then
        log_to_bible "Error: No se pudo manejar las listas."
        # Opcionalmente, podrías realizar una pausa para evitar reinicios rápidos
        sleep 1
    fi
done
