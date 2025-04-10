LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
ENTITY comparador_Nbits IS
    GENERIC (N : INTEGER := 4);
    PORT (
        gout, eout : OUT STD_LOGIC;
        gin, ein : IN STD_LOGIC;
        x, y : IN STD_LOGIC_VECTOR (N - 1 DOWNTO 0)
    );
END comparador_Nbits;

ARCHITECTURE estructura OF comparador_Nbits IS
    SIGNAL g_temp, e_temp : STD_LOGIC_VECTOR (N - 1 DOWNTO 0); -- Salidas de cada comparador
    SIGNAL gin_temp, ein_temp : STD_LOGIC_VECTOR (N DOWNTO 0); -- Conexiones en cascada
BEGIN
    gin_temp(0) <= gin; -- gin inicial
    ein_temp(0) <= ein; -- ein inicial

    gen_comparadores : FOR i IN 0 TO N - 1 GENERATE
        comp : ENTITY work.comparador_1bit
            PORT MAP(
                gout => g_temp(i),
                eout => e_temp(i),
                gin => gin_temp(i),
                ein => ein_temp(i),
                x => x(i),
                y => y(i)
            );
        gin_temp(i + 1) <= g_temp(i);
        ein_temp(i + 1) <= e_temp(i);
    END GENERATE;

    gout <= gin_temp(N);
    eout <= ein_temp(N);
END estructura;