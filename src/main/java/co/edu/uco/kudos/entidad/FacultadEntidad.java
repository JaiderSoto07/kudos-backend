package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa una fila de la tabla {@code facultad}.
 */
public final class FacultadEntidad {

    private final UUID id;
    private final String nombre;
    private final String estado;
    private final LocalDateTime fechaCreacion;

    private FacultadEntidad(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.estado = builder.estado;
        this.fechaCreacion = builder.fechaCreacion;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public static final class Builder {

        private UUID id;
        private String nombre;
        private String estado;
        private LocalDateTime fechaCreacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombre = UtilTexto.VACIO;
            estado = "EN_CONFORMACION";
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

        public Builder estado(String estado) {
            this.estado = UtilTexto.getUtilTexto().quitarEspaciosEnBlancoOValorDefecto(estado, "EN_CONFORMACION");
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public FacultadEntidad build() {
            return new FacultadEntidad(this);
        }
    }
}
