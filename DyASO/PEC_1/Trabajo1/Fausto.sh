iniciar_demonio(){
	echo "Iniciando el proceso demonio..."
	nohup ./Demonio.sh &> /dev/null &
	PID=$!
	echo "$(date +%T): ---------------Génesis---------------" >> Biblia.txt
	echo "$(date +%T): El demonio ha sido creado" >> Biblia.txt
}

reiniciar_estructuras(){
	
	ARCHIVOS=("procesos" "procesos_servicio" "procesos_periodicos" "Biblia.txt" "SanPedro")
	DIRECTORIO="Infierno"
	APOCALIPSIS="Apocalipsis"

	for archivo in "${ARCHIVOS[@]}"; do
		if [ -f "$archivo" ]; then
			rm "$archivo"
		fi
	done

	if [ -f "$APOCALIPSIS" ]; then
		rm "$APOCALIPSIS"
	fi

	if [ -d "$DIRECTORIO" ]; then
		rm -r "$DIRECTORIO"
	fi

	for archivo in "${ARCHIVOS[@]}"; do
		touch "$archivo"
	done

	mkdir "$DIRECTORIO"
	echo "$(date +%T) Estructuras reiniciadas." >> Biblia.txt
}

ejecutar_comando() {
	local tipo=$1
	local cmd=$2
	local period=$3

	case "$tipo" in
        run)
            nohup bash -c "$cmd" &> /dev/null & PID=$!
            if [ -n "$PID" ]; then
                        echo "$PID '$cmd'" >> procesos
                        echo "$(date +%T): El proceso $PID ejecutando '$cmd' ha nacido." >> Biblia.txt
                    fi
                    ;;
        run-service)
            nohup bash -c "$cmd" &> /dev/null & PID=$!
            if [ -n "$PID" ]; then
                        echo "$PID '$cmd'" >> procesos_servicio
                        echo "$(date +%T): El proceso $PID ejecutando '$cmd' ha nacido." >> Biblia.txt
                    fi
                    ;;
        run-periodic)
            nohup bash -c "$cmd" &> /dev/null & PID=$!
            if [ -n "$PID" ]; then
                        echo "0 $period $PID '$cmd'" >> procesos_periodicos
                        echo "$(date +%T): El proceso $PID ejecutando '$cmd' ha nacido." >> Biblia.txt
                    fi
                    ;;
        *)
            echo "Error, orden "$*" no reconocida, consulte las órdenes disponibles con ./Fausto.sh help"
            ;;
    esac
}

mostrar_ayuda() {
	echo "***** Comandos disponibles *****"
	echo "./Fausto.sh run 'comando'"
	echo "./Fausto.sh run-service 'comando'"
	echo "./Fausto.sh run-periodic T 'comando'"
	echo "./Fausto.sh list"
	echo "./Fausto.sh help"
	echo "./Fausto.sh stop PID"
	echo "./Fausto.sh end"
}

listar_procesos(){
	echo "**** Procesos Normales *****"
	flock SanPedro -c 'cat procesos'
	echo "**** Procesos Servicio *****"
	flock SanPedro -c 'cat procesos_servicio'
	echo "**** Procesos Periódicos *****"
	flock SanPedro -c 'cat procesos_periodicos'
}

detener_proceso(){
	local pid=$1
        if grep -q "$pid" procesos_servicio; then
		sed -i "/$pid/d" procesos_servicio
		echo "$(date +%T): El proceso $pid ha sido detenido y no será reiniciado." >> Biblia.txt
	fi
}

terminar_sistema(){
	touch "Apocalipsis"
	echo "$(date +%T): ---------------Apocalipsis---------------" >> Biblia.txt

	sleep 1
	
	ARCHIVOS_RESIDUALES=("procesos" "procesos_servicio" "procesos_periodicos" "SanPedro" "Apocalipsis")
	DIRECTORIO="Infierno"

	for archivo in "${ARCHIVOS_RESIDUALES[@]}"; do
		if [ -f "$archivo" ]; then
			rm "$archivo"
		fi
	done

	if [ -d "$DIRECTORIO" ]; then
		rm -r "$DIRECTORIO"
	fi
	
	echo "$(date +%T): Comprobando procesos vivos tras el Apocalipsis..." >> Biblia.txt
    echo "$(date +%T): Verificación completa." >> Biblia.txt
	
	echo "$(date +%T): Se acabó el mundo." >> Biblia.txt
}


if ! pgrep -f Demonio.sh >/dev/null; then
	echo "El demonio no está en ejecución. Reiniciando estructuras..."
	reiniciar_estructuras
	iniciar_demonio
fi

case "$1" in
	run)
        	ejecutar_comando "run" "$2"
        	;;
	run-service)
        	ejecutar_comando "run-service" "$2"
        	;;
	run-periodic)
        	ejecutar_comando "run-periodic" "$2" "$3"
        	;;
	list)
        	listar_procesos
        	;;
	help)
		mostrar_ayuda
		;;
	stop)
		detener_proceso "$2"
		;;
	end)
		terminar_sistema
		;;
	*)
		echo "Error: comando "$*" no reconocido. Use ./Fausto.sh help para ver la lista de comandos disponibles."
		;;
esac


