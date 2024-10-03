package pl.m4code.database;

import pl.m4code.Main;


import java.sql.*;
import java.util.UUID;

public class DatabaseConnection {
    private static final String HOST = Main.getInstance().getConfig().getString("database.host");
    private static final String PORT = Main.getInstance().getConfig().getString("database.port");
    private static final String NAME = Main.getInstance().getConfig().getString("database.name");
    private static final String USER = Main.getInstance().getConfig().getString("database.user");
    private static final String PASSWORD = Main.getInstance().getConfig().getString("database.password");

    private static final String JDBC_URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, NAME);

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createTableIfNotExists() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS DcUsers (" +
                             "uuid VARCHAR(64) PRIMARY KEY, " +
                             "name VARCHAR(32), " +
                             "connected BOOLEAN, " +
                             "discordId BIGINT, " +
                             "code BIGINT" +
                             ")"
             )
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUser(UUID uuid, String name, boolean connected, long code) {
        String insertUserSQL = "INSERT INTO DcUsers (uuid, name, connected, discordId, code) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertUserSQL)
        ) {
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.setBoolean(3, connected);
            statement.setNull(4, java.sql.Types.BIGINT);
            statement.setLong(5, code);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConnected(String nick, boolean connected) {
        if (nick == null) {
            // Obsłuż sytuację, gdy nick jest null
            System.err.println("Błąd: Nick jest null");
            return;
        }
        String query = "UPDATE DcUsers SET connected = ? WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setBoolean(1, connected);
            statement.setString(2, nick.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDiscordId(String id, String name) {
        String query = "UPDATE DcUsers SET discordId = ? WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, id); // Ustaw identyfikator Discord
            statement.setString(2, name); // Ustaw nazwę użytkownika
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setCode(UUID uuid, long code) {
        String query = "UPDATE DcUsers SET code = ? WHERE uuid = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, code);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isMcConnected(String nickname) {
        String query = "SELECT connected FROM DcUsers WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, nickname);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("connected");
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isCodeExist(String nickname) {
        String query = "SELECT code FROM DcUsers WHERE name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, nickname);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isDiscordUserExist(String discordId) {
        String query = "SELECT discordId FROM DcUsers WHERE discordId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, discordId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Zwraca true, jeśli discordId istnieje
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserConnected(String name) {
        boolean exists = false;
        boolean connected = false;
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT connected FROM DcUsers WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exists = true;
                connected = rs.getBoolean("connected");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Logging the exception
        }
        return exists && connected; // Return true if user exists and is connected
    }

    public String getNicknameByCode(long code) {
        String query = "SELECT name FROM DcUsers WHERE code = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, code);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
