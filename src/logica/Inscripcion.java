package logica;

public class Inscripcion {

    private final int id;
    private final Estudiante estudiante;
    private Curso curso;

    Inscripcion(int id, Estudiante estudiante, Curso curso) {
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
    }

    public int getId() { return id; }
    public Estudiante getEstudiante() { return estudiante; }
    public Curso getCurso() { return curso; }

    public Asignatura getAsignatura() {
        return curso.getAsignatura();
    }

    void cambiarCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return id + " - " + estudiante.getNombre()
                + " " + estudiante.getApellido()
                + " / " + getAsignatura().getNombre()
                + " / " + curso.getNombre();
    }
}