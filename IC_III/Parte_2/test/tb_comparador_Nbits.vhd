library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity tb_comparador_Nbits is
end tb_comparador_Nbits;

architecture test of tb_comparador_Nbits is
    constant N : integer := 4;
    signal x, y: std_logic_vector (N-1 downto 0);
    signal gout, eout: std_logic;
    signal gin, ein: std_logic;
    
    component comparador_Nbits is
        generic ( N: integer := 4);
        port(   gout: out std_logic;
                eout: out std_logic;
                gin: in std_logic;
                ein: in std_logic;
                x: in std_logic_vector (N-1 downto 0);
                y: in std_logic_vector (N-1 downto 0)
        );
    end component;
begin
    uut: comparador_Nbits
        generic map (N => N)
        port map (
            gout => gout,
            eout => eout,
            gin => gin,
            ein => ein,
            x => x,
            y => y
        );
    
    -- Proceso de estimulación
    process
        variable error_count : integer := 0;
    begin
        -- Caso 1: ein=1, gin=0 (operación normal)
        report "Probando modo normal (ein=1, gin=0)";
        gin <= '0';
        ein <= '1';
        
        -- Probar todas las combinaciones posibles para N=4
        for i in 0 to 2**N-1 loop
            for j in 0 to 2**N-1 loop
                x <= std_logic_vector(to_unsigned(i, N));
                y <= std_logic_vector(to_unsigned(j, N));
                wait for 10 ns;
                
                -- Verificar resultados
                if (i > j and gout /= '1') then
                    report "Error: x=" & integer'image(i) & " > y=" & integer'image(j) & 
                           " pero gout=0" severity error;
                    error_count := error_count + 1;
                elsif (i <= j and gout = '1') then
                    report "Error: x=" & integer'image(i) & " <= y=" & integer'image(j) & 
                           " pero gout=1" severity error;
                    error_count := error_count + 1;
                end if;
                
                if (i = j and eout /= '1') then
                    report "Error: x=" & integer'image(i) & " = y=" & integer'image(j) & 
                           " pero eout=0" severity error;
                    error_count := error_count + 1;
                elsif (i /= j and eout = '1') then
                    report "Error: x=" & integer'image(i) & " != y=" & integer'image(j) & 
                           " pero eout=1" severity error;
                    error_count := error_count + 1;
                end if;
            end loop;
        end loop;
        
        -- Caso 2: ein=0 (bloquea igualdad)
        report "Probando ein=0 (debería bloquear eout)";
        gin <= '0';
        ein <= '0';
        x <= "1010";  -- 10
        y <= "1010";  -- 10
        wait for 10 ns;
        if eout /= '0' then
            report "Error: ein=0 pero eout=1 con x=y" severity error;
            error_count := error_count + 1;
        end if;
        
        -- Caso 3: gin=1 (fuerza mayor que si bits iguales)
        report "Probando gin=1 (debería propagar mayor que)";
        gin <= '1';
        ein <= '1';
        x <= "0101";  -- 5
        y <= "0101";  -- 5
        wait for 10 ns;
        if gout /= '1' then
            report "Error: gin=1 con x=y pero gout=0" severity error;
            error_count := error_count + 1;
        end if;
        if eout /= '1' then
            report "Error: ein=1 con x=y pero eout=0" severity error;
            error_count := error_count + 1;
        end if;
        
        -- Resumen final
        if error_count = 0 then
            report "¡Todos los tests pasaron correctamente!" severity note;
        else
            report "Prueba fallida con " & integer'image(error_count) & " errores" severity error;
        end if;
        
        wait;
    end process;
end test;