package Interfaz;

import java.util.Arrays;
import logica.Administrador;
import logica.Autenticacion;
import logica.Docente;
import logica.Usuario;

public class frmLogin extends javax.swing.JFrame {
    public frmLogin() {
        initComponents();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(jButton1);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        char[] clave = jPasswordField1.getPassword();
        Usuario sesion;
        try {
            sesion = Autenticacion.iniciarSesion(txtusuario.getText(), new String(clave));
        } finally {
            Arrays.fill(clave, '\0');
            jPasswordField1.setText("");
        }
        if (sesion == null) {
            lblEstado.setText("Usuario o contraseña incorrectos.");
            jPasswordField1.requestFocusInWindow();
            return;
        }
        if (sesion instanceof Administrador) {
            new Vista().setVisible(true);
        } else if (sesion instanceof Docente) {
            new VistaDocente((Docente) sesion).setVisible(true);
        }
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Autenticacion.cerrarSesion();
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String[] args) {
        frmSplash.main(args);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtusuario = new javax.swing.JTextField();
        lblAyuda = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        setDefaultCloseOperation(3);
        setTitle("SGA - Iniciar sesión");
        setResizable(false);
        lblTitulo.setText("Sistema de Gestión Académica");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22));
        jLabel1.setText("Usuario");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtusuario.setColumns(25);
        lblAyuda.setText("Docentes: nombre y apellido. Administrador: admin.");
        lblAyuda.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel2.setText("Contraseña");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPasswordField1.setColumns(25);
        jButton1.setText("Ingresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Salir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        lblEstado.setText(" ");
        lblEstado.setFont(new java.awt.Font("Segoe UI", 0, 14));
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(txtusuario, 0, 450, Short.MAX_VALUE)
                .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPasswordField1, 0, 450, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(lblEstado, 0, 450, Short.MAX_VALUE))
                .addGap(28));
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(24)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addComponent(txtusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addComponent(lblAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20));
        }
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtusuario;
    private javax.swing.JLabel lblAyuda;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lblEstado;
    // End of variables declaration//GEN-END:variables
}
