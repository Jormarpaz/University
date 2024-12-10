#!/bin/bash
cd Trabajo2/

# Compilar los archivos padre.c e hijo.c
gcc -o PADRE padre.c -lrt
gcc -o HIJO hijo.c -lrt

# Crear FIFO resultado
FIFO="resultado"
if [ ! -e "$FIFO" ]; then
    mkfifo "$FIFO"
    echo "[SCRIPT] FIFO 'resultado' creado."
else
    echo "[SCRIPT] FIFO ya existe."
fi

# Ejecutar el proceso PADRE
./PADRE

# Limpieza al final
rm -f PADRE HIJO $FIFO
echo "[SCRIPT] Limpieza completada."
