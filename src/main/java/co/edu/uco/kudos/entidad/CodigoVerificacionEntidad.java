package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CodigoVerificacionEntidad {

    private final UUID id;
    private final UsuarioEntidad usuario;
    private final String canal;
    private final String destino;
    private final String codigo;
    private final LocalDateTime fechaGeneracion;
    private final LocalDateTime fechaExpiracion;
    private final int intentosFallidos;
    private final boolean usado;

    private CodigoVerificacionEntidad(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.canal = builder.canal;
        this.destino = builder.destino;
        this.codigo = builder.codigo;
        this.fechaGeneracion = builder.fechaGeneracion;
        this.fechaExpiracion = builder.fechaExpiracion;
        this.intentosFallidos = builder.intentosFallidos;
        this.usado = builder.usado;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public String getCanal() {
        return canal;
    }

    public String getDestino() {
        return destino;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public boolean isUsado() {
        return usado;
    }

    public static final class Builder {

        private UUID id;
        private UsuarioEntidad usuario;
        private String canal;
        private String destino;
        private String codigo;
        private LocalDateTime fechaGeneracion;
        private LocalDateTime fechaExpiracion;
        private int intentosFallidos;
        private boolean usado;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioEntidad.Builder().build();
            canal = UtilTexto.VACIO;
            destino = UtilTexto.VACIO;
            codigo = UtilTexto.VACIO;
            fechaGeneracion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            fechaExpiracion = UtilFecha.FECHA_HORA_POR_DEFECTO;
            intentosFallidos = UtilNumero.CERO;
            usado = false;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioEntidad usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioEntidad.Builder().build());
            return this;
        }

        public Builder canal(String canal) {
            this.canal = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(canal);
            return this;
        }

        public Builder destino(String destino) {
            this.destino = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(destino);
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = UtilTexto.obtenerValorDefecto(codigo);
            return this;
        }

        public Builder fechaGeneracion(LocalDateTime fechaGeneracion) {
            this.fechaGeneracion = UtilFecha.valorDefecto(fechaGeneracion);
            return this;
        }

        public Builder fechaExpiracion(LocalDateTime fechaExpiracion) {
            this.fechaExpiracion = UtilFecha.valorDefecto(fechaExpiracion);
            return this;
        }

        public Builder intentosFallidos(Integer intentosFallidos) {
            this.intentosFallidos = UtilNumero.obtenerValorDefectoSiEsNuloONegativo(intentosFallidos, UtilNumero.CERO);
            return this;
        }

        public Builder usado(boolean usado) {
            this.usado = usado;
            return this;
        }

        public CodigoVerificacionEntidad build() {
            return new CodigoVerificacionEntidad(this);
        }
    }
}
