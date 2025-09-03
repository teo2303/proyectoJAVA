package app.controller;

import app.model.Usuario;
import app.model.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class FormController {
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField telefonoField;
    @FXML private TextField dniField;

    @FXML
    private void guardarUsuario() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();
        String dni = dniField.getText();

        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || dni.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return;
        }

        // Validar duplicados antes de insertar
        if (UsuarioDAO.existeUsuario(email, telefono, dni)) {
            mostrarAlerta("Error", "Ya existe un usuario con el mismo Email, Teléfono o DNI.");
            return;
        }

        Usuario usuario = new Usuario(nombre, email, telefono, dni);
        boolean registrado = UsuarioDAO.insertar(usuario);

        if (registrado) {
            mostrarAlerta("Éxito", "Usuario registrado correctamente");
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el usuario");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void limpiarCampos() {
        nombreField.clear();
        emailField.clear();
        telefonoField.clear();
        dniField.clear();
    }
}

