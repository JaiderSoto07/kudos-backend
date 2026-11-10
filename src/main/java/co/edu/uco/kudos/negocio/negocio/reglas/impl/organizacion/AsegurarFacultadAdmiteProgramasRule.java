package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarFacultadAdmiteProgramasRule implements Rule<FacultadDominio> {

    private static final AsegurarFacultadAdmiteProgramasRule INSTANCIA = new AsegurarFacultadAdmiteProgramasRule();

    private AsegurarFacultadAdmiteProgramasRule() {
    }

    public static AsegurarFacultadAdmiteProgramasRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(FacultadDominio... datos) {
        if (!datos[0].admiteProgramas()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.FACULTAD_NO_ADMITE_PROGRAMAS);
        }
    }
}
