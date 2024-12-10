#!/bin/bash

cd Trabajo2/

# 1 compila los fuentes padre.c e hijo.c con gcc
gcc -o PADRE padre.c -lrt
gcc -o HIJO hijo.c -lrt

# 2 crea el fihero fifo "resultado"
FIFO="resultado"
if [ ! -e "$FIFO" ]; then
    mkfifo "$FIFO"
    echo "[SCRIPT] FIFO 'resultado' creado."
else
    echo "[SCRIPT] FIFO 'resultado' ya existe."
fi

# lanza un cat en segundo plano para leer "resultado"  
cat $FIFO &
CAT_PID=$!
echo "[SCRIPT] Proceso cat lanzado con PID $CAT_PID."

# lanza el proceso padre
./PADRE

# al acabar limpia todos los ficheros que ha creado
echo "Eliminando procesos residuales..."
pkill -f HIJO
pkill -f PADRE
pkill -f "cat resultado"

# Eliminar archivos temporales
rm -f PADRE HIJO resultado
echo "Archivos temporales eliminados."
