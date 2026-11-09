package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class ParametroGlobalDominio {

    private final UUID id;
    private final String clave;
    private String valor;
    private final TipoParametro tipoDato;
    private final String descripcion;
    private LocalDateTime fechaModificacion;

    private ParametroGlobalDominio(Builder builder) {
        this.id = builder.id;
        this.clave = builder.clave;
        this.valor = builder.valor;
        this.tipoDato = builder.tipoDato;
        this.descripcion = builder.descripcion;
        this.fechaModificacion = builder.fechaModificacion;
    }

    public static final String DOMINIO_CORREO = "DOMINIO_CORREO";
    public static final String VIGENCIA_CODIGO_MINUTOS = "VIGENCIA_CODIGO_MINUTOS";
    public static final String MAX_INTENTOS_VERIFICACION = "MAX_INTENTOS_VERIFICACION";

    public void cambiarValor(String nuevoValor) {
        valor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoValor);
        fechaModificacion = UtilFecha.fechaActual();
    }

    public int valorComoEntero() {
        if (tipoDato != TipoParametro.ENTERO || !valor.matches("[0-9]+")) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.PARAMETRO_NO_ES_ENTERO);
        }
        return Integer.parseInt(valor);
    }

    public UUID getId() {
        return id;
    }

    public String getClave() {
        return clave;
    }

    public String getValor() {
        return valor;
    }

    public TipoParametro getTipoDato() {
        return tipoDato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public static final class Builder {

        private UUID id;
        private String clave;
        private String valor;
        private TipoParametro tipoDato;
        private String descripcion;
        private LocalDateTime fechaModificacion;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            clave = UtilTexto.VACIO;
            valor = UtilTexto.VACIO;
            tipoDato = TipoParametro.TEXTO;
            descripcion = UtilTexto.VACIO;
            fechaModificacion = UtilFecha.FECHA_HORA_POR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder clave(String clave) {
            this.clave = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(clave);
            return this;
        }

        public Builder valor(String valor) {
            this.valor = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(valor);
            return this;
        }

        public Builder tipoDato(TipoParametro tipoDato) {
            this.tipoDato = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tipoDato, TipoParametro.TEXTO);
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(descripcion);
            return this;
        }

        public Builder fechaModificacion(LocalDateTime fechaModificacion) {
            this.fechaModificacion = UtilFecha.valorDefecto(fechaModificacion);
            return this;
        }

        public ParametroGlobalDominio build() {
            return new ParametroGlobalDominio(this);
        }
    }
}
