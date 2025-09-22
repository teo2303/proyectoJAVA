package app.model;

import app.model.Membresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAO {

    private final String url = "jdbc:mysql://localhost:3306/app_usuarios";
    private final String user = "root";     // cámbialo según tu configuración
    private final String password = "";     // cámbialo según tu configuración

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // --- CREATE ---
    public boolean insertar(Membresia membresia) {
        String sql = "INSERT INTO membresia (nombre, duracion_dias, precio) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, membresia.getNombre());
            ps.setInt(2, membresia.getDuracionDias());
            ps.setDouble(3, membresia.getPrecio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error insertando membresía: " + e.getMessage());
            return false;
        }
    }

    // --- READ ALL ---
    public List<Membresia> listar() {
        List<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM membresia";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Membresia m = new Membresia(
                        rs.getInt("id_membresia"),
                        rs.getString("nombre"),
                        rs.getInt("duracion_dias"),
                        rs.getDouble("precio")
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error listando membresías: " + e.getMessage());
        }
        return lista;
    }

    // --- READ ONE ---
    public Membresia buscarPorId(int id) {
        String sql = "SELECT * FROM membresia WHERE id_membresia = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Membresia(
                        rs.getInt("id_membresia"),
                        rs.getString("nombre"),
                        rs.getInt("duracion_dias"),
                        rs.getDouble("precio")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error buscando membresía: " + e.getMessage());
        }
        return null;
    }

    // --- UPDATE ---
    public boolean actualizar(Membresia membresia) {
        String sql = "UPDATE membresia SET nombre=?, duracion_dias=?, precio=? WHERE id_membresia=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, membresia.getNombre());
            ps.setInt(2, membresia.getDuracionDias());
            ps.setDouble(3, membresia.getPrecio());
            ps.setInt(4, membresia.getIdMembresia());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error actualizando membresía: " + e.getMessage());
            return false;
        }
    }

    // --- DELETE ---
    public boolean eliminar(int id) {
        String sql = "DELETE FROM membresia WHERE id_membresia=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error eliminando membresía: " + e.getMessage());
            return false;
        }
    }
}
