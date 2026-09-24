package logica;

public final class Promedios {

    private Promedios() {
    }

    public static Double simpleEstudiante(int idEstudiante) {
        double suma = 0;
        int cantidad = 0;

        for (Calificacion c :
                Calificaciones.deEstudiante(idEstudiante)) {
            suma += c.getNota();
            cantidad++;
        }

        return cantidad == 0 ? null : suma / cantidad;
    }

    public static Double ponderadoEstudiante(int idEstudiante) {
        double sumaPonderada = 0;
        int sumaCreditos = 0;

        for (Calificacion c :
                Calificaciones.deEstudiante(idEstudiante)) {
            int creditos = c.getInscripcion()
                    .getAsignatura().getCreditos();
            sumaPonderada += c.getNota() * creditos;
            sumaCreditos += creditos;
        }

        return sumaCreditos == 0
                ? null : sumaPonderada / sumaCreditos;
    }

    public static Double porAsignatura(int idAsignatura) {
        double suma = 0;
        int cantidad = 0;

        for (Calificacion c :
                Calificaciones.deAsignatura(idAsignatura)) {
            suma += c.getNota();
            cantidad++;
        }

        return cantidad == 0 ? null : suma / cantidad;
    }

    public static Double generalInstitucional() {
        double suma = 0;
        int cantidad = 0;

        for (Calificacion c : Calificaciones.listar()) {
            suma += c.getNota();
            cantidad++;
        }

        return cantidad == 0 ? null : suma / cantidad;
    }
}