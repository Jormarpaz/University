library IEEE;
use IEEE.std_logic_1164.all;


entity xnor_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end xnor_gate;

architecture behavior of xnor_gate is
begin
    y <= a xnor b;
end behavior;