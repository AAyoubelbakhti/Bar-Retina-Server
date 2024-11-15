package com.project;

import org.java_websocket.server.WebSocketServer; 
import org.java_websocket.WebSocket; 
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer extends WebSocketServer {

    private static final AtomicInteger connectionCount = new AtomicInteger(0);
    private static final int MAX_CONNECTIONS = 5;
    private static final Map<WebSocket, String> clients = new ConcurrentHashMap<>();
    private final BDD bdd;

    public MainServer(int port) {
        super(new InetSocketAddress(port));
        this.bdd = new BDD();
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

        JSONObject messageJson = new JSONObject(message);
        String type = messageJson.getString("type");
        String clientName = clients.get(conn);
        System.out.println("Missatge de " + clientName + ": " + message);
        JSONObject response = new JSONObject();
        switch (type) {
            case "tags":
                String tag = messageJson.getString("body");
                response.put("type", "tags");
                response.put("products", FuncsBar.mostrarTags(tag));
                break;
            case "productes":
                response.put("type", "productes");
                response.put("products", FuncsBar.mostrarProductes());
                List<Comanda> comandes = bdd.obtenirComandes();
                break;
            case "select-comanda":
                // List<Comanda> comandes = bdd.obtenirComandes();
                // response.put("type", "comandes");
                // response.put("body", comandes.toString());
                break;

            default:
                response.put("type", "error");
                response.put("message", "Comanda desconeguda");
                break;
        }
        //System.out.println("response: " + response.toString());
        conn.send(response.toString()); 
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        bdd.connect();
        System.out.println("Servidor WebSocket iniciat al port: " + getPort());
    }

    public static void main(String[] args) {
        MainServer server = new MainServer(3000);
        server.start();

        System.out.println("Servidor en execució. Prem CTRL+C per aturar-lo.");
    }
}
