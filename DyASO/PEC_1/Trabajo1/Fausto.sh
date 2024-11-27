#!/bin/bash

# --- Definimos nombres de los ficheros y carpetas ---
FILES=("procesos" "procesos_servicio" "procesos_periodicos" "SanPedro")
FOLDER="Infierno"
DEMONIO="./Demonio.sh"
BIBLIA="Biblia.txt"

# --- Función para registrar eventos en la Biblia ---
log_event() {
    echo "$(date +'%H:%M:%S') [Fausto] $1" >>$BIBLIA
}

check_demonio() {
    if pgrep -f "./Demonio.sh" >/dev/null; then
        return 0
    fi

    # Si no está en ejecución, reiniciar estructuras y lanzar el demonio
    log_event "El demonio no está en ejecución. Reiniciando estructuras..."
    [ ! -f procesos ] && touch procesos
    [ ! -f procesos_servicio ] && touch procesos_servicio
    [ ! -f procesos_periodicos ] && touch procesos_periodicos
    [ ! -d Infierno ] && mkdir Infierno

    # Vaciar Biblia.txt si ya existe al iniciar Fausto
    if [ -f Biblia.txt ]; then
        > Biblia.txt
    fi

    # Iniciar el demonio
    log_event "---------------Génesis---------------"
    log_event "El demonio ha sido creado"
    nohup bash -c "$DEMONIO" >/dev/null 2>&1 &
    demonio_pid=$!
}

# --- Ejecutar un comando normal ---
run_command() {
    local cmd="$1" # El comando a ejecutar

    if [ ! -f "SanPedro.lock" ]; then
        touch SanPedro.lock
    fi

    # Lanzar el comando en segundo plano usando bash
    nohup bash -c "$cmd" >/dev/null 2>&1 &
    local pid=$! # Capturar el PID del proceso bash lanzado

    exec 200>SanPedro.lock
    flock -x 200

    echo "$pid '$cmd'" >> procesos

    flock -u 200

    # Registrar el evento en la Biblia
    log_event " El proceso $pid '$cmd' ha nacido."

    # Retornar inmediatamente
    return 0

}

# --- Ejecutar un comando como servicio ---
run_service() {
    local cmd="$1" # El comando a ejecutar como servicio

    if [ ! -f "SanPedro.lock" ]; then
        touch SanPedro.lock
    fi

    # Lanzar el comando como servicio usando nohup para que se ejecute independientemente del terminal
    nohup bash -c "$cmd" >/dev/null 2>&1 &
    local pid=$! # Capturar el PID del proceso bash lanzado

    exec 200>SanPedro.lock
    flock -x 200

    echo "$pid '$cmd'" >> procesos_servicio

    flock -u 200

    # Registrar el evento en la Biblia
    log_event " El proceso $pid '$cmd' ha nacido."

    # Retornar inmediatamente
    return 0
}

# --- Ejecutar un comando con reinicio periódico ---
run_periodic() {
    local period=$1 # El periodo en segundos
    local cmd="$2"  # El comando a ejecutar

   # Ejecutar el comando en segundo plano
    nohup bash -c "$cmd" >/dev/null 2>&1 &
    local pid=$! # Capturar el PID del proceso bash lanzado

    sleep 1

    flock SanPedro echo "0 $period $pid '$cmd'" >> procesos_periodicos

    # Registrar el evento en la Biblia
    log_event " El proceso '$cmd' ha nacido para ejecutarse periódicamente cada $period segundos."
}

# --- Función para manejar el comando 'list' ---
list_processes() {

        echo \"***** Procesos *****\"
        cat procesos

        echo \"***** Procesos_Servicio *****\"
        cat procesos_servicio

        echo \"***** Procesos_Periodicos *****\"
        cat procesos_periodicos
    
}

# --- Función para detener un proceso ---
stop_process() {
    PID=$1
    process_found=0

    exec 200>SanPedro.lock

    flock -x 200
    
        # Comprobar si el PID está en la lista de procesos
        if grep -q "^$PID" procesos; then
            process_found=1
        fi

        # Comprobar si el PID está en la lista de procesos_servicio
        if grep -q "^$PID" procesos_servicio; then
            process_found=1
        fi

        # Comprobar si el PID está en la lista de procesos_periodicos
        if grep -q "^$PID" procesos_periodicos; then
            process_found=1
        fi

        if [ "$process_found" -eq 1 ]; then
            mkdir -p ./Infierno
            touch ./Infierno/$PID
            echo "$PID marcado para eliminación" >&2
        else
            echo "Error: Proceso $PID no encontrado en ninguna lista" >&2
        fi
    
    flock -u 200
}

# --- Función para terminar todos los procesos y activar el Apocalipsis ---
end_system() {
    # Crear el archivo Apocalipsis en el directorio actual
    touch "./Apocalipsis"
}

# --- Mostrar ayuda ---
show_help() {
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

# Fausto.sh
COMMAND=$1
shift

# Verificar el demonio antes de procesar cualquier comando
check_demonio

case $COMMAND in
run)
    if [ "$#" -eq 0 ]; then
        echo "Error: No se pasaron argumentos."
        exit 1
    fi
    run_command "$@"
    ;;
run-service)
    if [ "$#" -eq 0 ]; then
        echo "Error: Debes proporcionar un comando para ejecutar como servicio."
        exit 1
    fi
    run_service "$@"
    ;;
run-periodic)
    if [ "$#" -lt 2 ]; then
        echo "Error: Debes proporcionar un período y un comando."
        exit 1
    fi
    run_periodic "$@"
    ;;
list)
    list_processes
    ;;
stop)
    if [ "$#" -eq 0 ]; then
        echo "Error: Debes especificar un PID."
    else
        stop_process $@
    fi
    ;;
end)
    end_system
    ;;
help)
    show_help
    ;;
*)
    echo "Comando '$COMMAND' no reconocido. Usa 'help' para más información."
    ;;
esac
