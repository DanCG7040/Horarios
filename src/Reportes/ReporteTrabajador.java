/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

/**
 *
 * @author danie
 */
public class ReporteTrabajador {
    private String Nombre;
    private int HorasTrabajadas;
    private String FechaInicio;
    private String FechaFin;
     private String  ObservacionDiaInicio;
     private String FechaObservacion;
    

    // Constructor
    public ReporteTrabajador(String Nombre, int HorasTrabajadas, String FechaInicio, String FechaFin,String  ObservacionDiaInicio,String FechaObservacion) {
        this.Nombre = Nombre;
        this.HorasTrabajadas = HorasTrabajadas;
        this.FechaInicio = FechaInicio;
        this.FechaFin = FechaFin;
        this.ObservacionDiaInicio =  ObservacionDiaInicio;
        this.FechaObservacion = FechaObservacion;
    }

    // Getters y Setters
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }



    public int getHorasTrabajadas() {
        return HorasTrabajadas;
    }

    public void setHorasTrabajadas(int HorasTrabajadas) {
        this.HorasTrabajadas = HorasTrabajadas;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String FechaFin) {
        this.FechaFin = FechaFin;
    }


    public String getObservacionDiaInicio() {
        return ObservacionDiaInicio;
    }

    public void setObservacionDiaInicio(String ObservacionDiaInicio) {
        this.ObservacionDiaInicio = ObservacionDiaInicio;
    }

    public String getFechaObservacion() {
        return FechaObservacion;
    }

    public void setFechaObservacion(String FechaObservacion) {
        this.FechaObservacion = FechaObservacion;
    }


    

    // Sobrescribir el método toString para facilitar la impresión
    @Override
    public String toString() {
        return "ReporteTrabajador{" +
                "Nombre='" + Nombre + '\'' +
                ", HorasTrabajadas=" + HorasTrabajadas +
                ", FechaInicio='" + FechaInicio + '\'' +
                ", FechaFin='" + FechaFin + '\'' +
                ", Observaciones='" +  ObservacionDiaInicio + '\'' +
                 ", Observaciones='" +  FechaObservacion + '\'' +
                '}';
    }
}
