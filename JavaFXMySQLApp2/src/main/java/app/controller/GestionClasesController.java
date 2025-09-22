package app.controller;

import app.model.Clase;
import app.model.ClaseDAO;
import app.model.HorarioClase;
import app.model.HorarioClaseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GestionClasesController {

    // tablas
    @FXML private TableView<Clase> tblClases;
    @FXML private TableColumn<Clase, Integer> colId;
    @FXML private TableColumn<Clase, String> colNombre;
    @FXML private TableColumn<Clase, Integer> colCupo;

    @FXML private TableView<HorarioClase> tblHorarios;
    @FXML private TableColumn<HorarioClase, String> colDiaH;
    @FXML private TableColumn<HorarioClase, String> colIniH;
    @FXML private TableColumn<HorarioClase, String> colFinH;

    // form
    @FXML private TextField tfNombre;
    @FXML private Spinner<Integer> spCupo;
    @FXML private CheckBox chkLunes, chkMartes, chkMiercoles, chkJueves, chkViernes, chkSabado, chkDomingo;
    @FXML private TextField tfHoraIni, tfHoraFin;

    // botones
    @FXML private Button btnNuevo, btnGuardar, btnEliminarClase, btnEliminarHorario;

    @FXML private Label lblStatus;

    private final ObservableList<Clase> clases = FXCollections.observableArrayList();
    private final ObservableList<HorarioClase> horarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // columnas clases
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getIdClase()).asObject());
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colCupo.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getCupoMaximo()).asObject());
        tblClases.setItems(clases);

        // columnas horarios (modelo con String para horas)
        colDiaH.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDia()));
        colIniH.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getHoraInicio()));
        colFinH.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getHoraFin()));
        tblHorarios.setItems(horarios);

        // spinner cupo
        spCupo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 20));

        // eventos
        tblClases.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) cargarHorarios(sel.getIdClase());
            else horarios.clear();
        });

        btnNuevo.setOnAction(e -> limpiarForm());
        btnGuardar.setOnAction(e -> guardarClase());
        btnEliminarClase.setOnAction(e -> eliminarClaseSeleccionada());
        btnEliminarHorario.setOnAction(e -> eliminarHorarioSeleccionado());

        cargarClases();
    }

    // === acciones ===

    private void cargarClases() {
        clases.setAll(ClaseDAO.listar());
        lblStatus.setText("Clases: " + clases.size());
    }

    private void cargarHorarios(int idClase) {
        horarios.setAll(HorarioClaseDAO.listarPorClase(idClase));
        lblStatus.setText("Horarios cargados: " + horarios.size());
    }

    private void limpiarForm() {
        tfNombre.clear();
        spCupo.getValueFactory().setValue(20);
        chkLunes.setSelected(false); chkMartes.setSelected(false); chkMiercoles.setSelected(false);
        chkJueves.setSelected(false); chkViernes.setSelected(false); chkSabado.setSelected(false); chkDomingo.setSelected(false);
        tfHoraIni.clear(); tfHoraFin.clear();
        lblStatus.setText("Formulario limpio");
    }

    private void guardarClase() {
        String nombre = tfNombre.getText();
        Integer cupo = spCupo.getValue();
        String hi = tfHoraIni.getText();
        String hf = tfHoraFin.getText();

        if (nombre == null || nombre.isBlank()) { alert("Ingresá un nombre"); return; }
        if (!chkLunes.isSelected() && !chkMartes.isSelected() && !chkMiercoles.isSelected() &&
                !chkJueves.isSelected() && !chkViernes.isSelected() && !chkSabado.isSelected() && !chkDomingo.isSelected()) {
            alert("Seleccioná al menos un día"); return;
        }
        if (!isHHMM(hi) || !isHHMM(hf)) { alert("Horas inválidas. Usá HH:mm (ej: 09:00)"); return; }

        try {
            // 1) inserto clase y obtengo ID
            Clase nueva = new Clase(0,nombre , null, null);
            int idClase = ClaseDAO.insertarYDevolverId(nueva);

            // 2) armo lista de días seleccionados
            java.util.List<String> dias = new java.util.ArrayList<>();
            if (chkLunes.isSelected()) dias.add("Lunes");
            if (chkMartes.isSelected()) dias.add("Martes");
            if (chkMiercoles.isSelected()) dias.add("Miércoles");
            if (chkJueves.isSelected()) dias.add("Jueves");
            if (chkViernes.isSelected()) dias.add("Viernes");
            if (chkSabado.isSelected()) dias.add("Sábado");
            if (chkDomingo.isSelected()) dias.add("Domingo");

            // 3) inserto horarios (batch)
            HorarioClaseDAO.insertarHorarios(idClase, dias, hi, hf);

            // 4) refresco UI
            cargarClases();
            // re-selecciono la nueva clase por ID
            tblClases.getItems().stream().filter(c -> c.getIdClase() == idClase).findFirst()
                    .ifPresent(c -> tblClases.getSelectionModel().select(c));

            lblStatus.setText("Clase y horarios guardados");
            limpiarForm();
        } catch (RuntimeException ex) {
            alert("Error guardando: " + ex.getMessage());
        }
    }

    private void eliminarClaseSeleccionada() {
        Clase sel = tblClases.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Seleccioná una clase"); return; }
        // primero borrar horarios (FK), luego clase
        try {
            for (HorarioClase h : HorarioClaseDAO.listarPorClase(sel.getIdClase())) {
                HorarioClaseDAO.eliminar(h.getId());
            }
            if (ClaseDAO.eliminar(sel.getIdClase())) {
                clases.remove(sel); horarios.clear();
                lblStatus.setText("Clase eliminada");
            } else alert("No se pudo eliminar la clase");
        } catch (RuntimeException ex) {
            alert("Error eliminando: " + ex.getMessage());
        }
    }

    private void eliminarHorarioSeleccionado() {
        HorarioClase selH = tblHorarios.getSelectionModel().getSelectedItem();
        Clase selC = tblClases.getSelectionModel().getSelectedItem();
        if (selH == null) { alert("Seleccioná un horario"); return; }
        try {
            if (HorarioClaseDAO.eliminar(selH.getId())) {
                if (selC != null) cargarHorarios(selC.getIdClase());
                lblStatus.setText("Horario eliminado");
            } else alert("No se pudo eliminar el horario");
        } catch (RuntimeException ex) {
            alert("Error eliminando: " + ex.getMessage());
        }
    }

    // === helpers ===
    private boolean isHHMM(String s) { return s != null && s.matches("\\d{2}:\\d{2}"); }
    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null); a.showAndWait();
    }
}
