package co.edu.uco.kudos.negocio.negocio.enviador;

import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;

public interface EnviadorNotificacion {

    void enviarCodigo(CodigoVerificacionDominio codigo, String mensaje);
}
