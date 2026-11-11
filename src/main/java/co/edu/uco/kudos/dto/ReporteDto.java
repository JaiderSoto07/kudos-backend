package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReporteDto {

    private UUID id;
    private String motivo;
    private String descripcion;
    private LocalDateTime fechaReporte;
    private String estado;
    private String tipoContenido;
    private UUID idContenido;
    private UUID idKudo;
    private String contenido;
    private String nombreAutorContenido;
    private String nombreReportante;

    public ReporteDto() {
        setId(UtilId.VALOR_DEFECTO);
        setMotivo(UtilTexto.VACIO);
        setDescripcion(UtilTexto.VACIO);
        setFechaReporte(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setEstado(UtilTexto.VACIO);
        setTipoContenido(UtilTexto.VACIO);
        setIdContenido(UtilId.VALOR_DEFECTO);
        setIdKudo(UtilId.VALOR_DEFECTO);
        setContenido(UtilTexto.VACIO);
        setNombreAutorContenido(UtilTexto.VACIO);
        setNombreReportante(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilId.valorDefecto(id);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = UtilFecha.valorDefecto(fechaReporte);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
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

    public String getNombreAutorContenido() {
        return nombreAutorContenido;
    }

    public void setNombreAutorContenido(String nombreAutorContenido) {
        this.nombreAutorContenido = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreAutorContenido);
    }

    public String getNombreReportante() {
        return nombreReportante;
    }

    public void setNombreReportante(String nombreReportante) {
        this.nombreReportante = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreReportante);
    }
}
