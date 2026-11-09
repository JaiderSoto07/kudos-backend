package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ModeracionEntidad {

    private final UUID id;
    private final KudoEntidad kudo;
    private final ReplicaEntidad replica;
    private final UsuarioEntidad moderador;
    private final String estado;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaCierre;
    private final String decision;
    private final String motivoDecision;

    private ModeracionEntidad(Builder builder) {
        this.id = builder.id;
        this.kudo = builder.kudo;
        this.replica = builder.replica;
        this.moderador = builder.moderador;
        this.estado = builder.estado;
        this.fechaInicio = builder.fechaInicio;
        this.fechaCierre = builder.fechaCierre;
        this.decision = builder.decision;
        this.motivoDecision = builder.motivoDecision;
    }

    public UUID getId() {
        return id;
    }

    public KudoEntidad getKudo() {
        return kudo;
    }

    public ReplicaEntidad getReplica() {
        return replica;
    }

    public UsuarioEntidad getModerador() {
        return moderador;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public String getDecision() {
        return decision;
    }

    public String getMotivoDecision() {
        return motivoDecision;
    }

    public static final class Builder {

        private UUID id;
        private KudoEntidad kudo;
        private ReplicaEntidad replica;
        private UsuarioEntidad moderador;
        private String estado;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaCierre;
        private String decision;
        private String motivoDecision;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            kudo = new KudoEntidad.Builder().build();
            replica = new ReplicaEntidad.Builder().build();
            moderador = new UsuarioEntidad.Builder().build();
            estado = UtilTexto.VACIO;
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaCierre = UtilFecha.FECHA_HORA_POR_DEFECTO;
            decision = UtilTexto.VACIO;
            motivoDecision = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
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

        public Builder moderador(UsuarioEntidad moderador) {
            this.moderador = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(moderador, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder estado(String estado) {
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estado);
            return this;
        }

        public Builder fechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = UtilFecha.valorDefecto(fechaInicio);
            return this;
        }

        public Builder fechaCierre(LocalDateTime fechaCierre) {
            this.fechaCierre = UtilFecha.valorDefecto(fechaCierre);
            return this;
        }

        public Builder decision(String decision) {
            this.decision = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(decision);
            return this;
        }

        public Builder motivoDecision(String motivoDecision) {
            this.motivoDecision = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoDecision);
            return this;
        }

        public ModeracionEntidad build() {
            return new ModeracionEntidad(this);
        }
    }
}
