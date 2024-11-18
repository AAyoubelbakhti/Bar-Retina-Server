import mysql.connector
from mysql.connector import Error

def connect():
    try:
        connection = mysql.connector.connect(
            host='localhost',
            database='comandes',
            user='barretina',
            password='barretina'
        )
        return connection
    except Error as e:
        print(f"Error al conectar a MySQL: {e}")
        return None

def disconnect(connection):
    if connection and connection.is_connected():
        connection.close()

def obtener_comandes():
    connection = connect()
    if not connection:
        return
    try:
        cursor = connection.cursor(dictionary=True)
        query = "SELECT * FROM comandes ORDER BY data_comanda DESC"
        cursor.execute(query)
        resultados = cursor.fetchall()
        for fila in resultados:
            print(fila)
        return resultados
    except Error as e:
        print(f"Error al obtener comandes: {e}")
    finally:
        disconnect(connection)

def insertar_comanda(id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada):
    connection = connect()
    if not connection:
        return False
    try:
        cursor = connection.cursor()
        query = """
            INSERT INTO comandes (id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(query, (id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada))
        connection.commit()
        print("Comanda insertada exitosamente.")
        return True
    except Error as e:
        print(f"Error al insertar comanda: {e}")
        return False
    finally:
        disconnect(connection)

def actualizar_comanda(id_comanda, id_taula=None, id_cambrer=None, comanda=None, data_comanda=None, estat_comanda=None, preu_comanda=None, pagada=None):
    connection = connect()
    if not connection:
        return False
    try:
        cursor = connection.cursor()
        updates = []
        values = []
        if id_taula is not None:
            updates.append("id_taula = %s")
            values.append(id_taula)
        if id_cambrer is not None:
            updates.append("id_cambrer = %s")
            values.append(id_cambrer)
        if comanda is not None:
            updates.append("comanda = %s")
            values.append(comanda)
        if data_comanda is not None:
            updates.append("data_comanda = %s")
            values.append(data_comanda)
        if estat_comanda is not None:
            updates.append("estat_comanda = %s")
            values.append(estat_comanda)
        if preu_comanda is not None:
            updates.append("preu_comanda = %s")
            values.append(preu_comanda)
        if pagada is not None:
            updates.append("pagada = %s")
            values.append(pagada)
        if not updates:
            print("No se proporcionaron campos para actualizar.")
            return False
        query = f"UPDATE comandes SET {', '.join(updates)} WHERE id_comanda = %s"
        values.append(id_comanda)
        cursor.execute(query, values)
        connection.commit()
        print("Comanda actualizada exitosamente.")
        return True
    except Error as e:
        print(f"Error al actualizar comanda: {e}")
        return False
    finally:
        disconnect(connection)

def comandes_per_taula(id_taula):
    connection = connect()
    if not connection:
        return
    try:
        cursor = connection.cursor(dictionary=True)
        query = "SELECT * FROM comandes WHERE id_taula = %s ORDER BY data_comanda DESC"
        cursor.execute(query, (id_taula,))
        resultados = cursor.fetchall()
        for fila in resultados:
            print(fila)
        return resultados
    except Error as e:
        print(f"Error al obtener comandes de la mesa: {e}")
    finally:
        disconnect(connection)

def menu():
    while True:
        print("\n=== Menú de Opciones ===")
        print("1. Obtener todas las comandes")
        print("2. Insertar una comanda")
        print("3. Actualizar una comanda")
        print("4. Obtener comandes por taula")
        print("5. Salir")
        
        opcion = input("Selecciona una opción: ")
        if opcion == "1":
            obtener_comandes()
        elif opcion == "2":
            id_taula = int(input("ID de la taula: "))
            id_cambrer = int(input("ID del cambrer: "))
            comanda = input("Descripción de la comanda: ")
            data_comanda = input("Fecha de la comanda (YYYY-MM-DD HH:MM:SS): ")
            estat_comanda = input("Estado de la comanda: ")
            preu_comanda = float(input("Precio de la comanda: "))
            pagada = input("¿Pagada? (True/False): ").lower() == "true"
            insertar_comanda(id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada)
        elif opcion == "3":
            id_comanda = int(input("ID de la comanda a actualizar: "))
            id_taula = input("Nuevo ID de la taula (opcional): ")
            id_cambrer = input("Nuevo ID del cambrer (opcional): ")
            comanda = input("Nueva descripción de la comanda (opcional): ")
            data_comanda = input("Nueva fecha de la comanda (YYYY-MM-DD HH:MM:SS) (opcional): ")
            estat_comanda = input("Nuevo estado de la comanda (opcional): ")
            preu_comanda = input("Nuevo precio de la comanda (opcional): ")
            pagada = input("¿Pagada? (True/False) (opcional): ")
            actualizar_comanda(
                id_comanda,
                int(id_taula) if id_taula else None,
                int(id_cambrer) if id_cambrer else None,
                comanda if comanda else None,
                data_comanda if data_comanda else None,
                estat_comanda if estat_comanda else None,
                float(preu_comanda) if preu_comanda else None,
                pagada.lower() == "true" if pagada else None
            )
        elif opcion == "4":
            id_taula = int(input("ID de la taula: "))
            comandes_per_taula(id_taula)
        elif opcion == "5":
            print("Saliendo del programa.")
            break
        else:
            print("Opción no válida. Intenta de nuevo.")

if __name__ == "__main__":
    menu()
