package app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class InicioController {

    @FXML
    private void abrirRegistro(ActionEvent event) {
        cargarVentana("/app/formulario.fxml", event);
    }

    @FXML
    private void abrirLogin(ActionEvent event) {
        cargarVentana("/app/login.fxml", event);
    }

    private void cargarVentana(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
