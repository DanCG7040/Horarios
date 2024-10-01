package Reportes;

public class ReporteObservacion {
    private String Nombre;
    private String FechaInicio;
    private String Observaciones;

    // Constructor
    public ReporteObservacion(String Nombre, String FechaInicio, String Observaciones) {
        this.Nombre = Nombre;
        this.FechaInicio = FechaInicio;
        this.Observaciones = Observaciones;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String FechaInicio) {
        this.FechaInicio = FechaInicio;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observacion) {
        this.Observaciones = Observacion;
    }

    @Override
    public String toString() {
        return "ReporteObservacion{" +
                "Nombre='" + Nombre + '\'' +
                ", FechaInicio='" + FechaInicio + '\'' +
                ", Observaciones='" + Observaciones + '\'' +
                '}';
    }
}
