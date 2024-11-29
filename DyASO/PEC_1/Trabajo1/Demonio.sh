#!/bin/bash

# Detiene el script en errores
set -e
# Captura errores y registra el evento en Biblia.txt
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

    # Crear y bloquear el archivo SanPedro.lock
    exec 200>SanPedro.lock
    if flock -w 5 200; then
        # Procesar lista de procesos normales
        if [ -f procesos ]; then
            while read -r pid comando; do
                # Verificar si el proceso sigue activo
                if ! ps -p "$pid" >/dev/null 2>&1; then
                    log_to_bible "El proceso $pid ha terminado."
                    # Eliminar el proceso terminado de la lista
                    sed -i "/^$(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos
                fi
            done <procesos
        fi

        # Procesar lista de procesos de servicio
        if [ -f procesos_servicio ]; then
            while read -r pid comando; do
                # Verificar si el proceso sigue activo
                if ! ps -p "$pid" >/dev/null 2>&1; then
                    if [ -f "./Infierno/$pid" ]; then
                        log_to_bible "El proceso $pid ha sido eliminado manualmente."
                        # Eliminar el archivo del proceso en Infierno
                        rm -f "./Infierno/$pid"
                        # Eliminar el proceso terminado de la lista
                        sed -i "/^$(echo "$pid" | sed 's/[][\.*^$/]/\\&/g') /d" procesos_servicio
                    else
                        log_to_bible "El proceso $pid ha terminado."
                        # Reiniciar el proceso si no está en Infierno
                        nohup bash -c "$comando" >/dev/null 2>&1 &
                        new_pid=$!
                        escaped_pid=$(printf '%s\n' "$pid" | sed 's/[][\.*^$/]/\\&/g')
                        # Actualizar la lista con el nuevo PID
                        sed -i "s/^$escaped_pid /$new_pid /" procesos_servicio
                        log_to_bible "El proceso $pid resucita con pid $new_pid."
                    fi
                fi
            done <procesos_servicio
        fi

        if [ -f procesos_periodicos ]; then

            # Obtener el timestamp actual
            current_time=$(date +%s)
            elapsed=$((current_time - last_update)) # Calcular tiempo transcurrido
            last_update=$current_time               # Actualizar la última iteración

            # Crear un archivo temporal para procesar las líneas actualizadas
            temp_file=$(mktemp)

            while IFS= read -r line; do

                # Extraer campos: tiempo, período, PID y comando
                time=$(echo "$line" | awk '{print $1}')
                period=$(echo "$line" | awk '{print $2}')
                pid=$(echo "$line" | awk '{print $3}')
                comando=$(echo "$line" | cut -d' ' -f4- | sed "s/^'//;s/'$//")

                # Incrementar el tiempo acumulado
                time=$((time + elapsed))

                if ps -p "$pid" >/dev/null 2>&1; then
                    :
                else
                    if ((time >= period)); then
                        # Reencarnar el proceso si ha alcanzado el período
                        nohup bash -c "$comando" >/dev/null 2>&1 &
                        new_pid=$!
                        log_to_bible "El proceso periódico $pid se reencarna con pid $new_pid."
                        time=0 # Reiniciar el tiempo tras reencarnar
                        pid=$new_pid
                    fi
                fi

                # Guardar la línea actualizada en el archivo temporal
                echo "$time $period $pid '$comando'" >>"$temp_file"
                sleep 1
            done <procesos_periodicos

            # Reemplazar el archivo original con el archivo temporal
            mv "$temp_file" procesos_periodicos
        fi
    fi
    # Liberar el bloqueo del archivo SanPedro.lock
    flock -u 200

}

# --- Función para manejar el Apocalipsis ---
Apocalipsis() {

    log_to_bible "---------------Apocalipsis---------------"
    log_to_bible "Iniciando la eliminación de procesos y listas."

    # Crear y bloquear el archivo SanPedro.lock
    exec 200>SanPedro.lock
    if flock -w 5 200; then
        # Procesar y eliminar lista de procesos normales
        if [ -f procesos ]; then
            while IFS= read -r line; do
                pid=$(echo "$line" | awk '{print $1}')
                log_to_bible "El proceso $pid ha terminado."
                # Verificar si el proceso sigue activo y terminarlo
                if ps -p "$pid" >/dev/null 2>&1; then
                    kill "$pid" 2>/dev/null
                fi
                # Eliminar el proceso terminado de la lista
                pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
                sed -i "/^$pattern /d" procesos
            done <procesos
            # Eliminar el archivo de procesos
            rm -f procesos
        fi

        # Procesar y eliminar lista de procesos de servicio
        if [ -f procesos_servicio ]; then
            while IFS= read -r line; do
                pid=$(echo "$line" | awk '{print $1}')
                log_to_bible "El proceso $pid ha terminado."
                # Verificar si el proceso sigue activo y terminarlo
                if ps -p "$pid" >/dev/null 2>&1; then
                    kill "$pid" 2>/dev/null
                fi
                # Eliminar el proceso terminado de la lista
                pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
                sed -i "/^$pattern /d" procesos_servicio
            done <procesos_servicio
            # Eliminar el archivo de procesos de servicio
            rm -f procesos_servicio
        fi

        # Procesar y eliminar lista de procesos periódicos
        if [ -f procesos_periodicos ]; then
            while IFS= read -r line; do
                pid=$(echo "$line" | awk '{print $3}')
                log_to_bible "El proceso $pid ha terminado."
                # Verificar si el proceso sigue activo y terminarlo
                if ps -p "$pid" >/dev/null 2>&1; then
                    kill "$pid" 2>/dev/null
                fi
                # Eliminar el proceso terminado de la lista
                pattern=$(echo "$pid" | sed 's/[][\.*^$\/]/\\&/g')
                sed -i "/^.* .* $pattern /d" procesos_periodicos
            done <procesos_periodicos
            # Eliminar el archivo de procesos periódicos
            rm -f procesos_periodicos
        fi
        # Eliminar directorio Infierno y fichero SanPedro
        rm -rf Infierno SanPedro.lock
        rm -f Apocalipsis SanPedro
        log_to_bible "Se acabó el mundo."
    fi
    # Liberar el bloqueo del archivo SanPedro.lock
    flock -u 200
}

# Exportar la función Apocalipsis
export -f Apocalipsis

# --- Bucle principal del demonio ---
while true; do
    sleep 1

    # Comprobar si ha llegado el Apocalipsis
    if [ -f "./Apocalipsis" ]; then
        Apocalipsis
        # Finalizar el bucle después del Apocalipsis
        exit 0
    fi

    # Manejar listas
    if ! handle_lists; then
        log_to_bible "Error: No se pudo manejar las listas."
    fi
done
