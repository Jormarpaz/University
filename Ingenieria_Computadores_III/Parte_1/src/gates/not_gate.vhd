LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
ENTITY puerta_not IS
    PORT (
        a : IN STD_LOGIC;
        y : OUT STD_LOGIC
    );
END puerta_not;

ARCHITECTURE comportamiento OF puerta_not IS
BEGIN
    y <= NOT a;
END comportamiento;