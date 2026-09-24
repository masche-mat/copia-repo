package logica;

public class Asignatura {

    private final int id;
    private String nombre;
    private int creditos;
    private int cupoMaximo;
    private EstadoAsignatura estado;

    Asignatura(int id, String nombre, int creditos,
               int cupoMaximo, EstadoAsignatura estado) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        this.id = id;
        actualizar(nombre, creditos, cupoMaximo, estado);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCreditos() { return creditos; }
    public int getCupoMaximo() { return cupoMaximo; }
    public EstadoAsignatura getEstado() { return estado; }

    void actualizar(String nombre, int creditos,
                    int cupoMaximo, EstadoAsignatura estado) {
        String nuevoNombre =
                Persona.validarTexto(nombre, "El nombre de la asignatura");

        if (creditos <= 0) {
            throw new IllegalArgumentException(
                    "Los créditos deben ser mayores que cero");
        }
        if (cupoMaximo <= 0) {
            throw new IllegalArgumentException(
                    "El cupo debe ser mayor que cero");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (cupoMaximo < Inscripciones.cantidadEnAsignatura(id)) {
            throw new IllegalArgumentException(
                    "El cupo no puede ser menor que los inscriptos actuales");
        }

        this.nombre = nuevoNombre;
        this.creditos = creditos;
        this.cupoMaximo = cupoMaximo;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " | créditos: "
                + creditos + " | cupo: " + cupoMaximo
                + " | " + estado;
    }
}