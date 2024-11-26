package com.project;

import org.java_websocket.server.WebSocketServer; 
import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        //System.out.println("Missatge de " + clientName + ": " + message);

        JSONObject response = new JSONObject();
        switch (type) {
            case "tags":
                response.put("type", "tags");
                response.put("products", FuncsBar.mostrarTags(messageJson.getString("body")));
                response.put("imatges", FuncsBar.mostrarImagenes().toString());
                break;
            case "productes":
                JSONArray comandes = bdd.obtenirComandes();
                response.put("type", "productes");
                response.put("products", FuncsBar.mostrarProductes());
                response.put("body", comandes.toString());
                response.put("imatges", FuncsBar.mostrarImagenes().toString());
                break;
            case "top-productes":
            System.out.println("Estoy en el caso del servidor ");
                comandes = bdd.obtenirComandes();
                response.put("type", "top-productes");
                response.put("products", FuncsBar.mostrarTopProductes(comandes));
                // response.put("imatges", FuncsBar.mostrarImagenes().toString());
               // response.put("body", comandes.toString());

                break;
            case "select-comanda":
                comandes = bdd.obtenirComandes();
                response.put("type", "comandes");
                response.put("body", comandes.toString());
                break;

            case "select-comanda-taula":
                JSONObject comanda = bdd.obtenirÚltimaComandaPerTaula(messageJson.getInt("idMesa"));
                response.put("type", "comanda-taula");
                response.put("body", comanda.toString());
                break;
               
            case "update-comanda":
            case "insert-comanda":
                JSONObject messajeData = messageJson.getJSONObject("body"); 
            // [{"id":"1","nom":"CafÃ¨","descripcio":"Beguda calenta feta de grans de cafÃ¨.","imatge":"cafe.png","preu":3,"quantitat":2},
            // {"id":"2","nom":"Te","descripcio":"Beguda calenta feta de fulles de te.","imatge":"te.png","preu":6.5,"quantitat":5},{"id":"3","nom":"Refresc","descripcio":"Beguda freda amb gas.","imatge":"refresc.png","preu":2,"quantitat":2},
            // {"id":"4","nom":"Suc de taronja","descripcio":"Suc de taronja acabat d'esprÃ©mer.","imatge":"suc-de-taronja.png","preu":1.8,"quantitat":1}]
                String comandaTxt = messajeData.getJSONArray("comandaTxt").toString(); 

                int idTaula = messajeData.getInt("idTaula");
               
                int idCambrer = messajeData.getInt("idCambrer");
                String estatComanda = messajeData.getString("estatComanda");
                Double preuComanda = messajeData.getDouble("preuComanda");

                Date dataComanda = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(dataComanda);
                
                if (type.equals("update-comanda")){
                    int idComanda = bdd.obtenirUltimIdComandaPerTaula(idTaula);
                    bdd.cambiComanda(idComanda, idTaula, idCambrer, comandaTxt , formattedDate.toString(), estatComanda, preuComanda, false);
                    if(messajeData.getBoolean("llest")){
                        JSONObject mensaje = new JSONObject();
                        mensaje.put("type", "producte-llest");
                        String productName = messajeData.getString("producte");
                        mensaje.put("body", "El producto "+ productName+ " esta listo");
                       // conn.send(mensaje.toString());
                       broadcastMessage(mensaje.toString(), null);
                        System.out.println("Joloco");
                    }
                }else {
                    bdd.insertComanda(idTaula, idCambrer, comandaTxt , formattedDate.toString(), estatComanda, preuComanda, false);

                }
                System.out.println(message);
               
                break;

            
                
                // insertComanda(int idTaula, int idCambrer, String comanda, String dataComanda, String estatComanda,
                // double preuComanda, boolean pagada)
                

            default:
                response.put("type", "error");
                response.put("message", "Comanda desconeguda");
                break;
        }
        //System.out.println("response: " + response.toString());
        conn.send(response.toString()); 
    }


      private void broadcastMessage(String message, WebSocket sender) {
        for (Map.Entry<WebSocket, String> entry : clients.entrySet()) {
            WebSocket conn = entry.getKey();
            if (conn != sender) {
                try {
                    conn.send(message);
                } catch (WebsocketNotConnectedException e) {
                    System.out.println("Client " + entry.getValue() + " not connected.");
                    clients.remove(conn);
                    //availableNames.add(entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
