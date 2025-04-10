LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
ENTITY puerta_and IS
    PORT (
        a, b : IN STD_LOGIC;
        y : OUT STD_LOGIC
    );
END puerta_and;

ARCHITECTURE comportamiento OF puerta_and IS
BEGIN
    y <= a AND b;
END comportamiento;