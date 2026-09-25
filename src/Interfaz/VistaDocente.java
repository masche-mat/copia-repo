package Interfaz;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import logica.Autenticacion;
import logica.Calificacion;
import logica.Calificaciones;
import logica.Docente;
import logica.EscalaNotas;
import logica.Inscripcion;

/** El docente solo ve y califica las inscripciones de sus cursos. */
public class VistaDocente extends javax.swing.JFrame {
    private final Docente docente;

    public VistaDocente() {
        this(Autenticacion.exigirDocente());
    }

    public VistaDocente(Docente docente) {
        if (Autenticacion.exigirDocente() != docente) {
            throw new IllegalStateException("La cuenta no coincide con la sesión.");
        }
        this.docente = docente;
        initComponents();
        setLocationRelativeTo(null);
        tablaInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInscripciones.setAutoCreateRowSorter(true);
        tablaInscripciones.getSelectionModel().addListSelectionListener(evt -> actualizarBotones());
        mostrarInscripciones();
    }

    private void mostrarInscripciones() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Inscripción", "Estudiante", "Asignatura", "Curso", "Nota"}, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
            @Override public Class<?> getColumnClass(int columna) {
                return columna == 0 ? Integer.class : columna == 4 ? Double.class : String.class;
            }
        };
        for (Inscripcion i : docente.getInscripcionesDeMisCursos()) {
            Calificacion nota = Calificaciones.buscarPorInscripcion(i.getId());
            modelo.addRow(new Object[]{i.getId(),
                i.getEstudiante().getNombre() + " " + i.getEstudiante().getApellido(),
                i.getAsignatura().getNombre(), i.getCurso().getNombre(),
                nota == null ? null : nota.getNota()});
        }
        tablaInscripciones.setModel(modelo);
        lblDocente.setText("Docente: " + docente.getNombre() + " " + docente.getApellido());
        lblEstado.setText(modelo.getRowCount() + " inscripciones | Escala: "
                + EscalaNotas.getMinima() + " a " + EscalaNotas.getMaxima());
        actualizarBotones();
    }

    private void actualizarBotones() {
        int fila = tablaInscripciones.getSelectedRow();
        btnCalificar.setEnabled(fila >= 0);
        btnQuitarNota.setEnabled(fila >= 0
                && tablaInscripciones.getValueAt(fila, 4) != null);
    }

    private int inscripcionSeleccionada() {
        if (Autenticacion.exigirDocente() != docente) {
            throw new IllegalStateException("La sesión cambió. Volvé a iniciar sesión.");
        }
        int fila = tablaInscripciones.getSelectedRow();
        if (fila < 0) throw new IllegalStateException("Seleccioná un estudiante de la tabla.");
        return ((Number) tablaInscripciones.getValueAt(fila, 0)).intValue();
    }

    private void btnCalificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalificarActionPerformed
        try {
            int id = inscripcionSeleccionada();
            Calificacion anterior = Calificaciones.buscarPorInscripcion(id);
            String valor = JOptionPane.showInputDialog(this,
                    "Nota entre " + EscalaNotas.getMinima() + " y " + EscalaNotas.getMaxima(),
                    anterior == null ? "" : String.valueOf(anterior.getNota()));
            if (valor == null) return;
            double nota = Double.parseDouble(valor.trim().replace(',', '.'));
            if (anterior == null) docente.registrarCalificacion(id, nota);
            else docente.modificarCalificacion(id, nota);
            mostrarInscripciones();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo calificar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCalificarActionPerformed

    private void btnQuitarNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarNotaActionPerformed
        try {
            int id = inscripcionSeleccionada();
            if (JOptionPane.showConfirmDialog(this, "¿Quitar la calificación seleccionada?",
                    "Quitar nota", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                docente.eliminarCalificacion(id);
                mostrarInscripciones();
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitarNotaActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        Autenticacion.cerrarSesion();
        new frmLogin().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    public static void main(String[] args) {
        frmSplash.main(args);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblDocente = new javax.swing.JLabel();
        lblIndicacion = new javax.swing.JLabel();
        btnCalificar = new javax.swing.JButton();
        btnQuitarNota = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        scrollInscripciones = new javax.swing.JScrollPane();
        tablaInscripciones = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        setDefaultCloseOperation(3);
        setTitle("SGA - Docente");
        lblDocente.setText("Docente");
        lblDocente.setFont(new java.awt.Font("Segoe UI", 1, 22));
        lblIndicacion.setText("Seleccioná un estudiante de tus cursos para calificar.");
        lblIndicacion.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnCalificar.setText("Registrar / modificar nota");
        btnCalificar.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalificarActionPerformed(evt);
            }
        });
        btnQuitarNota.setText("Quitar nota");
        btnQuitarNota.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarNotaActionPerformed(evt);
            }
        });
        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        scrollInscripciones.setViewportView(tablaInscripciones);
        tablaInscripciones.setRowHeight(26);
        tablaInscripciones.setModel(new javax.swing.table.DefaultTableModel(
                new Object[0][5], new String[]{"Inscripción", "Estudiante", "Asignatura", "Curso", "Nota"}) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
        lblEstado.setText("Sin inscripciones");
        lblEstado.setFont(new java.awt.Font("Segoe UI", 0, 14));
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblIndicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(btnCalificar, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnQuitarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(scrollInscripciones, 0, 870, Short.MAX_VALUE)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20));
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addComponent(lblDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(lblIndicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnCalificar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnQuitarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12)
                .addComponent(scrollInscripciones, 0, 365, Short.MAX_VALUE)
                .addGap(12)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20));
        }
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblDocente;
    private javax.swing.JLabel lblIndicacion;
    private javax.swing.JButton btnCalificar;
    private javax.swing.JButton btnQuitarNota;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JScrollPane scrollInscripciones;
    private javax.swing.JTable tablaInscripciones;
    private javax.swing.JLabel lblEstado;
    // End of variables declaration//GEN-END:variables
}
