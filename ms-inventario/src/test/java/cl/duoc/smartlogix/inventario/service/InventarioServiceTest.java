package cl.duoc.smartlogix.inventario.service;

import cl.duoc.smartlogix.inventario.dto.InventarioDTO;
import cl.duoc.smartlogix.inventario.exception.ProductoNotFoundException;
import cl.duoc.smartlogix.inventario.model.Producto;
import cl.duoc.smartlogix.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - InventarioService")
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto productoMock;

    @BeforeEach
    void setUp() {
        productoMock = new Producto(1L, "PROD-001", "Laptop Dell", 50, 10, 1L, 999.99);
    }

    // ── listarTodos ────────────────────────────────────────────────────────

    @Test
    @DisplayName("listarTodos: debe retornar lista de productos correctamente")
    void listarTodos_debeRetornarLista() {
        Producto p2 = new Producto(2L, "PROD-002", "Mouse Logitech", 100, 20, 1L, 29.99);
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(productoMock, p2));

        List<InventarioDTO.ProductoResponse> resultado = inventarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("PROD-001", resultado.get(0).getCodigo());
        assertEquals("PROD-002", resultado.get(1).getCodigo());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodos: lista vacía cuando no hay productos")
    void listarTodos_listaVacia() {
        when(inventarioRepository.findAll()).thenReturn(List.of());

        List<InventarioDTO.ProductoResponse> resultado = inventarioService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ── obtenerPorId ───────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId: retorna producto cuando existe")
    void obtenerPorId_productoExiste() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        InventarioDTO.ProductoResponse resultado = inventarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop Dell", resultado.getNombre());
        assertFalse(resultado.getBajoStock()); // 50 >= 10
    }

    @Test
    @DisplayName("obtenerPorId: lanza excepción cuando no existe")
    void obtenerPorId_productoNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class,
                () -> inventarioService.obtenerPorId(99L));
    }

    // ── obtenerStock ───────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerStock: retorna stock correcto")
    void obtenerStock_retornaStockCorrecto() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        InventarioDTO.StockResponse resultado = inventarioService.obtenerStock(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getProductoId());
        assertEquals(50, resultado.getStockActual());
        assertEquals("Stock disponible", resultado.getMensaje());
    }

    @Test
    @DisplayName("obtenerStock: lanza excepción si producto no existe")
    void obtenerStock_productoNoExiste() {
        when(inventarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductoNotFoundException.class,
                () -> inventarioService.obtenerStock(99L));
    }

    // ── crearProducto ──────────────────────────────────────────────────────

    @Test
    @DisplayName("crearProducto: crea producto exitosamente")
    void crearProducto_exitoso() {
        InventarioDTO.ProductoRequest request = new InventarioDTO.ProductoRequest(
                "PROD-003", "Teclado", 30, 5, 1L, 49.99);
        Producto guardado = new Producto(3L, "PROD-003", "Teclado", 30, 5, 1L, 49.99);

        when(inventarioRepository.existsByCodigo("PROD-003")).thenReturn(false);
        when(inventarioRepository.save(any(Producto.class))).thenReturn(guardado);

        InventarioDTO.ProductoResponse resultado = inventarioService.crearProducto(request);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("PROD-003", resultado.getCodigo());
        verify(inventarioRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("crearProducto: lanza excepción si código ya existe")
    void crearProducto_codigoDuplicado() {
        InventarioDTO.ProductoRequest request = new InventarioDTO.ProductoRequest(
                "PROD-001", "Laptop", 10, 2, 1L, 100.0);
        when(inventarioRepository.existsByCodigo("PROD-001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.crearProducto(request));
        verify(inventarioRepository, never()).save(any());
    }

    // ── actualizarStock ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarStock ENTRADA: suma correctamente al stock")
    void actualizarStock_entrada() {
        InventarioDTO.StockUpdateRequest request = new InventarioDTO.StockUpdateRequest(1L, 20, "ENTRADA");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(inventarioRepository.save(any(Producto.class))).thenReturn(productoMock);

        InventarioDTO.StockResponse resultado = inventarioService.actualizarStock(request);

        assertEquals(70, resultado.getStockActual()); // 50 + 20
        assertEquals("Stock actualizado correctamente", resultado.getMensaje());
    }

    @Test
    @DisplayName("actualizarStock SALIDA: resta correctamente del stock")
    void actualizarStock_salida() {
        InventarioDTO.StockUpdateRequest request = new InventarioDTO.StockUpdateRequest(1L, 10, "SALIDA");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(inventarioRepository.save(any(Producto.class))).thenReturn(productoMock);

        InventarioDTO.StockResponse resultado = inventarioService.actualizarStock(request);

        assertEquals(40, resultado.getStockActual()); // 50 - 10
    }

    @Test
    @DisplayName("actualizarStock SALIDA: lanza excepción por stock insuficiente")
    void actualizarStock_stockInsuficiente() {
        InventarioDTO.StockUpdateRequest request = new InventarioDTO.StockUpdateRequest(1L, 100, "SALIDA");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.actualizarStock(request));
    }

    @Test
    @DisplayName("actualizarStock: lanza excepción por operación inválida")
    void actualizarStock_operacionInvalida() {
        InventarioDTO.StockUpdateRequest request = new InventarioDTO.StockUpdateRequest(1L, 5, "INVALIDA");
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        assertThrows(IllegalArgumentException.class,
                () -> inventarioService.actualizarStock(request));
    }

    // ── listarBajoStock ────────────────────────────────────────────────────

    @Test
    @DisplayName("listarBajoStock: filtra correctamente productos con bajo stock")
    void listarBajoStock_filtraCorrectamente() {
        Producto bajStock = new Producto(2L, "PROD-002", "Impresora", 3, 10, 1L, 150.0); // 3 < 10
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(productoMock, bajStock));

        List<InventarioDTO.ProductoResponse> resultado = inventarioService.listarBajoStock();

        assertEquals(1, resultado.size());
        assertEquals("PROD-002", resultado.get(0).getCodigo());
        assertTrue(resultado.get(0).getBajoStock());
    }

    // ── eliminarProducto ───────────────────────────────────────────────────

    @Test
    @DisplayName("eliminarProducto: elimina correctamente cuando existe")
    void eliminarProducto_exitoso() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inventarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> inventarioService.eliminarProducto(1L));
        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarProducto: lanza excepción si no existe")
    void eliminarProducto_noExiste() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNotFoundException.class,
                () -> inventarioService.eliminarProducto(99L));
        verify(inventarioRepository, never()).deleteById(anyLong());
    }

    // ── bajoStock flag ─────────────────────────────────────────────────────

    @Test
    @DisplayName("bajoStock: flag true cuando stock < mínimo")
    void bajoStock_flagVerdadero() {
        Producto pBajo = new Producto(5L, "PROD-005", "Cable USB", 2, 10, 1L, 5.0);
        when(inventarioRepository.findById(5L)).thenReturn(Optional.of(pBajo));

        InventarioDTO.ProductoResponse resultado = inventarioService.obtenerPorId(5L);

        assertTrue(resultado.getBajoStock());
    }

    @Test
    @DisplayName("bajoStock: flag false cuando stock >= mínimo")
    void bajoStock_flagFalso() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        InventarioDTO.ProductoResponse resultado = inventarioService.obtenerPorId(1L);

        assertFalse(resultado.getBajoStock());
    }
}
