package app.controller;

import app.model.Socio;
import app.model.SocioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesActivosController {

    @FXML private TableView<Socio> tablaClientes;
    @FXML private TableColumn<Socio, String> colNombre;
    @FXML private TableColumn<Socio, String> colApellido;
    @FXML private TableColumn<Socio, String> colDni;
    @FXML private TableColumn<Socio, String> colTelefono;
    @FXML private TableColumn<Socio, String> colEmail;

    private final ObservableList<Socio> socios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlazar columnas con atributos del modelo
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Cargar datos desde DAO
        socios.setAll(SocioDAO.listar());
        tablaClientes.setItems(socios);
    }
}
