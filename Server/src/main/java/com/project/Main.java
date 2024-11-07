package com.project;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket servidorSocket = new ServerSocket(4545);
            System.out.println("esperant connexions...");
            GeneradorXML.generarXML();
            Socket socket = servidorSocket.accept();
            System.out.println("Client connectat");
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String missatgeClient = input.readLine();
            System.out.println("Missatge del client: " + missatgeClient);
            output.println("Server OK");
            input.close();
            output.close();
            socket.close();
            servidorSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

