#!/bin/bash

# Navegar al directorio de trabajo
cd Trabajo2/

# 1. Compilar los archivos fuente
gcc -o PADRE padre.c -lrt -pthread
gcc -o HIJO hijo.c -lrt -pthread

# 2. Crear FIFO 'resultado'
FIFO="resultado"
if [ ! -e "$FIFO" ]; then
    mkfifo "$FIFO"
    echo "[SCRIPT] FIFO 'resultado' creado."
else
    echo "[SCRIPT] FIFO ya existe."
fi

# 3. Lanzar un proceso `cat` en segundo plano para leer el resultado del FIFO
cat $FIFO &
CAT_PID=$!
echo "[SCRIPT] Proceso cat lanzado con PID $CAT_PID."

# 4. Ejecutar el proceso PADRE con el argumento FIFO
echo "[SCRIPT] Ejecutando PADRE con el argumento $FIFO..."
./PADRE "$FIFO"

# 5. Esperar a que el proceso `cat` termine
wait $CAT_PID

# 6. Eliminar los archivos temporales y ejecutables
rm -f PADRE HIJO $FIFO
echo "[SCRIPT] Limpieza completada."
