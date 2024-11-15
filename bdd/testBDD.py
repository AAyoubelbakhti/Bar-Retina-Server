import mysql.connector
from mysql.connector import Error

def obtener_comandes():
    connection = None
    try:
        connection = mysql.connector.connect(
            host='localhost',
            database='comandes',
            user='barretina',
            password='barretina'
        )

        if connection.is_connected():
            cursor = connection.cursor(dictionary=True)
            query = "SELECT * FROM comandes ORDER BY data_comanda DESC"
            cursor.execute(query)
            resultados = cursor.fetchall()
            for fila in resultados:
                print(fila)
            return resultados

    except Error as e:
        print(f"Error al conectar a MySQL: {e}")
        return None
    finally:
        if connection and connection.is_connected():
            cursor.close()
            connection.close()

if __name__ == "__main__":
    comandes = obtener_comandes()
    if comandes:
        print(f"Se obtuvieron {len(comandes)} registros.")
