library IEEE;
use IEEE.Std_logic_1164.all;
use IEEE.Numeric_Std.all;

entity tb_comparador_1bit is
end tb_comparador_1bit;

architecture behavioral of tb_comparador_1bit is
  component comparador_1bit
    port (
      gout, eout: out std_logic;
      gin, ein: in std_logic;
      x, y: in std_logic
    );
  end component;

  signal gin, ein: std_logic;
  signal x, y: std_logic;
  signal gout, eout: std_logic;
  
  type test_vector is record
    gin, ein, x, y: std_logic;
    gout, eout: std_logic;
  end record;
  
  type test_vector_array is array (natural range <>) of test_vector;
  
  constant test_vectors: test_vector_array := (
    -- gin, ein, x, y, gout, eout
    ('0', '1', '0', '0', '0', '1'), -- iguales
    ('0', '1', '1', '1', '0', '1'), -- iguales
    ('0', '1', '1', '0', '1', '0'), -- x > y
    ('0', '1', '0', '1', '0', '0'), -- x < y
    ('1', '0', '0', '0', '1', '0'), -- gin=1 propaga
    ('0', '0', '1', '1', '0', '0')  -- ein=0 bloquea
  );
  
begin
  uut: comparador_1bit port map (
    gout => gout,
    eout => eout,
    gin => gin,
    ein => ein,
    x => x,
    y => y
  );

  stim_proc: process
    variable error_count: integer := 0;
  begin
    report "Iniciando simulacion...";
    
    for i in test_vectors'range loop
      gin <= test_vectors(i).gin;
      ein <= test_vectors(i).ein;
      x <= test_vectors(i).x;
      y <= test_vectors(i).y;
      
      wait for 10 ns;
      
      if gout /= test_vectors(i).gout or eout /= test_vectors(i).eout then
        report "Error en test case " & integer'image(i) & 
               " Esperado: gout=" & std_logic'image(test_vectors(i).gout) & 
               " eout=" & std_logic'image(test_vectors(i).eout) &
               " Obtenido: gout=" & std_logic'image(gout) & 
               " eout=" & std_logic'image(eout)
               severity error;
        error_count := error_count + 1;
      end if;
    end loop;
    
    if error_count = 0 then
      report "¡Todos los tests pasaron correctamente!" severity note;
    else
      report integer'image(error_count) & " tests fallaron" severity error;
    end if;
    
    wait;
  end process;
end behavioral;