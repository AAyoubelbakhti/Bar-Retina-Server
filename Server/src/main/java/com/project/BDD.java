package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BDD {

    private static final String URL = "jdbc:mysql://localhost:3306/comandes";
    private static final String USER = "barretina";
    private static final String PASSWORD = "barretina";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("S'ha connectat a la base de dades");
        } catch (SQLException e) {
            System.err.println("Error al connectar a la base de dades: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexió a la base de dades tancada");
            } catch (SQLException e) {
                System.err.println("Error al tancar la connexió: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            connect();
        }
        return connection;
    }

    public boolean insertComanda(int idTaula, int idCambrer, String comanda, String dataComanda, String estatComanda,
            double preuComanda, boolean pagada) {
        String sql = "INSERT INTO comandes (id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, idTaula);
            statement.setInt(2, idCambrer);
            statement.setString(3, comanda);
            statement.setString(4, dataComanda);
            statement.setString(5, estatComanda);
            statement.setDouble(6, preuComanda);
            statement.setBoolean(7, pagada);

            int rowsInserted = statement.executeUpdate();
            System.out.println("Comanda inserida");
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al inserir la comanda: " + e.getMessage());
            return false;
        }
    }

    public JSONArray obtenirComandes() {
        String sql = "SELECT id_comanda, id_taula, id_cambrer, comanda, data_comanda, estat_comanda, preu_comanda, pagada FROM comandes ORDER BY data_comanda DESC";
        JSONArray arrayComandes = new JSONArray();

        try (PreparedStatement statement = getConnection().prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                JSONObject comanda = new JSONObject();

                comanda.put("id_comanda", resultSet.getInt("id_comanda"));
                comanda.put("id_taula", resultSet.getInt("id_taula"));
                comanda.put("id_cambrer", resultSet.getInt("id_cambrer"));
                comanda.put("comanda", resultSet.getString("comanda"));
                comanda.put("data_comanda", resultSet.getString("data_comanda"));
                comanda.put("estat_comanda", resultSet.getString("estat_comanda"));
                comanda.put("preu_comanda", resultSet.getDouble("preu_comanda"));
                comanda.put("pagada", resultSet.getBoolean("pagada"));
                arrayComandes.put(comanda);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtenir les comandes: " + e.getMessage());
        }
        return arrayComandes;
    }

    public boolean cambiComanda(int idComanda, Integer idTaula, Integer idCambrer, String comanda, String dataComanda,
            String estatComanda, Double preuComanda, Boolean pagada) {
        StringBuilder sql = new StringBuilder("UPDATE comandes SET ");
        List<Object> params = new ArrayList<>();
        if (idTaula != null) {
            sql.append("id_taula = ?, ");
            params.add(idTaula);
        }
        if (idCambrer != null) {
            sql.append("id_cambrer = ?, ");
            params.add(idCambrer);
        }
        if (comanda != null) {
            sql.append("comanda = ?, ");
            params.add(comanda);
        }
        if (dataComanda != null) {
            sql.append("data_comanda = ?, ");
            params.add(dataComanda);
        }
        if (estatComanda != null) {
            sql.append("estat_comanda = ?, ");
            params.add(estatComanda);
        }
        if (preuComanda != null) {
            sql.append("preu_comanda = ?, ");
            params.add(preuComanda);
        }
        if (pagada != null) {
            sql.append("pagada = ?, ");
            params.add(pagada);
        }
        if (params.isEmpty()) {
            System.out.println("No hi  ha camps");
            return false;
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id_comanda = ?");
        params.add(idComanda);

        try (PreparedStatement statement = getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Comanda actualitzada");
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualitzar la comanda: " + e.getMessage());
            return false;
        }
    }

}
