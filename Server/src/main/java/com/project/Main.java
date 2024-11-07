package com.project;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket servidorSocket = new ServerSocket(4545);
            System.out.println("Esperant connexions...");
            FuncsBar.generarXML();
            Socket socket = servidorSocket.accept();
            System.out.println("Client connectat");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String missatgeClient = input.readLine();
            System.out.println("Missatge del client: " + missatgeClient);
            switch (missatgeClient) {
                case "Beguda":
                case "Primer plat":
                case "Reposteria":
                case "Tapa":
                case "Postre":
                    output.println(FuncsBar.mostrarTags(missatgeClient));
                    break;
                case "productes":
                    output.println(FuncsBar.mostrarProductes());
                    break;
                default:
                    output.println("Comanda desconeguda");
                    break;
            }
            input.close();
            output.close();
            socket.close();
            servidorSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
