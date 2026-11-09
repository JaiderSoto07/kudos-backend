package co.edu.uco.kudos.dominio;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDominioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class UsuarioDominio {

    private final UUID id;
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private boolean correoVerificado;
    private String numeroCelular;
    private boolean celularVerificado;
    private final String contrasena;
    private boolean aprobada;
    private EstadoCuenta estadoCuenta;
    private final LocalDateTime fechaRegistro;

    private UsuarioDominio(Builder builder) {
        this.id = builder.id;
        this.nombres = builder.nombres;
        this.apellidos = builder.apellidos;
        this.correoInstitucional = builder.correoInstitucional;
        this.correoVerificado = builder.correoVerificado;
        this.numeroCelular = builder.numeroCelular;
        this.celularVerificado = builder.celularVerificado;
        this.contrasena = builder.contrasena;
        this.aprobada = builder.aprobada;
        this.estadoCuenta = builder.estadoCuenta;
        this.fechaRegistro = builder.fechaRegistro;
    }

    public void aprobar() {
        if (estadoCuenta != EstadoCuenta.PENDIENTE_APROBACION) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.USUARIO_NO_ESTA_PENDIENTE_DE_APROBACION);
        }
        aprobada = true;
        estadoCuenta = EstadoCuenta.PENDIENTE_VERIFICACION;
        intentarActivar();
    }

    public void verificarCorreo() {
        correoVerificado = true;
        intentarActivar();
    }

    public void verificarCelular() {
        celularVerificado = true;
        intentarActivar();
    }

    public boolean intentarActivar() {
        if (estadoCuenta == EstadoCuenta.PENDIENTE_VERIFICACION && aprobada && correoVerificado && celularVerificado) {
            estadoCuenta = EstadoCuenta.ACTIVA;
        }
        return estaActiva();
    }

    public boolean estaActiva() {
        return estadoCuenta == EstadoCuenta.ACTIVA;
    }

    public void actualizarDatosPersonales(String nuevosNombres, String nuevosApellidos) {
        nombres = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevosNombres);
        apellidos = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevosApellidos);
    }

    public void cambiarCorreo(String nuevoCorreo) {
        correoInstitucional = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoCorreo).toLowerCase();
        correoVerificado = false;
        volverAPendienteDeVerificacion();
    }

    public void cambiarCelular(String nuevoCelular) {
        numeroCelular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nuevoCelular);
        celularVerificado = false;
        volverAPendienteDeVerificacion();
    }

    public void suspender() {
        if (estadoCuenta == EstadoCuenta.SUSPENDIDA || estadoCuenta == EstadoCuenta.DESACTIVADA) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.USUARIO_YA_SUSPENDIDO_O_DESACTIVADO);
        }
        estadoCuenta = EstadoCuenta.SUSPENDIDA;
    }

    public void reactivar() {
        if (estadoCuenta != EstadoCuenta.SUSPENDIDA) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.USUARIO_NO_ESTA_SUSPENDIDO);
        }
        if (!aprobada) {
            estadoCuenta = EstadoCuenta.PENDIENTE_APROBACION;
        } else if (correoVerificado && celularVerificado) {
            estadoCuenta = EstadoCuenta.ACTIVA;
        } else {
            estadoCuenta = EstadoCuenta.PENDIENTE_VERIFICACION;
        }
    }

    public void desactivar() {
        if (estadoCuenta == EstadoCuenta.DESACTIVADA) {
            throw KudosDominioExcepcion.crear(CatalogoMensajes.Dominio.USUARIO_YA_DESACTIVADO);
        }
        estadoCuenta = EstadoCuenta.DESACTIVADA;
    }

    public String nombreCompleto() {
        return nombres + " " + apellidos;
    }

    private void volverAPendienteDeVerificacion() {
        if (estadoCuenta == EstadoCuenta.ACTIVA) {
            estadoCuenta = EstadoCuenta.PENDIENTE_VERIFICACION;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public boolean isCorreoVerificado() {
        return correoVerificado;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public boolean isCelularVerificado() {
        return celularVerificado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public static final class Builder {

        private UUID id;
        private String nombres;
        private String apellidos;
        private String correoInstitucional;
        private boolean correoVerificado;
        private String numeroCelular;
        private boolean celularVerificado;
        private String contrasena;
        private boolean aprobada;
        private EstadoCuenta estadoCuenta;
        private LocalDateTime fechaRegistro;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombres = UtilTexto.VACIO;
            apellidos = UtilTexto.VACIO;
            correoInstitucional = UtilTexto.VACIO;
            correoVerificado = false;
            numeroCelular = UtilTexto.VACIO;
            celularVerificado = false;
            contrasena = UtilTexto.VACIO;
            aprobada = false;
            estadoCuenta = EstadoCuenta.PENDIENTE_APROBACION;
            fechaRegistro = UtilFecha.FECHA_HORA_POR_DEFECTO;
        }

        public Builder id(UUID id) {
            this.id = UtilId.valorDefecto(id);
            return this;
        }

        public Builder nombres(String nombres) {
            this.nombres = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(nombres);
            return this;
        }

        public Builder apellidos(String apellidos) {
            this.apellidos = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(apellidos);
            return this;
        }

        public Builder correoInstitucional(String correoInstitucional) {
            this.correoInstitucional = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correoInstitucional);
            return this;
        }

        public Builder correoVerificado(boolean correoVerificado) {
            this.correoVerificado = correoVerificado;
            return this;
        }

        public Builder numeroCelular(String numeroCelular) {
            this.numeroCelular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(numeroCelular);
            return this;
        }

        public Builder celularVerificado(boolean celularVerificado) {
            this.celularVerificado = celularVerificado;
            return this;
        }

        public Builder contrasena(String contrasena) {
            this.contrasena = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(contrasena);
            return this;
        }

        public Builder aprobada(boolean aprobada) {
            this.aprobada = aprobada;
            return this;
        }

        public Builder estadoCuenta(EstadoCuenta estadoCuenta) {
            this.estadoCuenta = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoCuenta, EstadoCuenta.PENDIENTE_APROBACION);
            return this;
        }

        public Builder fechaRegistro(LocalDateTime fechaRegistro) {
            this.fechaRegistro = UtilFecha.valorDefecto(fechaRegistro);
            return this;
        }

        public UsuarioDominio build() {
            return new UsuarioDominio(this);
        }
    }
}
