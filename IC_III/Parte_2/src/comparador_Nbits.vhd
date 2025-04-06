library IEEE;
use IEEE.std_logic_1164.all;


entity comparador_Nbits is
    generic ( N: integer := 4);
    port(   gout, eout: out std_logic;
            gin, ein: in std_logic;
            x, y: in std_logic_vector (N-1 downto 0)
    );
end comparador_Nbits;

architecture estructura of comparador_Nbits is
    signal g_temp, e_temp: std_logic_vector (N-1 downto 0); -- Salidas de cada comparador
    signal gin_temp, ein_temp: std_logic_vector (N downto 0); -- Conexiones en cascada
begin
    gin_temp(0) <= gin; -- gin inicial
    ein_temp(0) <= ein; -- ein inicial
    
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