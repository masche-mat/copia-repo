package logica;

public final class Autenticacion {

    private Autenticacion() {
    }

    public static Usuario iniciarSesion(
            String usuario, String contrasena) {
        if (usuario == null || contrasena == null) {
            return null;
        }

        String ingresado = usuario.trim();
        Administrador administrador =
                Administrador.getInstancia();

        if (administrador.getUsuario().equals(ingresado)
                && administrador.verificarContrasena(
                        contrasena)) {
            return administrador;
        }

        Docente docente =
                Docentes.buscarPorUsuario(ingresado);

        if (docente != null
                && docente.verificarContrasena(contrasena)) {
            return docente;
        }

        return null;
    }
}