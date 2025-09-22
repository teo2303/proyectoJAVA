package app.controller;

import app.model.Clase;
import app.model.ClaseDAO;
import app.model.HorarioClase;
import app.model.HorarioClaseDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ClaseController {

    // === Tabla de CLASES (izquierda)
    @FXML private TableView<Clase> classesTable;
    @FXML private TableColumn<Clase, Integer> colId;
    @FXML private TableColumn<Clase, String>  colNombre;
    @FXML private TableColumn<Clase, Integer> colCupo;
    @FXML private TableColumn<Clase, Integer> colUsuario;

    // === Formulario (derecha)
    @FXML private TextField tfId;
    @FXML private TextField tfNombre;
    @FXML private Spinner<Integer> spnCupo;
    @FXML private TextField tfIdUsuario;
    @FXML private Label lblStatus;

    // === Turnos (parte inferior)
    @FXML private TableView<HorarioClase> horariosTable;
    @FXML private TableColumn<HorarioClase, String> colDiaH;
    @FXML private TableColumn<HorarioClase, String> colInicioH;
    @FXML private TableColumn<HorarioClase, String> colFinH;
    @FXML private ComboBox<String> cbDia;
    @FXML private TextField tfHoraInicioH;
    @FXML private TextField tfHoraFinH;

    // listas observables
    private final ObservableList<Clase> listaClases = FXCollections.observableArrayList();
    private final ObservableList<HorarioClase> horariosObs = FXCollections.observableArrayList();

    // -------- Helpers --------
    private boolean isHHMM(String s) { return s != null && s.matches("\\d{2}:\\d{2}"); }

    // -------- Init --------
    @FXML
    public void initialize() {
        // columnas CLASE
        colId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getIdClase()).asObject());
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colCupo.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getCupoMaximo()).asObject());
        colUsuario.setCellValueFactory(d -> {
            Integer idU = d.getValue().getIdUsuario();
            return new SimpleIntegerProperty(idU != null ? idU : 0).asObject();
        });

        // spinner
        if (spnCupo != null) {
            spnCupo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 20));
        }

        // tabla de turnos + combo de días
        if (horariosTable != null) {
            if (colDiaH != null)    colDiaH.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDia()));
            if (colInicioH != null) colInicioH.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHoraInicio()));
            if (colFinH != null)    colFinH.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHoraFin()));
            horariosTable.setItems(horariosObs);
        }
        if (cbDia != null) {
            cbDia.setItems(FXCollections.observableArrayList(
                    "Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"
            ));
        }

        // al seleccionar clase, cargar turnos
        classesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) cargarHorariosDeClase(newSel.getIdClase()); else horariosObs.clear();
        });

        // cargar lista
        cargarDatos();
    }

    private void cargarDatos() {
        listaClases.setAll(ClaseDAO.listar());
        classesTable.setItems(listaClases);
        Clase sel = classesTable.getSelectionModel().getSelectedItem();
        if (sel != null) cargarHorariosDeClase(sel.getIdClase());
    }

    // -------- Botones superiores/derecha --------
    @FXML private void onNew() {
        limpiarFormulario();
        lblStatus.setText("Creando nueva clase…");
    }

    @FXML private void onEdit() {
        Clase seleccionada = classesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarAlerta("Seleccione una clase para editar"); return; }

        tfId.setText(String.valueOf(seleccionada.getIdClase()));
        tfNombre.setText(seleccionada.getNombre());
        if (spnCupo != null) spnCupo.getValueFactory().setValue(seleccionada.getCupoMaximo());
        tfIdUsuario.setText(seleccionada.getIdUsuario() != null ? String.valueOf(seleccionada.getIdUsuario()) : "");
        lblStatus.setText("Editando clase existente");

        cargarHorariosDeClase(seleccionada.getIdClase());
    }

    @FXML private void onDelete() {
        Clase seleccionada = classesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { mostrarAlerta("Seleccione una clase para eliminar"); return; }

        if (ClaseDAO.eliminar(seleccionada.getIdClase())) {
            listaClases.remove(seleccionada);
            horariosObs.clear();
            lblStatus.setText("Clase eliminada");
        } else {
            mostrarAlerta("No se pudo eliminar la clase");
        }
    }

    @FXML private void onSave() {
        try {
            String nombre = tfNombre.getText();
            if (nombre == null || nombre.isBlank()) { mostrarAlerta("Ingresá un nombre"); return; }
            int cupo = spnCupo.getValue();
            Integer idUsuario = tfIdUsuario.getText().isEmpty() ? null : Integer.parseInt(tfIdUsuario.getText());

            if (tfId.getText().isEmpty()) {
                // NUEVO
                Clase nueva = new Clase(0, nombre, cupo, idUsuario);
                int idClase = ClaseDAO.insertarYDevolverId(nueva);
                if (idClase <= 0) { mostrarAlerta("No se pudo guardar la clase"); return; }
                // por defecto crea un único turno por cada día marcado con el horario general
            } else {
                // EDITAR
                int id = Integer.parseInt(tfId.getText());
                Clase editada = new Clase(id, nombre, cupo, idUsuario);
                if (!ClaseDAO.actualizar(editada)) { mostrarAlerta("No se pudo actualizar la clase"); return; }
                // sincronizar turnos “base”
                HorarioClaseDAO.eliminarPorClase(id);
            }

            cargarDatos();
            lblStatus.setText("Clase y turnos guardados");
            limpiarFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error guardando: " + (e.getMessage()!=null ? e.getMessage() : "ver consola"));
        }
    }

    @FXML private void onCancel() {
        limpiarFormulario();
        lblStatus.setText("Edición cancelada");
        horariosObs.clear();
    }

    // para el botón "Limpiar" del FXML
    @FXML private void onClear() { limpiarFormulario(); }

    private void limpiarFormulario() {
        tfId.clear();
        tfNombre.clear();
        if (spnCupo != null) spnCupo.getValueFactory().setValue(20);
        tfIdUsuario.clear();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    // ------- turnos -------
    private void cargarHorariosDeClase(int idClase) {
        horariosObs.setAll(HorarioClaseDAO.listarPorClase(idClase));
    }

    @FXML
    private void onAgregarTurno() {
        Clase claseSel = classesTable.getSelectionModel().getSelectedItem();
        if (claseSel == null) { mostrarAlerta("Seleccioná una clase primero."); return; }

        if (cbDia == null || tfHoraInicioH == null || tfHoraFinH == null) {
            mostrarAlerta("Faltan vincular los campos de turnos (cbDia / tfHoraInicioH / tfHoraFinH).");
            return;
        }

        String dia = cbDia.getValue();
        String hi  = tfHoraInicioH.getText(); // HH:mm
        String hf  = tfHoraFinH.getText();    // HH:mm

        if (dia == null || dia.isBlank() || hi == null || hf == null || hi.isBlank() || hf.isBlank()) {
            mostrarAlerta("Completá día y horas (HH:mm).");
            return;
        }
        if (!hi.matches("\\d{2}:\\d{2}") || !hf.matches("\\d{2}:\\d{2}")) {
            mostrarAlerta("Formato de hora inválido. Usá HH:mm (ej: 09:00).");
            return;
        }

        HorarioClase h = new HorarioClase();
        h.setIdClase(claseSel.getIdClase());
        h.setDia(dia);
        h.setHoraInicio(hi);
        h.setHoraFin(hf);

        if (HorarioClaseDAO.insertar(h)) {
            cargarHorariosDeClase(claseSel.getIdClase());
            tfHoraInicioH.clear();
            tfHoraFinH.clear();
            lblStatus.setText("Turno agregado");
        } else {
            mostrarAlerta("No se pudo insertar el turno.");
        }
    }

    @FXML
    private void onEliminarTurno() {
        HorarioClase sel = (horariosTable != null) ? horariosTable.getSelectionModel().getSelectedItem() : null;
        if (sel == null) { mostrarAlerta("Seleccioná un turno."); return; }

        if (HorarioClaseDAO.eliminar(sel.getId())) {
            Clase c = classesTable.getSelectionModel().getSelectedItem();
            if (c != null) cargarHorariosDeClase(c.getIdClase());
            lblStatus.setText("Turno eliminado");
        } else {
            mostrarAlerta("No se pudo eliminar el turno.");
        }
    }

    @FXML
    private void abrirDialogoNuevoHorario() {
        Clase claseSel = classesTable.getSelectionModel().getSelectedItem();
        if (claseSel == null) { mostrarAlerta("Seleccioná una clase primero."); return; }

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Nuevo turno");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<String> dia = new ComboBox<>(FXCollections.observableArrayList(
                "Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"
        ));
        TextField hi = new TextField(); hi.setPromptText("HH:mm");
        TextField hf = new TextField(); hf.setPromptText("HH:mm");

        GridPane gp = new GridPane();
        gp.setHgap(8); gp.setVgap(8);
        gp.addRow(0, new Label("Día:"), dia);
        gp.addRow(1, new Label("Inicio:"), hi);
        gp.addRow(2, new Label("Fin:"), hf);
        dlg.getDialogPane().setContent(gp);

        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                String d = dia.getValue();
                String t1 = hi.getText();
                String t2 = hf.getText();
                if (d == null || d.isBlank() || t1 == null || t2 == null ||
                        !t1.matches("\\d{2}:\\d{2}") || !t2.matches("\\d{2}:\\d{2}")) {
                    mostrarAlerta("Completá día y horas (HH:mm).");
                    return null;
                }
                HorarioClase h = new HorarioClase();
                h.setIdClase(claseSel.getIdClase());
                h.setDia(d);
                h.setHoraInicio(t1);
                h.setHoraFin(t2);
                if (HorarioClaseDAO.insertar(h)) {
                    cargarHorariosDeClase(claseSel.getIdClase());
                    lblStatus.setText("Turno agregado");
                } else {
                    mostrarAlerta("No se pudo insertar el turno.");
                }
            }
            return null;
        });

        dlg.showAndWait();
    }
}
