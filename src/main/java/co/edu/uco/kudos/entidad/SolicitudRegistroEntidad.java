package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa una fila de la tabla {@code solicitud_registro}.
 */
public final class SolicitudRegistroEntidad {

    private final UUID id;
    private final UsuarioEntidad solicitante;
    private final String rolSolicitado;
    private final FacultadEntidad facultad;
    private final ProgramaAcademicoEntidad programa;
    private final String estado;
    private final LocalDateTime fechaSolicitud;
    private final UsuarioEntidad decididaPor;
    private final LocalDateTime fechaDecision;
    private final String motivoRechazo;

    private SolicitudRegistroEntidad(Builder builder) {
        this.id = builder.id;
        this.solicitante = builder.solicitante;
        this.rolSolicitado = builder.rolSolicitado;
        this.facultad = builder.facultad;
        this.programa = builder.programa;
        this.estado = builder.estado;
        this.fechaSolicitud = builder.fechaSolicitud;
        this.decididaPor = builder.decididaPor;
        this.fechaDecision = builder.fechaDecision;
        this.motivoRechazo = builder.motivoRechazo;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getSolicitante() {
        return solicitante;
    }

    public String getRolSolicitado() {
        return rolSolicitado;
    }

    public FacultadEntidad getFacultad() {
        return facultad;
    }

    public ProgramaAcademicoEntidad getPrograma() {
        return programa;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public UsuarioEntidad getDecididaPor() {
        return decididaPor;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioEntidad solicitante;
        private String rolSolicitado;
        private FacultadEntidad facultad;
        private ProgramaAcademicoEntidad programa;
        private String estado;
        private LocalDateTime fechaSolicitud;
        private UsuarioEntidad decididaPor;
        private LocalDateTime fechaDecision;
        private String motivoRechazo;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            solicitante = new UsuarioEntidad.Builder().build();
            rolSolicitado = UtilTexto.VACIO;
            facultad = new FacultadEntidad.Builder().build();
            programa = new ProgramaAcademicoEntidad.Builder().build();
            estado = "PENDIENTE";
            fechaSolicitud = UtilFecha.FECHA_HORA_POR_DEFECTO;
            decididaPor = new UsuarioEntidad.Builder().build();
            fechaDecision = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoRechazo = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder solicitante(UsuarioEntidad solicitante) {
            this.solicitante = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(solicitante, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder rolSolicitado(String rolSolicitado) {
            this.rolSolicitado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rolSolicitado);
            return this;
        }

        public Builder facultad(FacultadEntidad facultad) {
            this.facultad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(facultad, new FacultadEntidad.Builder().build());
            return this;
        }

        public Builder programa(ProgramaAcademicoEntidad programa) {
            this.programa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(programa, new ProgramaAcademicoEntidad.Builder().build());
            return this;
        }

        public Builder estado(String estado) {
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlancoOValorDefecto(estado, "PENDIENTE");
            return this;
        }

        public Builder fechaSolicitud(LocalDateTime fechaSolicitud) {
            this.fechaSolicitud = UtilFecha.valorDefecto(fechaSolicitud);
            return this;
        }

        public Builder decididaPor(UsuarioEntidad decididaPor) {
            this.decididaPor = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(decididaPor, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder fechaDecision(LocalDateTime fechaDecision) {
            this.fechaDecision = UtilFecha.valorDefecto(fechaDecision);
            return this;
        }

        public Builder motivoRechazo(String motivoRechazo) {
            this.motivoRechazo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoRechazo);
            return this;
        }

        public SolicitudRegistroEntidad build() {
            return new SolicitudRegistroEntidad(this);
        }
    }
}
