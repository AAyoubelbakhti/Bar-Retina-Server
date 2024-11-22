package com.project;

import org.json.JSONObject;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        String wsLocation = "ws://localhost:3000";
        UtilsWS wsClient = UtilsWS.getSharedInstance(wsLocation);

        wsClient.onOpen(message -> {
            System.out.println("Connexió establerta: " + message);
        });

        wsClient.onMessage(message -> {
            System.out.println("Resposta del servidor: " + message);
            try {
                JSONObject response = new JSONObject(message);
                String type = response.getString("type");

                switch (type) {
                    case "tags":
                        System.out.println("Respostes per la categoria seleccionada (Tags):");
                        System.out.println(response.get("products"));
                        break;
                    case "productes":
                        System.out.println("Llista de productes:");
                        System.out.println(response.get("products"));
                        break;
                    case "productesMesVenuts":
                        System.out.println("Productes més venuts:");
                        System.out.println(response.get("products"));
                        break;
                    case "error":
                        System.out.println("Error: " + response.getString("message"));
                        break;
                    default:
                        System.out.println("Resposta desconeguda del servidor.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Error al processar la resposta del servidor: " + e.getMessage());
                e.printStackTrace();
            }
        });

        wsClient.onClose(message -> {
            System.out.println("Connexió tancada: " + message);
        });

        wsClient.onError(message -> {
            System.out.println("Error de connexió: " + message);
        });

        while (!wsClient.isOpen()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Selecciona una categoria:");
            System.out.println("1. Beguda");
            System.out.println("2. Primer plat");
            System.out.println("3. Reposteria");
            System.out.println("4. Tapa");
            System.out.println("5. Postre");
            System.out.println("6. Productes més venuts");
            System.out.println("7. Sortir");

            int option = scanner.nextInt();
            JSONObject message = new JSONObject();

            switch (option) {
                case 1:
                    message.put("type", "tags");
                    message.put("body", "Beguda");
                    wsClient.safeSend(message.toString());
                    break;
                case 2:
                    message.put("type", "tags");
                    message.put("body", "Primer plat");
                    wsClient.safeSend(message.toString());
                    break;
                case 3:
                    message.put("type", "tags");
                    message.put("body", "Reposteria");
                    wsClient.safeSend(message.toString());
                    break;
                case 4:
                    message.put("type", "tags");
                    message.put("body", "Tapa");
                    wsClient.safeSend(message.toString());
                    break;
                case 5:
                    message.put("type", "tags");
                    message.put("body", "Postre");
                    wsClient.safeSend(message.toString());
                    break;
                case 6:
                    message.put("type", "venuts");
                    wsClient.safeSend(message.toString());
                    break;
                case 7:
                    System.out.println("Sortint...");
                    wsClient.safeSend("Sortir");
                    wsClient.forceExit();
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a provar.");
            }
        }
    }
}
