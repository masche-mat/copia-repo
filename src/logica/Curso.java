package logica;

public class Curso {

    private final int id;
    private String nombre;
    private final Asignatura asignatura;
    private Docente docente;

    Curso(int id, String nombre, Asignatura asignatura) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        if (asignatura == null) {
            throw new IllegalArgumentException(
                    "El curso debe tener una asignatura");
        }

        this.id = id;
        this.asignatura = asignatura;
        cambiarNombre(nombre);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Asignatura getAsignatura() { return asignatura; }
    public Docente getDocente() { return docente; }

    void cambiarNombre(String nombre) {
        this.nombre = Persona.validarTexto(
                nombre, "El nombre del curso");
    }

    void asignarDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public String toString() {
        return id + " - " + nombre
                + " | asignatura: " + asignatura.getNombre();
    }
}