LIBRARY IEEE;
USE IEEE.Std_logic_1164.ALL;

ENTITY funciones_logicas IS
    PORT (
        x, y, z : IN STD_LOGIC;
        F, G : OUT STD_LOGIC
    );
END funciones_logicas;

ARCHITECTURE comportamiento OF funciones_logicas IS
BEGIN
    -- F = xy'z' + x' + xyz'
    F <= (x AND NOT y AND NOT z) OR (NOT x) OR (x AND y AND NOT z);

    -- G = xy + x'z + yz
    G <= (x AND y) OR (NOT x AND z) OR (y AND z);
END comportamiento;
ARCHITECTURE estructura OF funciones_logicas IS
    -- Señales internas
    SIGNAL not_x, not_y, not_z : STD_LOGIC;
    SIGNAL xy, xz, yz, xy_notz, xyz_notz : STD_LOGIC;
    SIGNAL tmp_F1, tmp_F2, tmp_G1 : STD_LOGIC;

    -- Declaración de componentes
    COMPONENT puerta_and
        PORT (
            a, b : IN STD_LOGIC;
            y : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT puerta_or
        PORT (
            a, b : IN STD_LOGIC;
            y : OUT STD_LOGIC);
    END COMPONENT;

    COMPONENT puerta_not
        PORT (
            a : IN STD_LOGIC;
            y : OUT STD_LOGIC);
    END COMPONENT;

BEGIN
    -- Puertas NOT
    U1 : COMPONENT puerta_not PORT MAP(x, not_x);
    U2 : COMPONENT puerta_not PORT MAP(y, not_y);
    U3 : COMPONENT puerta_not PORT MAP(z, not_z);

    -- Cálculo de F = xy'z' + x' + xyz'
    U4 : COMPONENT puerta_and PORT MAP(x, not_y, tmp_F1); -- xy'
    U5 : COMPONENT puerta_and PORT MAP(tmp_F1, not_z, xy_notz); -- xy'z'
    U6 : COMPONENT puerta_and PORT MAP(x, y, xy); -- xy
    U7 : COMPONENT puerta_and PORT MAP(xy, not_z, xyz_notz); -- xyz'
    U8 : COMPONENT puerta_or PORT MAP(xy_notz, not_x, tmp_F2); -- xy'z' + x'
    U9 : COMPONENT puerta_or PORT MAP(tmp_F2, xyz_notz, F); -- (xy'z' + x') + xyz'

    -- Cálculo de G = xy + x'z + yz
    U10 : COMPONENT puerta_and PORT MAP(not_x, z, xz); -- x'z
    U11 : COMPONENT puerta_and PORT MAP(y, z, yz); -- yz
    U12 : COMPONENT puerta_or PORT MAP(xy, xz, tmp_G1); -- xy + x'z
    U13 : COMPONENT puerta_or PORT MAP(tmp_G1, yz, G); -- (xy + x'z) + yz
END estructura;