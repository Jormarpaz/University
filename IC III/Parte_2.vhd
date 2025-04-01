-- a)
entity comparador_1bit is
    port (  gout: out std_logic;
            eout: out std_logic;
            x: in std_logic;
            y: in std_logic
    );
end comparador_1bit;

-- b)
entity comparador_Nbits is
    generic ( N: integer := 4);
    port(   gout: out std_logic;
            eout: out std_logic;
            x: in std_logic (N-1 downto 0);
            y: in std_logic (N-1 downto 0)
    );
end comparador_Nbits;

-- c)
architecture estructura of comparador_Nbits is
    -- Señales
    signal g_temp, e_temp: std_logic (N-1 downto 0);

    begin
        gen_comparadores: for i in 0 to N-1 generate
        comp: entity .comparador_1bit
            port map (
                gout => g_temp(i),
                eout => e_temp(i),
                x => x(i),
                y => y(i)
            );
        end generate;

        -- Lógica para combinar resultados
        -- (Implementar la lógica para determinar gout y eout finales)
    end estructura;


entity tb_comparador_Nbits is
end tb_comparador_Nbits;

architecture test of tb_comparador_Nbits is
    constant N : integer := 4;
    -- Señales y componentes
begin
    -- Proceso de estimulación
    process
        variable error_count : integer := 0;
    begin
        -- Probar todas las combinaciones posibles para N=4
        -- Verificar resultados y contar errores
        report "Simulación finalizada. Errores: " & integer'image(error_count);
        wait;
    end process;
end test;