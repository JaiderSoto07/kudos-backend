package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarFacultadExisteRule implements Rule<FacultadDominio> {

    private static final AsegurarFacultadExisteRule INSTANCIA = new AsegurarFacultadExisteRule();

    private AsegurarFacultadExisteRule() {
    }

    public static AsegurarFacultadExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(FacultadDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.FACULTAD_NO_EXISTE);
        }
    }
}
