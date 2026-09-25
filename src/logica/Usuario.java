package logica;

public abstract class Usuario extends Persona {

    private String usuario;
    private String contrasena;

    protected Usuario(String nombre, String apellido, String ci,
                      String usuario, String contrasena) {
        super(nombre, apellido, ci);
        cambiarUsuario(usuario);
        cambiarContrasena(contrasena);
    }

    public String getUsuario() { return usuario; }

    public static String normalizarUsuario(String valor) {
        return valor == null ? "" : valor.trim().replaceAll("\\s+", " ");
    }

    public static void validarContrasena(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
    }

    boolean verificarContrasena(String ingresada) {
        return ingresada != null && contrasena.equals(ingresada);
    }

    void cambiarUsuario(String usuario) {
        this.usuario = normalizarUsuario(validarTexto(usuario, "El usuario"));
    }

    void cambiarContrasena(String contrasena) {
        validarContrasena(contrasena);
        this.contrasena = contrasena;
    }
}
