package app.model;

public class Cliente {
    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private Integer idMembresia; // puede ser null

    public Cliente() {}

    public Cliente(String nombre, String apellido, String dni, String telefono, String email, Integer idMembresia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.idMembresia = idMembresia;
    }

    // Getters & setters
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getIdMembresia() { return idMembresia; }
    public void setIdMembresia(Integer idMembresia) { this.idMembresia = idMembresia; }
}
