package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SolicitudRegistroDominio {

    private final UUID id;
    private final UsuarioDominio solicitante;
    private final Rol rolSolicitado;
    private final FacultadDominio facultad;
    private final ProgramaAcademicoDominio programa;
    private EstadoSolicitud estado;
    private final LocalDateTime fechaSolicitud;
    private UsuarioDominio decididaPor;
    private LocalDateTime fechaDecision;
    private String motivoRechazo;

    private SolicitudRegistroDominio(Builder builder) {
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

    public boolean estaPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
    }

    public void aprobar(UsuarioDominio autoridad) {
        decidir(EstadoSolicitud.APROBADA, autoridad);
    }

    public void rechazar(UsuarioDominio autoridad, String motivo) {
        decidir(EstadoSolicitud.RECHAZADA, autoridad);
        motivoRechazo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
    }

    private void decidir(EstadoSolicitud decision, UsuarioDominio autoridad) {
        if (!estaPendiente()) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.SOLICITUD_YA_DECIDIDA);
        }
        estado = decision;
        decididaPor = autoridad;
        fechaDecision = UtilFecha.fechaActual();
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getSolicitante() {
        return solicitante;
    }

    public Rol getRolSolicitado() {
        return rolSolicitado;
    }

    public FacultadDominio getFacultad() {
        return facultad;
    }

    public ProgramaAcademicoDominio getPrograma() {
        return programa;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public UsuarioDominio getDecididaPor() {
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
        private UsuarioDominio solicitante;
        private Rol rolSolicitado;
        private FacultadDominio facultad;
        private ProgramaAcademicoDominio programa;
        private EstadoSolicitud estado;
        private LocalDateTime fechaSolicitud;
        private UsuarioDominio decididaPor;
        private LocalDateTime fechaDecision;
        private String motivoRechazo;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            solicitante = new UsuarioDominio.Builder().build();
            rolSolicitado = Rol.ESTUDIANTE;
            facultad = new FacultadDominio.Builder().build();
            programa = new ProgramaAcademicoDominio.Builder().build();
            estado = EstadoSolicitud.PENDIENTE;
            fechaSolicitud = UtilFecha.FECHA_HORA_POR_DEFECTO;
            decididaPor = new UsuarioDominio.Builder().build();
            fechaDecision = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoRechazo = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder solicitante(UsuarioDominio solicitante) {
            this.solicitante = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(solicitante, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder rolSolicitado(Rol rolSolicitado) {
            this.rolSolicitado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(rolSolicitado, Rol.ESTUDIANTE);
            return this;
        }

        public Builder facultad(FacultadDominio facultad) {
            this.facultad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(facultad, new FacultadDominio.Builder().build());
            return this;
        }

        public Builder programa(ProgramaAcademicoDominio programa) {
            this.programa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(programa, new ProgramaAcademicoDominio.Builder().build());
            return this;
        }

        public Builder estado(EstadoSolicitud estado) {
            this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoSolicitud.PENDIENTE);
            return this;
        }

        public Builder fechaSolicitud(LocalDateTime fechaSolicitud) {
            this.fechaSolicitud = UtilFecha.valorDefecto(fechaSolicitud);
            return this;
        }

        public Builder decididaPor(UsuarioDominio decididaPor) {
            this.decididaPor = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(decididaPor, new UsuarioDominio.Builder().build());
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

        public SolicitudRegistroDominio build() {
            return new SolicitudRegistroDominio(this);
        }
    }
}
