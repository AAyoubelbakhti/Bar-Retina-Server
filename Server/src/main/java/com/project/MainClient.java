package com.project;

import java.io.*;
import java.net.*;


public class MainClient {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 4545);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Beguda");
            String respostaServidor = input.readLine();
            System.out.println("\n---------------------------------------------");
            System.out.println("Resposta del servidor: " + respostaServidor);
            System.out.println("---------------------------------------------\n");
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
