# ms-pedidos — SmartLogix

Microservicio de Pedidos para SmartLogix. Implementa el patrón **Factory Method** para crear pedidos de tipo NORMAL, URGENTE y MAYORISTA con lógica de negocio diferenciada.

## Tecnologías
- Java 17 + Spring Boot 3.2.5
- Spring Data JPA + MySQL
- Patrón Factory Method
- Swagger/OpenAPI 3
- JUnit 5 + Mockito | JaCoCo

## Requisitos
- JDK 17+ | Maven 3.8+ | MySQL 8+

## Configuración BD
```sql
CREATE DATABASE smartlogix_pedidos;
```

## Ejecución
```bash
mvn clean install
mvn spring-boot:run
```
Arranca en `http://localhost:8082`

## Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/pedidos` | Crear pedido |
| GET | `/api/pedidos` | Listar todos |
| GET | `/api/pedidos/{id}` | Obtener por ID |
| GET | `/api/pedidos/cliente/{clienteId}` | Por cliente |
| PUT | `/api/pedidos/{id}/estado` | Cambiar estado |
| DELETE | `/api/pedidos/{id}` | Cancelar pedido |

## Tipos de pedido

| Tipo | Días | Precio |
|------|------|--------|
| NORMAL | 5 | Sin recargo |
| URGENTE | 1 | +15% |
| MAYORISTA | 7 | -20% |

## Pruebas
```bash
mvn test        # Ejecutar pruebas
mvn verify      # Generar reporte JaCoCo en target/site/jacoco/
```
