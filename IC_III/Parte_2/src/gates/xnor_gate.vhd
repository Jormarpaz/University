library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity xnor_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end xnor_gate;

architecture behavior of xnor_gate is
begin
    y <= a xnor b;
end behavior;