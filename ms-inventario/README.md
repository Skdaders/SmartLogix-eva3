# ms-inventario — SmartLogix

Microservicio de Inventario para la plataforma SmartLogix. Gestiona el stock de productos por bodega, aplica el patrón **Repository** con Spring Data JPA y protege sus endpoints con **Circuit Breaker** (Resilience4j).

## Tecnologías
- Java 17 + Spring Boot 3.2.5
- Spring Data JPA + PostgreSQL
- Resilience4j (Circuit Breaker)
- Swagger/OpenAPI 3
- JUnit 5 + Mockito (pruebas unitarias)
- JaCoCo (cobertura de código)

## Requisitos previos
- JDK 17+
- Maven 3.8+
- PostgreSQL 14+ corriendo en `localhost:5432`

## Configuración de base de datos
```sql
CREATE DATABASE smartlogix_inventario;
```

## Ejecución
```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

El servicio arranca en `http://localhost:8081`

## Endpoints principales

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/inventario/productos` | Listar todos los productos |
| GET | `/api/inventario/productos/{id}` | Obtener producto por ID |
| POST | `/api/inventario/productos` | Crear producto |
| GET | `/api/inventario/stock/{productoId}` | Consultar stock |
| PUT | `/api/inventario/stock` | Actualizar stock (ENTRADA/SALIDA) |
| GET | `/api/inventario/bajo-stock` | Productos bajo stock mínimo |
| DELETE | `/api/inventario/productos/{id}` | Eliminar producto |

## Swagger UI
```
http://localhost:8081/swagger-ui.html
```

## Pruebas unitarias
```bash
# Ejecutar pruebas
mvn test

# Generar reporte de cobertura JaCoCo
mvn verify
```

El reporte HTML de cobertura se genera en:
```
target/site/jacoco/index.html
```

## Estructura del proyecto
```
ms-inventario/
├── src/
│   ├── main/java/cl/duoc/smartlogix/inventario/
│   │   ├── controller/    → InventarioController (endpoints REST)
│   │   ├── service/       → InventarioService (lógica de negocio)
│   │   ├── repository/    → InventarioRepository (patrón Repository, JPA)
│   │   ├── model/         → Producto (entidad JPA)
│   │   ├── dto/           → InventarioDTO (objetos de transferencia)
│   │   └── exception/     → Manejo centralizado de errores
│   └── test/              → Pruebas unitarias con JUnit 5 + Mockito
└── pom.xml
```
