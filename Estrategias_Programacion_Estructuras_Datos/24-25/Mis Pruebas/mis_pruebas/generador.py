import random
from datetime import datetime, timedelta

def generate_random_date(start_date, end_date):
    """Genera una fecha aleatoria entre start_date y end_date"""
    delta = end_date - start_date
    random_days = random.randint(0, delta.days)
    return start_date + timedelta(days=random_days)

def generate_task_description():
    """Genera una descripción de tarea basada en el índice"""
    tasks = [
        "Comprar leche", "Hacer ejercicio", "Llamar al medico", 
        "Reunion con equipo", "Preparar presentacion", "Enviar documentos",
        "Leer libro", "Escribir informe", "Revisar codigo", "Planificar semana",
        "Limpiar casa", "Hacer la compra", "Estudiar para examen", "Resolver bugs",
        "Optimizar codigo", "Revisar documentacion", "Entrenar modelo", "Analizar datos"
    ]
    return random.choice(tasks)

def generate_test_file(filename, operation_counts):
    """
    Genera un archivo de prueba con las operaciones especificadas
    
    Args:
        filename: nombre del archivo de salida
        operation_counts: diccionario con el número de cada operación a generar
            Ejemplo: {'add': 100, 'delete': 50, 'move': 20, ...}
    """
    start_date = datetime(2025, 1, 1)
    end_date = datetime(2500, 12, 31)
    
    # Generamos algunas fechas base para las operaciones move
    base_dates = [generate_random_date(start_date, end_date) for _ in range(20)]
    
    # Diccionario para mantener las tareas añadidas (para operaciones delete y move)
    added_tasks = {}
    
    with open(filename, 'w') as f:
        # Primero generamos todas las operaciones add
        for i in range(operation_counts.get('add', 0)):
            task_date = generate_random_date(start_date, end_date)
            task_desc = generate_task_description()
            added_tasks[i] = task_date.strftime('%Y%m%d')
            f.write(f"add\t{task_desc}\t{task_date.strftime('%Y%m%d')}\n")
        
        # Generamos operaciones execute
        for _ in range(operation_counts.get('execute', 0)):
            f.write("execute\n")
        
        # Generamos operaciones iteratorFuture e iteratorPast
        for _ in range(operation_counts.get('iteratorFuture', 0)):
            f.write("iteratorFuture\n")
        
        for _ in range(operation_counts.get('iteratorPast', 0)):
            f.write("iteratorPast\n")
        
        # Generamos operaciones delete (sobre tareas existentes)
        delete_indices = random.sample(list(added_tasks.keys()), 
                                     min(operation_counts.get('delete', 0), len(added_tasks)))
        for task_id in delete_indices:
            f.write(f"delete\t{added_tasks[task_id]}\n")
            del added_tasks[task_id]
        
        # Generamos operaciones move (sobre tareas existentes)
        move_indices = random.sample(list(added_tasks.keys()), 
                                   min(operation_counts.get('move', 0), len(added_tasks)))
        for task_id in move_indices:
            old_date = added_tasks[task_id]
            new_date = generate_random_date(start_date, end_date).strftime('%Y%m%d')
            f.write(f"move\t{old_date}\t{new_date}\n")
            added_tasks[task_id] = new_date
        
        # Algunas operaciones discard al final si hay
        for _ in range(operation_counts.get('discard', 0)):
            f.write("discard\n")

def generate_test_suite():
    """Genera una suite de pruebas con diferentes tamaños"""

    test_sizes = [
        ( 'test_add_100.tsv', {'add': 100}),
        ( 'test_add_1000.tsv', {'add': 1000}),
        ( 'test_add_10000.tsv', {'add': 10000}),
        ( 'test_delete_100.tsv', {'add': 100, 'delete': 100}),
        ( 'test_delete_1000.tsv', {'add': 1000, 'delete': 1000}),
        ( 'test_delete_10000.tsv', {'add': 10000, 'delete': 10000}),
        ( 'test_move_100.tsv', {'add': 100, 'move': 100}),
        ( 'test_move_1000.tsv', {'add': 1000, 'move': 1000}),
        ( 'test_move_10000.tsv', {'add': 10000, 'move': 10000}),
        ('test_iterators.tsv', {'add': 100, 'iteratorFuture': 1000, 'iteratorPast': 1000})
    ]
    
    for filename, operations in test_sizes:
        print(f"Generando {filename}...")
        generate_test_file(filename, operations)
    
    print("Generación de pruebas completada.")

if __name__ == "__main__":
    generate_test_suite()