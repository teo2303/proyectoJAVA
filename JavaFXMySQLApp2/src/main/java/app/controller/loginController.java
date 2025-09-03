package app.controller;

import app.model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtDni;

    @FXML
    private Label lblMensaje;

    @FXML
    private void validarLogin() {
        String email = txtEmail.getText().trim();
        String dni = txtDni.getText().trim();

        if (email.isEmpty() || dni.isEmpty()) {
            lblMensaje.setText("Por favor complete todos los campos.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND dni = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, dni);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lblMensaje.setText("¡Inicio de sesión exitoso!");
                lblMensaje.setStyle("-fx-text-fill: green;");

            } else {
                lblMensaje.setText("Datos incorrectos. Intente nuevamente.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMensaje.setText("Error al conectar con la base de datos.");
        }
    }
}
