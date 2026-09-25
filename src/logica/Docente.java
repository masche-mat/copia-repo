package logica;

import java.util.ArrayList;

public class Docente extends Usuario {

    private final int id;

    Docente(int id, String nombre, String apellido, String ci,
            String contrasena) {
        super(nombre, apellido, ci, usuarioPara(nombre, apellido), contrasena);
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        this.id = id;
    }

    // El acceso siempre sigue al nombre y apellido actuales.
    @Override
    public String getUsuario() {
        return usuarioPara(getNombre(), getApellido());
    }

    static String usuarioPara(String nombre, String apellido) {
        return Usuario.normalizarUsuario(
                validarTexto(nombre, "El nombre") + " "
                + validarTexto(apellido, "El apellido"));
    }

    public int getId() { return id; }

    public ArrayList<Curso> getCursos() {
        ArrayList<Curso> resultado = new ArrayList<>();
        for (Curso curso : Cursos.listar()) {
            if (curso.getDocente() == this) {
                resultado.add(curso);
            }
        }
        return resultado;
    }

    public ArrayList<Inscripcion> getInscripcionesDeMisCursos() {
        ArrayList<Inscripcion> resultado = new ArrayList<>();
        for (Inscripcion inscripcion : Inscripciones.listar()) {
            if (inscripcion.getCurso().getDocente() == this) {
                resultado.add(inscripcion);
            }
        }
        return resultado;
    }

    public Calificacion registrarCalificacion(
            int idInscripcion, double nota) {
        return Calificaciones.registrar(this, idInscripcion, nota);
    }

    public boolean modificarCalificacion(
            int idInscripcion, double nota) {
        return Calificaciones.modificar(this, idInscripcion, nota);
    }

    public boolean eliminarCalificacion(int idInscripcion) {
        return Calificaciones.eliminar(this, idInscripcion);
    }

    @Override
    public String toString() {
        return id + " - " + super.toString()
                + " | usuario: " + getUsuario();
    }
}
