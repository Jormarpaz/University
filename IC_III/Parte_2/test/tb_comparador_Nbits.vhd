library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

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
        gin <= '0';
        ein <= '1';
        
        -- Probar todas las combinaciones posibles para N=4
        for i in 0 to 2**N-1 loop
            for j in 0 to 2**N-1 loop
                x <= std_logic_vector(to_unsigned(i, N));
                y <= std_logic_vector(to_unsigned(j, N));
                wait for 10 ns;
                
                -- Verificar resultados
                if (i > j and gout /= '1') or (i <= j and gout = '1') then
                    error_count := error_count + 1;
                end if;
                
                if (i = j and eout /= '1') or (i /= j and eout = '1') then
                    error_count := error_count + 1;
                end if;
            end loop;
        end loop;
        
        -- Probar con gin=1, ein=0
        gin <= '1';
        ein <= '0';
        x <= "1111";
        y <= "1111";
        wait for 10 ns;
        if gout /= '1' or eout /= '0' then
            error_count := error_count + 1;
        end if;
        
        report "Simulación finalizada. Errores: " & integer'image(error_count);
        wait;
    end process;
end test;