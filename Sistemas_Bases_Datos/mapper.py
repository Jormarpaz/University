import sys

for line in sys.stdin:
    line = line.strip()
    columns = line.split(",")
    
    if len(columns) > 5:
        tipo_procedimiento = columns[5].strip()
        if tipo_procedimiento == "Contratación Directa":
            print(line)