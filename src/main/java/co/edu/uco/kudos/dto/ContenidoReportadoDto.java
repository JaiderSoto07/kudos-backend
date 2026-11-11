package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContenidoReportadoDto {

    private String tipoContenido;
    private UUID idContenido;
    private UUID idKudo;
    private String contenido;
    private String nombreAutor;
    private String estadoContenido;
    private int cantidadReportes;
    private LocalDateTime fechaUltimoReporte;
    private List<ReporteDto> reportes;

    public ContenidoReportadoDto() {
        setTipoContenido(UtilTexto.VACIO);
        setIdContenido(UtilId.VALOR_DEFECTO);
        setIdKudo(UtilId.VALOR_DEFECTO);
        setContenido(UtilTexto.VACIO);
        setNombreAutor(UtilTexto.VACIO);
        setEstadoContenido(UtilTexto.VACIO);
        setCantidadReportes(0);
        setFechaUltimoReporte(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setReportes(new ArrayList<>());
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoContenido);
    }

    public UUID getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(UUID idContenido) {
        this.idContenido = UtilId.valorDefecto(idContenido);
    }

    public UUID getIdKudo() {
        return idKudo;
    }

    public void setIdKudo(UUID idKudo) {
        this.idKudo = UtilId.valorDefecto(idKudo);
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(contenido);
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreAutor);
    }

    public String getEstadoContenido() {
        return estadoContenido;
    }

    public void setEstadoContenido(String estadoContenido) {
        this.estadoContenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoContenido);
    }

    public int getCantidadReportes() {
        return cantidadReportes;
    }

    public void setCantidadReportes(int cantidadReportes) {
        this.cantidadReportes = cantidadReportes;
    }

    public LocalDateTime getFechaUltimoReporte() {
        return fechaUltimoReporte;
    }

    public void setFechaUltimoReporte(LocalDateTime fechaUltimoReporte) {
        this.fechaUltimoReporte = UtilFecha.valorDefecto(fechaUltimoReporte);
    }

    public List<ReporteDto> getReportes() {
        return reportes;
    }

    public void setReportes(List<ReporteDto> reportes) {
        this.reportes = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(reportes, new ArrayList<>());
    }
}
