package app.controller;

import app.model.Cliente;
import app.model.ClienteDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class RegistroClienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private Button   btnRegistrarCliente;

    private static final Pattern EMAIL_RE = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @FXML
    private void initialize() {
        System.out.println("[Init] RegistroClienteController cargado: " + this);

        // Filtros de entrada
        txtDni.setTextFormatter(new TextFormatter<>(ch ->
                ch.getControlNewText().matches("\\d{0,10}") ? ch : null
        ));
        txtTelefono.setTextFormatter(new TextFormatter<>(ch ->
                ch.getControlNewText().matches("\\+?\\d{0,15}") ? ch : null
        ));

        // Fallback: si por cualquier motivo el FXML no llamó onAction, lo engancho acá también
        if (btnRegistrarCliente != null) {
            btnRegistrarCliente.setOnAction(e -> registrarCliente());
            System.out.println("[Init] btnRegistrarCliente inyectado y onAction enganchado");
        } else {
            System.out.println("[Init] btnRegistrarCliente == null (fx:id o fx:controller?)");
        }
    }

    @FXML
    private void registrarCliente() {
        System.out.println("[Action] registrarCliente() ejecutándose");

        String nombre   = safe(txtNombre.getText());
        String apellido = safe(txtApellido.getText());
        String dni      = safe(txtDni.getText());
        String tel      = safe(txtTelefono.getText());
        String email    = safe(txtEmail.getText());

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            alert(Alert.AlertType.ERROR, "Campos requeridos", "Completá Nombre, Apellido y DNI.");
            return;
        }
        if (!dni.matches("\\d{7,10}")) {
            alert(Alert.AlertType.ERROR, "DNI inválido", "Ingresá solo números (7 a 10 dígitos).");
            return;
        }
        if (!tel.isEmpty() && !tel.matches("\\+?\\d{7,15}")) {
            alert(Alert.AlertType.ERROR, "Teléfono inválido", "Usá solo dígitos (opcional + al inicio).");
            return;
        }
        if (!email.isEmpty() && !EMAIL_RE.matcher(email).matches()) {
            alert(Alert.AlertType.ERROR, "Email inválido", "Formato: nombre@dominio.com");
            return;
        }

        try {
            if (ClienteDAO.existeDni(dni)) {
                alert(Alert.AlertType.WARNING, "Duplicado", "Ya existe un cliente con ese DNI.");
                return;
            }
            if (!email.isEmpty() && ClienteDAO.existeEmail(email)) {
                alert(Alert.AlertType.WARNING, "Duplicado", "Ya existe un cliente con ese email.");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            alert(Alert.AlertType.ERROR, "Error BD", ex.getMessage());
            return;
        }

        Cliente c = new Cliente(nombre, apellido, dni, tel, email, null);
        boolean ok = ClienteDAO.insertar(c);
        if (ok) {
            alert(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado correctamente.");
            limpiar();
        } else {
            alert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el cliente.");
        }
    }

    // ---- Helpers ----
    private static String safe(String s) { return s == null ? "" : s.trim(); }

    private void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtDni.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtNombre.requestFocus();
    }

    private void alert(Alert.AlertType type, String title, String content) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setTitle(title);
        a.setContentText(content);
        a.showAndWait();
    }
}
