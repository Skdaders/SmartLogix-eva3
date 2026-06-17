package cl.duoc.smartlogix.inventario.service;

import cl.duoc.smartlogix.inventario.dto.InventarioDTO;
import cl.duoc.smartlogix.inventario.exception.ProductoNotFoundException;
import cl.duoc.smartlogix.inventario.model.Producto;
import cl.duoc.smartlogix.inventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<InventarioDTO.ProductoResponse> listarTodos() {
        return inventarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InventarioDTO.ProductoResponse obtenerPorId(Long id) {
        Producto producto = inventarioRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        return toResponse(producto);
    }

    public InventarioDTO.StockResponse obtenerStock(Long productoId) {
        Producto producto = inventarioRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNotFoundException(productoId));
        return new InventarioDTO.StockResponse(
                productoId,
                producto.getStockActual(),
                "Stock disponible"
        );
    }

    @Transactional
    public InventarioDTO.ProductoResponse crearProducto(InventarioDTO.ProductoRequest request) {
        if (inventarioRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + request.getCodigo());
        }
        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setStockActual(request.getStockActual());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setBodegaId(request.getBodegaId());
        producto.setPrecioUnitario(request.getPrecioUnitario());
        return toResponse(inventarioRepository.save(producto));
    }

    @Transactional
    public InventarioDTO.StockResponse actualizarStock(InventarioDTO.StockUpdateRequest request) {
        Producto producto = inventarioRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException(request.getProductoId()));

        if ("ENTRADA".equalsIgnoreCase(request.getOperacion())) {
            producto.setStockActual(producto.getStockActual() + request.getCantidad());
        } else if ("SALIDA".equalsIgnoreCase(request.getOperacion())) {
            if (producto.getStockActual() < request.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getCodigo());
            }
            producto.setStockActual(producto.getStockActual() - request.getCantidad());
        } else {
            throw new IllegalArgumentException("Operación inválida. Use 'ENTRADA' o 'SALIDA'.");
        }

        inventarioRepository.save(producto);
        return new InventarioDTO.StockResponse(
                producto.getId(),
                producto.getStockActual(),
                "Stock actualizado correctamente"
        );
    }

    public List<InventarioDTO.ProductoResponse> listarBajoStock() {
        return inventarioRepository.findAll()
                .stream()
                .filter(p -> p.getStockActual() < p.getStockMinimo())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        inventarioRepository.deleteById(id);
    }

    private InventarioDTO.ProductoResponse toResponse(Producto p) {
        return new InventarioDTO.ProductoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getStockActual(),
                p.getStockMinimo(),
                p.getBodegaId(),
                p.getPrecioUnitario(),
                p.getStockActual() < p.getStockMinimo()
        );
    }
}