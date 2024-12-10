#!/bin/bash
#este archivo es un scrip que:
#1 compila los fuentes padre.c e hijo.c con gcc
#2 crea el fihero fifo "resultado"
#lanza un cat en segundo plano para leer "resultado"  
#lanza el proceso padre
#al acabar limpia todos los ficheros que ha creado


# Compilar padre.c e hijo.c
gcc -o PADRE Trabajo2/padre.c -lrt
gcc -o HIJO Trabajo2/hijo.c -lrt

# 2. Crear FIFO resultado
FIFO="resultado"
[ -e $FIFO ] || mkfifo $FIFO

# 3. Lanzar cat en segundo plano
cat $FIFO &
CAT_PID=$!

# 4. Ejecutar el proceso PADRE
./PADRE 10

# 5. Limpiar recursos
wait $CAT_PID
rm -f PADRE HIJO $FIFO
echo "Archivos temporales eliminados."

