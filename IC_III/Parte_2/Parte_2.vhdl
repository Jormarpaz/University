-- a)
library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity comparador_1bit is
    port (  gout: out std_logic;
            eout: out std_logic;
            gin: in std_logic;
            ein: in std_logic;
            x: in std_logic;
            y: in std_logic
    );
end comparador_1bit;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity comparador_Nbits is
    generic ( N: integer := 4);
    port(   gout: out std_logic;
            eout: out std_logic;
            gin: in std_logic;
            ein: in std_logic;
            x: in std_logic (N-1 downto 0);
            y: in std_logic (N-1 downto 0)
    );
end comparador_Nbits;

-- b)

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity and_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end and_gate;

architecture behavior of and_gate is
begin
    y <= a and b;
end behavior;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity or_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end or_gate;

architecture behavior of or_gate is
begin
    y <= a or b;
end behavior;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity xnor_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end xnor_gate;

architecture behavior of xnor_gate is
begin
    y <= a xnor b;
end behavior;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity not_gate is
    port (  a: in std_logic;
            y: out std_logic
    );
end not_gate;

architecture behavior of not_gate is
begin
    y <= not a;
end behavior;

-- Estructura del comparador de 1 bit
architecture estructura of comparador_1bit is
    signal x_neg, y_neg, x_igual_y, x_mayor_y: std_logic;
begin
    -- Instanciación de puertas
    u1: entity work.not_gate port map (y, y_neg);
    u2: entity work.and_gate port map (x, y_neg, x_mayor_y);
    u3: entity work.xnor_gate port map (x, y, x_igual_y);
    u4: entity work.and_gate port map (x_igual_y, gin, g_temp);
    u5: entity work.or_gate port map (x_mayor_y, g_temp, gout);
    u6: entity work.and_gate port map (x_igual_y, ein, eout);
end estructura;

-- c)
library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
architecture estructura of comparador_Nbits is
    signal g_temp, e_temp: std_logic_vector (N-1 downto 0);
    signal gin_temp, ein_temp: std_logic_vector (N downto 0);
begin
    gin_temp(0) <= gin;
    ein_temp(0) <= ein;
    
    gen_comparadores: for i in 0 to N-1 generate
        comp: entity work.comparador_1bit
            port map (
                gout => g_temp(i),
                eout => e_temp(i),
                gin => gin_temp(i),
                ein => ein_temp(i),
                x => x(i),
                y => y(i)
            );
        gin_temp(i+1) <= g_temp(i);
        ein_temp(i+1) <= e_temp(i);
    end generate;
    
    gout <= gin_temp(N);
    eout <= ein_temp(N);
end estructura;

-- d)

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