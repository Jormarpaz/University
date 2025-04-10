LIBRARY IEEE;
USE IEEE.Std_logic_1164.ALL;
USE IEEE.Numeric_Std.ALL;

ENTITY tb_funciones_logicas IS
END tb_funciones_logicas;

ARCHITECTURE test OF tb_funciones_logicas IS
    SIGNAL x, y, z : STD_LOGIC;
    SIGNAL F_comportamiento, G_comportamiento, F_estructura, G_estructura : STD_LOGIC;
    SIGNAL test : STD_LOGIC_VECTOR(2 DOWNTO 0);

    CONSTANT PERIODO : TIME := 20 ns;
BEGIN
    -- Instancias
    DUT_comportamiento : ENTITY work.funciones_logicas(comportamiento)
        PORT MAP(x, y, z, F_comportamiento, G_comportamiento);

    DUT_estructura : ENTITY work.funciones_logicas(estructura)
        PORT MAP(x, y, z, F_estructura, G_estructura);

    -- Proceso de estimulación
    PROCESS
        VARIABLE error_count : INTEGER := 0;
        VARIABLE tv: std_logic_vector(2 downto 0);
    BEGIN
        REPORT "Iniciando simulacion...";

        FOR i IN 0 TO 7 LOOP
            tv := STD_LOGIC_VECTOR(to_unsigned(i, 3));
            test <= tv;
            x <= tv(2);
            y <= tv(1);
            z <= tv(0);
            WAIT FOR PERIODO;

            -- Verificación
            IF F_comportamiento /= F_estructura OR G_comportamiento /= G_estructura THEN
                REPORT "Error en combinación " & INTEGER'image(i) &
                    " (x=" & STD_LOGIC'image(x) &
                    ", y=" & STD_LOGIC'image(y) &
                    ", z=" & STD_LOGIC'image(z) & ")" &
                    " F_comportamiento=" & STD_LOGIC'image(F_comportamiento) &
                    " F_estructura=" & STD_LOGIC'image(F_estructura) &
                    " G_comportamiento=" & STD_LOGIC'image(G_comportamiento) &
                    " G_estructura=" & STD_LOGIC'image(G_estructura)
                    SEVERITY error;
                error_count := error_count + 1;
            END IF;
        END LOOP;

        -- Resumen
        IF error_count = 0 THEN
            REPORT "¡Todos los tests pasaron correctamente!" SEVERITY note;
        ELSE
            REPORT "Prueba fallida con " & INTEGER'image(error_count) & " errores" SEVERITY error;
        END IF;

        WAIT;
    END PROCESS;
END test;