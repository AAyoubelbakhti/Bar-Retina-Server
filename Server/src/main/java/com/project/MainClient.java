package com.project;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4545);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Selecciona una categoría par mostrar:");
            System.out.println("1. Beguda");
            System.out.println("2. Primer plat");
            System.out.println("3. Reposteria");
            System.out.println("4. Tapa");
            System.out.println("5. Postre");
            System.out.print("Tria una opció (1-5): ");
                        int opcion = scanner.nextInt();

            String categoria = "";
            switch (opcion) {
                case 1:
                    categoria = "Beguda";
                    break;
                case 2:
                    categoria = "Primer plat";
                    break;
                case 3:
                    categoria = "Reposteria";
                    break;
                case 4:
                    categoria = "Tapa";
                    break;
                case 5:
                    categoria = "Postre";
                    break;
                default:
                    System.out.println("Opción inválida.");
                    categoria = "Comanda desconeguda";
                    break;
            }

            output.println(categoria);
            String respostaServidor = input.readLine();
            System.out.println("\n---------------------------------------------");
            System.out.println(respostaServidor);
            System.out.println("---------------------------------------------\n");
            scanner.close();
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
