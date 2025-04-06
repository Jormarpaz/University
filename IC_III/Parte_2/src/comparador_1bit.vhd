library IEEE;
use IEEE.std_logic_1164.all;


entity comparador_1bit is
    port (  gout, eout: out std_logic;  --Salidas
            gin, ein: in std_logic;     -- Cascada     
            x, y: in std_logic   -- Bits a comparar
    );
end comparador_1bit;

-- Estructura del comparador de 1 bit
architecture estructura of comparador_1bit is
    signal y_neg: std_logic;
    signal x_mayor_y: std_logic;
    signal x_igual_y: std_logic;
    signal g_temp: std_logic;
begin
    -- Puerta NOT para negar y
    u1: entity work.not_gate port map (a => y, y => y_neg);
    
    -- AND para detectar x > y (x=1 y y=0)
    u2: entity work.and_gate port map (a => x, b => y_neg, y => x_mayor_y);
    
    -- XNOR para detectar igualdad (x == y)
    u3: entity work.xnor_gate port map (a => x, b => y, y => x_igual_y);
    
    -- AND para propagar la igualdad (ein AND x_igual_y)
    u4: entity work.and_gate port map (a => x_igual_y, b => ein, y => eout);
    
    -- AND para propagar el mayor anterior (gin AND x_igual_y)
    u5: entity work.and_gate port map (a => x_igual_y, b => gin, y => g_temp);
    
    -- OR para determinar si x > y (x_mayor_y OR g_temp)
    u6: entity work.or_gate port map (a => x_mayor_y, b => g_temp, y => gout);
end estructura;
