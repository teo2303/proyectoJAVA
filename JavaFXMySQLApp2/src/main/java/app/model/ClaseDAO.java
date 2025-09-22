package app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ClaseDAO {

    // ⚠️ Ajustá estos datos si difieren
    private static final String URL =
            "jdbc:mysql://localhost:3306/app_usuarios?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // --- SELECT listado ---
    public static List<Clase> listar() {

        String sql = "SELECT id_clase, nombre, " +
                "       cupo_maximo, id_usuario " +
                "FROM clase ORDER BY id_clase";
        List<Clase> out = new ArrayList<>();
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Clase c = new Clase(
                        rs.getInt("id_clase"),
                        rs.getString("nombre"),
                        rs.getInt("cupo_maximo"),
                        (Integer) rs.getObject("id_usuario")
                );
                out.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL (listar): " + e.getMessage(), e);
        }
        return out;
    }

    // --- INSERT y devolver ID ---
    public static int insertarYDevolverId(Clase c) {
        String sql = "INSERT INTO clase (nombre, cupo_maximo, id_usuario) " +
                "VALUES (?,?,?)";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getCupoMaximo());
            if (c.getIdUsuario() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, c.getIdUsuario());

            int rows = ps.executeUpdate();
            if (rows == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("SQL (insertarYDevolverId): " + e.getMessage(), e);
        }
    }

    // --- UPDATE ---
    public static boolean actualizar(Clase c) {
        String sql = "UPDATE clase SET nombre=?, cupo_maximo=?, id_usuario=? WHERE id_clase=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getCupoMaximo());
            if (c.getIdUsuario() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, c.getIdUsuario());
            ps.setInt(4, c.getIdClase());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL (actualizar): " + e.getMessage(), e);
        }
    }

    // --- DELETE ---
    public static boolean eliminar(int idClase) {
        String sql = "DELETE FROM clase WHERE id_clase=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("SQL (eliminar): " + e.getMessage(), e);
        }
    }
}
 
