#!/bin/bash

set -e # Detiene el script en errores
trap 'log_to_bible "Error detectado en Demonio.sh en línea $LINENO"; exit 1' ERR

# --- Función para registrar eventos en Biblia.txt ---
log_to_bible() {
    local message="$1"
    if [ -n "$message" ]; then
        echo "$(date '+%H:%M:%S') [Demonio] $message" >>Biblia.txt
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
        #if [ -f procesos_periodicos ]; then
        #    while read -r time period pid comando; do
        #        if ! ps -p "$pid" >/dev/null 2>&1; then
        #            log_to_bible "El proceso $pid '$comando' ha terminado."
        #            sed -i "/^$time $period $(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos_periodicos
        #        elif ((time >= period)); then
        #            kill \"$pid\" 2>/dev/null
        #            log_to_bible "El proceso $pid '$comando' ha terminado."
        #            nohup bash -c "$comando" >/dev/null 2>&1 &
        #            new_pid=\$!
        #            sed -i "s/^$time $period $pid /0 $period $new_pid /" procesos_periodicos
        #            log_to_bible "El proceso $pid se reencarna con pid $new_pid."
        #        else
        #            sed -i \"s/^$time $period $pid /$((time + 1)) $period $pid /\" procesos_periodicos
        #        fi
        #    done <procesos_periodicos
        #fi
        if [ -f procesos_periodicos ]; then
            

            while IFS= read -r line; do
                

                # Extraer campos: tiempo, período, PID, y comando
                time=$(echo "$line" | awk '{print $1}')
                period=$(echo "$line" | awk '{print $2}')
                pid=$(echo "$line" | awk '{print $3}')
                comando=$(echo "$line" | cut -d' ' -f4- | sed "s/^'//;s/'$//") # Elimina comillas externas

                # Incrementar tiempo de ejecución
                ((time++))

                if ((time >= period)); then
                    log_to_bible "Tiempo $time ha alcanzado el período $period. Reencarnando proceso $pid."
                    # Matar el proceso actual
                    if ps -p "$pid" >/dev/null 2>&1; then
                        kill "$pid" 2>/dev/null
                    fi

                    # Lanzar un nuevo proceso
                    nohup bash -c "$comando" >/dev/null 2>&1 &
                    new_pid=$!
                    

                    
                    sed -i "/^$pid /d" procesos_periodicos
                    if [ $? -eq 0 ]; then
                        log_to_bible "Líneas antiguas con PID $pid eliminadas."
                    else
                        log_to_bible "Error al eliminar las líneas antiguas con PID $pid."
                    fi                     
                    echo "0 $period $new_pid '$comando'" >>procesos_periodicos 

                    log_to_bible "El proceso periódico $pid se reencarna con pid $new_pid."
                else
                    log_to_bible "Actualizando tiempo del proceso $pid a $time."
                    sed -i "/^$pid /d" procesos_periodicos
                    if [ $? -eq 0 ]; then
                        log_to_bible "Líneas antiguas con PID $pid eliminadas."
                    else
                        log_to_bible "Error al eliminar las líneas antiguas con PID $pid."
                    fi                     
                    echo "$time $period $pid '$comando'" >> procesos_periodicos 

                    
                fi
            done <procesos_periodicos
        fi
    fi
    flock -u 200

}

# --- Función para manejar el Apocalipsis ---
Apocalipsis() {

    log_to_bible "---------------Apocalipsis---------------"
    log_to_bible "Iniciando la eliminación de procesos y listas."

    exec 200>SanPedro.lock
    if flock -w 5 200; then
        # Procesar y eliminar listas
        # Procesar y eliminar lista de procesos normales
        if [ -f procesos ]; then
            while IFS= read -r line; do
            pid=$(echo "$line" | awk '{print $1}')
            log_to_bible "El proceso $pid ha terminado."
            if ps -p "$pid" >/dev/null 2>&1; then
                kill "$pid" 2>/dev/null
            fi
            pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
            sed -i "/^$pattern /d" procesos
            done <procesos
            rm -f procesos
        fi

        # Procesar y eliminar lista de procesos de servicio
        if [ -f procesos_servicio ]; then
            while IFS= read -r line; do
            pid=$(echo "$line" | awk '{print $1}')
            log_to_bible "El proceso $pid ha terminado."
            if ps -p "$pid" >/dev/null 2>&1; then
                kill "$pid" 2>/dev/null
            fi
            pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
            sed -i "/^$pattern /d" procesos_servicio
            done <procesos_servicio
            rm -f procesos_servicio
        fi

        # Procesar y eliminar lista de procesos periódicos
        if [ -f procesos_periodicos ]; then
            while IFS= read -r line; do
            pid=$(echo "$line" | awk '{print $3}')
            log_to_bible "El proceso $pid ha terminado."
            if ps -p "$pid" >/dev/null 2>&1; then
                kill "$pid" 2>/dev/null
            fi
            pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
            sed -i "/^.* .* $pattern /d" procesos_periodicos
            done <procesos_periodicos
            rm -f procesos_periodicos
        fi
        # Eliminar directorio Infierno y fichero SanPedro
        rm -rf Infierno SanPedro.lock
        rm -f Apocalipsis SanPedro
        log_to_bible "Se acabó el mundo."
    fi
    flock -u 200
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
