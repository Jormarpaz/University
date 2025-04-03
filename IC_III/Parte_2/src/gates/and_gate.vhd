library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity and_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end and_gate;

architecture behavior of and_gate is
begin
    y <= a and b;
end behavior;