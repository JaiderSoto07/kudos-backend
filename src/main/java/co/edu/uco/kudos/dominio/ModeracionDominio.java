package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ModeracionDominio {

    private final UUID id;
    private final KudoDominio kudo;
    private final ReplicaDominio replica;
    private final UsuarioDominio moderador;
    private EstadoModeracion estado;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private DecisionModeracion decision;
    private String motivoDecision;

    private ModeracionDominio(Builder builder) {
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

    public boolean estaEnCurso() {
        return estado == EstadoModeracion.EN_CURSO;
    }

    public boolean terminoEnVeto() {
        return decision == DecisionModeracion.VETAR;
    }

    public void vetar(String motivo) {
        cerrar(DecisionModeracion.VETAR, motivo);
    }

    public void liberar(String motivo) {
        cerrar(DecisionModeracion.LIBERAR, motivo);
    }

    public boolean esSobreKudo() {
        return !UtilId.esValorDefecto(kudo.getId());
    }

    private void cerrar(DecisionModeracion decisionTomada, String motivo) {
        if (!estaEnCurso()) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.MODERACION_YA_CERRADA);
        }
        decision = decisionTomada;
        motivoDecision = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
        estado = EstadoModeracion.CERRADA;
        fechaCierre = UtilFecha.fechaActual();
    }

    public UUID getId() {
        return id;
    }

    public KudoDominio getKudo() {
        return kudo;
    }

    public ReplicaDominio getReplica() {
        return replica;
    }

    public UsuarioDominio getModerador() {
        return moderador;
    }

    public EstadoModeracion getEstado() {
        return estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public DecisionModeracion getDecision() {
        return decision;
    }

    public String getMotivoDecision() {
        return motivoDecision;
    }

    public static final class Builder {

        private UUID id;
        private KudoDominio kudo;
        private ReplicaDominio replica;
        private UsuarioDominio moderador;
        private EstadoModeracion estado;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaCierre;
        private DecisionModeracion decision;
        private String motivoDecision;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            kudo = new KudoDominio.Builder().build();
            replica = new ReplicaDominio.Builder().build();
            moderador = new UsuarioDominio.Builder().build();
            estado = EstadoModeracion.EN_CURSO;
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaCierre = UtilFecha.FECHA_HORA_POR_DEFECTO;
            decision = null;
            motivoDecision = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder kudo(KudoDominio kudo) {
            this.kudo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(kudo, new KudoDominio.Builder().build());
            return this;
        }

        public Builder replica(ReplicaDominio replica) {
            this.replica = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(replica, new ReplicaDominio.Builder().build());
            return this;
        }

        public Builder moderador(UsuarioDominio moderador) {
            this.moderador = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(moderador, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder estado(EstadoModeracion estado) {
            this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoModeracion.EN_CURSO);
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

        public Builder decision(DecisionModeracion decision) {
            this.decision = decision;
            return this;
        }

        public Builder motivoDecision(String motivoDecision) {
            this.motivoDecision = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoDecision);
            return this;
        }

        public ModeracionDominio build() {
            return new ModeracionDominio(this);
        }
    }
}
