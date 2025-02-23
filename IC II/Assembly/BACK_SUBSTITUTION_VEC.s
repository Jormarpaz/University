;*****************************************************
;**********************PEC_IC_II**********************
;*****************************************************

		.data

        .align 3                ; Alinear para accesos mas rapidos
U:      .double 1, 2, -2, 4
        .double 0, 1, -5, -3
        .double 0, 0, 1, 1
        .double 0, 0, 0, 1      ; Matriz U (triangular superior)

x:      .space 32               ; Vector x (4*8 bytes = 32)

b:      .double -5, 0, -3, 6    ; Vector b

n:      .word 4                 ; Tamaño del sistema de ecuaciones

        .text
        .global main

main:
        addi r1, r0, U          ; Puntero a la matriz U
        addi r2, r0, x          ; Puntero a x
        addi r3, r0, b          ; Puntero a b
        addi r4, r0, n          ; Puntero a n
        lw r5, 0(r4)            ; Cargar n en r5
        lw r6, 0(r4)            ; Copia de n en r6	
        addi r7, r0, 8          ; Constante 8 para desplazamientos
        mult r18, r7, r6        ; 4 * 8 para desplazamientos (vlr = 4)

iniciobucle:
        beqz r5, findebucle     ; Si k = 0, finaliza

        subi r17, r5, 1         ; k - 1 
        mult r8, r17, r7        ; Desplazamiento de k * 8 bytes

        add r9, r3, r8          ; Direccion de b[k]
        add r10, r2, r8         ; Direccion de x[k]
        ld f0, 0(r9)            ; Cargar b[k] en f0

        addi r11, r5, 1         ; i = k + 1
        sd 0(r10), f0           ; x[k] = b[k]

        ; Implementacion vectorial eliminando el bucle interno
        sub r19, r6, r5         ; Cantidad de elementos a procesar (n - k)
        movi2s vlr, r19         ; Cargar vlr con el numero de elementos
	
	lv v4, 0(r10)           ; Cargar x[k] en v4

        mult r13, r17, r6       ; k * n
        subi r12, r11, 1        ; i = i - 1
        add r13, r13, r12       ; k * n + i
        mult r16, r12, r7       ; i * 8 para desplazamiento
        mult r13, r13, r7       ; (k * n + i) * 8 para direcciones
        add r14, r2, r16        ; Direccion de x[i]
        add r15, r1, r13        ; Direccion de U[k,i]

        lv v1, 0(r14)           ; Cargar vector x[i]
        lv v2, 0(r15)           ; Cargar vector U[k,i]

        multv v3, v1, v2        ; Vector v3 = x[i] * U[k,i]
	add r22,r0,r5		; inicializo contador k
	addi r25,r0,0		; le doy un 0 a r25
	movi2fp f8,r25		; le paso el 0 a r25
	sv 0(r20),v3		; guardo v3 en direccion r20
	subd f8,f8,f8		; por si acaso, elimino f8

bucle_suma:
		sne r24,r22,r6
		beqz r24,finsuma

		ld f2, 0(r20)		; cargo el valor de v3 en f2
		addi r20,r20,8		; aumento el puntero
		addi r22,r22,1		; aumento k
		addd f8,f8,f2		; guardo la suma de ambos
			
		j bucle_suma
finsuma:
	beqz r19,saltar
	addi r25,r0,1
	movi2s vlr,r25		; cambio el vlr
saltar:        

        subvs v5,v4,f8         ; x[k] = x[k] - (x[i] * U[k,i])
	subi r5, r5, 1          ; k--
        sv 0(r10), v5           ; Guardar x[k]
        
        j iniciobucle

findebucle:
        trap 6                  ; Finalizar ejecucion


