LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
ENTITY puerta_or IS
    PORT (
        a, b : IN STD_LOGIC;
        y : OUT STD_LOGIC
    );
END puerta_or;

ARCHITECTURE comportamiento OF puerta_or IS
BEGIN
    y <= a OR b;
END comportamiento;