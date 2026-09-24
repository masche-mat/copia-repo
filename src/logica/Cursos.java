package logica;

import java.util.ArrayList;

public final class Cursos {

    private static final ArrayList<Curso> LISTA =
            new ArrayList<>();

    private Cursos() {
    }

    public static ArrayList<Curso> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Curso buscarPorId(int id) {
        for (Curso c : LISTA) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    static Curso registrar(int id, String nombre, int idAsignatura) {
        Asignatura asignatura =
                Asignaturas.buscarPorId(idAsignatura);

        if (asignatura == null) {
            throw new IllegalArgumentException(
                    "La asignatura no existe");
        }
        if (buscarPorId(id) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un curso con ese id");
        }

        Curso nuevo = new Curso(id, nombre, asignatura);
        LISTA.add(nuevo);
        return nuevo;
    }

    static boolean modificarNombre(int id, String nombre) {
        Curso c = buscarPorId(id);
        if (c == null) return false;

        c.cambiarNombre(nombre);
        return true;
    }

    static boolean asignarDocente(int idCurso, int idDocente) {
        Curso c = buscarPorId(idCurso);
        Docente d = Docentes.buscarPorId(idDocente);

        if (c == null || d == null || c.getDocente() == d) {
            return false;
        }

        c.asignarDocente(d);
        return true;
    }

    static boolean quitarDocente(int idCurso) {
        Curso c = buscarPorId(idCurso);
        if (c == null || c.getDocente() == null) return false;

        c.asignarDocente(null);
        return true;
    }

    static boolean eliminar(int id) {
        Curso c = buscarPorId(id);
        if (c == null) return false;

        for (Inscripcion i : Inscripciones.listar()) {
            if (i.getCurso() == c) {
                throw new IllegalStateException(
                        "El curso todavía tiene inscripciones");
            }
        }

        return LISTA.remove(c);
    }
}