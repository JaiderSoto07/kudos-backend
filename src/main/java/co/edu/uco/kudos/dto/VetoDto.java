package co.edu.uco.kudos.dto;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public class VetoDto {

    private UUID idModeracion;
    private String tipoContenido;
    private UUID idContenido;
    private UUID idKudo;
    private String contenido;
    private String nombreAutor;
    private String nombreModerador;
    private LocalDateTime fechaVeto;
    private String motivo;
    private int cantidadReportes;

    public VetoDto() {
        setIdModeracion(UtilId.VALOR_DEFECTO);
        setTipoContenido(UtilTexto.VACIO);
        setIdContenido(UtilId.VALOR_DEFECTO);
        setIdKudo(UtilId.VALOR_DEFECTO);
        setContenido(UtilTexto.VACIO);
        setNombreAutor(UtilTexto.VACIO);
        setNombreModerador(UtilTexto.VACIO);
        setFechaVeto(UtilFecha.FECHA_HORA_POR_DEFECTO);
        setMotivo(UtilTexto.VACIO);
        setCantidadReportes(0);
    }

    public UUID getIdModeracion() {
        return idModeracion;
    }

    public void setIdModeracion(UUID idModeracion) {
        this.idModeracion = UtilId.valorDefecto(idModeracion);
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

    public String getNombreModerador() {
        return nombreModerador;
    }

    public void setNombreModerador(String nombreModerador) {
        this.nombreModerador = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombreModerador);
    }

    public LocalDateTime getFechaVeto() {
        return fechaVeto;
    }

    public void setFechaVeto(LocalDateTime fechaVeto) {
        this.fechaVeto = UtilFecha.valorDefecto(fechaVeto);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
    }

    public int getCantidadReportes() {
        return cantidadReportes;
    }

    public void setCantidadReportes(int cantidadReportes) {
        this.cantidadReportes = cantidadReportes;
    }
}
