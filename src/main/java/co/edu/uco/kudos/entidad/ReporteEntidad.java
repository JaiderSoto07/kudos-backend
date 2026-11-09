package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ReporteEntidad {

    private final UUID id;
    private final UsuarioEntidad reportante;
    private final KudoEntidad kudo;
    private final ReplicaEntidad replica;
    private final String motivo;
    private final String descripcion;
    private final LocalDateTime fechaReporte;
    private final String estado;
    private final ModeracionEntidad moderacion;

    private ReporteEntidad(Builder builder) {
        this.id = builder.id;
        this.reportante = builder.reportante;
        this.kudo = builder.kudo;
        this.replica = builder.replica;
        this.motivo = builder.motivo;
        this.descripcion = builder.descripcion;
        this.fechaReporte = builder.fechaReporte;
        this.estado = builder.estado;
        this.moderacion = builder.moderacion;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getReportante() {
        return reportante;
    }

    public KudoEntidad getKudo() {
        return kudo;
    }

    public ReplicaEntidad getReplica() {
        return replica;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public String getEstado() {
        return estado;
    }

    public ModeracionEntidad getModeracion() {
        return moderacion;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioEntidad reportante;
        private KudoEntidad kudo;
        private ReplicaEntidad replica;
        private String motivo;
        private String descripcion;
        private LocalDateTime fechaReporte;
        private String estado;
        private ModeracionEntidad moderacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            reportante = new UsuarioEntidad.Builder().build();
            kudo = new KudoEntidad.Builder().build();
            replica = new ReplicaEntidad.Builder().build();
            motivo = UtilTexto.VACIO;
            descripcion = UtilTexto.VACIO;
            fechaReporte = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = UtilTexto.VACIO;
            moderacion = new ModeracionEntidad.Builder().build();
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder reportante(UsuarioEntidad reportante) {
            this.reportante = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(reportante, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder kudo(KudoEntidad kudo) {
            this.kudo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(kudo, new KudoEntidad.Builder().build());
            return this;
        }

        public Builder replica(ReplicaEntidad replica) {
            this.replica = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(replica, new ReplicaEntidad.Builder().build());
            return this;
        }

        public Builder motivo(String motivo) {
            this.motivo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
            return this;
        }

        public Builder fechaReporte(LocalDateTime fechaReporte) {
            this.fechaReporte = UtilFecha.valorDefecto(fechaReporte);
            return this;
        }

        public Builder estado(String estado) {
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
            return this;
        }

        public Builder moderacion(ModeracionEntidad moderacion) {
            this.moderacion = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(moderacion, new ModeracionEntidad.Builder().build());
            return this;
        }

        public ReporteEntidad build() {
            return new ReporteEntidad(this);
        }
    }
}
