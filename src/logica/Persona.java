package logica;

public abstract class Persona {

    private String nombre;
    private String apellido;
    private String ci;

    protected Persona(String nombre, String apellido, String ci) {
        actualizarDatos(nombre, apellido, ci);
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCi() { return ci; }

    void actualizarDatos(String nombre, String apellido, String ci) {
        String n = validarTexto(nombre, "El nombre");
        String a = validarTexto(apellido, "El apellido");
        String c = validarTexto(ci, "La cédula");

        this.nombre = n;
        this.apellido = a;
        this.ci = c;
    }

    protected static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + ci + ")";
    }
}