/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InterfazRegistroTrabajador extends javax.swing.JFrame {

    // Lista para almacenar los trabajadores registrados
    private List<String[]> listaTrabajadores;

    // Modelo de la tabla para actualizar los datos
    private DefaultTableModel modeloTabla;
    private final String archivoTrabajadores = "trabajadores.txt"; // Archivo donde se guardarán los trabajadores


    public InterfazRegistroTrabajador() {
        initComponents();
        
        listaTrabajadores = new ArrayList<>();
        modeloTabla = (DefaultTableModel) TablaTrabajador.getModel(); // Obtener el modelo de la tabla
           cargarTrabajadoresDesdeArchivo(); // Cargar datos al iniciar
        actualizarTablaTrabajadores();    // Actualizar la tabla con los datos cargados
    }
        public void actualizarTablaTrabajadores() {
        modeloTabla.setRowCount(0);

        for (String[] trabajador : listaTrabajadores) {
            modeloTabla.addRow(new Object[]{trabajador[0], trabajador[1]});
        }
    }
       // Método para guardar la lista de trabajadores en un archivo
    public void guardarTrabajadoresEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTrabajadores))) {
            for (String[] trabajador : listaTrabajadores) {
                writer.write(trabajador[0] + "," + trabajador[1]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar la lista de trabajadores desde un archivo
    public void cargarTrabajadoresDesdeArchivo() {
        File archivo = new File(archivoTrabajadores);
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(",");
                    listaTrabajadores.add(datos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextApellido = new javax.swing.JTextField();
        jGuardar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaTrabajador = new javax.swing.JTable();
        Editar = new javax.swing.JButton();
        jEliminar = new javax.swing.JButton();
        GuardarCambios = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(246, 171, 7));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel1.setText("Registro trabajador");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nombre");

        jTextNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextNombreActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Apellido");

        jGuardar.setText("Guardar");
        jGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(53, 53, 53)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jGuardar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TablaTrabajador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre", "Apellido"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TablaTrabajador);

        Editar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-editar-48.png"))); // NOI18N
        Editar.setText("Editar");
        Editar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Editar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarActionPerformed(evt);
            }
        });

        jEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-papelera-48.png"))); // NOI18N
        jEliminar.setText("Eliminar");
        jEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEliminarActionPerformed(evt);
            }
        });

        GuardarCambios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-guardar-48.png"))); // NOI18N
        GuardarCambios.setText("guardar");
        GuardarCambios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GuardarCambios.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        GuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarCambiosActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-atrás-50.png"))); // NOI18N
        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1)
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(GuardarCambios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Editar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(Editar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(GuardarCambios)
                        .addContainerGap(163, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(12, 12, 12))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextNombreActionPerformed

    private void jGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGuardarActionPerformed
       String nombre = jTextNombre.getText();
        String apellido = jTextApellido.getText();

        listaTrabajadores.add(new String[]{nombre, apellido});

        actualizarTablaTrabajadores();

        // Limpiar los campos de texto
        jTextNombre.setText("");
        jTextApellido.setText("");

        guardarTrabajadoresEnArchivo(); // Guardar los datos después de agregar un trabajador
    }//GEN-LAST:event_jGuardarActionPerformed

    private void EditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarActionPerformed
         // Obtener la fila seleccionada
    int filaSeleccionada = TablaTrabajador.getSelectedRow();

    if (filaSeleccionada >= 0) {
        // Obtener los valores de la fila seleccionada
        String nombre = (String) TablaTrabajador.getValueAt(filaSeleccionada, 0);
        String apellido = (String) TablaTrabajador.getValueAt(filaSeleccionada, 1);

        // Asignar los valores a los JTextField para permitir la edición
        jTextNombre.setText(nombre);
        jTextApellido.setText(apellido);

        // Eliminar temporalmente la fila de la tabla (esperando a que se guarden los cambios)
        listaTrabajadores.remove(filaSeleccionada);
        actualizarTablaTrabajadores();
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un trabajador para editar.");
    }
    }//GEN-LAST:event_EditarActionPerformed

    private void jEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEliminarActionPerformed
     // Obtener la fila seleccionada
    int filaSeleccionada = TablaTrabajador.getSelectedRow();

    if (filaSeleccionada >= 0) {
        // Confirmar antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este trabajador?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar el trabajador de la lista y actualizar la tabla
            listaTrabajadores.remove(filaSeleccionada);
            actualizarTablaTrabajadores();
            guardarTrabajadoresEnArchivo(); // Guardar los cambios en el archivo
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un trabajador para eliminar.");
    }
    }//GEN-LAST:event_jEliminarActionPerformed

    private void GuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarCambiosActionPerformed
         // Obtener los valores de los JTextField
    String nombre = jTextNombre.getText();
    String apellido = jTextApellido.getText();

    if (!nombre.isEmpty() && !apellido.isEmpty()) {
        // Agregar los cambios a la lista de trabajadores
        listaTrabajadores.add(new String[]{nombre, apellido});
        
        // Actualizar la tabla
        actualizarTablaTrabajadores();

        // Limpiar los JTextField
        jTextNombre.setText("");
        jTextApellido.setText("");

        // Guardar los cambios en el archivo
        guardarTrabajadoresEnArchivo();
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos antes de guardar.");
    }
    }//GEN-LAST:event_GuardarCambiosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      abrirMenuPrincipal();
    }//GEN-LAST:event_jButton1ActionPerformed
private void abrirMenuPrincipal() {
    // Crear el JFrame para el menú principal usando InterfazMenu
    InterfazMenu menuPrincipal = new InterfazMenu();
    menuPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizar en pantalla completa
    menuPrincipal.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    menuPrincipal.setVisible(true); // Hacer visible el JFrame del menú principal

    // Ocultar el JFrame del login
    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    if (loginFrame != null) {
         this.dispose(); 
    }
      this.dispose(); 

    }
    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Editar;
    private javax.swing.JButton GuardarCambios;
    private javax.swing.JTable TablaTrabajador;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jEliminar;
    private javax.swing.JButton jGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextApellido;
    private javax.swing.JTextField jTextNombre;
    // End of variables declaration//GEN-END:variables
}
