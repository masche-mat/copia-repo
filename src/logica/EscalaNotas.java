package logica;

public final class EscalaNotas {

    private static double minima = 0;
    private static double maxima = 100;
    private static double aprobacion = 60;

    private EscalaNotas() {
    }

    public static double getMinima() { return minima; }
    public static double getMaxima() { return maxima; }
    public static double getNotaAprobacion() { return aprobacion; }

    static void configurar(double nuevaMinima, double nuevaMaxima,
                           double nuevaAprobacion) {
        if (!Double.isFinite(nuevaMinima)
                || !Double.isFinite(nuevaMaxima)
                || !Double.isFinite(nuevaAprobacion)
                || nuevaMinima >= nuevaMaxima
                || nuevaAprobacion < nuevaMinima
                || nuevaAprobacion > nuevaMaxima) {
            throw new IllegalArgumentException(
                    "La escala de notas no es válida");
        }

        for (Calificacion calificacion : Calificaciones.listar()) {
            double nota = calificacion.getNota();
            if (nota < nuevaMinima || nota > nuevaMaxima) {
                throw new IllegalArgumentException(
                        "Hay calificaciones fuera de la nueva escala");
            }
        }

        minima = nuevaMinima;
        maxima = nuevaMaxima;
        aprobacion = nuevaAprobacion;
    }

    static void validar(double nota) {
        if (!Double.isFinite(nota)
                || nota < minima || nota > maxima) {
            throw new IllegalArgumentException(
                    "La nota debe estar entre "
                    + minima + " y " + maxima);
        }
    }
}