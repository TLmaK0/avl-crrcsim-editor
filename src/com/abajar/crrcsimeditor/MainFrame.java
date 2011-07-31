/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 03-jul-2011, 22:45:18
 */

package com.abajar.crrcsimeditor;

/**
 *
 * @author hfreire
 */
public class MainFrame extends javax.swing.JFrame {

    private CRRCsimEditor controller;
    final GeometryEditor geoEditor;

    /** Creates new form MainFrame */
    public MainFrame(CRRCsimEditor controller) {
        this.controller = controller;
        this.geoEditor =  new GeometryEditor(this.controller);

        initComponents();
    }

    public void showGeoEditor(){
        geoEditor.setVisible(true);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topViewButton = new javax.swing.JButton();
        frontViewButton = new javax.swing.JButton();
        rightViewButton = new javax.swing.JButton();
        addSurfaceButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.abajar.crrcsimeditor.CRRCsimEditor.class).getContext().getResourceMap(MainFrame.class);
        topViewButton.setText(resourceMap.getString("topViewButton.text")); // NOI18N
        topViewButton.setName("topViewButton"); // NOI18N
        topViewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topViewButtonMouseClicked(evt);
            }
        });

        frontViewButton.setText(resourceMap.getString("frontViewButton.text")); // NOI18N
        frontViewButton.setName("frontViewButton"); // NOI18N
        frontViewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frontViewButtonMouseClicked(evt);
            }
        });

        rightViewButton.setText(resourceMap.getString("rightViewButton.text")); // NOI18N
        rightViewButton.setName("rightViewButton"); // NOI18N
        rightViewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rightViewButtonMouseClicked(evt);
            }
        });

        addSurfaceButton.setLabel(resourceMap.getString("addSurfaceButton.label")); // NOI18N
        addSurfaceButton.setName("addSurfaceButton"); // NOI18N
        addSurfaceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addSurfaceButtonMouseClicked(evt);
            }
        });

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(frontViewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(topViewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rightViewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(addSurfaceButton))
                .addContainerGap(309, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topViewButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frontViewButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightViewButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addSurfaceButton)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void topViewButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topViewButtonMouseClicked
        controller.topView();
    }//GEN-LAST:event_topViewButtonMouseClicked

    private void frontViewButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_frontViewButtonMouseClicked
        controller.frontView();
    }//GEN-LAST:event_frontViewButtonMouseClicked

    private void rightViewButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightViewButtonMouseClicked
        controller.rightView();
    }//GEN-LAST:event_rightViewButtonMouseClicked

    private void addSurfaceButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addSurfaceButtonMouseClicked
        controller.showAvlEditor();
    }//GEN-LAST:event_addSurfaceButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSurfaceButton;
    private javax.swing.JButton frontViewButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JButton rightViewButton;
    private javax.swing.JButton topViewButton;
    // End of variables declaration//GEN-END:variables

}
