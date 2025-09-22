package app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class InicioController {

    @FXML private StackPane contentArea;

    @FXML
    private void irAlRegistro() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/formulario.fxml"));
            contentArea.getChildren().setAll(view);   // se muestra dentro del dashboard
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irAlLogin() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/login.fxml"));
            contentArea.getChildren().setAll(view);   // se muestra dentro del dashboard
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void IrRegistroClientes() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/clientes.fxml"));
            contentArea.getChildren().setAll(view);   // se muestra dentro del dashboard
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void IrMembresia(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/membresia.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Nuevo: carga la vista de clases dentro del dashboard
    @FXML
    private void irAClases(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/Clase.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irAlListado(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/app/clientesActivos.fxml"));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
