LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;

ENTITY comparador_1bit IS
    PORT (
        gout, eout : OUT STD_LOGIC;
        gin, ein : IN STD_LOGIC;
        x, y : IN STD_LOGIC
    );
END comparador_1bit;

-- Estructura del comparador de 1 bit
ARCHITECTURE estructura OF comparador_1bit IS
    SIGNAL y_neg : STD_LOGIC;
    SIGNAL x_mayor_y : STD_LOGIC;
    SIGNAL x_igual_y : STD_LOGIC;
    SIGNAL g_temp : STD_LOGIC;
BEGIN
    -- Puerta NOT para negar y
    u1 : ENTITY work.puerta_not PORT MAP (a => y, y => y_neg);

    -- AND para detectar x > y (x=1 y y=0)
    u2 : ENTITY work.puerta_and PORT MAP (a => x, b => y_neg, y => x_mayor_y);

    -- XNOR para detectar igualdad (x == y)
    u3 : ENTITY work.puerta_xnor PORT MAP (a => x, b => y, y => x_igual_y);

    -- AND para propagar la igualdad (ein AND x_igual_y)
    u4 : ENTITY work.puerta_and PORT MAP (a => x_igual_y, b => ein, y => eout);

    -- AND para propagar el mayor anterior (gin AND x_igual_y)
    u5 : ENTITY work.puerta_and PORT MAP (a => x_igual_y, b => gin, y => g_temp);

    -- OR para determinar si x > y (x_mayor_y OR g_temp)
    u6 : ENTITY work.puerta_or PORT MAP (a => x_mayor_y, b => g_temp, y => gout);
END estructura;
