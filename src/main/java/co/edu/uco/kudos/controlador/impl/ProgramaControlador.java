package co.edu.uco.kudos.controlador.impl;

import co.edu.uco.kudos.controlador.AbstractRestController;
import co.edu.uco.kudos.controlador.respuesta.RespuestaOk;
import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.ProgramaAcademicoDto;
import co.edu.uco.kudos.dto.VinculoDto;
import co.edu.uco.kudos.negocio.fachada.ProgramaFachada;
import co.edu.uco.kudos.negocio.fachada.VinculacionFachada;
import co.edu.uco.kudos.negocio.fachada.impl.ProgramaFachadaImpl;
import co.edu.uco.kudos.negocio.fachada.impl.VinculacionFachadaImpl;
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
@RequestMapping("/api/v1/programas")
public class ProgramaControlador extends AbstractRestController {

    private final ProgramaFachada fachada = new ProgramaFachadaImpl();
    private final VinculacionFachada vinculacionFachada = new VinculacionFachadaImpl();

    @GetMapping
    public ResponseEntity<RespuestaOk<ProgramaAcademicoDto>> consultar(@AuthenticationPrincipal OidcUser datosUsuario) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultar(obtenerIdActor(datosUsuario))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaOk<ProgramaAcademicoDto>> consultarPorId(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarPorId(obtenerIdActor(datosUsuario), id)));
    }

    @PostMapping
    public ResponseEntity<RespuestaOk<String>> registrar(@AuthenticationPrincipal OidcUser datosUsuario,
            @RequestBody ProgramaAcademicoDto datos) {
        fachada.registrar(obtenerIdActor(datosUsuario), datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.PROGRAMA_REGISTRADO);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespuestaOk<String>> modificar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody ProgramaAcademicoDto datos) {
        fachada.modificar(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.PROGRAMA_MODIFICADO);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/{id}/inactivacion")
    public ResponseEntity<RespuestaOk<String>> inactivar(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        fachada.inactivar(obtenerIdActor(datosUsuario), id);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.PROGRAMA_INACTIVADO);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/coordinadores")
    public ResponseEntity<RespuestaOk<CargoDto>> consultarCoordinadores(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(fachada.consultarCoordinadores(obtenerIdActor(datosUsuario), id)));
    }

    @GetMapping("/{id}/vinculos")
    public ResponseEntity<RespuestaOk<VinculoDto>> consultarVinculos(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id) {
        return ResponseEntity.ok(RespuestaOk.crear(vinculacionFachada.consultarDePrograma(obtenerIdActor(datosUsuario), id)));
    }

    @PostMapping("/{id}/vinculos")
    public ResponseEntity<RespuestaOk<String>> vincular(@AuthenticationPrincipal OidcUser datosUsuario, @PathVariable UUID id,
            @RequestBody VinculoDto datos) {
        vinculacionFachada.vincular(obtenerIdActor(datosUsuario), id, datos);
        RespuestaOk<String> respuesta = RespuestaOk.crear(CatalogoMensajes.OrganizacionControlador.PERSONA_VINCULADA);
        return ResponseEntity.ok(respuesta);
    }
}
