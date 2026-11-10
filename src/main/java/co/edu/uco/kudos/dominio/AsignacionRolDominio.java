package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AsignacionRolDominio {

    private final UUID id;
    private final UsuarioDominio usuario;
    private final Rol rol;
    private final FacultadDominio facultad;
    private final ProgramaAcademicoDominio programa;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String motivoFin;

    private AsignacionRolDominio(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.rol = builder.rol;
        this.facultad = builder.facultad;
        this.programa = builder.programa;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.motivoFin = builder.motivoFin;
    }

    public boolean estaVigente() {
        return UtilFecha.esValorDefecto(fechaFin);
    }

    public void cerrar(String motivo) {
        if (!estaVigente()) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.ASIGNACION_YA_CERRADA);
        }
        motivoFin = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivo);
        fechaFin = UtilFecha.fechaActual();
    }

    public boolean puedeVerFacultad(UUID idFacultad) {
        if (UtilId.esValorDefecto(id) || !estaVigente()) {
            return false;
        }
        return switch (rol) {
            case ADMINISTRADOR, RECTOR, COORDINADOR_ACADEMICO -> true;
            case COORDINADOR_FACULTAD -> facultad.getId().equals(idFacultad);
            case COORDINADOR_PROGRAMA -> programa.getFacultad().getId().equals(idFacultad);
            default -> false;
        };
    }

    public boolean puedeVerPrograma(ProgramaAcademicoDominio programaConsultado) {
        if (UtilId.esValorDefecto(id) || !estaVigente()) {
            return false;
        }
        return switch (rol) {
            case ADMINISTRADOR, RECTOR, COORDINADOR_ACADEMICO -> true;
            case COORDINADOR_FACULTAD -> facultad.getId().equals(programaConsultado.getFacultad().getId());
            case COORDINADOR_PROGRAMA -> programa.getId().equals(programaConsultado.getId());
            default -> false;
        };
    }

    public boolean coordinaPrograma(UUID idPrograma) {
        return !UtilId.esValorDefecto(id) && estaVigente() && rol == Rol.COORDINADOR_PROGRAMA && programa.getId().equals(idPrograma);
    }

    public boolean puedeDecidir(SolicitudRegistroDominio solicitud) {
        if (UtilId.esValorDefecto(id) || !estaVigente() || rol != solicitud.getRolSolicitado().rolAprobador()) {
            return false;
        }
        return switch (solicitud.getRolSolicitado()) {
            case ESTUDIANTE, DOCENTE -> programa.getId().equals(solicitud.getPrograma().getId());
            case COORDINADOR_PROGRAMA -> facultad.getId().equals(solicitud.getPrograma().getFacultad().getId());
            default -> true;
        };
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public FacultadDominio getFacultad() {
        return facultad;
    }

    public ProgramaAcademicoDominio getPrograma() {
        return programa;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public String getMotivoFin() {
        return motivoFin;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioDominio usuario;
        private Rol rol;
        private FacultadDominio facultad;
        private ProgramaAcademicoDominio programa;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private String motivoFin;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioDominio.Builder().build();
            rol = Rol.ESTUDIANTE;
            facultad = new FacultadDominio.Builder().build();
            programa = new ProgramaAcademicoDominio.Builder().build();
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaFin = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoFin = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder rol(Rol rol) {
            this.rol = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(rol, Rol.ESTUDIANTE);
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

        public Builder fechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = UtilFecha.valorDefecto(fechaInicio);
            return this;
        }

        public Builder fechaFin(LocalDateTime fechaFin) {
            this.fechaFin = UtilFecha.valorDefecto(fechaFin);
            return this;
        }

        public Builder motivoFin(String motivoFin) {
            this.motivoFin = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(motivoFin);
            return this;
        }

        public AsignacionRolDominio build() {
            return new AsignacionRolDominio(this);
        }
    }
}
