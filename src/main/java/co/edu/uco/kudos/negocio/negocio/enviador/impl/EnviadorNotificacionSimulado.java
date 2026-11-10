package co.edu.uco.kudos.negocio.negocio.enviador.impl;

import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.enviador.EnviadorNotificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnviadorNotificacionSimulado implements EnviadorNotificacion {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviadorNotificacionSimulado.class);
    private static final String TIPO_REFERENCIA = "CodigoVerificacion";

    private final NotificacionNegocio notificacionNegocio;

    public EnviadorNotificacionSimulado(NotificacionNegocio notificacionNegocio) {
        this.notificacionNegocio = notificacionNegocio;
    }

    @Override
    public void enviarCodigo(CodigoVerificacionDominio codigo, String mensaje) {
        LOGGER.info("Envío simulado por {}: {}", codigo.getCanal(), mensaje);

        var notificacion = new NotificacionDominio.Builder()
                .destinatario(codigo.getUsuario())
                .tipo(TipoNotificacion.CODIGO_VERIFICACION)
                .mensaje(mensaje)
                .tipoReferencia(TIPO_REFERENCIA)
                .idReferencia(codigo.getId())
                .build();
        notificacionNegocio.notificar(notificacion);
    }
}
