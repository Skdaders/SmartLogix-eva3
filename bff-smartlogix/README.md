# bff-smartlogix — SmartLogix

Backend For Frontend que agrega y adapta las respuestas de `ms-inventario` y `ms-pedidos` al formato que necesita el frontend React. Implementa el patrón **BFF** para reducir el acoplamiento entre la UI y los microservicios.

## Tecnologías
- Java 17 + Spring Boot 3.2.5
- RestTemplate (cliente HTTP)
- Swagger/OpenAPI 3

## Requisitos
- JDK 17+ | Maven 3.8+
- ms-inventario corriendo en `localhost:8081`
- ms-pedidos corriendo en `localhost:8082`

## Ejecución
```bash
mvn clean install
mvn spring-boot:run
```
Arranca en `http://localhost:8080`

## Endpoints BFF

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/bff/dashboard` | Dashboard consolidado |
| GET | `/bff/productos` | Lista de productos |
| GET | `/bff/productos/{id}` | Detalle de producto + stock |
| POST | `/bff/pedidos` | Crear pedido |
| GET | `/bff/pedidos/cliente/{clienteId}` | Pedidos por cliente |

## Swagger
```
http://localhost:8080/swagger-ui.html
```
