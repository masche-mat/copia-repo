package logica;

import java.util.ArrayList;

public final class Estudiantes {

    private static final ArrayList<Estudiante> LISTA =
            new ArrayList<>();

    private Estudiantes() {
    }

    public static ArrayList<Estudiante> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Estudiante buscarPorId(int id) {
        for (Estudiante e : LISTA) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    static boolean existeCi(String ci) {
        for (Estudiante e : LISTA) {
            if (e.getCi().equals(ci)) return true;
        }
        return false;
    }

    static Estudiante registrar(int id, String nombre,
                                String apellido, String ci) {
        Estudiante nuevo = new Estudiante(id, nombre, apellido, ci);

        if (buscarPorId(id) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante con ese id");
        }
        if (existeCi(nuevo.getCi())
                || Docentes.existeCi(nuevo.getCi())
                || Administrador.getInstancia()
                        .getCi().equals(nuevo.getCi())) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con esa cédula");
        }

        LISTA.add(nuevo);
        return nuevo;
    }

    static boolean modificar(int id, String nombre,
                              String apellido, String ci) {
        Estudiante e = buscarPorId(id);
        if (e == null) return false;

        String nuevaCi = Persona.validarTexto(ci, "La cédula");

        if (!e.getCi().equals(nuevaCi)
                && (existeCi(nuevaCi)
                || Docentes.existeCi(nuevaCi)
                || Administrador.getInstancia()
                        .getCi().equals(nuevaCi))) {
            throw new IllegalArgumentException(
                    "Ya existe una persona con esa cédula");
        }

        e.actualizarDatos(nombre, apellido, nuevaCi);
        return true;
    }

    static boolean eliminar(int id) {
        Estudiante e = buscarPorId(id);
        if (e == null) return false;

        if (!Inscripciones.deEstudiante(id).isEmpty()) {
            throw new IllegalStateException(
                    "Primero debe resolver las inscripciones del estudiante");
        }

        return LISTA.remove(e);
    }
}