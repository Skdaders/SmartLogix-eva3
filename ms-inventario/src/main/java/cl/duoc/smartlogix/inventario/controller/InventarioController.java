package cl.duoc.smartlogix.inventario.controller;

import cl.duoc.smartlogix.inventario.dto.InventarioDTO;
import cl.duoc.smartlogix.inventario.service.InventarioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Gestión de productos e inventario SmartLogix")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/productos")
    @Operation(summary = "Listar todos los productos")
    @CircuitBreaker(name = "inventarioService", fallbackMethod = "fallbackListar")
    public ResponseEntity<List<InventarioDTO.ProductoResponse>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/productos/{id}")
    @Operation(summary = "Obtener producto por ID")
    @CircuitBreaker(name = "inventarioService", fallbackMethod = "fallbackProducto")
    public ResponseEntity<InventarioDTO.ProductoResponse> obtenerProducto(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @GetMapping("/stock/{productoId}")
    @Operation(summary = "Consultar stock de un producto")
    @CircuitBreaker(name = "inventarioService", fallbackMethod = "fallbackStock")
    public ResponseEntity<InventarioDTO.StockResponse> consultarStock(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerStock(productoId));
    }

    @PostMapping("/productos")
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<InventarioDTO.ProductoResponse> crearProducto(
            @RequestBody InventarioDTO.ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.crearProducto(request));
    }

    @PutMapping("/stock")
    @Operation(summary = "Actualizar stock (ENTRADA o SALIDA)")
    public ResponseEntity<InventarioDTO.StockResponse> actualizarStock(
            @RequestBody InventarioDTO.StockUpdateRequest request) {
        return ResponseEntity.ok(inventarioService.actualizarStock(request));
    }

    @GetMapping("/bajo-stock")
    @Operation(summary = "Listar productos con stock bajo el mínimo")
    public ResponseEntity<List<InventarioDTO.ProductoResponse>> listarBajoStock() {
        return ResponseEntity.ok(inventarioService.listarBajoStock());
    }

    @DeleteMapping("/productos/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<InventarioDTO.ProductoResponse>> fallbackListar(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<InventarioDTO.ProductoResponse> fallbackProducto(Long id, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<InventarioDTO.StockResponse> fallbackStock(Long productoId, Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new InventarioDTO.StockResponse(productoId, -1, "Servicio no disponible temporalmente"));
    }
}