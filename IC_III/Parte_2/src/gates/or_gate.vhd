library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity or_gate is
    port (  a, b: in std_logic;
            y: out std_logic
    );
end or_gate;

architecture behavior of or_gate is
begin
    y <= a or b;
end behavior;