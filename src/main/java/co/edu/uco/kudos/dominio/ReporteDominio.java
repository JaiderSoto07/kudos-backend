package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ReporteDominio {

    private final UUID id;
    private final UsuarioDominio reportante;
    private final KudoDominio kudo;
    private final ReplicaDominio replica;
    private final MotivoReporte motivo;
    private final String descripcion;
    private final LocalDateTime fechaReporte;
    private EstadoReporte estado;
    private UUID idModeracion;

    private ReporteDominio(Builder builder) {
        this.id = builder.id;
        this.reportante = builder.reportante;
        this.kudo = builder.kudo;
        this.replica = builder.replica;
        this.motivo = builder.motivo;
        this.descripcion = builder.descripcion;
        this.fechaReporte = builder.fechaReporte;
        this.estado = builder.estado;
        this.idModeracion = builder.idModeracion;
    }

    public boolean estaAbierto() {
        return estado.estaAbierto();
    }

    public void pasarARevision(UUID idModeracionEnCurso) {
        if (estado != EstadoReporte.PENDIENTE) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.REPORTE_NO_PENDIENTE);
        }
        estado = EstadoReporte.EN_REVISION;
        idModeracion = idModeracionEnCurso;
    }

    public void aceptar(UUID idModeracionQueResuelve) {
        resolver(EstadoReporte.ACEPTADO, idModeracionQueResuelve);
    }

    public void rechazar(UUID idModeracionQueResuelve) {
        resolver(EstadoReporte.RECHAZADO, idModeracionQueResuelve);
    }

    public boolean esSobreKudo() {
        return !UtilId.esValorDefecto(kudo.getId());
    }

    private void resolver(EstadoReporte decision, UUID idModeracionQueResuelve) {
        if (!estaAbierto()) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.REPORTE_YA_RESUELTO);
        }
        estado = decision;
        idModeracion = idModeracionQueResuelve;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getReportante() {
        return reportante;
    }

    public KudoDominio getKudo() {
        return kudo;
    }

    public ReplicaDominio getReplica() {
        return replica;
    }

    public MotivoReporte getMotivo() {
        return motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public EstadoReporte getEstado() {
        return estado;
    }

    public UUID getIdModeracion() {
        return idModeracion;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioDominio reportante;
        private KudoDominio kudo;
        private ReplicaDominio replica;
        private MotivoReporte motivo;
        private String descripcion;
        private LocalDateTime fechaReporte;
        private EstadoReporte estado;
        private UUID idModeracion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            reportante = new UsuarioDominio.Builder().build();
            kudo = new KudoDominio.Builder().build();
            replica = new ReplicaDominio.Builder().build();
            motivo = MotivoReporte.OTRO;
            descripcion = UtilTexto.VACIO;
            fechaReporte = UtilFecha.FECHA_HORA_POR_DEFECTO;
            estado = EstadoReporte.PENDIENTE;
            idModeracion = UtilId.VALOR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder reportante(UsuarioDominio reportante) {
            this.reportante = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(reportante, new UsuarioDominio.Builder().build());
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

        public Builder motivo(MotivoReporte motivo) {
            this.motivo = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(motivo, MotivoReporte.OTRO);
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

        public Builder estado(EstadoReporte estado) {
            this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoReporte.PENDIENTE);
            return this;
        }

        public Builder idModeracion(UUID idModeracion) {
            this.idModeracion = UtilId.valorDefecto(idModeracion);
            return this;
        }

        public ReporteDominio build() {
            return new ReporteDominio(this);
        }
    }
}
