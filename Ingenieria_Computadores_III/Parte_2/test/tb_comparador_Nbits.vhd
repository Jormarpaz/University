LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY tb_comparador_Nbits IS
END tb_comparador_Nbits;

ARCHITECTURE test OF tb_comparador_Nbits IS
    CONSTANT N : INTEGER := 4;
    SIGNAL x, y : STD_LOGIC_VECTOR (N - 1 DOWNTO 0);
    SIGNAL gout, eout : STD_LOGIC;
    SIGNAL gin, ein : STD_LOGIC;

    COMPONENT comparador_Nbits IS
        GENERIC (N : INTEGER := 4);
        PORT (
            gout : OUT STD_LOGIC;
            eout : OUT STD_LOGIC;
            gin : IN STD_LOGIC;
            ein : IN STD_LOGIC;
            x : IN STD_LOGIC_VECTOR (N - 1 DOWNTO 0);
            y : IN STD_LOGIC_VECTOR (N - 1 DOWNTO 0)
        );
    END COMPONENT;
BEGIN
    uut : comparador_Nbits
    GENERIC MAP(N => N)
    PORT MAP(
        gout => gout,
        eout => eout,
        gin => gin,
        ein => ein,
        x => x,
        y => y
    );

    -- Proceso de estimulación
    PROCESS
        VARIABLE error_count : INTEGER := 0;
    BEGIN
        -- Prueba 1: Modo normal (ein=1, gin=0) - Todas combinaciones para N=4
        REPORT "Probando modo normal (ein=1, gin=0) - Todas combinaciones";
        gin <= '0';
        ein <= '1';

        -- Probar todas las combinaciones posibles para N=4
        FOR i IN 0 TO 2 ** N - 1 LOOP
            FOR j IN 0 TO 2 ** N - 1 LOOP
                x <= STD_LOGIC_VECTOR(to_unsigned(i, N));
                y <= STD_LOGIC_VECTOR(to_unsigned(j, N));
                WAIT FOR 10 ns;

                -- Verificación
                IF (i > j AND gout /= '1') OR (i <= j AND gout = '1') THEN
                    error_count := error_count + 1;
                END IF;

                IF (i = j AND eout /= '1') OR (i /= j AND eout = '1') THEN
                    error_count := error_count + 1;
                END IF;
            END LOOP;
        END LOOP;

        -- Prueba 2: Casos específicos críticos de la tabla de verdad
        REPORT "Probando casos especificos criticos de la tabla de verdad";

        -- Caso 2.1: x=0, y=0, ein=1 (debería activar eout)
        REPORT "  Probando x=0,y=0,ein=1 (deberia activar eout)";
        gin <= '0';
        ein <= '1';
        x <= "0000"; -- 0
        y <= "0000"; -- 0
        WAIT FOR 10 ns;
        IF eout /= '1' THEN
            REPORT "Error: x=0,y=0,ein=1 pero eout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Caso 2.2: x=0, y=0, gin=1 (debería activar gout)
        REPORT "  Probando x=0,y=0,gin=1 (deberia activar gout)";
        gin <= '1';
        ein <= '0';
        x <= "0000"; -- 0
        y <= "0000"; -- 0
        WAIT FOR 10 ns;
        IF gout /= '1' THEN
            REPORT "Error: x=0,y=0,gin=1 pero gout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Caso 2.3: x=1, y=0, ein=1 (debería activar gout)
        REPORT "  Probando x=1,y=0,ein=1 (deberia activar gout)";
        gin <= '0';
        ein <= '1';
        x <= "0001"; -- 1
        y <= "0000"; -- 0
        WAIT FOR 10 ns;
        IF gout /= '1' THEN
            REPORT "Error: x=1,y=0,ein=1 pero gout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Caso 2.4: x=1, y=0, gin=1, ein=0 (debería activar gout)
        REPORT "  Probando x=1,y=0,gin=1,ein=0 (deberia activar gout)";
        gin <= '1';
        ein <= '0';
        x <= "0001"; -- 1
        y <= "0000"; -- 0
        WAIT FOR 10 ns;
        IF gout /= '1' THEN
            REPORT "Error: x=1,y=0,gin=1,ein=0 pero gout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;
        IF eout /= '0' THEN
            REPORT "Error: x=1,y=0,gin=1,ein=0 pero eout no es 0" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Caso 2.5: x=1, y=1, gin=0, ein=1 (debería activar eout)
        REPORT "  Probando x=1,y=1,gin=0,ein=1 (deberia activar eout)";
        gin <= '0';
        ein <= '1';
        x <= "0001"; -- 1
        y <= "0001"; -- 1
        WAIT FOR 10 ns;
        IF eout /= '1' THEN
            REPORT "Error: x=1,y=1,gin=0,ein=1 pero eout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Caso 2.6: x=1, y=1, gin=1, ein=0 (debería activar gout)
        REPORT "  Probando x=1,y=1,gin=1,ein=0 (deberia activar gout)";
        gin <= '1';
        ein <= '0';
        x <= "0001"; -- 1
        y <= "0001"; -- 1
        WAIT FOR 10 ns;
        IF gout /= '1' THEN
            REPORT "Error: x=1,y=1,gin=1,ein=0 pero gout no es 1" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Prueba 3: Caso especial del testbench original
        REPORT "Probando caso especial del testbench original";

        -- Caso 3.1: gin=1, ein=1 con x=y (comportamiento especial)
        REPORT "  Probando gin=1,ein=1 con x=y (comportamiento especial)";
        gin <= '1';
        ein <= '1';
        x <= "0101"; -- 5
        y <= "0101"; -- 5
        WAIT FOR 10 ns;
        IF gout /= '1' THEN
            REPORT "Error: gin=1,ein=1 con x=y pero gout=0" SEVERITY error;
            error_count := error_count + 1;
        END IF;
        IF eout /= '1' THEN
            REPORT "Error: gin=1,ein=1 con x=y pero eout=0" SEVERITY error;
            error_count := error_count + 1;
        END IF;

        -- Resumen final
        IF error_count = 0 THEN
            REPORT "¡Todos los tests pasaron correctamente!" SEVERITY note;
        ELSE
            REPORT "Prueba fallida con " & INTEGER'image(error_count) & " errores" SEVERITY error;
        END IF;

        WAIT;
    END PROCESS;
END test;