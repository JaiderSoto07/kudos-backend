package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AuditoriaEntidad {

    private final UUID id;
    private final LocalDateTime fechaHora;
    private final UsuarioEntidad actor;
    private final String accion;
    private final String tipoObjeto;
    private final UUID idObjeto;
    private final String estadoAnterior;
    private final String estadoNuevo;
    private final String motivo;
    private final String detalle;

    private AuditoriaEntidad(Builder builder) {
        this.id = builder.id;
        this.fechaHora = builder.fechaHora;
        this.actor = builder.actor;
        this.accion = builder.accion;
        this.tipoObjeto = builder.tipoObjeto;
        this.idObjeto = builder.idObjeto;
        this.estadoAnterior = builder.estadoAnterior;
        this.estadoNuevo = builder.estadoNuevo;
        this.motivo = builder.motivo;
        this.detalle = builder.detalle;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public UsuarioEntidad getActor() {
        return actor;
    }

    public String getAccion() {
        return accion;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public UUID getIdObjeto() {
        return idObjeto;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDetalle() {
        return detalle;
    }

    public static final class Builder {

        private UUID id;
        private LocalDateTime fechaHora;
        private UsuarioEntidad actor;
        private String accion;
        private String tipoObjeto;
        private UUID idObjeto;
        private String estadoAnterior;
        private String estadoNuevo;
        private String motivo;
        private String detalle;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            fechaHora = UtilFecha.FECHA_HORA_POR_DEFECTO;
            actor = new UsuarioEntidad.Builder().build();
            accion = UtilTexto.VACIO;
            tipoObjeto = UtilTexto.VACIO;
            idObjeto = UtilId.VALOR_DEFECTO;
            estadoAnterior = UtilTexto.VACIO;
            estadoNuevo = UtilTexto.VACIO;
            motivo = UtilTexto.VACIO;
            detalle = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder fechaHora(LocalDateTime fechaHora) {
            this.fechaHora = UtilFecha.valorDefecto(fechaHora);
            return this;
        }

        public Builder actor(UsuarioEntidad actor) {
            this.actor = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(actor, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder accion(String accion) {
            this.accion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(accion);
            return this;
        }

        public Builder tipoObjeto(String tipoObjeto) {
            this.tipoObjeto = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(tipoObjeto);
            return this;
        }

        public Builder idObjeto(UUID idObjeto) {
            this.idObjeto = UtilId.valorDefecto(idObjeto);
            return this;
        }

        public Builder estadoAnterior(String estadoAnterior) {
            this.estadoAnterior = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoAnterior);
            return this;
        }

        public Builder estadoNuevo(String estadoNuevo) {
            this.estadoNuevo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoNuevo);
            return this;
        }

        public Builder motivo(String motivo) {
            this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
            return this;
        }

        public Builder detalle(String detalle) {
            this.detalle = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(detalle);
            return this;
        }

        public AuditoriaEntidad build() {
            return new AuditoriaEntidad(this);
        }
    }
}
