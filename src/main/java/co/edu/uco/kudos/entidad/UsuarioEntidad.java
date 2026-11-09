package co.edu.uco.kudos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.time.LocalDateTime;
import java.util.UUID;

public final class UsuarioEntidad {

    private final UUID id;
    private final String nombres;
    private final String apellidos;
    private final String correoInstitucional;
    private final boolean correoVerificado;
    private final String numeroCelular;
    private final boolean celularVerificado;
    private final boolean aprobada;
    private final String estadoCuenta;
    private final LocalDateTime fechaRegistro;

    private UsuarioEntidad(Builder builder) {
        this.id = builder.id;
        this.nombres = builder.nombres;
        this.apellidos = builder.apellidos;
        this.correoInstitucional = builder.correoInstitucional;
        this.correoVerificado = builder.correoVerificado;
        this.numeroCelular = builder.numeroCelular;
        this.celularVerificado = builder.celularVerificado;
        this.aprobada = builder.aprobada;
        this.estadoCuenta = builder.estadoCuenta;
        this.fechaRegistro = builder.fechaRegistro;
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

    public boolean isAprobada() {
        return aprobada;
    }

    public String getEstadoCuenta() {
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
        private boolean aprobada;
        private String estadoCuenta;
        private LocalDateTime fechaRegistro;

        public Builder() {
            id = UtilId.VALOR_DEFECTO;
            nombres = UtilTexto.VACIO;
            apellidos = UtilTexto.VACIO;
            correoInstitucional = UtilTexto.VACIO;
            correoVerificado = false;
            numeroCelular = UtilTexto.VACIO;
            celularVerificado = false;
            aprobada = false;
            estadoCuenta = UtilTexto.VACIO;
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

        public Builder aprobada(boolean aprobada) {
            this.aprobada = aprobada;
            return this;
        }

        public Builder estadoCuenta(String estadoCuenta) {
            this.estadoCuenta = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(estadoCuenta);
            return this;
        }

        public Builder fechaRegistro(LocalDateTime fechaRegistro) {
            this.fechaRegistro = UtilFecha.valorDefecto(fechaRegistro);
            return this;
        }

        public UsuarioEntidad build() {
            return new UsuarioEntidad(this);
        }
    }
}
