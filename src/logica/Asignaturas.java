package logica;

import java.util.ArrayList;

public final class Asignaturas {

    private static final ArrayList<Asignatura> LISTA =
            new ArrayList<>();

    private Asignaturas() {
    }

    public static ArrayList<Asignatura> listar() {
        return new ArrayList<>(LISTA);
    }

    public static Asignatura buscarPorId(int id) {
        for (Asignatura a : LISTA) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    static Asignatura registrar(int id, String nombre,
                                int creditos, int cupo,
                                EstadoAsignatura estado) {
        Asignatura nueva =
                new Asignatura(id, nombre, creditos, cupo, estado);

        if (buscarPorId(id) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una asignatura con ese id");
        }

        LISTA.add(nueva);
        return nueva;
    }

    static boolean modificar(int id, String nombre,
                              int creditos, int cupo,
                              EstadoAsignatura estado) {
        Asignatura a = buscarPorId(id);
        if (a == null) return false;

        a.actualizar(nombre, creditos, cupo, estado);
        return true;
    }

    static boolean eliminar(int id) {
        Asignatura a = buscarPorId(id);
        if (a == null) return false;

        for (Curso curso : Cursos.listar()) {
            if (curso.getAsignatura() == a) {
                throw new IllegalStateException(
                        "La asignatura todavía tiene cursos");
            }
        }

        return LISTA.remove(a);
    }
}