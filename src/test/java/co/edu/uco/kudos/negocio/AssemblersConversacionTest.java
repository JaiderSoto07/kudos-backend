package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CalificacionDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.CalificacionDto;
import co.edu.uco.kudos.dto.ReplicaDto;
import co.edu.uco.kudos.dto.ValoracionesDto;
import co.edu.uco.kudos.entidad.CalificacionEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ConversacionDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ReplicaDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ValoracionesDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CalificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores de réplicas, calificaciones, conversación y valoraciones (M5)")
class AssemblersConversacionTest {

    @Test
    @DisplayName("La réplica y la calificación pasan de dominio a entidad y de vuelta sin perder datos; los nulos dan valores por defecto")
    void entidades() {
        var replica = new ReplicaDominio.Builder()
                .id(UtilId.generarId())
                .autor(new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Ana").apellidos("Pérez").build())
                .kudo(new KudoDominio.Builder().id(UtilId.generarId()).build())
                .idReplicaPadre(UtilId.generarId())
                .contenido("Gracias")
                .fechaCreacion(UtilFecha.fechaActual())
                .estado(EstadoContenido.VISIBLE)
                .promedioCalificacion(new BigDecimal("4.50"))
                .cantidadCalificaciones(2)
                .build();
        var replicaDeVuelta = ReplicaEntidadAssembler.getInstance().convertirADominio(ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
        assertEquals(replica.getId(), replicaDeVuelta.getId());
        assertEquals(replica.getKudo().getId(), replicaDeVuelta.getKudo().getId());
        assertEquals(replica.getIdReplicaPadre(), replicaDeVuelta.getIdReplicaPadre());
        assertEquals(EstadoContenido.VISIBLE, replicaDeVuelta.getEstado());
        assertEquals(new BigDecimal("4.50"), replicaDeVuelta.getPromedioCalificacion());
        assertEquals(2, replicaDeVuelta.getCantidadCalificaciones());

        var calificacion = new CalificacionDominio.Builder()
                .id(UtilId.generarId())
                .autor(new UsuarioDominio.Builder().id(UtilId.generarId()).build())
                .replica(replica)
                .estrellas(4)
                .fechaCalificacion(UtilFecha.fechaActual())
                .activa(true)
                .build();
        var calificacionDeVuelta = CalificacionEntidadAssembler.getInstance().convertirADominio(CalificacionEntidadAssembler.getInstance().convertirAEntidad(calificacion));
        assertEquals(calificacion.getId(), calificacionDeVuelta.getId());
        assertEquals(replica.getId(), calificacionDeVuelta.getReplica().getId());
        assertFalse(calificacionDeVuelta.esSobreKudo());
        assertEquals(4, calificacionDeVuelta.getEstrellas());
        assertTrue(calificacionDeVuelta.isActiva());

        assertTrue(UtilId.esValorDefecto(ReplicaEntidadAssembler.getInstance().convertirADominio((ReplicaEntidad) null).getId()));
        assertTrue(UtilId.esValorDefecto(CalificacionEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(ReplicaEntidadAssembler.getInstance().convertirADominio((List<ReplicaEntidad>) null).isEmpty());
        assertTrue(CalificacionEntidadAssembler.getInstance().convertirADominio((List<CalificacionEntidad>) null).isEmpty());
    }

    @Test
    @DisplayName("El DTO de réplica lleva solo el contenido de entrada; una lista o un árbol nulos quedan vacíos")
    void dtos() {
        var dto = new ReplicaDto();
        dto.setContenido("  Hola  ");
        dto.setRespuestas(null);
        var dominio = ReplicaDtoAssembler.getInstance().convertirADominio(dto);
        assertEquals("Hola", dominio.getContenido());
        assertTrue(dto.getRespuestas().isEmpty());
        assertTrue(UtilId.esValorDefecto(ReplicaDtoAssembler.getInstance().convertirADTO((ReplicaDominio) null).getId()));
        assertTrue(ReplicaDtoAssembler.getInstance().convertirADTO((List<ReplicaDominio>) null).isEmpty());
        assertTrue(ConversacionDtoAssembler.getInstance().armarArbol(null).isEmpty());
        assertEquals(0, ValoracionesDtoAssembler.getInstance().armar(null, UtilId.generarId()).getCantidad());

        var calificacion = new CalificacionDto();
        assertEquals(0, calificacion.getEstrellas());
        calificacion.setEstrellas(4);
        assertEquals(4, calificacion.getEstrellas());

        var vacia = new ReplicaDto();
        vacia.setVetada(true);
        vacia.setPromedioCalificacion(null);
        vacia.setCantidadCalificaciones(-3);
        assertTrue(vacia.isVetada());
        assertEquals(BigDecimal.ZERO, vacia.getPromedioCalificacion());
        assertEquals(0, vacia.getCantidadCalificaciones());
        assertTrue(UtilId.esValorDefecto(vacia.getIdKudo()));
        assertTrue(UtilId.esValorDefecto(vacia.getIdAutor()));
        assertTrue(vacia.getNombreAutor().isEmpty());
        assertTrue(vacia.getEstado().isEmpty());
        assertTrue(UtilFecha.esValorDefecto(vacia.getFechaCreacion()));

        var valoraciones = new ValoracionesDto();
        valoraciones.setPromedio(null);
        valoraciones.setDistribucion(null);
        assertEquals(BigDecimal.ZERO, valoraciones.getPromedio());
        assertTrue(valoraciones.getDistribucion().isEmpty());
    }
}
