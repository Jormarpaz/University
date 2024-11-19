#!/bin/bash

# --- Definimos nombres de los ficheros y carpetas ---
FILES=("procesos" "procesos_servicio" "procesos_periodicos" "SanPedro")
FOLDER="Infierno"
DEMONIO="./Demonio.sh"
BIBLIA="Biblia.txt"

# --- Función para registrar eventos en la Biblia ---
log_event() {
    echo "$(date +'%H:%M:%S') [Fausto] $1" >> $BIBLIA
}

# --- Función para iniciar el demonio ---
start_demonio() {
    if ! pgrep -f "$DEMONIO" >/dev/null; then
        log_event "---------------Génesis---------------"
        log_event "El demonio ha sido creado"
        nohup bash -c "$DEMONIO" >/dev/null 2>&1 &
        demonio_pid=$!
        log_event "Demonio lanzado con PID $demonio_pid"
    else
        log_event "El demonio ya está en ejecución."
    fi
}

check_demonio() {
    if ! pgrep -f "$DEMONIO" >/dev/null; then
        log_event "El demonio no está en ejecución. Reiniciando estructuras..."
        # Crea estructuras necesarias si faltan
        for file in "${FILES[@]}"; do
            [ ! -f "$file" ] && log_event "Creando archivo $file..." && touch "$file"
        done
        [ ! -d "$FOLDER" ] && log_event "Creando carpeta $FOLDER..." && mkdir -p "$FOLDER"

        # Reiniciar el demonio
        log_event "Reiniciando el demonio..."
        start_demonio

        # Verificar si el demonio está activo tras el reinicio
        sleep 2  # Pequeño retraso para permitir que el demonio arranque
        if ! pgrep -f "$DEMONIO" >/dev/null; then
            log_event "Error: El demonio sigue sin estar activo tras reinicio."
        fi
    else
        log_event "El demonio ya está en ejecución."
    fi
}

# --- Ejecutar un comando normal ---
run_command() {
    local cmd="$1"  # El comando a ejecutar
    local log_time=$(date +'%H:%M:%S')

    # Lanzar el comando en segundo plano usando bash
    bash -c "$cmd" &
    local pid=$!  # Capturar el PID del proceso bash lanzado

    # Añadir el PID y el comando al archivo procesos (sin condiciones de carrera)
    {
        flock -x 200  # Bloquear el archivo durante la escritura
        echo "$pid '$cmd'" >> procesos
    } 200<>procesos

    # Registrar el evento en la Biblia
    log_event "El proceso $pid '$cmd' ha nacido."

    # Retornar inmediatamente
    echo "Comando lanzado con PID $pid."
    log_event "Comando lanzado con PID $pid: $cmd"
}

# --- Ejecutar un comando como servicio ---
run_service() {
    local cmd="$1"  # El comando a ejecutar como servicio
    local log_time=$(date +'%H:%M:%S')

    # Lanzar el comando como servicio usando nohup para que se ejecute independientemente del terminal
    nohup bash -c "$cmd" > /dev/null 2>&1 &
    local pid=$!  # Capturar el PID del proceso bash lanzado

    # Añadir el PID y el comando al archivo procesos_servicio (sin condiciones de carrera)
    {
        flock -x 200  # Bloquear el archivo durante la escritura
        echo "$pid '$cmd'" >> procesos_servicio
    } 200<>procesos_servicio

    # Registrar el evento en la Biblia
    log_event "El servicio $pid '$cmd' ha sido creado."

    # Retornar inmediatamente
    echo "Servicio lanzado con PID $pid."
    log_event "Servicio lanzado con PID $pid: $cmd"
}

# --- Ejecutar un comando con reinicio periódico ---
run_periodic() {
    local period=$1  # El periodo en segundos
    local cmd="$2"   # El comando a ejecutar
    local log_time=$(date +'%H:%M:%S')

    # Ejecutar el comando por primera vez
    execute_command "$cmd" "$period"  # Llamar a una función para ejecutar el comando y reiniciarlo

    # Registrar el evento en la Biblia
    log_event "El proceso '$cmd' ha sido creado para ejecutarse periódicamente cada $period segundos."
}

# Función para ejecutar el comando y registrar el PID
execute_command() {
    local cmd="$1"
    local period="$2"
    local log_time=$(date +'%H:%M:%S')

    # Ejecutar el comando en segundo plano
    nohup bash -c "$cmd" > /dev/null 2>&1 &
    local pid=$!  # Capturar el PID del proceso bash lanzado

    # Guardar en procesos_periodicos: tiempo de ejecución, periodo, PID y comando
    {
        flock -x 200  # Bloquear el archivo durante la escritura
        echo "0 $period $pid '$cmd'" >> procesos_periodicos
    } 200<>procesos_periodicos

    # Registrar el nacimiento del proceso en Biblia.txt
    log_event "El proceso $pid '$cmd' ha nacido."
}

# --- Función para manejar el comando 'list' ---
list_processes() {
    log_event "Listando procesos."
    echo "***** Procesos *****"
    if [ -f "procesos" ]; then
        cat procesos
    else
        echo "El archivo 'procesos' no existe o está vacío."
    fi

    echo "***** Procesos_Servicio *****"
    if [ -f "procesos_servicio" ]; then
        cat procesos_servicio
    else
        echo "El archivo 'procesos_servicio' no existe o está vacío."
    fi

    echo "***** Procesos_Periodicos *****"
    if [ -f "procesos_periodicos" ]; then
        cat procesos_periodicos
    else
        echo "El archivo 'procesos_periodicos' no existe o está vacío."
    fi
}

# --- Función para detener un proceso ---
stop_process() {
    PID=$1

    # Verificar si el directorio Infierno existe, si no, crearlo
    if [ ! -d "./Infierno" ]; then
        mkdir ./Infierno
    fi

    # Sincronización: bloqueo para manipular archivos en Infierno
    (
        flock -x 200
        # Verificar si el PID está en alguno de los archivos de listas
        process_found=0

        # Comprobar si el PID está en la lista de procesos
        if grep -q "^$PID" "./procesos"; then
            process_found=1
        fi

        # Comprobar si el PID está en la lista de procesos_servicio
        if grep -q "^$PID" "./procesos_servicio"; then
            process_found=1
        fi

        # Comprobar si el PID está en la lista de procesos_periodicos
        if grep -q "^$PID" "./procesos_periodicos"; then
            process_found=1
        fi

        # Si el PID fue encontrado en alguna lista
        if [ $process_found -eq 1 ]; then
            # Crear el archivo vacío en el directorio Infierno
            touch "./Infierno/$PID"
            log_event "El proceso $PID ha sido marcado para ser detenido"
            echo "Proceso $PID detenido y marcado para eliminación."
        else
            # Si el PID no se encuentra en las listas
            echo "Error: El proceso con PID $PID no está en ninguna lista."
            echo "Por favor, usa './Fausto.sh list' para ver los procesos activos."
        fi
    ) 200>stop_process.lock
}

# --- Función para terminar todos los procesos y activar el Apocalipsis ---
end_system() {
    # Crear el archivo Apocalipsis en el directorio actual
    touch "./Apocalipsis"
    log_event "Apocalipsis: Fin de los tiempos, todos los procesos serán eliminados."
}

# --- Mostrar ayuda ---
show_help() {
    log_event "Mostrando ayuda."
    echo "  Uso: ./Fausto.sh [comando]"
    echo ""
    echo "  ***** Comandos disponibles *****"
    echo ""
    echo "  run comando             Ejecuta un proceso normal"
    echo "  run-service comando     Ejecuta un proceso como servicio"
    echo "  run-periodic T comando  Ejecuta un comando periódicamente cada T segundos"
    echo "  list                    Lista todos los procesos"
    echo "  help                    Muestra esta ayuda"
    echo "  stop PID                Detiene un proceso por su PID"
    echo "  end                     Finaliza el demonio y limpia recursos"
}

# --- Lógica principal ---
check_demonio

case "$1" in
    run)
        if [ -z "$2" ]; then
            echo "Error: Debes proporcionar un comando para ejecutar."
            exit 1
        fi
        run_command "$2"
        ;;
    run-service)
        if [ -z "$2" ]; then
            echo "Error: Debes proporcionar un comando para ejecutar como servicio."
            exit 1
        fi
        run_service "$2"
        ;;
    run-periodic)
        if [ -z "$2" ] || [ -z "$3" ]; then
            echo "Error: Debes proporcionar un período y un comando."
            exit 1
        fi
        run_periodic "$2" "$3"
        ;;
    list)
        list_processes
        ;;
    stop)
        if [ -z "$2" ]; then
            echo "Error: Debes especificar un PID."
        else
            stop_process $2
        fi
        ;;
    end)
        end_system
        ;;
    help)
        show_help
        ;;
    *)
        echo "Comando '$1' no reconocido. Usa 'help' para más información."
        ;;
esac