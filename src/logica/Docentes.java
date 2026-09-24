package logica;

import java.util.ArrayList;

public final class Docentes {

    private static final ArrayList<Docente> LISTA =
            new ArrayList<>();

    private Docentes() {
    }

    public static ArrayList<Docente> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Docente buscarPorId(int id) {
        for (Docente d : LISTA) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    public static Docente buscarPorUsuario(String usuario) {
        if (usuario == null) return null;

        for (Docente d : LISTA) {
            if (d.getUsuario().equals(usuario.trim())) {
                return d;
            }
        }
        return null;
    }

    static boolean existeCi(String ci) {
        for (Docente d : LISTA) {
            if (d.getCi().equals(ci)) return true;
        }
        return false;
    }

    static boolean existeUsuario(String usuario) {
        return buscarPorUsuario(usuario) != null;
    }

    static Docente registrar(int id, String nombre,
                             String apellido, String ci,
                             String usuario, String contrasena) {
        Docente nuevo = new Docente(
                id, nombre, apellido, ci, usuario, contrasena);

        if (buscarPorId(id) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un docente con ese id");
        }
        if (existeCi(nuevo.getCi())
                || Estudiantes.existeCi(nuevo.getCi())
                || Administrador.getInstancia()
                        .getCi().equals(nuevo.getCi())) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con esa cédula");
        }
        if (existeUsuario(nuevo.getUsuario())
                || Administrador.getInstancia()
                        .getUsuario().equals(nuevo.getUsuario())) {
            throw new IllegalArgumentException(
                    "Ese usuario ya está registrado");
        }

        LISTA.add(nuevo);
        return nuevo;
    }

    static boolean modificar(int id, String nombre,
                              String apellido, String ci,
                              String usuario) {
        Docente d = buscarPorId(id);
        if (d == null) return false;

        String nuevaCi = Persona.validarTexto(ci, "La cédula");
        String nuevoUsuario =
                Persona.validarTexto(usuario, "El usuario");
        Docente otro = buscarPorUsuario(nuevoUsuario);

        if (!d.getCi().equals(nuevaCi)
                && (existeCi(nuevaCi)
                || Estudiantes.existeCi(nuevaCi)
                || Administrador.getInstancia()
                        .getCi().equals(nuevaCi))) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con esa cédula");
        }
        if ((otro != null && otro != d)
                || Administrador.getInstancia()
                        .getUsuario().equals(nuevoUsuario)) {
            throw new IllegalArgumentException(
                    "Ese usuario ya está registrado");
        }

        d.actualizarDatos(nombre, apellido, nuevaCi);
        d.cambiarUsuario(nuevoUsuario);
        return true;
    }

    static boolean cambiarContrasena(
            int id, String nuevaContrasena) {
        Docente d = buscarPorId(id);
        if (d == null) return false;

        d.cambiarContrasena(nuevaContrasena);
        return true;
    }

    static boolean eliminar(int id) {
        Docente d = buscarPorId(id);
        if (d == null) return false;

        if (!d.getCursos().isEmpty()) {
            throw new IllegalStateException(
                    "Primero debe reasignar los cursos del docente");
        }

        return LISTA.remove(d);
    }
}