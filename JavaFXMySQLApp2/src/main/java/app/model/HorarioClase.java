package app.model;

public class HorarioClase {
    private int id;
    private int idClase;
    private String dia;        // "Lunes".."Domingo"
    private String horaInicio; // "HH:mm"
    private String horaFin;    // "HH:mm"

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
}
