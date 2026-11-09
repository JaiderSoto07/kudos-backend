package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ParametroGlobalEntidad {

    private final UUID id;
    private final String clave;
    private final String valor;
    private final String tipoDato;
    private final String descripcion;
    private final LocalDateTime fechaModificacion;

    private ParametroGlobalEntidad(Builder builder) {
        this.id = builder.id;
        this.clave = builder.clave;
        this.valor = builder.valor;
        this.tipoDato = builder.tipoDato;
        this.descripcion = builder.descripcion;
        this.fechaModificacion = builder.fechaModificacion;
    }

    public UUID getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getValor() {
        return valor;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public static final class Builder {

        private UUID id;
        private String clave;
        private String valor;
        private String tipoDato;
        private String descripcion;
        private LocalDateTime fechaModificacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            clave = UtilTexto.VACIO;
            valor = UtilTexto.VACIO;
            tipoDato = UtilTexto.VACIO;
            descripcion = UtilTexto.VACIO;
            fechaModificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder clave(String clave) {
            this.clave = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(clave);
            return this;
        }

        public Builder valor(String valor) {
            this.valor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(valor);
            return this;
        }

        public Builder tipoDato(String tipoDato) {
            this.tipoDato = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoDato);
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
            return this;
        }

        public Builder fechaModificacion(LocalDateTime fechaModificacion) {
            this.fechaModificacion = UtilFecha.valorDefecto(fechaModificacion);
            return this;
        }

        public ParametroGlobalEntidad build() {
            return new ParametroGlobalEntidad(this);
        }
    }
}
