BIBLIA="Biblia.txt"
LOCK_FILE="SanPedro"

registrar_biblia(){
	flock "$LOCK_FILE" -c "echo "$(date +%T) $1" >> "$BIBLIA""
}

terminar_procesos(){
	local pid=$1
	local hijos=$(pstree -p $pid | grep -oP '\(\d+\)' | tr -d '()')
	for hijo in $hijos; do
		kill -TERM $hijo
	done
	kill -TERM $pid
	registrar_biblia "El proceso $pid ha terminado."
} 


while true; do
	if [ -f "Apocalipsis" ]; then
		flock "$LOCK_FILE" bash -c '
		registrar_biblia "---------------Apocalipsis---------------"
		
		for archivo in procesos procesos_servicio procesos_periodicos; do
			if [ -f "$archivo" ]; then
				while read -r linea; do
					pid=$(echo $linea | awk '{print \$1}')
					if ps -p $pid > /dev/null; then
						terminar_procesos $pid
						registrar_biblia "El proceso $pid ha terminado"
					fi
				done < "$archivo"
				> "$archivo"
			fi
		done
		
		rm -f Apocalipsis
		rm -rf Infierno
		registrar_biblia "Se acabó el mundo."
		'
		exit 0
	fi

	sleep 1
	
	flock "$LOCK_FILE" bash -c '
	if [ -f "procesos" ]; then
		while read -r linea; do
			pid=$(echo $linea | awk '{print \$1}')
			if [ -f "Infierno/$pid" ]; then
				terminar_procesos $pid
				registrar_biblia "El proceso $pid ha terminado"
				sed -i "/$linea/d" procesos
				rm -f "Infierno/$pid"
			elif ! ps -p $pid > /dev/null; then
				registrar_biblia "El proceso $pid ha terminado"
				sed -i "/$linea/d" procesos
			fi
		done < procesos
	fi

	if [ -f "procesos_servicio" ]; then
    		while read -r linea; do
        		pid=$(echo $linea | awk '{print \$1}')
        		if ! ps -p $pid > /dev/null; then
					cmd=$(echo $linea | awk '{\$1=\"\"; print \$0}' | xargs)
					nohup bash -c "$cmd" &> /dev/null & new_pid=$!
					registrar_biblia "El proceso $pid resucita con pid $new_pid"
					sed -i "s/$pid/$new_pid/" procesos_servicio
        		fi
    		done < procesos_servicio
	fi
	
	if [ -f "procesos_periodicos" ]; then
		while read -r linea; do
			contador=$(echo $linea | awk '{print \$1}')
			periodo=$(echo $linea | awk '{print \$2}')
			pid=$(echo $linea | awk '{print \$3}')
			cmd=$(echo $linea | awk '{\$1=\$2=\$3=\"\"; print \$0}' | xargs)

			if [ ! -f "Infierno/$pid" ]; then
				if ! ps -p $pid >/dev/null; then
					contador=$((contador + 1))
					if [ "$contador" -ge "$periodo" ]; then
						nohup bash -c "$cmd" &> /dev/null & new_pid=$!
						registrar_biblia "El proceso $pid '$cmd' se reencarna con pid $new_pid"
						echo "0 $periodo $new_pid '$cmd'" >> procesos_periodicos
						sed -i "/$linea/d" procesos_periodicos
					else
						echo "$contador $periodo $pid '$cmd'" >> procesos_periodicos.tmp
					fi
				else
					echo "$contador $periodo $pid '$cmd'" >> procesos_periodicos.tmp
				fi
			else
				sed -i "/$linea/d" procesos_periodicos
			fi
		done < procesos_periodicos
		mv procesos_periodicos.tmp procesos_periodicos	
	fi
	'
done





