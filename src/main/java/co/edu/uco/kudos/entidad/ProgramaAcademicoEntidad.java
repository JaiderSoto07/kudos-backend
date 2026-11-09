package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ProgramaAcademicoEntidad {

    private final UUID id;
    private final String nombre;
    private final FacultadEntidad facultad;
    private final boolean activo;
    private final LocalDateTime fechaCreacion;

    private ProgramaAcademicoEntidad(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.facultad = builder.facultad;
        this.activo = builder.activo;
        this.fechaCreacion = builder.fechaCreacion;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public FacultadEntidad getFacultad() {
        return facultad;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public static final class Builder {

        private UUID id;
        private String nombre;
        private FacultadEntidad facultad;
        private boolean activo;
        private LocalDateTime fechaCreacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombre = UtilTexto.VACIO;
            facultad = new FacultadEntidad.Builder().build();
            activo = true;
            fechaCreacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombre);
            return this;
        }

        public Builder facultad(FacultadEntidad facultad) {
            this.facultad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(facultad, new FacultadEntidad.Builder().build());
            return this;
        }

        public Builder activo(boolean activo) {
            this.activo = activo;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public ProgramaAcademicoEntidad build() {
            return new ProgramaAcademicoEntidad(this);
        }
    }
}
