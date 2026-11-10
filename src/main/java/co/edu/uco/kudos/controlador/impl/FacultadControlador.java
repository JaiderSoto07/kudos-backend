package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.FacultadDto;
import co.edu.uco.kudos.negocio.fachada.FacultadFachada;
import co.edu.uco.kudos.negocio.fachada.impl.FacultadFachadaImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/facultades")
public class FacultadControlador extends AbstractRestController {

    private final FacultadFachada fachada = new FacultadFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<FacultadDto>> consultar(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultar(obtenerIdActor(datosUsuario))));
    }

    @PostMapping
    public ResponseEntity<RespuestaOk<String>> registrar(@AuthenticationPrincipal OidcUser datosUsuario, @RequestBody FacultadDto datos) {
        fachada.registrar(obtenerIdActor(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.FACULTAD_REGISTRADA);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaOk<String>> modificar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody FacultadDto datos) {
        fachada.modificar(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.FACULTAD_MODIFICADA);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/inactivacion")
    public ResponseEntity<RespuestaOk<String>> inactivar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.inactivar(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.FACULTAD_INACTIVADA);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/coordinadores")
    public ResponseEntity<RespuestaOk<CargoDto>> consultarCoordinadores(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarCoordinadores(obtenerIdActor(datosUsuario), id)));
    }
}
