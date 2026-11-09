package co.edu.uco.kudos.negocio.negocio.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.assembler.EntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioEntidadAssembler implements EntidadAssembler<UsuarioDominio, UsuarioEntidad> {

    private static final EntidadAssembler<UsuarioDominio, UsuarioEntidad> INSTANCIA = new UsuarioEntidadAssembler();

    private UsuarioEntidadAssembler() {
    }

    public static EntidadAssembler<UsuarioDominio, UsuarioEntidad> getInstance() {
        return INSTANCIA;
    }

    @Override
    public UsuarioEntidad convertirAEntidad(UsuarioDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new UsuarioDominio.Builder().build());
        return new UsuarioEntidad.Builder()
                .id(dominioSeguro.getId())
                .nombres(dominioSeguro.getNombres())
                .apellidos(dominioSeguro.getApellidos())
                .correoInstitucional(dominioSeguro.getCorreoInstitucional())
                .correoVerificado(dominioSeguro.isCorreoVerificado())
                .numeroCelular(dominioSeguro.getNumeroCelular())
                .celularVerificado(dominioSeguro.isCelularVerificado())
                .contrasena(dominioSeguro.getContrasena())
                .aprobada(dominioSeguro.isAprobada())
                .estadoCuenta(UtilEnum.obtenerTexto(dominioSeguro.getEstadoCuenta()))
                .fechaRegistro(dominioSeguro.getFechaRegistro())
                .build();
    }

    @Override
    public UsuarioDominio convertirADominio(UsuarioEntidad entidad) {
        var entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(entidad, new UsuarioEntidad.Builder().build());
        return new UsuarioDominio.Builder()
                .id(entidadSegura.getId())
                .nombres(entidadSegura.getNombres())
                .apellidos(entidadSegura.getApellidos())
                .correoInstitucional(entidadSegura.getCorreoInstitucional())
                .correoVerificado(entidadSegura.isCorreoVerificado())
                .numeroCelular(entidadSegura.getNumeroCelular())
                .celularVerificado(entidadSegura.isCelularVerificado())
                .contrasena(entidadSegura.getContrasena())
                .aprobada(entidadSegura.isAprobada())
                .estadoCuenta(UtilEnum.obtenerValor(EstadoCuenta.class, entidadSegura.getEstadoCuenta(), EstadoCuenta.PENDIENTE_APROBACION))
                .fechaRegistro(entidadSegura.getFechaRegistro())
                .build();
    }

    @Override
    public List<UsuarioDominio> convertirADominio(List<UsuarioEntidad> listaEntidades) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaEntidades, new ArrayList<UsuarioEntidad>());
        var listaDominio = new ArrayList<UsuarioDominio>();
        for (var entidad : listaSegura) {
            listaDominio.add(convertirADominio(entidad));
        }
        return listaDominio;
    }
}
