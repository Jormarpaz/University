``` bash
ghdl -a "elementos necesarios para compilar con sus rutas directas"^
```
``` bash
ghdl -e "elementos necesarios para testear con la ruta raiz"
```
```
ghdl -r "los elementos de testeo compilados previamente" --vcd=waveform.vcd para crear las ondas
```
```bash
gtkwave waveform.vcd para checkear las ondas
```
