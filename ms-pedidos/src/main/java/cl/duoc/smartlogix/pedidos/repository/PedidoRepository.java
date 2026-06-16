package cl.duoc.smartlogix.pedidos.repository;

import cl.duoc.smartlogix.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(String clienteId);
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
    List<Pedido> findByTipo(String tipo);
}
