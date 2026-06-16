package cl.duoc.smartlogix.inventario.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }
}
