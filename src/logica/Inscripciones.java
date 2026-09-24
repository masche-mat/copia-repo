package logica;

import java.util.ArrayList;

public final class Inscripciones {

    private static final ArrayList<Inscripcion> LISTA =
            new ArrayList<>();
    private static int siguienteId = 1;

    private Inscripciones() {
    }

    public static ArrayList<Inscripcion> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Inscripcion buscarPorId(int id) {
        for (Inscripcion i : LISTA) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    public static ArrayList<Inscripcion> deEstudiante(
            int idEstudiante) {
        ArrayList<Inscripcion> resultado = new ArrayList<>();

        for (Inscripcion i : LISTA) {
            if (i.getEstudiante().getId() == idEstudiante) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public static ArrayList<Inscripcion> deAsignatura(
            int idAsignatura) {
        ArrayList<Inscripcion> resultado = new ArrayList<>();

        for (Inscripcion i : LISTA) {
            if (i.getAsignatura().getId() == idAsignatura) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public static int cantidadEnAsignatura(int idAsignatura) {
        return deAsignatura(idAsignatura).size();
    }

    static Inscripcion registrar(
            int idEstudiante, int idCurso) {
        Estudiante estudiante =
                Estudiantes.buscarPorId(idEstudiante);
        Curso curso = Cursos.buscarPorId(idCurso);

        if (estudiante == null || curso == null) {
            throw new IllegalArgumentException(
                    "El estudiante o el curso no existe");
        }

        Asignatura asignatura = curso.getAsignatura();

        if (asignatura.getEstado() != EstadoAsignatura.ACTIVA) {
            throw new IllegalStateException(
                    "Solo se permite inscribir en asignaturas activas");
        }

        for (Inscripcion i : LISTA) {
            if (i.getEstudiante() == estudiante
                    && i.getAsignatura() == asignatura) {
                throw new IllegalArgumentException(
                        "El estudiante ya está inscripto en esa asignatura");
            }
        }

        if (cantidadEnAsignatura(asignatura.getId())
                >= asignatura.getCupoMaximo()) {
            throw new IllegalStateException(
                    "La asignatura alcanzó su cupo máximo");
        }

        Inscripcion nueva =
                new Inscripcion(siguienteId++, estudiante, curso);
        LISTA.add(nueva);
        return nueva;
    }

    static boolean cambiarCurso(int idInscripcion,
                                int idNuevoCurso) {
        Inscripcion i = buscarPorId(idInscripcion);
        Curso nuevo = Cursos.buscarPorId(idNuevoCurso);

        if (i == null || nuevo == null
                || i.getCurso() == nuevo) {
            return false;
        }
        if (i.getAsignatura() != nuevo.getAsignatura()) {
            throw new IllegalArgumentException(
                    "El nuevo curso debe ser de la misma asignatura");
        }
        if (Calificaciones.buscarPorInscripcion(idInscripcion)
                != null) {
            throw new IllegalStateException(
                    "No se cambia de curso una inscripción ya calificada");
        }

        i.cambiarCurso(nuevo);
        return true;
    }

    static boolean eliminar(int idInscripcion) {
        Inscripcion i = buscarPorId(idInscripcion);
        if (i == null) return false;

        if (Calificaciones.buscarPorInscripcion(idInscripcion)
                != null) {
            throw new IllegalStateException(
                    "No se elimina una inscripción con calificación");
        }

        return LISTA.remove(i);
    }
}