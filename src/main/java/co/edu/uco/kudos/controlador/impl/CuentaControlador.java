package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CuentaDto;
import co.edu.uco.kudos.negocio.fachada.CuentaFachada;
import co.edu.uco.kudos.negocio.fachada.impl.CuentaFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cuenta")
public class CuentaControlador extends AbstractRestController {

    private final CuentaFachada fachada = new CuentaFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<CuentaDto>> consultarCuenta(@AuthenticationPrincipal OidcUser datosUsuario) {
        var cuenta = fachada.consultarCuenta(obtenerIdUsuarioRegistrado(datosUsuario));
        return ResponseEntity.ok(RespuestaOk.crear(cuenta));
    }

    @PutMapping("/datos-personales")
    public ResponseEntity<RespuestaOk<String>> actualizarDatosPersonales(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody CuentaDto datos) {
        fachada.actualizarDatosPersonales(obtenerIdUsuarioRegistrado(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.DATOS_PERSONALES_ACTUALIZADOS);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/celular")
    public ResponseEntity<RespuestaOk<String>> cambiarCelular(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody CuentaDto datos) {
        fachada.cambiarCelular(obtenerIdUsuarioRegistrado(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.CELULAR_CAMBIADO);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/correo")
    public ResponseEntity<RespuestaOk<String>> cambiarCorreo(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody CuentaDto datos) {
        fachada.cambiarCorreo(obtenerIdUsuarioRegistrado(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.IdentidadControlador.CORREO_CAMBIADO);
        return ResponseEntity.ok(respuesta);
    }
}
