LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
ENTITY puerta_xnor IS
    PORT (
        a, b : IN STD_LOGIC;
        y : OUT STD_LOGIC
    );
END puerta_xnor;

ARCHITECTURE comportamiento OF puerta_xnor IS
BEGIN
    y <= a XNOR b;
END comportamiento;
