package app.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioClaseDAO {

    // ⚠️ Ajustá a tu base (usá los mismos valores que en ClaseDAO)
    private static final String URL = "jdbc:mysql://localhost:3306/app_usuarios";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ===== CRUD =====

    public static List<HorarioClase> listarPorClase(int idClase) {
        String sql = "SELECT id, id_clase, dia, DATE_FORMAT(hora_inicio,'%H:%i') AS hi, " +
                "DATE_FORMAT(hora_fin,'%H:%i') AS hf " +
                "FROM horario_clase WHERE id_clase=? ORDER BY FIELD(dia,'Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo'), hora_inicio";
        List<HorarioClase> list = new ArrayList<>();
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HorarioClase h = new HorarioClase();
                    h.setId(rs.getInt("id"));
                    h.setIdClase(rs.getInt("id_clase"));
                    h.setDia(rs.getString("dia"));
                    h.setHoraInicio(rs.getString("hi"));
                    h.setHoraFin(rs.getString("hf"));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL (listarPorClase): " + e.getMessage(), e);
        }
        return list;
    }

    public static boolean insertar(HorarioClase h) {
        String sql = "INSERT INTO horario_clase (id_clase, dia, hora_inicio, hora_fin) VALUES (?,?,?,?)";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, h.getIdClase());
            ps.setString(2, h.getDia());
            ps.setString(3, h.getHoraInicio()); // "HH:mm"
            ps.setString(4, h.getHoraFin());    // "HH:mm"
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("SQL (insertar): " + e.getMessage(), e);
        }
    }

    public static void insertarLote(int idClase, List<HorarioClase> turnos) {
        String sql = "INSERT INTO horario_clase (id_clase, dia, hora_inicio, hora_fin) VALUES (?,?,?,?)";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            for (HorarioClase h : turnos) {
                ps.setInt(1, idClase);
                ps.setString(2, h.getDia());
                ps.setString(3, h.getHoraInicio());
                ps.setString(4, h.getHoraFin());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("SQL (insertarLote): " + e.getMessage(), e);
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM horario_clase WHERE id=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("SQL (eliminar): " + e.getMessage(), e);
        }
    }

    public static void eliminarPorClase(int idClase) {
        String sql = "DELETE FROM horario_clase WHERE id_clase=?";
        try (Connection cn = getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL (eliminarPorClase): " + e.getMessage(), e);
        }
    }

    // Wrapper para compatibilidad con código viejo
    public static void insertarHorarios(int idClase, List<String> dias, String hi, String hf) {
        List<HorarioClase> list = new ArrayList<>();
        for (String d : dias) {
            HorarioClase h = new HorarioClase();
            h.setIdClase(idClase);
            h.setDia(d);
            h.setHoraInicio(hi);
            h.setHoraFin(hf);
            list.add(h);
        }
        insertarLote(idClase, list);
    }
    // HorarioClaseDAO


}
