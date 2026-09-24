package logica;

import java.util.ArrayList;

public final class Calificaciones {

    private static final ArrayList<Calificacion> LISTA =
            new ArrayList<>();
    private static int siguienteId = 1;

    private Calificaciones() {
    }

    public static ArrayList<Calificacion> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Calificacion buscarPorInscripcion(
            int idInscripcion) {
        for (Calificacion c : LISTA) {
            if (c.getInscripcion().getId() == idInscripcion) {
                return c;
            }
        }
        return null;
    }

    public static ArrayList<Calificacion> deEstudiante(
            int idEstudiante) {
        ArrayList<Calificacion> resultado =
                new ArrayList<>();

        for (Calificacion c : LISTA) {
            if (c.getInscripcion()
                    .getEstudiante().getId() == idEstudiante) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public static ArrayList<Calificacion> deAsignatura(
            int idAsignatura) {
        ArrayList<Calificacion> resultado =
                new ArrayList<>();

        for (Calificacion c : LISTA) {
            if (c.getInscripcion()
                    .getAsignatura().getId() == idAsignatura) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    static Calificacion registrar(
            Docente docente, int idInscripcion, double nota) {
        Inscripcion i = verificarPermiso(docente, idInscripcion);

        if (buscarPorInscripcion(idInscripcion) != null) {
            throw new IllegalArgumentException(
                    "La inscripción ya tiene una calificación");
        }

        EscalaNotas.validar(nota);
        Calificacion nueva =
                new Calificacion(siguienteId++, i, nota);
        LISTA.add(nueva);
        return nueva;
    }

    static boolean modificar(
            Docente docente, int idInscripcion, double nota) {
        verificarPermiso(docente, idInscripcion);
        Calificacion c = buscarPorInscripcion(idInscripcion);

        if (c == null) return false;

        c.cambiarNota(nota);
        return true;
    }

    static boolean eliminar(
            Docente docente, int idInscripcion) {
        verificarPermiso(docente, idInscripcion);
        Calificacion c = buscarPorInscripcion(idInscripcion);

        return c != null && LISTA.remove(c);
    }

    private static Inscripcion verificarPermiso(
            Docente docente, int idInscripcion) {
        Inscripcion i =
                Inscripciones.buscarPorId(idInscripcion);

        if (docente == null || i == null
                || Docentes.buscarPorId(docente.getId()) != docente
                || i.getCurso().getDocente() != docente) {
            throw new IllegalArgumentException(
                    "El docente no puede calificar esa inscripción");
        }

        return i;
    }
}