package main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
        jScrollPane3 = new javax.swing.JScrollPane();
        PitchList = new javax.swing.JList<>();
        PitchLabel = new javax.swing.JLabel();
        pitchToBmpButton = new javax.swing.JButton();
        pitchToSprButton = new javax.swing.JButton();
        autoColorEnabledCheckbox = new javax.swing.JCheckBox();
        logEnabledCheckbox = new javax.swing.JCheckBox();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("USM98 Textures Manager 1.1");

        addboardsLabel.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
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

        PitchList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Normal left", "Normal right", "Snow left", "Snow right", "Winter left", "Winter right", "Wet left", "Wet right" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(PitchList);

        PitchLabel.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
        PitchLabel.setText("Pitch");

        pitchToBmpButton.setText("Export to BMP");
        pitchToBmpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pitchToBmpButtonActionPerformed(evt);
            }
        });

        pitchToSprButton.setText("Export to SPR");
        pitchToSprButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pitchToSprButtonActionPerformed(evt);
            }
        });

        autoColorEnabledCheckbox.setText("Enable auto-color");
        autoColorEnabledCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoColorEnabledCheckboxActionPerformed(evt);
            }
        });

        logEnabledCheckbox.setText("Enable logs");
        logEnabledCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logEnabledCheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addboardsLabel)
                            .addComponent(PitchLabel)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pitchToBmpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pitchToSprButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(addboardsToBmpButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addboardsToSprButton))))
                        .addGap(158, 158, 158)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(autoColorEnabledCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logEnabledCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitButton))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PitchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pitchToBmpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pitchToSprButton)))
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoColorEnabledCheckbox)
                    .addComponent(logEnabledCheckbox)
                    .addComponent(exitButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Click when converting addboards to BMP
    private void addboardsToBmpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addboardsToBmpButtonActionPerformed
        this.triggerActionToKernel("addboardsToBmp", "This conversion usually takes a few seconds.", 0);
    }//GEN-LAST:event_addboardsToBmpButtonActionPerformed

    // Exit the app
    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    // Click when converting addboards to SPR
    private void addboardsToSprButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addboardsToSprButtonActionPerformed
        this.triggerActionToKernel("addboardsToSpr", "This conversion usually takes around one minute.", 0);
    }//GEN-LAST:event_addboardsToSprButtonActionPerformed

    // Convert a pitch to BMP
    private void pitchToBmpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pitchToBmpButtonActionPerformed
        int selectedIndex = this.PitchList.getSelectedIndex();
        if (selectedIndex == -1) {
            this.showErrorBox("You must select a pitch to be converted!");
            return;
        }
        this.triggerActionToKernel("pitchToBmp", "This conversion usually takes a few seconds.", selectedIndex);
    }//GEN-LAST:event_pitchToBmpButtonActionPerformed

    // Convert a pitch to SPR
    private void pitchToSprButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pitchToSprButtonActionPerformed
        int selectedIndex = this.PitchList.getSelectedIndex();
        if (selectedIndex == -1) {
            this.showErrorBox("You must select a pitch to be converted!");
            return;
        }
        this.triggerActionToKernel("pitchToSpr", "This conversion usually takes a few seconds.", selectedIndex);
    }//GEN-LAST:event_pitchToSprButtonActionPerformed

    private void autoColorEnabledCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoColorEnabledCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_autoColorEnabledCheckboxActionPerformed

    private void logEnabledCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logEnabledCheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logEnabledCheckboxActionPerformed

    private void triggerActionToKernel(String action, String message,  int param1) {
        try {
            this.process(action, message, param1, this.logEnabledCheckbox.isSelected(), this.autoColorEnabledCheckbox.isSelected());
        } catch (InterruptedException ex) {
            this.showErrorBox("Impossible to perform operation: " + ex.getMessage());
        }
    }
    
    // Controlling the whole process in a thread
    void process(
            String action,
            String message,
            int param1,
            boolean logEnabled,
            boolean autocolorSelectionEnabled
    ) throws InterruptedException {
        this.consoleBox.setText("");
        this.log("Starting process...");
        this.log(message);
        this.enableUi(false);
        
        Kernel kernel = new Kernel(this, logEnabled, autocolorSelectionEnabled);
        kernel.setAction(action);
        kernel.setParam1(param1);
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
        this.pitchToBmpButton.setEnabled(status);
        this.pitchToSprButton.setEnabled(status);
    }

    private void showErrorBox(String msg) {
        showMessageDialog(this,  msg, "Message", ERROR_MESSAGE);
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
    private javax.swing.JLabel PitchLabel;
    private javax.swing.JList<String> PitchList;
    private javax.swing.JLabel addboardsLabel;
    private javax.swing.JButton addboardsToBmpButton;
    private javax.swing.JButton addboardsToSprButton;
    private javax.swing.JCheckBox autoColorEnabledCheckbox;
    private javax.swing.JTextArea consoleBox;
    private javax.swing.JButton exitButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JCheckBox logEnabledCheckbox;
    private javax.swing.JButton pitchToBmpButton;
    private javax.swing.JButton pitchToSprButton;
    // End of variables declaration//GEN-END:variables
}
