# frontend-smartlogix

Frontend React para SmartLogix — Plataforma Inteligente para la Gestión Logística. Consume el **BFF (bff-smartlogix)** en `localhost:8080`.

## Tecnologías
- React 18 + React Router v6
- Axios (cliente HTTP al BFF)
- React Testing Library + Jest (pruebas unitarias)

## Requisitos
- Node.js 18+ | npm 9+
- bff-smartlogix corriendo en `http://localhost:8080`

## Instalación
```bash
npm install
```

## Ejecución
```bash
npm start
```
Abre `http://localhost:3000` en el navegador.

## Pruebas unitarias
```bash
npm test
```
Genera reporte de cobertura en `coverage/lcov-report/index.html`.

## Estructura
```
src/
├── components/
│   └── Navbar.jsx          → Barra de navegación
├── pages/
│   ├── Dashboard.jsx       → Resumen general + bajo stock
│   ├── Inventario.jsx      → Tabla de productos con búsqueda
│   ├── Pedidos.jsx         → Consulta pedidos por cliente
│   └── NuevoPedido.jsx     → Formulario crear pedido (NORMAL/URGENTE/MAYORISTA)
├── services/
│   └── api.js              → Llamadas HTTP al BFF
└── __tests__/
    └── components.test.js  → 18 pruebas unitarias con mocks
```

## Vistas principales

| Ruta | Vista |
|------|-------|
| `/` | Dashboard con estadísticas y alertas |
| `/inventario` | Lista de productos con búsqueda en tiempo real |
| `/pedidos` | Búsqueda de pedidos por cliente |
| `/nuevo-pedido` | Formulario de creación de pedidos |

## Build producción
```bash
npm run build
```
