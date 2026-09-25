package logica;

public final class Autenticacion {
    private static Usuario usuarioActual;

    private Autenticacion() {
    }

    public static Usuario iniciarSesion(String usuario, String contrasena) {
        usuarioActual = null;
        if (usuario == null || contrasena == null) return null;

        String ingresado = Usuario.normalizarUsuario(usuario);
        Administrador administrador = Administrador.getInstancia();
        if (administrador.getUsuario().equalsIgnoreCase(ingresado)
                && administrador.verificarContrasena(contrasena)) {
            usuarioActual = administrador;
        } else {
            Docente docente = Docentes.buscarPorUsuario(ingresado);
            if (docente != null && docente.verificarContrasena(contrasena)) {
                usuarioActual = docente;
            }
        }
        return usuarioActual;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static Administrador exigirAdministrador() {
        if (!(usuarioActual instanceof Administrador)) {
            throw new IllegalStateException("Iniciá sesión como administrador.");
        }
        return (Administrador) usuarioActual;
    }

    public static Docente exigirDocente() {
        if (!(usuarioActual instanceof Docente)) {
            throw new IllegalStateException("Iniciá sesión como docente.");
        }
        Docente docente = (Docente) usuarioActual;
        if (Docentes.buscarPorId(docente.getId()) != docente) {
            cerrarSesion();
            throw new IllegalStateException("La cuenta docente ya no está registrada.");
        }
        return docente;
    }
}
