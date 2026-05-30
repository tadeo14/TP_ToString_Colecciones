package com.tup.programacion3.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Categoria extends Base {

    private String nombre;
    private String descripcion;
    private Set<Producto> productos;

    public Categoria() {
        super();
        this.productos = new HashSet<>();
    }

    public Categoria(Long id, String nombre, String descripcion) {
        super(id);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = new HashSet<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Set<Producto> getProductos() { return productos; }
    public void setProductos(Set<Producto> productos) { this.productos = productos; }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(nombre, categoria.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                super.toString() +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cantidadProductos=" + productos.size() +
                '}';
    }
}
