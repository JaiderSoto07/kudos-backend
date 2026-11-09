package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CategoriaDominio {

    private final UUID id;
    private String nombre;
    private String descripcion;
    private boolean activa;
    private final LocalDateTime fechaCreacion;

    private CategoriaDominio(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.descripcion = builder.descripcion;
        this.activa = builder.activa;
        this.fechaCreacion = builder.fechaCreacion;
    }

    public void modificar(String nuevoNombre, String nuevaDescripcion) {
        nombre = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoNombre);
        descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevaDescripcion);
    }

    public void activar() {
        if (activa) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.CATEGORIA_YA_ACTIVA);
        }
        activa = true;
    }

    public void desactivar() {
        if (!activa) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.CATEGORIA_YA_INACTIVA);
        }
        activa = false;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public static final class Builder {

        private UUID id;
        private String nombre;
        private String descripcion;
        private boolean activa;
        private LocalDateTime fechaCreacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombre = UtilTexto.VACIO;
            descripcion = UtilTexto.VACIO;
            activa = true;
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

        public Builder descripcion(String descripcion) {
            this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
            return this;
        }

        public Builder activa(boolean activa) {
            this.activa = activa;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = UtilFecha.valorDefecto(fechaCreacion);
            return this;
        }

        public CategoriaDominio build() {
            return new CategoriaDominio(this);
        }
    }
}
