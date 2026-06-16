package cl.duoc.smartlogix.pedidos.service;

import cl.duoc.smartlogix.pedidos.dto.PedidoDTO;
import cl.duoc.smartlogix.pedidos.factory.PedidoFactory;
import cl.duoc.smartlogix.pedidos.model.Pedido;
import cl.duoc.smartlogix.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public PedidoDTO.PedidoResponse crearPedido(PedidoDTO.PedidoRequest request) {
        // Patrón Factory Method: delega la creación al tipo correcto
        Pedido pedido = PedidoFactory.fabricar(
                request.getTipo(),
                request.getClienteId(),
                request.getProductoId(),
                request.getCantidad(),
                request.getPrecioUnitario()
        );
        return toResponse(pedidoRepository.save(pedido));
    }

    public List<PedidoDTO.PedidoResponse> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public PedidoDTO.PedidoResponse obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        return toResponse(pedido);
    }

    public List<PedidoDTO.PedidoResponse> listarPorCliente(String clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO.PedidoResponse cambiarEstado(Long id, PedidoDTO.CambioEstadoRequest request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        pedido.setEstado(request.getNuevoEstado());
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        if (pedido.getEstado() == Pedido.EstadoPedido.ENVIADO ||
            pedido.getEstado() == Pedido.EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya enviado o entregado.");
        }
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private PedidoDTO.PedidoResponse toResponse(Pedido p) {
        return new PedidoDTO.PedidoResponse(
                p.getId(), p.getClienteId(), p.getProductoId(),
                p.getCantidad(), p.getTipo(), p.getPrecioTotal(),
                p.getDiasEntrega(), p.getEstado().name(), p.getFechaCreacion()
        );
    }
}
