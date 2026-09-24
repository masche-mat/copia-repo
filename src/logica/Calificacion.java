package logica;

import java.time.LocalDate;

public class Calificacion {

    private final int id;
    private final Inscripcion inscripcion;
    private double nota;
    private LocalDate fecha;

    Calificacion(int id, Inscripcion inscripcion, double nota) {
        this.id = id;
        this.inscripcion = inscripcion;
        cambiarNota(nota);
    }

    public int getId() { return id; }
    public Inscripcion getInscripcion() { return inscripcion; }
    public double getNota() { return nota; }
    public LocalDate getFecha() { return fecha; }

    public boolean estaAprobada() {
        return nota >= EscalaNotas.getNotaAprobacion();
    }

    void cambiarNota(double nota) {
        EscalaNotas.validar(nota);
        this.nota = nota;
        this.fecha = LocalDate.now();
    }

    @Override
    public String toString() {
        return inscripcion.toString() + " | nota: " + nota;
    }
}