package app.controller;

import app.model.Membresia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class MembresiaController {

    @FXML private TableView<Membresia> membershipTable;
    @FXML private TableColumn<Membresia, Integer> colId;
    @FXML private TableColumn<Membresia, String> colNombre;
    @FXML private TableColumn<Membresia, Integer> colDuracion;
    @FXML private TableColumn<Membresia, Double> colPrecio;

    @FXML private TextField tfId;
    @FXML private TextField tfNombre;
    @FXML private Spinner<Integer> spnDuracion;
    @FXML private TextField tfPrecio;

    @FXML private Label lblStatus;

    private ObservableList<Membresia> listaMembresias = FXCollections.observableArrayList();

    // Configuración inicial
    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdMembresia()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colDuracion.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDuracionDias()).asObject());
        colPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecio()).asObject());

        spnDuracion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1095, 30));

        cargarMembresias();

        membershipTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) mostrarDetalles(newSel);
        });
    }

    // Conexión a la BD
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/app_usuarios";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    // Cargar registros desde la BD
    private void cargarMembresias() {
        listaMembresias.clear();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM membresia")) {
            while (rs.next()) {
                listaMembresias.add(new Membresia(
                        rs.getInt("id_membresia"),
                        rs.getString("nombre"),
                        rs.getInt("duracion_dias"),
                        rs.getDouble("precio")
                ));
            }
            membershipTable.setItems(listaMembresias);
            lblStatus.setText("Membresías cargadas");
        } catch (SQLException e) {
            lblStatus.setText("Error cargando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mostrar detalles en el formulario
    private void mostrarDetalles(Membresia m) {
        tfId.setText(String.valueOf(m.getIdMembresia()));
        tfNombre.setText(m.getNombre());
        spnDuracion.getValueFactory().setValue(m.getDuracionDias());
        tfPrecio.setText(String.valueOf(m.getPrecio()));
    }

    // Botón Nuevo
    @FXML
    private void onNew() {
        tfId.clear();
        tfNombre.clear();
        spnDuracion.getValueFactory().setValue(30);
        tfPrecio.clear();
        lblStatus.setText("Formulario listo para nueva membresía");
    }

    // Botón Guardar
    @FXML
    private void onSave() {
        String nombre = tfNombre.getText();
        int duracion = spnDuracion.getValue();
        double precio;

        try {
            precio = Double.parseDouble(tfPrecio.getText());
        } catch (NumberFormatException e) {
            lblStatus.setText("Precio inválido");
            return;
        }

        if (tfId.getText().isEmpty()) {
            // Insertar nuevo
            String sql = "INSERT INTO membresia (nombre, duracion_dias, precio) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setInt(2, duracion);
                ps.setDouble(3, precio);
                ps.executeUpdate();
                lblStatus.setText("Membresía agregada");
                cargarMembresias();
                onNew();
            } catch (SQLException e) {
                lblStatus.setText("Error al guardar: " + e.getMessage());
            }
        } else {
            // Actualizar existente
            int id = Integer.parseInt(tfId.getText());
            String sql = "UPDATE membresia SET nombre=?, duracion_dias=?, precio=? WHERE id_membresia=?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setInt(2, duracion);
                ps.setDouble(3, precio);
                ps.setInt(4, id);
                ps.executeUpdate();
                lblStatus.setText("Membresía actualizada");
                cargarMembresias();
            } catch (SQLException e) {
                lblStatus.setText("Error al actualizar: " + e.getMessage());
            }
        }
    }

    // Botón Editar (carga datos seleccionados)
    @FXML
    private void onEdit() {
        Membresia seleccionada = membershipTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            mostrarDetalles(seleccionada);
            lblStatus.setText("Editando membresía ID " + seleccionada.getIdMembresia());
        } else {
            lblStatus.setText("Seleccione una membresía para editar");
        }
    }

    // Botón Eliminar
    @FXML
    private void onDelete() {
        Membresia seleccionada = membershipTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            String sql = "DELETE FROM membresia WHERE id_membresia=?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, seleccionada.getIdMembresia());
                ps.executeUpdate();
                lblStatus.setText("Membresía eliminada");
                cargarMembresias();
            } catch (SQLException e) {
                lblStatus.setText("Error al eliminar: " + e.getMessage());
            }
        } else {
            lblStatus.setText("Seleccione una membresía para eliminar");
        }
    }

    // Botón Cancelar
    @FXML
    private void onCancel() {
        onNew();
        lblStatus.setText("Edición cancelada");
    }
}
