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

    boolean verificarContrasena(String ingresada) {
        return ingresada != null && contrasena.equals(ingresada);
    }

    void cambiarUsuario(String usuario) {
        this.usuario = validarTexto(usuario, "El usuario");
    }

    void cambiarContrasena(String contrasena) {
        this.contrasena = validarTexto(contrasena, "La contraseña");
    }
}