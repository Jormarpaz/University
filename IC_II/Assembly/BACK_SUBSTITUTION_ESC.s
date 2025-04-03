;*****************************************************
;**********************PEC_IC_II**********************
;*****************************************************

		.data
		.align 3			; Alinear para accesos más rápidos
U:		.double 1,2, -2, 4	; Filas matriz U
		.double 0,1, -5, -3
		.double 0,0, 1, 1
		.double 0,0, 0, 1
x:		.space	32		; Fila matriz X (4*8 bytes = 32)
b:		.double -5,0,-3,6	; Fila matriz b
n:		.word 4			; Tamaño problema


		.text
		.global main
main:		
		addi r1, r0, U	; puntero de U
		addi r2, r0, x	; puntero de x
		addi r3, r0, b	; puntero de b
		addi r4, r0, n	; puntero de n
		lw r5, 0(r4)	; guardo valor de k = n
		lw r6, 0(r4)	; cargo n en r6
		addi r7, r0, 8	; constante 8 para multiplicaciones
iniciobucle:
		BEQZ r5, findebucle	; si k = 0, salto al final

		subi r17, r5, 1		; k-1 porque bits van de 0 a 31
		mult r8, r17, r7	; k * 8 para desplazamiento

		add r9, r3, r8		; puntero b[k] 
		add r10, r2, r8		; puntero x[k] 
		ld f0, 0(r9)		; carga b[k] en f0

		addi r11, r5, 1	; i = k + 1
		sd 0(r10), f0		; x[k] = b[k]

iniciobuclei:
		SLE r12, r11, r6	; si i <= n, continuar
		BEQZ r12, findebuclei

		subi r18, r11, 1	; i-1 porque bits van de 0 a 31
		
		mult r13, r17, r6	; k * n 
		mult r15, r18, r7	; i * 8		
		add r13, r13, r18	; k * n + i
		add r16, r2, r15	; puntero x[i]
		mult r13, r13, r7	; (k * n + i) * 8
		ld f4, 0(r16)		; carga x[i]
		add r14, r1, r13	; puntero U[k, i]
		ld f2, 0(r14)		; carga U[k, i]
		
		ld f6, 0(r10)		; carga x[k]

		multd f8, f4, f2	; x[i] * U[k, i]
		addi r11, r11, 1	; i++
		subd f6, f6, f8		; x[k] = x[k] - (x[i] * U[k, i])
		sd 0(r10), f6		; guardar x[k]

		J iniciobuclei
findebuclei:
		subi r5, r5, 1	; k--
		J iniciobucle
findebucle:
		trap 6		; fin de programa
