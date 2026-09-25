package logica;

public final class Administrador extends Usuario {

    private static final Administrador INSTANCIA =
            new Administrador();

    private Administrador() {
        super("Administrador", "Sistema", "ADMIN",
                "admin", "admin1234");
    }

    public static Administrador getInstancia() {
        return INSTANCIA;
    }

    public void cambiarMiUsuario(String nuevoUsuario) {
        String validado =
                validarTexto(nuevoUsuario, "El usuario");

        if (Docentes.existeUsuario(validado)) {
            throw new IllegalArgumentException(
                    "Ese usuario pertenece a un docente");
        }
        cambiarUsuario(validado);
    }

    public void cambiarMiContrasena(
            String nuevaContrasena) {
        cambiarContrasena(nuevaContrasena);
    }

    public void configurarEscala(
            double minima, double maxima, double aprobacion) {
        EscalaNotas.configurar(
                minima, maxima, aprobacion);
    }

    public Estudiante crearEstudiante(
            int id, String nombre, String apellido, String ci) {
        return Estudiantes.registrar(
                id, nombre, apellido, ci);
    }

    public boolean modificarEstudiante(
            int id, String nombre, String apellido, String ci) {
        return Estudiantes.modificar(
                id, nombre, apellido, ci);
    }

    public boolean eliminarEstudiante(int id) {
        return Estudiantes.eliminar(id);
    }

    public Docente crearDocente(
            int id, String nombre, String apellido,
            String ci, String contrasena) {
        return Docentes.registrar(
                id, nombre, apellido, ci, contrasena);
    }

    public boolean modificarDocente(
            int id, String nombre, String apellido,
            String ci) {
        return Docentes.modificar(
                id, nombre, apellido, ci);
    }

    public boolean cambiarContrasenaDocente(
            int id, String contrasena) {
        return Docentes.cambiarContrasena(
                id, contrasena);
    }

    public boolean eliminarDocente(int id) {
        return Docentes.eliminar(id);
    }

    public Asignatura crearAsignatura(
            int id, String nombre, int creditos,
            int cupo, EstadoAsignatura estado) {
        return Asignaturas.registrar(
                id, nombre, creditos, cupo, estado);
    }

    public boolean modificarAsignatura(
            int id, String nombre, int creditos,
            int cupo, EstadoAsignatura estado) {
        return Asignaturas.modificar(
                id, nombre, creditos, cupo, estado);
    }

    public boolean eliminarAsignatura(int id) {
        return Asignaturas.eliminar(id);
    }

    public Curso crearCurso(
            int id, String nombre, int idAsignatura) {
        return Cursos.registrar(
                id, nombre, idAsignatura);
    }

    public boolean modificarCurso(
            int id, String nombre) {
        return Cursos.modificarNombre(id, nombre);
    }

    public boolean eliminarCurso(int id) {
        return Cursos.eliminar(id);
    }

    public boolean asignarDocenteACurso(
            int idCurso, int idDocente) {
        return Cursos.asignarDocente(
                idCurso, idDocente);
    }

    public boolean quitarDocenteDeCurso(int idCurso) {
        return Cursos.quitarDocente(idCurso);
    }

    public Inscripcion inscribirEstudiante(
            int idEstudiante, int idCurso) {
        return Inscripciones.registrar(
                idEstudiante, idCurso);
    }

    public boolean cambiarEstudianteDeCurso(
            int idInscripcion, int idNuevoCurso) {
        return Inscripciones.cambiarCurso(
                idInscripcion, idNuevoCurso);
    }

    public boolean eliminarInscripcion(int idInscripcion) {
        return Inscripciones.eliminar(idInscripcion);
    }

    @Override
    public String toString() {
        return "Administrador | usuario: " + getUsuario();
    }
}
