
package app.controller;

import app.model.Usuario;
import app.model.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class FormController {

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField emailField;
    @FXML private TextField dniField;

    @FXML
    private void initialize() {
        // Solo dígitos y máx. 8 caracteres para DNI
        dniField.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) {
                dniField.setText(newV.replaceAll("\\D", ""));
            }
            if (dniField.getText().length() > 8) {
                dniField.setText(dniField.getText().substring(0, 8));
            }
        });
    }

    @FXML
    private void guardarUsuario() {
        String nombre   = safeText(nombreField);
        String apellido = safeText(apellidoField);
        String email    = safeText(emailField);
        String dni      = safeText(dniField);

        // Campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || dni.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Email válido
        if (!validarEmail(email)) {
            mostrarAlerta("Email inválido", "Por favor ingresá un email válido (ej: juana@mail.com).", Alert.AlertType.ERROR);
            return;
        }

        // DNI válido
        if (!validarDni(dni)) {
            mostrarAlerta("DNI inválido", "El DNI debe tener solo números y entre 7 y 8 dígitos.", Alert.AlertType.ERROR);
            return;
        }

        // Duplicados
        if (UsuarioDAO.existeUsuario(email, dni)) {
            mostrarAlerta("Error", "Ya existe un usuario con el mismo Email o DNI.", Alert.AlertType.ERROR);
            return;
        }

        // Insertar
        Usuario usuario = new Usuario(nombre, apellido, email, dni);
        boolean registrado = UsuarioDAO.insertar(usuario);

        if (registrado) {
            mostrarAlerta("Éxito", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el usuario.", Alert.AlertType.ERROR);
        }
    }

    // -------- Utilidades --------

    private String safeText(TextField tf) {
        return tf.getText() == null ? "" : tf.getText().trim();
    }

    // Regex  para email
    private boolean validarEmail(String email) {
        // Acepta letras, números, + _ . - antes de @ y dominios simples
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    // Solo números, 7 u 8 dígitos
    private boolean validarDni(String dni) {
        String regex = "^[0-9]{7,8}$";
        return dni != null && dni.matches(regex);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        dniField.clear();
    }
}
