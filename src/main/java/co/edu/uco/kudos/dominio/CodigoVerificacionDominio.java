package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilNumero;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CodigoVerificacionDominio {

    private final UUID id;
    private final UsuarioDominio usuario;
    private final CanalVerificacion canal;
    private final String destino;
    private final String codigo;
    private final LocalDateTime fechaGeneracion;
    private final LocalDateTime fechaExpiracion;
    private int intentosFallidos;
    private boolean usado;

    private CodigoVerificacionDominio(Builder builder) {
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

    public boolean estaVencido(LocalDateTime ahora) {
        return !ahora.isBefore(fechaExpiracion);
    }

    public boolean intentosAgotados(int maximoIntentos) {
        return intentosFallidos >= maximoIntentos;
    }

    public void registrarIntentoFallido() {
        intentosFallidos++;
    }

    public void marcarUsado() {
        usado = true;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public CanalVerificacion getCanal() {
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
        private UsuarioDominio usuario;
        private CanalVerificacion canal;
        private String destino;
        private String codigo;
        private LocalDateTime fechaGeneracion;
        private LocalDateTime fechaExpiracion;
        private int intentosFallidos;
        private boolean usado;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            usuario = new UsuarioDominio.Builder().build();
            canal = CanalVerificacion.CORREO;
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

        public Builder usuario(UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder canal(CanalVerificacion canal) {
            this.canal = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(canal, CanalVerificacion.CORREO);
            return this;
        }

        public Builder destino(String destino) {
            this.destino = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(destino);
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(codigo);
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

        public CodigoVerificacionDominio build() {
            return new CodigoVerificacionDominio(this);
        }
    }
}
