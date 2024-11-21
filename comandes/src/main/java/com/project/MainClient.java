package com.project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) {
        String wsLocation = "ws://barretina1.ieti.site:443";
        UtilsWS wsClient = UtilsWS.getSharedInstance(wsLocation);

        HashMap<Integer, Notificacio> notificacions = new HashMap<>();
        final int[] notificacioId = {0};

        wsClient.onOpen(message -> {
            System.out.println("Connexió establerta: " + message);
        });

        wsClient.onMessage(message -> {
            JSONObject response = new JSONObject(message);

            if (response.has("broadcast")) {
                JSONArray broadcastArray = response.getJSONArray("broadcast");
                for (int i = 0; i < broadcastArray.length(); i++) {
                    JSONObject notificacioJson = broadcastArray.getJSONObject(i);
                    int cambrerIdFromBroadcast = notificacioJson.getInt("id_cambrer");

                    if (cambrerIdFromBroadcast == MainClient.cambrerId) {
                        notificacioId[0]++;
                        notificacions.put(notificacioId[0], new Notificacio(
                                notificacioJson.getInt("id_comanda"),
                                notificacioJson.getString("estat_comanda"),
                                notificacioJson.getString("data_comanda"),
                                notificacioJson.getString("comanda"),
                                notificacioJson.getBoolean("pagada"),
                                notificacioJson.getInt("id_taula"),
                                notificacioJson.getDouble("preu_comanda"),
                                cambrerIdFromBroadcast
                        ));
                        System.out.println("Nova notificació rebuda: " + notificacioJson.getString("comanda"));
                    }
                }
            } else if ("error".equals(response.getString("type"))) {
                System.out.println("Error: " + response.getString("message"));
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

        System.out.println("Introdueix el teu ID de cambrer:");
        int cambrerId = scanner.nextInt();
        MainClient.cambrerId = cambrerId;

        while (true) {
            System.out.println("Selecciona una opció:");
            System.out.println("1. Mostrar totes les notificacions sense llegir");
            System.out.println("2. Mostrar l'última notificació");
            System.out.println("3. Marcar totes les notificacions com a llegides");
            System.out.println("4. Mostrar total de notificacions sense llegir");
            System.out.println("5. Sortir");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    mostrarNotificacions(notificacions);
                    break;
                case 2:
                    mostrarUltimaNotificacio(notificacions);
                    break;
                case 3:
                    marcarTotesLlegides(notificacions);
                    break;
                case 4:
                    mostrarTotalSenseLlegir(notificacions);
                    break;
                case 5:
                    System.out.println("Sortint...");
                    wsClient.forceExit();
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a provar.");
            }
        }
    }

    private static int cambrerId;

    private static void mostrarNotificacions(HashMap<Integer, Notificacio> notificacions) {
        boolean senseLlegir = false;
        for (Map.Entry<Integer, Notificacio> entry : notificacions.entrySet()) {
            Notificacio notificacio = entry.getValue();
            if (!notificacio.isLlegida()) {
                System.out.println(notificacio);
                senseLlegir = true;
            }
        }
        if (!senseLlegir) {
            System.out.println("No hi ha notificacions sense llegir.");
        }
    }

    private static void mostrarUltimaNotificacio(HashMap<Integer, Notificacio> notificacions) {
        Notificacio ultima = null;
        for (Notificacio notificacio : notificacions.values()) {
            if (!notificacio.isLlegida()) {
                ultima = notificacio;
            }
        }
        if (ultima != null) {
            System.out.println("Última notificació: " + ultima);
        } else {
            System.out.println("No hi ha notificacions sense llegir.");
        }
    }

    private static void marcarTotesLlegides(HashMap<Integer, Notificacio> notificacions) {
        for (Notificacio notificacio : notificacions.values()) {
            notificacio.setLlegida(true);
        }
        System.out.println("Totes les notificacions han estat marcades com a llegides.");
    }

    private static void mostrarTotalSenseLlegir(HashMap<Integer, Notificacio> notificacions) {
        long totalSenseLlegir = notificacions.values().stream().filter(notificacio -> !notificacio.isLlegida()).count();
        System.out.println("Total de notificacions sense llegir: " + totalSenseLlegir);
    }
}
