/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaces;

import Reportes.ReporteObservacion;
import Reportes.ReporteTrabajador;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author danie
 */
public class InterfazHorarios extends javax.swing.JFrame {
private JComboBox<String> comboBoxTrabajadores;

    /**
     * Creates new form interfazHorarios
     */
     public InterfazHorarios() {
        initComponents();
        // Al iniciar, todos los paneles estarán ocultos
        jPanelSeptiembre.setVisible(false);
        jPanelOctubre.setVisible(false);
        jPanelNoviembre.setVisible(false);
        cargarDatosDesdeArchivo(jTableSeptiembre, "tablaHorariosSeptiembre.txt");
        cargarDatosDesdeArchivo(jTableOctubre, "tablaHorariosOctubre.txt");
        cargarDatosDesdeArchivo(jTableNoviembre, "tablaHorariosNoviembre.txt");
       
       
        
    }
     
 // Método para cargar el archivo de texto en la primera columna de la tabla
private void cargarDiasEnTabla(String archivo, javax.swing.JTable tabla) {
    try {
        // Leer el archivo
        java.nio.file.Path path = java.nio.file.Paths.get(archivo);
        java.util.List<String> lineas = java.nio.file.Files.readAllLines(path);

        // Mostrar el contenido del archivo en la consola
        System.out.println("Contenido del archivo " + archivo + ":");
        for (String linea : lineas) {
            System.out.println(linea);
        }

        // Obtener el modelo de la tabla
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) tabla.getModel();

        // Limpiar la tabla antes de cargar nuevos datos
        modelo.setRowCount(0);

        // Agregar las filas con los días (si es necesario, ajusta las columnas)
        for (String dia : lineas) {
            // Agregar los días en la primera columna de la tabla
            modelo.addRow(new Object[]{dia, null, null, null, null, null, null});
        }

        // Mensaje de depuración para confirmar que se está actualizando la tabla
        System.out.println("Datos cargados en la tabla.");

    } catch (java.io.IOException e) {
        // En caso de error, imprimir el mensaje en consola
        e.printStackTrace();
        System.out.println("Error al leer el archivo: " + archivo);
    }
}

private void cargarTrabajadoresEnTabla(DefaultTableModel modeloTabla, String nombreArchivo) {
    // Limpiar la tabla existente
    modeloTabla.setRowCount(0);

    // Cargar los trabajadores desde el archivo correspondiente
    try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            // Suponiendo que los datos están separados por comas
            String[] datos = linea.split(",");

            // Combinar nombre y apellido en una sola columna
            if (datos.length > 1) {
                // Unir el nombre y apellido o manejar múltiples nombres en una celda
                String[] trabajadores = datos[0].split(";"); // Separar múltiples trabajadores por un delimitador (puede ser coma o ";")
                
                // Recorrer cada trabajador
                for (String trabajador : trabajadores) {
                    trabajador = trabajador.trim(); // Eliminar espacios innecesarios

                    // Crear un nuevo array para los datos, con el nombre completo en la primera columna
                    String[] filaDatos = new String[datos.length - 1];
                    filaDatos[0] = trabajador; // Asignar el nombre del trabajador

                    // Copiar el resto de los datos después del nombre completo (las horas trabajadas u otros datos)
                    for (int i = 1; i < datos.length; i++) {
                        filaDatos[i] = datos[i]; // El resto de la fila se mantiene igual
                    }

                    // Añadir fila a la tabla
                    modeloTabla.addRow(filaDatos);
                }
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los trabajadores desde el archivo: " + e.getMessage());
        e.printStackTrace();
    }
}
private List<ReporteTrabajador> generarReporteHorasTrabajadores() {
    List<ReporteTrabajador> reporteTrabajadores = new ArrayList<>();

    JTable tablaActiva = null;
    if (jPanelSeptiembre.isVisible()) {
        tablaActiva = jTableSeptiembre;
    } else if (jPanelOctubre.isVisible()) {
        tablaActiva = jTableOctubre;
    } else if (jPanelNoviembre.isVisible()) {
        tablaActiva = jTableNoviembre;
    }

    if (tablaActiva != null) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaActiva.getModel();

        int filaInicioSemana = 0; // Índice de la primera fila de la semana
        String fechaInicio = (String) modeloTabla.getValueAt(filaInicioSemana, 0); // Fecha de inicio de la primera semana
        HashMap<String, Integer> horasPorTrabajador = new HashMap<>(); // Acumulador de horas por trabajador en la semana
        List<String> observaciones = new ArrayList<>(); // Lista para las observaciones

        // Recorrer todas las filas de la tabla
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String diaSemana = (String) modeloTabla.getValueAt(fila, 0); // Obtener el día de la semana (columna 0)

            // Recoger observaciones de la fila actual
            String observacionFila = (String) modeloTabla.getValueAt(fila, 6); // Observación en la columna 6
            if (observacionFila != null && !observacionFila.trim().isEmpty()) {
                observaciones.add("Fecha: " + modeloTabla.getValueAt(fila, 0) + " - Observación: " + observacionFila);
            }

            // Sumar las horas por trabajador en las columnas 1 a 5 (excepto observaciones)
            for (int col = 1; col <= 5; col++) {
                String valorCelda = (String) modeloTabla.getValueAt(fila, col);
                if (valorCelda != null && !valorCelda.trim().isEmpty()) {
                    String[] trabajadoresEnCelda = valorCelda.split(";");

                    for (String trabajador : trabajadoresEnCelda) {
                        trabajador = trabajador.trim();
                        int horas = 0;

                        // Asignar horas según la columna
                        switch (col) {
                            case 1: horas = 5; break;
                            case 2: horas = 9; break;
                            case 3: horas = 8; break;
                            case 4: horas = 8; break;
                            case 5: horas = 7; break;
                        }

                        // Acumular horas por trabajador
                        horasPorTrabajador.put(trabajador, horasPorTrabajador.getOrDefault(trabajador, 0) + horas);
                    }
                }
            }

            // Si es domingo, es el fin de la semana
            if (diaSemana.toLowerCase().contains("domingo")) {
                String fechaFin = (String) modeloTabla.getValueAt(fila, 0); // Fecha de fin de la semana (día domingo)

                // Agregar reporte para cada trabajador con las horas acumuladas en la semana
                for (Map.Entry<String, Integer> entry : horasPorTrabajador.entrySet()) {
                    String trabajador = entry.getKey();
                    int horasTrabajadas = entry.getValue();

                    // Agregar reporte del trabajador con el rango de la semana
                    String observacionFinal = observaciones.isEmpty() ? "NO HAY OBSERVACIONES" : String.join(", ", observaciones);
                    reporteTrabajadores.add(new ReporteTrabajador(trabajador, horasTrabajadas, fechaInicio, fechaFin, observacionFinal, ""));
                }

                // Reiniciar los acumuladores para la siguiente semana
                horasPorTrabajador.clear();
                observaciones.clear();

                // Establecer el inicio de la próxima semana
                if (fila + 1 < modeloTabla.getRowCount()) {
                    fechaInicio = (String) modeloTabla.getValueAt(fila + 1, 0);
                }
            }
        }
    }
    return reporteTrabajadores;
}

private List<ReporteObservacion> generarReporteObservaciones() {
    List<ReporteObservacion> reporteObservaciones = new ArrayList<>();

    JTable tablaActiva = null;
    if (jPanelSeptiembre.isVisible()) {
        tablaActiva = jTableSeptiembre;
    } else if (jPanelOctubre.isVisible()) {
        tablaActiva = jTableOctubre;
    } else if (jPanelNoviembre.isVisible()) {
        tablaActiva = jTableNoviembre;
    }

    if (tablaActiva != null) {
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaActiva.getModel();

        // Recorrer todas las filas de la tabla
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            String fecha = (String) modeloTabla.getValueAt(fila, 0); // Fecha de la fila actual
            String observacion = (String) modeloTabla.getValueAt(fila, 6); // Columna 6 para observaciones

            // Solo añadir al reporte si hay una observación
            if (observacion != null && !observacion.trim().isEmpty()) {
                reporteObservaciones.add(new ReporteObservacion("Observación", fecha, observacion));
            }
        }
    }

    return reporteObservaciones;
}









    // Método para mostrar el reporte
 private void mostrarReporte() {
    List<ReporteTrabajador> datosReporte = generarReporteHorasTrabajadores();

    try {
        // Compilar el informe JRXML
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/test.jrxml"));
        
        
        // Crear un mapa de parámetros, si necesitas pasar algunos al informe
        Map<String, Object> parametros = new HashMap<>();
        
        // Crear la fuente de datos
        JRBeanCollectionDataSource datosSource = new JRBeanCollectionDataSource(datosReporte);
        
        // Llenar el informe
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, datosSource);
        System.out.println("datos" + datosReporte);
        
        // Mostrar el informe directamente en una vista
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Reporte de Trabajadores");
        viewer.setVisible(true);
    } catch (JRException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage());
    }
}

private void mostrarReporteObservaciones() {
    List<ReporteObservacion> datosReporteObservaciones = generarReporteObservaciones();

    try {
        // Compilar el informe JRXML para las observaciones
        JasperReport reporteObservaciones = JasperCompileManager.compileReport(getClass().getResourceAsStream("/observacion.jrxml"));

        // Crear la fuente de datos
        JRBeanCollectionDataSource datosSource = new JRBeanCollectionDataSource(datosReporteObservaciones);

        // Llenar el informe
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporteObservaciones, null, datosSource);

        // Mostrar el informe en la vista
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Reporte de Observaciones");
        viewer.setVisible(true);
    } catch (JRException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al generar el reporte de observaciones: " + e.getMessage());
    }
}






    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioSeptiembre = new javax.swing.JRadioButton();
        jRadiooctubre = new javax.swing.JRadioButton();
        jRadionoviembre = new javax.swing.JRadioButton();
        jPanelNoviembre = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableNoviembre = new javax.swing.JTable();
        jPanelSeptiembre = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableSeptiembre = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanelOctubre = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableOctubre = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        Guardar = new javax.swing.JButton();
        Trabajador = new javax.swing.JComboBox<>();
        informe = new javax.swing.JButton();
        jResultados = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        informe1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(246, 171, 7));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel1.setText("Horarios");

        jRadioSeptiembre.setText("Septiembre");
        jRadioSeptiembre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioSeptiembreActionPerformed(evt);
            }
        });

        jRadiooctubre.setText("Octubre");
        jRadiooctubre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadiooctubreActionPerformed(evt);
            }
        });

        jRadionoviembre.setText("Noviembre");
        jRadionoviembre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadionoviembreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(94, 94, 94)
                .addComponent(jRadioSeptiembre, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jRadiooctubre, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jRadionoviembre, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(799, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jRadioSeptiembre)
                    .addComponent(jRadiooctubre)
                    .addComponent(jRadionoviembre))
                .addGap(14, 14, 14))
        );

        jTableNoviembre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "7:00 A.M. A 12:00 P.M ", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 5:00 P.M", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 4:00 P.M", "1:00 P.M. A 9:00 P.M", " DOMINGO 1:00 P.M. - 8:00 P.M..", "OBSERVACIONES"
            }
        ));
        jScrollPane3.setViewportView(jTableNoviembre);

        javax.swing.GroupLayout jPanelNoviembreLayout = new javax.swing.GroupLayout(jPanelNoviembre);
        jPanelNoviembre.setLayout(jPanelNoviembreLayout);
        jPanelNoviembreLayout.setHorizontalGroup(
            jPanelNoviembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNoviembreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelNoviembreLayout.setVerticalGroup(
            jPanelNoviembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNoviembreLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTableSeptiembre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "7:00 A.M. A 12:00 P.M ", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 5:00 P.M", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 4:00 P.M", "1:00 P.M. A 9:00 P.M", " DOMINGO 1:00 P.M. - 8:00 P.M..", "OBSERVACIONES"
            }
        ));
        jScrollPane1.setViewportView(jTableSeptiembre);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelSeptiembreLayout = new javax.swing.GroupLayout(jPanelSeptiembre);
        jPanelSeptiembre.setLayout(jPanelSeptiembreLayout);
        jPanelSeptiembreLayout.setHorizontalGroup(
            jPanelSeptiembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeptiembreLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSeptiembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelSeptiembreLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1307, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelSeptiembreLayout.setVerticalGroup(
            jPanelSeptiembreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeptiembreLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTableOctubre.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "7:00 A.M. A 12:00 P.M ", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 5:00 P.M", "7:00 A.M. A 12:00 P.M - 1:00 P.M. A 4:00 P.M", "1:00 P.M. A  9:00 P.M", " DOMINGO 1:00 P.M. - 8:00 P.M..", "OBSERVACIONES"
            }
        ));
        jScrollPane2.setViewportView(jTableOctubre);

        javax.swing.GroupLayout jPanelOctubreLayout = new javax.swing.GroupLayout(jPanelOctubre);
        jPanelOctubre.setLayout(jPanelOctubreLayout);
        jPanelOctubreLayout.setHorizontalGroup(
            jPanelOctubreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOctubreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1395, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelOctubreLayout.setVerticalGroup(
            jPanelOctubreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOctubreLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-guardar-48.png"))); // NOI18N
        Guardar.setText("Guardar");
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        Trabajador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrabajadorActionPerformed(evt);
            }
        });

        informe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-papel-48.png"))); // NOI18N
        informe.setText("informe horas");
        informe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informeActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-atrás-50.png"))); // NOI18N
        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        informe1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/icons8-papel-48.png"))); // NOI18N
        informe1.setText("Observaciones");
        informe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informe1ActionPerformed(evt);
            }
        });

        jButton2.setText("Agregar trabajador");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Trabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Guardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(informe)))))
                .addGap(31, 31, 31)
                .addComponent(informe1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Trabajador, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Guardar)
                    .addComponent(informe)
                    .addComponent(informe1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(36, 36, 36))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelSeptiembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jPanelOctubre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(12, 12, 12)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jPanelNoviembre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelSeptiembre, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jPanelOctubre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(318, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jPanelNoviembre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(315, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioSeptiembreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioSeptiembreActionPerformed
         jPanelSeptiembre.setVisible(true);
        jPanelOctubre.setVisible(false);
        jPanelNoviembre.setVisible(false); // Al seleccionar Septiembre, solo mostrar el panel de Septiembr
           cargarDiasEnTabla("diastrabajo.txt", jTableSeptiembre);
          cargarDatosDesdeArchivo(jTableSeptiembre, "tablaHorariosSeptiembre.txt");
          
      
          cargarTrabajadoresEnComboBox(); // Llama a este método
      
        
    }//GEN-LAST:event_jRadioSeptiembreActionPerformed

    private void jRadiooctubreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadiooctubreActionPerformed
        jPanelSeptiembre.setVisible(false);
        jPanelOctubre.setVisible(true);
        jPanelNoviembre.setVisible(false); 
        cargarTrabajadoresEnComboBox(); // Llama a este método
         cargarDiasEnTabla("diastrabajooctubre.txt", jTableOctubre);
        cargarDatosDesdeArchivo(jTableOctubre, "tablaHorariosOctubre.txt");
       
              // TODO add your handling code here:
    }//GEN-LAST:event_jRadiooctubreActionPerformed

    private void jRadionoviembreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadionoviembreActionPerformed
         jPanelNoviembre.setVisible(true);
        jPanelSeptiembre.setVisible(false);
        jPanelOctubre.setVisible(false);
       
        
          cargarDiasEnTabla("diastrabajonoviembre.txt", jTableNoviembre);
         cargarDatosDesdeArchivo(jTableNoviembre, "tablaHorariosNoviembre.txt");
         cargarTrabajadoresEnComboBox(); // Llama a este método
      
        
    }//GEN-LAST:event_jRadionoviembreActionPerformed
// Método para cargar trabajadores desde el archivo
private List<String> cargarTrabajadores(String archivo) {
    List<String> trabajadores = new ArrayList<>();
    File file = new File(archivo);
    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                trabajadores.add(linea.trim()); // Agregar trabajador a la lista
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return trabajadores;
}
private void cargarDatosDesdeArchivo(JTable tabla, String nombreArchivo) {
    DefaultTableModel modeloTabla = (DefaultTableModel) tabla.getModel();
    modeloTabla.setRowCount(0); // Limpiar la tabla existente

    try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] datos = linea.split(",");
            modeloTabla.addRow(datos); // Añadir fila a la tabla
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar datos desde el archivo: " + e.getMessage());
    }
}



// Cargar trabajadores en el JComboBox
private void cargarTrabajadoresEnComboBox() {
    // Cargar los trabajadores desde el archivo
    List<String> trabajadores = cargarTrabajadores("trabajadores.txt");

    // Limpiar JComboBox antes de cargar nuevos datos
    Trabajador.removeAllItems();

    for (String trabajador : trabajadores) {
        // Suponiendo que los datos están separados por comas
        String[] datos = trabajador.split(",");

        // Verificar si hay al menos un nombre y un apellido
        if (datos.length >= 2) {
            String nombreCompleto = datos[0] + " " + datos[1]; // Concatenar nombre y apellido
            Trabajador.addItem(nombreCompleto); // Agregar nombre completo al JComboBox
        }
    }

    // Mostrar mensaje si no se encuentran trabajadores
    if (trabajadores.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron trabajadores en el archivo.");
    }
}


private String trabajadorSeleccionado; 
    private void TrabajadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrabajadorActionPerformed
      // Al seleccionar un trabajador, se almacena la selección
    trabajadorSeleccionado = (String) Trabajador.getSelectedItem();


    }//GEN-LAST:event_TrabajadorActionPerformed
private void guardarCambiosEnArchivo(JTable tabla, String nombreArchivo) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            for (int j = 0; j < tabla.getColumnCount(); j++) {
                Object valorCelda = tabla.getValueAt(i, j);
                if (valorCelda != null) {
                    // Escribimos cada valor de la celda en el archivo
                    writer.write(valorCelda.toString());
                }
                
                // Agregamos un separador de columnas
                if (j < tabla.getColumnCount() - 1) {
                    writer.write(","); // Usar coma como delimitador de columnas
                }
            }
            writer.newLine(); // Cambiamos de línea al finalizar cada fila
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + e.getMessage());
        e.printStackTrace();
    }
}




    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        // Determinar cuál tabla está activa (visible)
    JTable tablaActiva = null;
    String nombreArchivo = "";

    if (jPanelSeptiembre.isVisible()) {
        tablaActiva = jTableSeptiembre; // Tabla de septiembre
        nombreArchivo = "tablaHorariosSeptiembre.txt";
    } else if (jPanelOctubre.isVisible()) {
        tablaActiva = jTableOctubre; // Tabla de octubre
        nombreArchivo = "tablaHorariosOctubre.txt";
    } else if (jPanelNoviembre.isVisible()) {
        tablaActiva = jTableNoviembre; // Tabla de noviembre
        nombreArchivo = "tablaHorariosNoviembre.txt";
    }

    // Guardar la tabla activa en el archivo correspondiente
    if (tablaActiva != null) {
        // Verificar si el archivo existe y crear si no existe
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile(); // Crear el archivo vacío
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al crear el archivo: " + e.getMessage());
                return;
            }
        }

        // Guardar los datos de la tabla en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            DefaultTableModel modeloTabla = (DefaultTableModel) tablaActiva.getModel();
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                StringBuilder linea = new StringBuilder();
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    if (j > 0) {
                        linea.append(","); // Agregar coma entre columnas
                    }
                    String valor = modeloTabla.getValueAt(i, j) != null ? modeloTabla.getValueAt(i, j).toString() : "";
                    linea.append(valor); // Agregar valor de la celda
                }
                writer.write(linea.toString()); // Escribir la línea en el archivo
                writer.newLine(); // Nueva línea
            }
            JOptionPane.showMessageDialog(this, "Tabla guardada exitosamente en " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la tabla: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        JOptionPane.showMessageDialog(this, "No hay una tabla activa para guardar.");
    }
    }//GEN-LAST:event_GuardarActionPerformed

    private void informeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_informeActionPerformed
mostrarReporte();
    }//GEN-LAST:event_informeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        abrirMenuPrincipal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void informe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_informe1ActionPerformed
mostrarReporteObservaciones();        // TODO add your handling code here:
    }//GEN-LAST:event_informe1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   
    // Obtener la fila seleccionada de la tabla activa
    int filaSeleccionada = -1;
    JTable tablaActiva = null;

    // Determinar cuál tabla está activa (visible)
    if (jPanelSeptiembre.isVisible()) {
        tablaActiva = jTableSeptiembre; // Tabla de septiembre
    } else if (jPanelOctubre.isVisible()) {
        tablaActiva = jTableOctubre; // Tabla de octubre
    } else if (jPanelNoviembre.isVisible()) {
        tablaActiva = jTableNoviembre; // Tabla de noviembre
    }

    // Obtener la fila seleccionada de la tabla activa
    if (tablaActiva != null) {
        filaSeleccionada = tablaActiva.getSelectedRow();
    }

    // Verificar si hay una fila seleccionada
    if (filaSeleccionada != -1) {
        // Verificar si el trabajador ya está en otra columna de la fila excepto en la última columna (observaciones)
        boolean trabajadorYaExiste = false;
        int ultimaColumna = tablaActiva.getColumnCount() - 1; // Última columna (Observaciones)

        for (int col = 0; col < ultimaColumna; col++) {
            String valorColumna = (String) tablaActiva.getValueAt(filaSeleccionada, col);
            if (valorColumna != null && valorColumna.contains(trabajadorSeleccionado)) {
                trabajadorYaExiste = true;
                break;
            }
        }

        // Obtener la columna seleccionada de la tabla activa
        int columnaSeleccionada = tablaActiva.getSelectedColumn();

        if (trabajadorYaExiste && columnaSeleccionada != ultimaColumna) {
            // Si el trabajador ya existe en la fila y la columna no es la de observaciones, mostrar un mensaje
            JOptionPane.showMessageDialog(this, "El trabajador ya está asignado en esta fila.");
        } else if (columnaSeleccionada != -1) {
            // Obtener el valor actual de la celda seleccionada
            String valorActual = (String) tablaActiva.getValueAt(filaSeleccionada, columnaSeleccionada);

            // Si ya hay un valor, agregar el nuevo nombre; de lo contrario, asignar el nuevo nombre
            if (valorActual != null && !valorActual.trim().isEmpty()) {
                valorActual += "; " + trabajadorSeleccionado;
            } else {
                valorActual = trabajadorSeleccionado; // Asignar el nuevo nombre
            }

            // Asignar el nuevo valor a la celda
            tablaActiva.setValueAt(valorActual, filaSeleccionada, columnaSeleccionada);

            // Guardar automáticamente los cambios en el archivo después de asignar el trabajador
            cargarDiasEnTabla(valorActual, tablaActiva);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una columna en la tabla.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila en la tabla.");
    }


    }//GEN-LAST:event_jButton2ActionPerformed
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
    private javax.swing.JButton Guardar;
    private javax.swing.JComboBox<String> Trabajador;
    private javax.swing.JButton informe;
    private javax.swing.JButton informe1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelNoviembre;
    private javax.swing.JPanel jPanelOctubre;
    private javax.swing.JPanel jPanelSeptiembre;
    private javax.swing.JRadioButton jRadioSeptiembre;
    private javax.swing.JRadioButton jRadionoviembre;
    private javax.swing.JRadioButton jRadiooctubre;
    private javax.swing.JLabel jResultados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableNoviembre;
    private javax.swing.JTable jTableOctubre;
    private javax.swing.JTable jTableSeptiembre;
    // End of variables declaration//GEN-END:variables
}
