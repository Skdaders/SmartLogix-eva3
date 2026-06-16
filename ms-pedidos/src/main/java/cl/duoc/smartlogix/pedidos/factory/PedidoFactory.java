package cl.duoc.smartlogix.pedidos.factory;

import cl.duoc.smartlogix.pedidos.model.Pedido;

/**
 * Patrón Factory Method
 * Define la interfaz para crear un Pedido según su tipo.
 * Cada subclase encapsula la lógica de negocio propia del tipo de pedido.
 */
public abstract class PedidoFactory {

    // Método factory: cada subclase implementa cómo calcular precio y días
    public abstract Pedido crearPedido(String clienteId, Long productoId,
                                       Integer cantidad, Double precioUnitario);

    /**
     * Método estático que actúa como dispatcher:
     * recibe el tipo y delega a la fábrica correspondiente.
     */
    public static Pedido fabricar(String tipo, String clienteId, Long productoId,
                                  Integer cantidad, Double precioUnitario) {
        return switch (tipo.toUpperCase()) {
            case "NORMAL"    -> new PedidoNormalFactory()
                                    .crearPedido(clienteId, productoId, cantidad, precioUnitario);
            case "URGENTE"   -> new PedidoUrgenteFactory()
                                    .crearPedido(clienteId, productoId, cantidad, precioUnitario);
            case "MAYORISTA" -> new PedidoMayoristaFactory()
                                    .crearPedido(clienteId, productoId, cantidad, precioUnitario);
            default          -> throw new IllegalArgumentException("Tipo de pedido desconocido: " + tipo);
        };
    }
}
