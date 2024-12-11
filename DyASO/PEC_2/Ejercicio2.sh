#!/bin/bash

# Navegar al directorio de trabajo
cd Trabajo2/

# 1. Compilar los archivos fuente
gcc -o PADRE padre.c -lrt -pthread
gcc -o HIJO hijo.c -lrt -pthread

# 2. Crear FIFO 'resultado' (para depuración con cat, opcional)
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

# 4. Ejecutar el proceso PADRE
echo "[SCRIPT] Ejecutando PADRE..."
./PADRE "$FIFO"

# 5. Esperar a que el proceso `cat` termine
wait $CAT_PID

# 6. Verificar si los recursos IPC se han liberado correctamente
echo "[SCRIPT] Verificando recursos IPC..."
ipcs -q | grep -q "0x5678" && echo "[SCRIPT] Cola de mensajes no liberada." || echo "[SCRIPT] Cola de mensajes liberada."
ipcs -m | grep -q "0x9012" && echo "[SCRIPT] Memoria compartida no liberada." || echo "[SCRIPT] Memoria compartida liberada."
ipcs -s | grep -q "0x1234" && echo "[SCRIPT] Semáforo no liberado." || echo "[SCRIPT] Semáforo liberado."

# 7. Eliminar los archivos temporales y ejecutables
rm -f PADRE HIJO $FIFO
echo "[SCRIPT] Limpieza completada."
