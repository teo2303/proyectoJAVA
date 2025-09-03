package app.model;

public class Usuario {
    private String nombre;
    private String email;
    private String telefono;
    private String dni;

    public Usuario(String nombre, String email, String telefono, String dni) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El telefono no puede estar vacío.");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El dni no puede estar vacío.");
        }
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getDni() { return dni; }
}


