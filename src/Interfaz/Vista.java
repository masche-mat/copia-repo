package Interfaz;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import logica.*;

/** Pantalla administrativa. Los controles visibles también están en Vista.form. */
public class Vista extends javax.swing.JFrame {
    private enum Seccion {
        DOCENTES("Docentes"), ESTUDIANTES("Estudiantes"), ASIGNATURAS("Asignaturas"),
        CURSOS("Cursos"), INSCRIPCIONES("Inscripciones");
        private final String titulo;
        Seccion(String titulo) { this.titulo = titulo; }
    }

    private final Administrador administrador;
    private Seccion seccionActual = Seccion.DOCENTES;

    public Vista() {
        administrador = Autenticacion.exigirAdministrador();
        initComponents();
        setLocationRelativeTo(null);
        tablaRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRegistros.setAutoCreateRowSorter(true);
        tablaRegistros.getSelectionModel().addListSelectionListener(evt -> actualizarBotones());
        cambiarSeccion(Seccion.DOCENTES);
    }

    private void cambiarSeccion(Seccion seccion) {
        Autenticacion.exigirAdministrador();
        seccionActual = seccion;
        lblTitulo.setText(seccion.titulo);
        btnDocentes.setEnabled(seccion != Seccion.DOCENTES);
        btnEstudiantes.setEnabled(seccion != Seccion.ESTUDIANTES);
        btnAsignaturas.setEnabled(seccion != Seccion.ASIGNATURAS);
        btnCursos.setEnabled(seccion != Seccion.CURSOS);
        btnInscripciones.setEnabled(seccion != Seccion.INSCRIPCIONES);
        lblAyuda.setText(seccion == Seccion.DOCENTES
                ? "El acceso se forma automáticamente con el nombre y apellido."
                : seccion == Seccion.INSCRIPCIONES
                ? "Cada estudiante se inscribe en un curso de una asignatura."
                : "Seleccioná una fila para modificarla o darla de baja.");
        actualizarTabla();
    }

    private void actualizarTabla() {
        String[] columnas;
        List<Object[]> filas = new ArrayList<>();
        switch (seccionActual) {
            case DOCENTES:
                columnas = new String[]{"ID", "Nombre", "Apellido", "Cédula", "Acceso"};
                for (Docente d : Docentes.listar()) filas.add(new Object[]{
                    d.getId(), d.getNombre(), d.getApellido(), d.getCi(), d.getUsuario()});
                break;
            case ESTUDIANTES:
                columnas = new String[]{"ID", "Nombre", "Apellido", "Cédula", "Inscripciones"};
                for (Estudiante e : Estudiantes.listar()) filas.add(new Object[]{
                    e.getId(), e.getNombre(), e.getApellido(), e.getCi(), e.getInscripciones().size()});
                break;
            case ASIGNATURAS:
                columnas = new String[]{"ID", "Nombre", "Créditos", "Cupo", "Estado", "Inscriptos"};
                for (Asignatura a : Asignaturas.listar()) filas.add(new Object[]{
                    a.getId(), a.getNombre(), a.getCreditos(), a.getCupoMaximo(), a.getEstado().toString(),
                    Inscripciones.cantidadEnAsignatura(a.getId())});
                break;
            case CURSOS:
                columnas = new String[]{"ID", "Curso / grupo", "Asignatura", "Docente"};
                for (Curso c : Cursos.listar()) filas.add(new Object[]{
                    c.getId(), c.getNombre(), c.getAsignatura().getNombre(),
                    c.getDocente() == null ? "Sin asignar" : c.getDocente().getUsuario()});
                break;
            default:
                columnas = new String[]{"ID", "Estudiante", "Asignatura", "Curso", "Nota"};
                for (Inscripcion i : Inscripciones.listar()) {
                    Calificacion nota = Calificaciones.buscarPorInscripcion(i.getId());
                    filas.add(new Object[]{i.getId(),
                        i.getEstudiante().getNombre() + " " + i.getEstudiante().getApellido(),
                        i.getAsignatura().getNombre(), i.getCurso().getNombre(),
                        nota == null ? null : nota.getNota()});
                }
        }
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
            @Override public Class<?> getColumnClass(int columna) {
                if (columna == 0) return Integer.class;
                for (int fila = 0; fila < getRowCount(); fila++) {
                    Object valor = getValueAt(fila, columna);
                    if (valor != null) return valor.getClass();
                }
                return Object.class;
            }
        };
        for (Object[] fila : filas) modelo.addRow(fila);
        tablaRegistros.setModel(modelo);
        lblEstado.setText(filas.size() + " registros. Los datos se pierden al cerrar el programa.");
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean seleccion = tablaRegistros.getSelectedRow() >= 0;
        jButton2.setEnabled(seleccion);
        jButton3.setEnabled(seleccion);
    }

    private int idSeleccionado() {
        int fila = tablaRegistros.getSelectedRow();
        if (fila < 0) throw new IllegalStateException("Seleccioná una fila de la tabla.");
        // JTable traduce la fila visible aunque se haya ordenado por otra columna.
        return ((Number) tablaRegistros.getValueAt(fila, 0)).intValue();
    }

    private int siguienteId() {
        int maximo = 0;
        for (int fila = 0; fila < tablaRegistros.getRowCount(); fila++) {
            maximo = Math.max(maximo, ((Number) tablaRegistros.getValueAt(fila, 0)).intValue());
        }
        if (maximo == Integer.MAX_VALUE) throw new IllegalStateException("No hay más ID disponibles.");
        return maximo + 1;
    }

    private JTextField campoId(boolean nuevo, int id) {
        JTextField campo = new JTextField(String.valueOf(nuevo ? siguienteId() : id));
        campo.setEditable(nuevo);
        return campo;
    }

    private int leerId(JTextField campo) {
        try {
            int id = Integer.parseInt(campo.getText().trim());
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El ID debe ser un número entero mayor que cero.");
        }
    }

    private JSpinner entero(int valor) {
        return new JSpinner(new SpinnerNumberModel(valor, 1, Integer.MAX_VALUE, 1));
    }

    private int valorEntero(JSpinner campo) {
        try {
            campo.commitEdit();
            return ((Number) campo.getValue()).intValue();
        } catch (java.text.ParseException ex) {
            throw new IllegalArgumentException("Ingresá un número entero válido.");
        }
    }

    private <T> JComboBox<T> opciones(List<T> valores, boolean permitirVacio) {
        JComboBox<T> combo = new JComboBox<>();
        if (permitirVacio) combo.addItem(null);
        for (T valor : valores) combo.addItem(valor);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> lista, Object valor, int indice, boolean seleccionado, boolean foco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor == null) setText("Sin docente");
                else if (valor instanceof Docente) {
                    Docente d = (Docente) valor;
                    setText(d.getId() + " - " + d.getNombre() + " " + d.getApellido());
                } else if (valor instanceof Estudiante) {
                    Estudiante e = (Estudiante) valor;
                    setText(e.getId() + " - " + e.getNombre() + " " + e.getApellido());
                } else if (valor instanceof Asignatura) {
                    Asignatura a = (Asignatura) valor;
                    setText(a.getId() + " - " + a.getNombre());
                } else if (valor instanceof Curso) {
                    Curso c = (Curso) valor;
                    setText(c.getId() + " - " + c.getNombre() + " (" + c.getAsignatura().getNombre() + ")");
                }
                return this;
            }
        });
        return combo;
    }

    // Estos diálogos solicitan datos; el diseño de la ventana principal permanece en .form.
    private void formulario(String titulo, String[] etiquetas, JComponent[] campos, Runnable guardar) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (int i = 0; i < campos.length; i++) {
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        while (JOptionPane.showConfirmDialog(this, panel, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                Autenticacion.exigirAdministrador();
                guardar.run();
                actualizarTabla();
                return;
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Revisá los datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarRegistro(boolean nuevo) {
        Autenticacion.exigirAdministrador();
        int id = nuevo ? 0 : idSeleccionado();
        switch (seccionActual) {
            case DOCENTES: editarDocente(nuevo, id); break;
            case ESTUDIANTES: editarEstudiante(nuevo, id); break;
            case ASIGNATURAS: editarAsignatura(nuevo, id); break;
            case CURSOS: editarCurso(nuevo, id); break;
            case INSCRIPCIONES: editarInscripcion(nuevo, id); break;
        }
    }

    private void editarDocente(boolean nuevo, int id) {
        Docente d = nuevo ? null : Docentes.buscarPorId(id);
        JTextField codigo = campoId(nuevo, id);
        JTextField nombre = new JTextField(nuevo ? "" : d.getNombre(), 20);
        JTextField apellido = new JTextField(nuevo ? "" : d.getApellido());
        JTextField ci = new JTextField(nuevo ? "" : d.getCi());
        JPasswordField clave = new JPasswordField();
        formulario(nuevo ? "Agregar docente" : "Modificar docente",
                new String[]{"ID", "Nombre", "Apellido", "Cédula",
                    nuevo ? "Contraseña" : "Nueva contraseña (vacía = conservar)"},
                new JComponent[]{codigo, nombre, apellido, ci, clave}, () -> {
                    String contrasena = new String(clave.getPassword());
                    if (nuevo || !contrasena.isEmpty()) Usuario.validarContrasena(contrasena);
                    if (nuevo) {
                        administrador.crearDocente(leerId(codigo), nombre.getText(),
                                apellido.getText(), ci.getText(), contrasena);
                    } else {
                        administrador.modificarDocente(id, nombre.getText(), apellido.getText(), ci.getText());
                        if (!contrasena.isEmpty()) administrador.cambiarContrasenaDocente(id, contrasena);
                    }
                });
        clave.setText("");
    }

    private void editarEstudiante(boolean nuevo, int id) {
        Estudiante e = nuevo ? null : Estudiantes.buscarPorId(id);
        JTextField codigo = campoId(nuevo, id);
        JTextField nombre = new JTextField(nuevo ? "" : e.getNombre(), 20);
        JTextField apellido = new JTextField(nuevo ? "" : e.getApellido());
        JTextField ci = new JTextField(nuevo ? "" : e.getCi());
        formulario(nuevo ? "Agregar estudiante" : "Modificar estudiante",
                new String[]{"ID", "Nombre", "Apellido", "Cédula"},
                new JComponent[]{codigo, nombre, apellido, ci}, () -> {
                    if (nuevo) administrador.crearEstudiante(leerId(codigo),
                            nombre.getText(), apellido.getText(), ci.getText());
                    else administrador.modificarEstudiante(id, nombre.getText(), apellido.getText(), ci.getText());
                });
    }

    private void editarAsignatura(boolean nuevo, int id) {
        Asignatura a = nuevo ? null : Asignaturas.buscarPorId(id);
        JTextField codigo = campoId(nuevo, id);
        JTextField nombre = new JTextField(nuevo ? "" : a.getNombre(), 20);
        JSpinner creditos = entero(nuevo ? 1 : a.getCreditos());
        JSpinner cupo = entero(nuevo ? 30 : a.getCupoMaximo());
        JComboBox<EstadoAsignatura> estado = new JComboBox<>(EstadoAsignatura.values());
        estado.setSelectedItem(nuevo ? EstadoAsignatura.ACTIVA : a.getEstado());
        formulario(nuevo ? "Agregar asignatura" : "Modificar asignatura",
                new String[]{"ID", "Nombre", "Créditos", "Cupo máximo", "Estado"},
                new JComponent[]{codigo, nombre, creditos, cupo, estado}, () -> {
                    int nCreditos = valorEntero(creditos);
                    int nCupo = valorEntero(cupo);
                    EstadoAsignatura nEstado = (EstadoAsignatura) estado.getSelectedItem();
                    if (nuevo) administrador.crearAsignatura(leerId(codigo), nombre.getText(), nCreditos, nCupo, nEstado);
                    else administrador.modificarAsignatura(id, nombre.getText(), nCreditos, nCupo, nEstado);
                });
    }

    private void editarCurso(boolean nuevo, int id) {
        if (Asignaturas.listar().isEmpty()) {
            throw new IllegalStateException("Primero agregá una asignatura.");
        }
        Curso c = nuevo ? null : Cursos.buscarPorId(id);
        JTextField codigo = campoId(nuevo, id);
        JTextField nombre = new JTextField(nuevo ? "" : c.getNombre(), 20);
        JComboBox<Asignatura> asignatura = opciones(Asignaturas.listar(), false);
        JComboBox<Docente> docente = opciones(Docentes.listar(), true);
        if (!nuevo) {
            asignatura.setSelectedItem(c.getAsignatura());
            asignatura.setEnabled(false);
            docente.setSelectedItem(c.getDocente());
        }
        formulario(nuevo ? "Agregar curso" : "Modificar curso",
                new String[]{"ID", "Nombre / grupo", "Asignatura", "Docente"},
                new JComponent[]{codigo, nombre, asignatura, docente}, () -> {
                    int codigoCurso = leerId(codigo);
                    Asignatura a = (Asignatura) asignatura.getSelectedItem();
                    Docente d = (Docente) docente.getSelectedItem();
                    if (nuevo) administrador.crearCurso(codigoCurso, nombre.getText(), a.getId());
                    else administrador.modificarCurso(codigoCurso, nombre.getText());
                    if (d == null) administrador.quitarDocenteDeCurso(codigoCurso);
                    else administrador.asignarDocenteACurso(codigoCurso, d.getId());
                });
    }

    private void editarInscripcion(boolean nuevo, int id) {
        if (Estudiantes.listar().isEmpty() || Cursos.listar().isEmpty()) {
            throw new IllegalStateException("Primero agregá estudiantes y cursos.");
        }
        Inscripcion i = nuevo ? null : Inscripciones.buscarPorId(id);
        JComboBox<Estudiante> estudiante = opciones(Estudiantes.listar(), false);
        List<Curso> disponibles = new ArrayList<>();
        for (Curso c : Cursos.listar()) {
            if (nuevo || c.getAsignatura() == i.getAsignatura()) disponibles.add(c);
        }
        JComboBox<Curso> curso = opciones(disponibles, false);
        if (!nuevo) {
            estudiante.setSelectedItem(i.getEstudiante());
            estudiante.setEnabled(false);
            curso.setSelectedItem(i.getCurso());
        }
        formulario(nuevo ? "Inscribir estudiante" : "Cambiar de curso",
                new String[]{"Estudiante", "Curso"}, new JComponent[]{estudiante, curso}, () -> {
                    Estudiante e = (Estudiante) estudiante.getSelectedItem();
                    Curso c = (Curso) curso.getSelectedItem();
                    if (nuevo) administrador.inscribirEstudiante(e.getId(), c.getId());
                    else if (c != i.getCurso()) administrador.cambiarEstudianteDeCurso(id, c.getId());
                });
    }

    private void darDeBaja() {
        Autenticacion.exigirAdministrador();
        int id = idSeleccionado();
        Object nombre = tablaRegistros.getValueAt(tablaRegistros.getSelectedRow(), 1);
        if (JOptionPane.showConfirmDialog(this,
                "¿Dar de baja el registro " + id + " (" + nombre + ")?", "Confirmar baja",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) return;
        switch (seccionActual) {
            case DOCENTES: administrador.eliminarDocente(id); break;
            case ESTUDIANTES: administrador.eliminarEstudiante(id); break;
            case ASIGNATURAS: administrador.eliminarAsignatura(id); break;
            case CURSOS: administrador.eliminarCurso(id); break;
            case INSCRIPCIONES: administrador.eliminarInscripcion(id); break;
        }
        actualizarTabla();
    }

    private void ejecutarAccion(Runnable accion) {
        try {
            accion.run();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo completar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ejecutarAccion(() -> editarRegistro(true));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ejecutarAccion(() -> editarRegistro(false));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ejecutarAccion(this::darDeBaja);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnDocentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocentesActionPerformed
        cambiarSeccion(Seccion.DOCENTES);
    }//GEN-LAST:event_btnDocentesActionPerformed
    private void btnEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstudiantesActionPerformed
        cambiarSeccion(Seccion.ESTUDIANTES);
    }//GEN-LAST:event_btnEstudiantesActionPerformed
    private void btnAsignaturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignaturasActionPerformed
        cambiarSeccion(Seccion.ASIGNATURAS);
    }//GEN-LAST:event_btnAsignaturasActionPerformed
    private void btnCursosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCursosActionPerformed
        cambiarSeccion(Seccion.CURSOS);
    }//GEN-LAST:event_btnCursosActionPerformed
    private void btnInscripcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInscripcionesActionPerformed
        cambiarSeccion(Seccion.INSCRIPCIONES);
    }//GEN-LAST:event_btnInscripcionesActionPerformed

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
        jPanel1 = new javax.swing.JPanel();
        lblAdministrador = new javax.swing.JLabel();
        btnDocentes = new javax.swing.JButton();
        btnEstudiantes = new javax.swing.JButton();
        btnAsignaturas = new javax.swing.JButton();
        btnCursos = new javax.swing.JButton();
        btnInscripciones = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblAyuda = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        setDefaultCloseOperation(3);
        setTitle("SGA - Administración");
        jPanel1.setBackground(new java.awt.Color(237, 242, 248));
        lblAdministrador.setText("Administrador");
        lblAdministrador.setFont(new java.awt.Font("Segoe UI", 1, 20));
        btnDocentes.setText("Docentes");
        btnDocentes.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocentesActionPerformed(evt);
            }
        });
        btnEstudiantes.setText("Estudiantes");
        btnEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudiantesActionPerformed(evt);
            }
        });
        btnAsignaturas.setText("Asignaturas");
        btnAsignaturas.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignaturasActionPerformed(evt);
            }
        });
        btnCursos.setText("Cursos");
        btnCursos.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCursosActionPerformed(evt);
            }
        });
        btnInscripciones.setText("Inscripciones");
        btnInscripciones.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInscripcionesActionPerformed(evt);
            }
        });
        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        lblTitulo.setText("Docentes");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22));
        lblAyuda.setText("Seleccioná una fila para modificarla o darla de baja.");
        lblAyuda.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton3.setText("Baja");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaRegistros);
        tablaRegistros.setRowHeight(26);
        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
                new Object[0][5], new String[]{"ID", "Nombre", "Apellido", "Cédula", "Acceso"}) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
        lblEstado.setText("0 registros");
        lblEstado.setFont(new java.awt.Font("Segoe UI", 0, 14));
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane3, 0, 740, Short.MAX_VALUE)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20));
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6)
                .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12)
                .addComponent(jScrollPane3, 0, 390, Short.MAX_VALUE)
                .addGap(10)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20));
        }
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblAdministrador, 0, 180, Short.MAX_VALUE)
                .addComponent(btnDocentes, 0, 180, Short.MAX_VALUE)
                .addComponent(btnEstudiantes, 0, 180, Short.MAX_VALUE)
                .addComponent(btnAsignaturas, 0, 180, Short.MAX_VALUE)
                .addComponent(btnCursos, 0, 180, Short.MAX_VALUE)
                .addComponent(btnInscripciones, 0, 180, Short.MAX_VALUE)
                .addComponent(btnCerrarSesion, 0, 180, Short.MAX_VALUE))
                .addGap(16));
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(20)
                .addComponent(lblAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28)
                .addComponent(btnDocentes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnAsignaturas, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8)
                .addComponent(btnInscripciones, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32)
                .addComponent(btnCerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20));
        }
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel2, 0, 780, Short.MAX_VALUE));
            layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, 0, 590, Short.MAX_VALUE)
                .addComponent(jPanel2, 0, 590, Short.MAX_VALUE));
        }
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignaturas;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnCursos;
    private javax.swing.JButton btnDocentes;
    private javax.swing.JButton btnEstudiantes;
    private javax.swing.JButton btnInscripciones;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAdministrador;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables
}
