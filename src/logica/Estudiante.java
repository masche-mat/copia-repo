package logica;

import java.util.ArrayList;

public class Estudiante extends Persona {

    private final int id;

    Estudiante(int id, String nombre, String apellido, String ci) {
        super(nombre, apellido, ci);
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        this.id = id;
    }

    public int getId() { return id; }

    public ArrayList<Inscripcion> getInscripciones() {
        return Inscripciones.deEstudiante(id);
    }

    public ArrayList<Calificacion> getHistorialAcademico() {
        return Calificaciones.deEstudiante(id);
    }

    @Override
    public String toString() {
        return id + " - " + super.toString();
    }
}