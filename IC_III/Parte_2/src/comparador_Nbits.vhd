library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity comparador_Nbits is
    generic ( N: integer := 4);
    port(   gout: out std_logic;
            eout: out std_logic;
            gin: in std_logic;
            ein: in std_logic;
            x: in std_logic_vector (N-1 downto 0);
            y: in std_logic_vector (N-1 downto 0)
    );
end comparador_Nbits;

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