package cl.duoc.smartlogix.pedidos.controller;

import cl.duoc.smartlogix.pedidos.dto.PedidoDTO;
import cl.duoc.smartlogix.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos SmartLogix")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Crear pedido (NORMAL / URGENTE / MAYORISTA)")
    public ResponseEntity<PedidoDTO.PedidoResponse> crearPedido(
            @RequestBody PedidoDTO.PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crearPedido(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public ResponseEntity<List<PedidoDTO.PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<PedidoDTO.PedidoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente")
    public ResponseEntity<List<PedidoDTO.PedidoResponse>> porCliente(
            @PathVariable String clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de un pedido")
    public ResponseEntity<PedidoDTO.PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody PedidoDTO.CambioEstadoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}