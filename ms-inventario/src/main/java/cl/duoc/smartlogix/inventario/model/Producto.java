package cl.duoc.smartlogix.inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(nullable = false)
    private Long bodegaId;

    @Column(nullable = false)
    private Double precioUnitario;
}
