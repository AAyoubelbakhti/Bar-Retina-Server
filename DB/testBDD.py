import mysql.connector
from mysql.connector import Error
import json
from collections import defaultdict

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

def total_productos_y_precio():
    connection = connect()
    if not connection:
        return
    try:
        cursor = connection.cursor(dictionary=True)
        query = "SELECT comanda FROM comandes WHERE pagada = TRUE"
        cursor.execute(query)
        resultados = cursor.fetchall()
        
        productos_totales = defaultdict(lambda: {"cantidad": 0, "precio": 0.0})

        for fila in resultados:
            try:
                lista_productos = json.loads(fila['comanda'])
                for producto in lista_productos:
                    productos_totales[producto['nom']]['cantidad'] += producto['quantitat']
                    productos_totales[producto['nom']]['precio'] += producto['quantitat'] * producto['preu']
            except json.JSONDecodeError as e:
                print(f"Error al procesar una fila: {e}")
        
        print("\n=== Total de Productos y Precio ===")
        for producto, datos in productos_totales.items():
            print(f"{producto}: {datos['cantidad']} unidades vendidas, Total ganado: {datos['precio']:.2f} €")

        total_cantidad = sum(datos['cantidad'] for datos in productos_totales.values())
        total_precio = sum(datos['precio'] for datos in productos_totales.values())
        print("\n=== Totales Generales ===")
        print(f"Cantidad total de productos vendidos: {total_cantidad}")
        print(f"Precio total acumulado: {total_precio:.2f} €")
    except Error as e:
        print(f"Error al procesar los productos: {e}")
    finally:
        disconnect(connection)

if __name__ == "__main__":
    total_productos_y_precio()
