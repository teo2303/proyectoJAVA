package app.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/app_usuarios";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono, dni) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getDni());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existeUsuario(String email, String telefono, String dni) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? OR telefono = ? OR dni = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, telefono);
            stmt.setString(3, dni);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true si ya existe
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
