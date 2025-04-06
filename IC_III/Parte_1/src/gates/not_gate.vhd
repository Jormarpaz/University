library IEEE;
use IEEE.std_logic_1164.all;


entity not_gate is
    port (  a: in std_logic;
            y: out std_logic
    );
end not_gate;

architecture behavior of not_gate is
begin
    y <= not a;
end behavior;