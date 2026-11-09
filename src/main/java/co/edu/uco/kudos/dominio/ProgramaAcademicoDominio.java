package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ProgramaAcademicoDominio {

    private final UUID id;
    private String nombre;
    private final FacultadDominio facultad;
    private boolean activo;
    private final LocalDateTime fechaCreacion;

    private ProgramaAcademicoDominio(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.facultad = builder.facultad;
        this.activo = builder.activo;
        this.fechaCreacion = builder.fechaCreacion;
    }

    public void renombrar(String nuevoNombre) {
        nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoNombre);
    }

    public void inactivar() {
        if (!activo) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.PROGRAMA_YA_INACTIVO);
        }
        activo = false;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public FacultadDominio getFacultad() {
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
        private FacultadDominio facultad;
        private boolean activo;
        private LocalDateTime fechaCreacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombre = UtilTexto.VACIO;
            facultad = new FacultadDominio.Builder().build();
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

        public Builder facultad(FacultadDominio facultad) {
            this.facultad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(facultad, new FacultadDominio.Builder().build());
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

        public ProgramaAcademicoDominio build() {
            return new ProgramaAcademicoDominio(this);
        }
    }
}
