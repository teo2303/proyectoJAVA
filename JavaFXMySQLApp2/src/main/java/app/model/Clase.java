package app.model;

public class Clase {
    private int idClase;
    private String nombre;
    private int cupoMaximo;
    private Integer idUsuario;

    public Clase(int idClase, String nombre, Integer cupoMaximo, Integer idUsuario) {
        this.idClase = idClase;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
        this.idUsuario = idUsuario;
    }



    // Getters y setters
    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}

