import React from 'react';
import { NavLink } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        Smart<span>Logix</span>
      </div>
      <div className="navbar-links">
        <NavLink to="/" className={({ isActive }) => 'nav-link' + (isActive ? ' active' : '')}>
          Dashboard
        </NavLink>
        <NavLink to="/inventario" className={({ isActive }) => 'nav-link' + (isActive ? ' active' : '')}>
          Inventario
        </NavLink>
        <NavLink to="/pedidos" className={({ isActive }) => 'nav-link' + (isActive ? ' active' : '')}>
          Pedidos
        </NavLink>
        <NavLink to="/nuevo-pedido" className={({ isActive }) => 'nav-link' + (isActive ? ' active' : '')}>
          Nuevo Pedido
        </NavLink>
      </div>
    </nav>
  );
}

export default Navbar;
