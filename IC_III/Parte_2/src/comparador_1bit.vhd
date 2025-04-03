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

-- Estructura del comparador de 1 bit
architecture estructura of comparador_1bit is
    signal x_neg, y_neg, x_igual_y, x_mayor_y: std_logic;
    signal g_temp : std_logic;
begin
    -- Instanciación de puertas
    u1: entity work.not_gate port map (y, y_neg);
    u2: entity work.and_gate port map (x, y_neg, x_mayor_y);
    u3: entity work.xnor_gate port map (x, y, x_igual_y);
    u4: entity work.and_gate port map (x_igual_y, gin, g_temp);
    u5: entity work.or_gate port map (x_mayor_y, g_temp, gout);
    u6: entity work.and_gate port map (x_igual_y, ein, eout);
end estructura;
