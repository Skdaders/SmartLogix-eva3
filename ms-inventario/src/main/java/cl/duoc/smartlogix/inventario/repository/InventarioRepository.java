package cl.duoc.smartlogix.inventario.repository;

import cl.duoc.smartlogix.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Patrón Repository: abstrae el acceso a datos detrás de una interfaz
// Spring Data JPA genera automáticamente las consultas SQL
@Repository
public interface InventarioRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByBodegaId(Long bodegaId);

    // Productos con stock por debajo del mínimo
    List<Producto> findByStockActualLessThan(Integer stockMinimo);

    boolean existsByCodigo(String codigo);
}
