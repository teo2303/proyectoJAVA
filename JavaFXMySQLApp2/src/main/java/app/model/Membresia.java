package app.model;

public class Membresia {
    private int idMembresia;
    private String nombre;
    private int duracionDias;
    private double precio;

    // --- Constructores ---
    public Membresia() {
    }

    public Membresia(int idMembresia, String nombre, int duracionDias, double precio) {
        this.idMembresia = idMembresia;
        this.nombre = nombre;
        this.duracionDias = duracionDias;
        this.precio = precio;
    }

    public Membresia(String nombre, int duracionDias, double precio) {
        this.nombre = nombre;
        this.duracionDias = duracionDias;
        this.precio = precio;
    }

    // --- Getters & Setters ---
    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


    @Override
    public String toString() {
        return "Membresia{" +
                "idMembresia=" + idMembresia +
                ", nombre='" + nombre + '\'' +
                ", duracionDias=" + duracionDias +
                ", precio=" + precio +
                '}';
    }
}