package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class FacultadDominio {

    private final UUID id;
    private String nombre;
    private EstadoFacultad estado;
    private final LocalDateTime fechaCreacion;

    private FacultadDominio(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.estado = builder.estado;
        this.fechaCreacion = builder.fechaCreacion;
    }

    public void renombrar(String nuevoNombre) {
        nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoNombre);
    }

    public void activar() {
        if (estado == EstadoFacultad.EN_CONFORMACION) {
            estado = EstadoFacultad.ACTIVA;
        }
    }

    public void inactivar() {
        if (estado == EstadoFacultad.INACTIVA) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.FACULTAD_YA_INACTIVA);
        }
        estado = EstadoFacultad.INACTIVA;
    }

    public boolean admiteProgramas() {
        return estado.admiteProgramas();
    }

    public boolean admiteCoordinador() {
        return estado.admiteCoordinador();
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoFacultad getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public static final class Builder {

        private UUID id;
        private String nombre;
        private EstadoFacultad estado;
        private LocalDateTime fechaCreacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombre = UtilTexto.VACIO;
            estado = EstadoFacultad.EN_CONFORMACION;
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

        public Builder estado(EstadoFacultad estado) {
            this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoFacultad.EN_CONFORMACION);
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public FacultadDominio build() {
            return new FacultadDominio(this);
        }
    }
}
