package main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class USMTextureManager extends javax.swing.JFrame {
    /**
     * Creates new form USMTextureManager
     */
    public USMTextureManager() {
        initComponents();
      ImageIcon img = new ImageIcon("mainIcon.png");
      this.setIconImage(img.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        addboardsLabel = new javax.swing.JLabel();
        addboardsToBmpButton = new javax.swing.JButton();
        addboardsToSprButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        consoleBox = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USM98 Textures Manager 1.0");

        addboardsLabel.setText("Adboards");

        addboardsToBmpButton.setText("Export to BMP");
        addboardsToBmpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addboardsToBmpButtonActionPerformed(evt);
            }
        });

        addboardsToSprButton.setText("Export to SPR");
        addboardsToSprButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addboardsToSprButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        consoleBox.setEditable(false);
        consoleBox.setColumns(20);
        consoleBox.setRows(5);
        jScrollPane2.setViewportView(consoleBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exitButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(addboardsToBmpButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addboardsToSprButton))
                        .addComponent(addboardsLabel)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addboardsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addboardsToBmpButton)
                    .addComponent(addboardsToSprButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(exitButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Click when converting addboards to BMP
    private void addboardsToBmpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addboardsToBmpButtonActionPerformed
        try {
            this.process("addboardsToBmp", "This conversion usually takes a few seconds.");
        } catch (InterruptedException ex) {
            Logger.getLogger(USMTextureManager.class.getName()).log(Level.SEVERE, null, ex);
            this.showErrorBox("Impossible to perform operation: " + ex.getMessage());
        }
    }//GEN-LAST:event_addboardsToBmpButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    // Click when converting addboards to SPR
    private void addboardsToSprButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addboardsToSprButtonActionPerformed
        try {
            this.process("addboardsToSpr", "This conversion usually takes around one minute.");
        } catch (InterruptedException ex) {
            Logger.getLogger(USMTextureManager.class.getName()).log(Level.SEVERE, null, ex);
            this.showErrorBox("Impossible to perform operation: " + ex.getMessage());
        }
    }//GEN-LAST:event_addboardsToSprButtonActionPerformed

    // Controlling the whole process in a thread
    void process(String action, String message) throws InterruptedException {
        this.log("Starting process...");
        this.log(message);
        this.enableUi(false);
        Kernel kernel = new Kernel(this);
        kernel.setAction(action);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Thread kernelThread = new Thread(() -> {
            kernel.start();
        });

        executor.schedule(kernelThread, 0, TimeUnit.MILLISECONDS);
    }

    public void log(String msg) {
        this.consoleBox.append(msg + "\n");
    }

    public void enableUi(Boolean status) {
        this.addboardsToBmpButton.setEnabled(status);
        this.addboardsToSprButton.setEnabled(status);
    }

    private void showErrorBox(String msg) {
        showMessageDialog(null, "Message", msg, ERROR_MESSAGE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(USMTextureManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(USMTextureManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(USMTextureManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(USMTextureManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new USMTextureManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addboardsLabel;
    private javax.swing.JButton addboardsToBmpButton;
    private javax.swing.JButton addboardsToSprButton;
    private javax.swing.JTextArea consoleBox;
    private javax.swing.JButton exitButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
