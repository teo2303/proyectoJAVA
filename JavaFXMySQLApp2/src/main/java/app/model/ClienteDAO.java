
package app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.SQLException;

public class ClienteDAO {


    private static final String URL = "jdbc:mysql://localhost:3306/app_usuarios";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection conn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean insertar(Cliente c) {
        String sql = "INSERT INTO Cliente (nombre, apellido, dni, telefono, email, id_membresia) VALUES (?,?,?,?,?,?)";
        try (Connection cn = conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            if (c.getIdMembresia() == null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, c.getIdMembresia());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existeDni(String dni) throws SQLException {
        String sql = "SELECT 1 FROM Cliente WHERE dni = ? LIMIT 1";
        try (Connection cn = conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM Cliente WHERE email = ? LIMIT 1";
        try (Connection cn = conn();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}

