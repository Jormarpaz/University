-- a)
library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity funciones_logicas is
    port (  x: in std_logic;
            y: in std_logic;
            z: in std_logic;
            F: out std_logic;
            G: out std_logic
    );
end funciones_logicas;

-- b)
architecture comportamiento of funciones_logicas is
    begin
        F <= (x and not y and not z) or (not x) or (x and y and not z);
        G <= (x and y) or (not x and z) or (y and z);
    end comportamiento;
-- c)
library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity puerta_AND is
    port (  a : in std_logic;
            b : in std_logic;
            c : out std_logic
    );
end puerta_AND;

architecture comportamiento_AND of puerta_AND is
    begin
        c <= a and b;
    end comportamiento_AND;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity puerta_OR is
    port (  a: in std_logic;
            b: in std_logic;
            c: out std_logic
    );
end puerta_OR;

architecture comportamiento_OR of puerta_OR is
    begin
        c <= a or b;  -- Operación OR
    end comportamiento_OR;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
entity puerta_NOT is
    port(   a: in std_logic;
            b: out std_logic
    );
end puerta_NOT;

architecture comportamiento_NOT of puerta_NOT is
    begin
        b <= not a;  -- Operación NOT
    end comportamiento_NOT;

-- d)
architecture estructura of funciones_logicas is
    -- Señales
    signal not_x, not_y, not_z : std_logic; 
    signal and1_out, and2_out, and3_out, and4_out, and5_out, and6_out : std_logic;
    signal or1_out, or2_out : std_logic;

    -- Componentes
    component puerta_AND
        port(   a: in std_logic;
                b: in std_logic;
                c: out std_logic
        );
    end component;

    component puerta_OR
        port(   a: in std_logic;
                b: in std_logic;
                c: out std_logic
        ); 
    end component;

    component puerta_NOT
        port(   a: in std_logic;
                b: out std_logic
        );
    end component;

    begin
        -- Instanciación de puertas NOT
    U1: puerta_NOT port map (x, not_x);
    U2: puerta_NOT port map (y, not_y);
    U3: puerta_NOT port map (z, not_z);
    
    -- Instanciación de puertas AND para la función F
    U4: puerta_AND port map (x, not_y, and1_out);  -- x AND y'
    U5: puerta_AND port map (and1_out, not_z, and2_out);  -- xy'z'
    U6: puerta_AND port map (x, y, and3_out);  -- xy
    U7: puerta_AND port map (and3_out, not_z, and4_out);  -- xyz'
    
    -- Instanciación de puertas OR para la función F
    U8: puerta_OR port map (and2_out, not_x, or1_out);
    U9: puerta_OR port map (or1_out, and4_out, F);
    
    -- Instanciación de puertas AND para la función G
    U10: puerta_AND port map (not_x, z, and5_out);  -- x'z
    U11: puerta_AND port map (y, z, and6_out);  -- yz
    
    -- Instanciación de puertas OR para la función G
    U12: puerta_OR port map (and3_out, and5_out, or2_out);
    U13: puerta_OR port map (or2_out, and6_out, G);
    end estructura;

-- e)

entity tb_funciones_logicas is
end tb_funciones_logicas;

library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;
architecture testbench of tb_funciones_logicas is
    component funciones_logicas
        Port ( x : in std_logic;
               y : in std_logic;
               z : in std_logic;
               F : out std_logic;
               G : out std_logic);
    end component;

    -- Señales para el diseño behavioral
    signal x, y, z: std_logic;
    signal F_beh, G_beh: std_logic;
    
    -- Señales para el diseño structural
    signal F_struct, G_struct: std_logic;
    
    -- Contador de errores
    shared variable error_count: integer := 0;
    
    constant periodo: time := 10 ns;

begin
    -- Instancia del diseño behavioral
    UUT_beh: funciones_logicas 
        port map (
            x => x,
            y => y,
            z => z,
            F => F_beh,
            G => G_beh
        );
    
    -- Instancia del diseño structural
    UUT_struct: entity work.funciones_logicas(structural)
        port map (
            x => x,
            y => y,
            z => z,
            F => F_struct,
            G => G_struct
        );

    -- Proceso de estimulación
    stim_proc: process
    begin
        -- Probar todas las combinaciones posibles (8 casos)
        for i in 0 to 7 loop
            (x, y, z) <= std_logic_vector(to_unsigned(i, 3));
            wait for periodo;
            
            -- Verificar coincidencia entre ambos diseños
            assert (F_beh = F_struct) and (G_beh = G_struct)
                report "Error en combinación " & integer'image(i) & 
                       ": x=" & std_logic'image(x) & 
                       ", y=" & std_logic'image(y) & 
                       ", z=" & std_logic'image(z)
                severity error;
            
            if (F_beh /= F_struct) or (G_beh /= G_struct) then
                error_count := error_count + 1;
            end if;
        end loop;
        
        -- Reporte final
        assert false
            report "Simulación completada. Errores encontrados: " & integer'image(error_count)
            severity note;
        wait;
    end process;
end testbench;