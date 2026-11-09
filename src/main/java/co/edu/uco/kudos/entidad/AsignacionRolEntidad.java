package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AsignacionRolEntidad {

    private final UUID id;
    private final UsuarioEntidad usuario;
    private final String rol;
    private final FacultadEntidad facultad;
    private final ProgramaAcademicoEntidad programa;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private final String motivoFin;

    private AsignacionRolEntidad(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.rol = builder.rol;
        this.facultad = builder.facultad;
        this.programa = builder.programa;
        this.fechaInicio = builder.fechaInicio;
        this.fechaFin = builder.fechaFin;
        this.motivoFin = builder.motivoFin;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    public FacultadEntidad getFacultad() {
        return facultad;
    }

    public ProgramaAcademicoEntidad getPrograma() {
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
        private UsuarioEntidad usuario;
        private String rol;
        private FacultadEntidad facultad;
        private ProgramaAcademicoEntidad programa;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private String motivoFin;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioEntidad.Builder().build();
            rol = UtilTexto.VACIO;
            facultad = new FacultadEntidad.Builder().build();
            programa = new ProgramaAcademicoEntidad.Builder().build();
            fechaInicio = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaFin = UtilFecha.FECHA_HORA_POR_DEFECTO;
            motivoFin = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioEntidad usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder rol(String rol) {
            this.rol = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(rol);
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

        public AsignacionRolEntidad build() {
            return new AsignacionRolEntidad(this);
        }
    }
}
