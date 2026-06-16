package cl.duoc.smartlogix.pedidos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String tipo; // NORMAL, URGENTE, MAYORISTA

    @Column(nullable = false)
    private Double precioTotal;

    @Column(nullable = false)
    private Integer diasEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, EN_PROCESO, ENVIADO, ENTREGADO, CANCELADO
    }
}
