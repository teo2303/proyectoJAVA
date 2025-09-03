package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showInicio(); // arranca en inicio
    }

    // ---- Navegar a INICIO ----
    public static void showInicio() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/app/inicio.fxml"));
            primaryStage.setTitle("Inicio");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- Navegar a ABM PAGOS ----
    public static void showPagos() {
        try {
            Parent root = FXMLLoader.load(MainApp.class.getResource("/app/app/pago.fxml"));
            primaryStage.setTitle("ABM Pagos");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}

