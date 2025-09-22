package app.model;

public class Usuario {
    private String nombre;
    private String email;
    private String dni;
    private String apellido;

    public Usuario(String nombre, String apellido, String email, String dni) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El dni no puede estar vacío.");
        }

        this.nombre = nombre;
        this.email = email;
        this.dni = dni;
        this.apellido = apellido;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getDni() { return dni; }
    public String getApellido() {return apellido; }
}


