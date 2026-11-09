package co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarDatosCategoriaRule implements Rule<CategoriaDominio> {

    private static final int LONGITUD_MINIMA_NOMBRE = 3;
    private static final int LONGITUD_MAXIMA_NOMBRE = 50;
    private static final int LONGITUD_MINIMA_DESCRIPCION = 10;
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 250;

    private static final ValidarDatosCategoriaRule INSTANCIA = new ValidarDatosCategoriaRule();

    private ValidarDatosCategoriaRule() {
    }

    public static ValidarDatosCategoriaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(CategoriaDominio... datos) {
        var dominio = datos[0];

        validarObligatoriedadDatos(dominio);
        validarNombre(dominio.getNombre());
        validarDescripcion(dominio.getDescripcion());
    }

    private void validarObligatoriedadDatos(CategoriaDominio dominio) {
        if (UtilObjeto.esNulo(dominio)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.DATOS_CATEGORIA_OBLIGATORIOS);
        }
    }

    private void validarNombre(String nombre) {
        if (UtilTexto.getUtilTexto().esVacia(nombre)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(nombre, LONGITUD_MINIMA_NOMBRE, LONGITUD_MAXIMA_NOMBRE, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_LONGITUD_INVALIDA);
        }
    }

    private void validarDescripcion(String descripcion) {
        if (UtilTexto.getUtilTexto().esVacia(descripcion)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.DESCRIPCION_CATEGORIA_OBLIGATORIA);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(descripcion, LONGITUD_MINIMA_DESCRIPCION, LONGITUD_MAXIMA_DESCRIPCION, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.DESCRIPCION_CATEGORIA_LONGITUD_INVALIDA);
        }
    }
}
