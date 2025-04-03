library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity not_gate is
    port (  a: in std_logic;
            y: out std_logic
    );
end not_gate;

architecture behavior of not_gate is
begin
    y <= not a;
end behavior;