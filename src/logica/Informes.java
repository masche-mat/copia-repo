package logica;
import java.util.ArrayList;
public final class Informes {

    private Informes() {
    }

    public static ArrayList<Calificacion> aprobadas() {
        ArrayList<Calificacion> resultado = new ArrayList<>();

        for (Calificacion c : Calificaciones.listar()) {
            if (c.estaAprobada()) resultado.add(c);
        }
        return resultado;
    }

    public static ArrayList<Calificacion> reprobadas() {
        ArrayList<Calificacion> resultado = new ArrayList<>();

        for (Calificacion c : Calificaciones.listar()) {
            if (!c.estaAprobada()) resultado.add(c);
        }
        return resultado;
    }

    public static int cantidadEstudiantesPorAsignatura(
            int idAsignatura) {
        return Inscripciones.cantidadEnAsignatura(idAsignatura);
    }

    public static ArrayList<Estudiante> rankingPonderado() {
        ArrayList<Estudiante> resultado = new ArrayList<>();

        for (Estudiante e : Estudiantes.listar()) {
            if (Promedios.ponderadoEstudiante(e.getId())
                    != null) {
                resultado.add(e);
            }
        }

        resultado.sort((a, b) -> Double.compare(
                Promedios.ponderadoEstudiante(b.getId()),
                Promedios.ponderadoEstudiante(a.getId())
        ));

        return resultado;
    }

    public static ArrayList<Asignatura>
            asignaturasPorCantidadDeInscriptos() {
        ArrayList<Asignatura> resultado =
                Asignaturas.listar();

        resultado.sort((a, b) -> Integer.compare(
                Inscripciones.cantidadEnAsignatura(b.getId()),
                Inscripciones.cantidadEnAsignatura(a.getId())
        ));

        return resultado;
    }

    public static Estudiante mejorPromedioPonderado() {
        ArrayList<Estudiante> ranking =
                rankingPonderado();
        return ranking.isEmpty() ? null : ranking.get(0);
    }
}