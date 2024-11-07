package com.project;

import org.java-websocket.server.WebSocketServer;
import org.java-websocket.WebSocket;
import org.java-websocket.handshake.ClientHandshake;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer extends WebSocketServer {

    private static final AtomicInteger connectionCount = new AtomicInteger(0);
    private static final int MAX_CONNECTIONS = 5; // Máximo número de conexiones permitidas
    private static final Map<WebSocket, String> clients = new ConcurrentHashMap<>();

    public MainServer(int port) {
        super(port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (connectionCount.incrementAndGet() > MAX_CONNECTIONS) {
            conn.close();
            connectionCount.decrementAndGet();
            System.out.println("S'ha arribat al màxim de connexions. Connexió rebutjada.");
        } else {
            clients.put(conn, "Client_" + connectionCount.get());
            System.out.println("Nou client connectat: Client_" + connectionCount.get());
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String clientName = clients.get(conn);
        clients.remove(conn);
        connectionCount.decrementAndGet();
        System.out.println("Client desconnectat: " + clientName);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String clientName = clients.get(conn);
        System.out.println("Missatge de " + clientName + ": " + message);

        JSONObject response = new JSONObject();
        switch (message) {
            case "Beguda":
            case "Primer plat":
            case "Reposteria":
            case "Tapa":
            case "Postre":
                response.put("type", "tags");
                response.put("tags", FuncsBar.mostrarTags(message)); // Aquí usas tu lógica de tags
                break;
            case "productes":
                response.put("type", "productes");
                response.put("products", FuncsBar.mostrarProductes()); // Aquí usas tu lógica de productos
                break;
            default:
                response.put("type", "error");
                response.put("message", "Comanda desconeguda");
                break;
        }
        conn.send(response.toString());  // Enviar resposta al client
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Servidor WebSocket iniciat al port: " + getPort());
    }

    public static void main(String[] args) {
        MainServer server = new MainServer(4545);
        server.start();

        System.out.println("Servidor en execució. Prem CTRL+C per aturar-lo.");
    }
}
