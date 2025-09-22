package app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class SocioDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/app_usuarios?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // --- SELECT todos los socios ---
    public static List<Socio> listar() {
        String sql = "SELECT id_cliente, nombre, apellido, dni, telefono, email FROM cliente ORDER BY id_cliente";
        List<Socio> out = new ArrayList<>();
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Socio s = new Socio(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                out.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL (listar socios): " + e.getMessage(), e);
        }
        return out;
    }
}
