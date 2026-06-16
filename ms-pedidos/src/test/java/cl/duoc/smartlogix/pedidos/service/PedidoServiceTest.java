package cl.duoc.smartlogix.pedidos.service;

import cl.duoc.smartlogix.pedidos.dto.PedidoDTO;
import cl.duoc.smartlogix.pedidos.factory.PedidoFactory;
import cl.duoc.smartlogix.pedidos.model.Pedido;
import cl.duoc.smartlogix.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - PedidoService y Factory Method")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        pedidoMock = new Pedido(1L, "CLI-001", 1L, 2, "NORMAL",
                199.98, 5, Pedido.EstadoPedido.PENDIENTE, LocalDateTime.now());
    }

    // ── Factory Method: Pedido Normal ──────────────────────────────────────

    @Test
    @DisplayName("Factory NORMAL: precio sin recargo, 5 días entrega")
    void factory_pedidoNormal() {
        Pedido p = PedidoFactory.fabricar("NORMAL", "CLI-001", 1L, 2, 100.0);
        assertEquals("NORMAL", p.getTipo());
        assertEquals(200.0, p.getPrecioTotal());   // 100 * 2 sin recargo
        assertEquals(5, p.getDiasEntrega());
        assertEquals(Pedido.EstadoPedido.PENDIENTE, p.getEstado());
    }

    @Test
    @DisplayName("Factory URGENTE: +15% recargo, 1 día entrega")
    void factory_pedidoUrgente() {
        Pedido p = PedidoFactory.fabricar("URGENTE", "CLI-002", 2L, 1, 100.0);
        assertEquals("URGENTE", p.getTipo());
        assertEquals(115.0, p.getPrecioTotal());   // 100 * 1 * 1.15
        assertEquals(1, p.getDiasEntrega());
    }

    @Test
    @DisplayName("Factory MAYORISTA: -20% descuento, 7 días entrega")
    void factory_pedidoMayorista() {
        Pedido p = PedidoFactory.fabricar("MAYORISTA", "CLI-003", 3L, 10, 100.0);
        assertEquals("MAYORISTA", p.getTipo());
        assertEquals(800.0, p.getPrecioTotal());   // 100 * 10 * 0.80
        assertEquals(7, p.getDiasEntrega());
    }

    @Test
    @DisplayName("Factory: lanza excepción con tipo desconocido")
    void factory_tipoDesconocido() {
        assertThrows(IllegalArgumentException.class,
                () -> PedidoFactory.fabricar("EXPRESS", "CLI-001", 1L, 1, 100.0));
    }

    @Test
    @DisplayName("Factory: tipo en minúsculas funciona correctamente")
    void factory_tipoMinusculas() {
        assertDoesNotThrow(() -> PedidoFactory.fabricar("normal", "CLI-001", 1L, 1, 50.0));
    }

    // ── PedidoService: crearPedido ─────────────────────────────────────────

    @Test
    @DisplayName("crearPedido: crea y persiste pedido NORMAL")
    void crearPedido_normal() {
        PedidoDTO.PedidoRequest request = new PedidoDTO.PedidoRequest(
                "CLI-001", 1L, 2, "NORMAL", 99.99);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        PedidoDTO.PedidoResponse response = pedidoService.crearPedido(request);

        assertNotNull(response);
        assertEquals("CLI-001", response.getClienteId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("crearPedido: tipo inválido lanza excepción antes de persistir")
    void crearPedido_tipoInvalido() {
        PedidoDTO.PedidoRequest request = new PedidoDTO.PedidoRequest(
                "CLI-001", 1L, 1, "INVALIDO", 50.0);

        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.crearPedido(request));
        verify(pedidoRepository, never()).save(any());
    }

    // ── PedidoService: listarTodos ─────────────────────────────────────────

    @Test
    @DisplayName("listarTodos: retorna lista correctamente")
    void listarTodos_retornaLista() {
        Pedido p2 = new Pedido(2L, "CLI-002", 2L, 5, "MAYORISTA",
                400.0, 7, Pedido.EstadoPedido.CONFIRMADO, LocalDateTime.now());
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoMock, p2));

        List<PedidoDTO.PedidoResponse> resultado = pedidoService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("NORMAL", resultado.get(0).getTipo());
        assertEquals("MAYORISTA", resultado.get(1).getTipo());
    }

    // ── PedidoService: obtenerPorId ────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId: retorna pedido cuando existe")
    void obtenerPorId_existe() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        PedidoDTO.PedidoResponse response = pedidoService.obtenerPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("obtenerPorId: lanza excepción cuando no existe")
    void obtenerPorId_noExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.obtenerPorId(99L));
    }

    // ── PedidoService: cambiarEstado ───────────────────────────────────────

    @Test
    @DisplayName("cambiarEstado: cambia estado correctamente")
    void cambiarEstado_exitoso() {
        PedidoDTO.CambioEstadoRequest req = new PedidoDTO.CambioEstadoRequest(
                Pedido.EstadoPedido.CONFIRMADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));
        when(pedidoRepository.save(any())).thenReturn(pedidoMock);

        PedidoDTO.PedidoResponse response = pedidoService.cambiarEstado(1L, req);

        assertNotNull(response);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    // ── PedidoService: cancelarPedido ──────────────────────────────────────

    @Test
    @DisplayName("cancelarPedido: cancela pedido PENDIENTE")
    void cancelarPedido_pendiente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));
        when(pedidoRepository.save(any())).thenReturn(pedidoMock);

        assertDoesNotThrow(() -> pedidoService.cancelarPedido(1L));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("cancelarPedido: no se puede cancelar pedido ENVIADO")
    void cancelarPedido_enviado() {
        pedidoMock.setEstado(Pedido.EstadoPedido.ENVIADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        assertThrows(IllegalStateException.class, () -> pedidoService.cancelarPedido(1L));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancelarPedido: no se puede cancelar pedido ENTREGADO")
    void cancelarPedido_entregado() {
        pedidoMock.setEstado(Pedido.EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        assertThrows(IllegalStateException.class, () -> pedidoService.cancelarPedido(1L));
    }

    // ── Precios Factory ────────────────────────────────────────────────────

    @Test
    @DisplayName("Precio URGENTE es mayor que NORMAL para la misma cantidad")
    void precio_urgenteMayorQueNormal() {
        Pedido normal   = PedidoFactory.fabricar("NORMAL",   "C1", 1L, 3, 100.0);
        Pedido urgente  = PedidoFactory.fabricar("URGENTE",  "C1", 1L, 3, 100.0);
        assertTrue(urgente.getPrecioTotal() > normal.getPrecioTotal());
    }

    @Test
    @DisplayName("Precio MAYORISTA es menor que NORMAL para la misma cantidad")
    void precio_mayoristasMenorQueNormal() {
        Pedido normal    = PedidoFactory.fabricar("NORMAL",    "C1", 1L, 10, 100.0);
        Pedido mayorista = PedidoFactory.fabricar("MAYORISTA", "C1", 1L, 10, 100.0);
        assertTrue(mayorista.getPrecioTotal() < normal.getPrecioTotal());
    }
}
