package com.tup.programacion3.entities;

import java.util.Objects;

public class DetallePedido extends Base {

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido() {
        super();
    }

    public DetallePedido(Long id, int cantidad, Producto producto) {
        super(id);
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = producto.getPrecio() * cantidad;
    }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePedido that = (DetallePedido) o;
        return Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto);
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                super.toString() +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                ", producto=" + (producto != null ? producto.getNombre() : "null") +
                '}';
    }
}
