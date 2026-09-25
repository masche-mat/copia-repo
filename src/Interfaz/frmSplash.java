package Interfaz;

/** Primera ventana del programa. Su diseño está en frmSplash.form. */
public class frmSplash extends javax.swing.JFrame implements Runnable {
    private javax.swing.Timer reloj;

    public frmSplash() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        if (!javax.swing.SwingUtilities.isEventDispatchThread()) {
            javax.swing.SwingUtilities.invokeLater(this);
            return;
        }
        if (reloj != null) return;
        setVisible(true);
        reloj = new javax.swing.Timer(2000, evt -> {
            new frmLogin().setVisible(true);
            dispose();
        });
        reloj.setRepeats(false);
        reloj.start();
    }

    @Override
    public void dispose() {
        if (reloj != null) reloj.stop();
        super.dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info
                        : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ReflectiveOperationException
                    | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(frmSplash.class.getName())
                        .log(java.util.logging.Level.FINE, "Se usa el aspecto predeterminado", ex);
            }
            new frmSplash().run();
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDetalle = new javax.swing.JLabel();
        lblAutores = new javax.swing.JLabel();
        setDefaultCloseOperation(3);
        setTitle("SGA");
        setUndecorated(true);
        lblTitulo.setText("Sistema de Gestión Académica");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel2.setText("Iniciando el sistema...");
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblDetalle.setText("Taller Integrador de Sistemas");
        lblDetalle.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblAutores.setText("Autores: Mathias Mascheroni y Damián Olivera");
        lblAutores.setFont(new java.awt.Font("Segoe UI", 0, 14));
        {
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36));
            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(42)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14)
                .addComponent(lblDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14)
                .addComponent(lblAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42));
        }
        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblDetalle;
    private javax.swing.JLabel lblAutores;
    // End of variables declaration//GEN-END:variables
}
