package com.tup.programacion3;

import com.tup.programacion3.entities.*;
import com.tup.programacion3.enums.Estado;
import com.tup.programacion3.enums.FormaPago;
import com.tup.programacion3.enums.Rol;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // ── Categorías ──────────────────────────────────────────────────────────
        Categoria electronica = new Categoria(1L, "Electrónica", "Dispositivos electrónicos y gadgets");
        Categoria ropa        = new Categoria(2L, "Ropa",        "Indumentaria y accesorios");
        Categoria hogar       = new Categoria(3L, "Hogar",       "Artículos para el hogar");

        // ── Productos (10) ───────────────────────────────────────────────────────
        Producto p1  = new Producto(1L,  "Notebook HP",         850.00,  "Laptop 15 pulgadas",     10, "notebook.jpg",  true);
        Producto p2  = new Producto(2L,  "Mouse Logitech",       25.00,  "Mouse inalámbrico",      50, "mouse.jpg",     true);
        Producto p3  = new Producto(3L,  "Teclado Redragon",     45.00,  "Teclado mecánico RGB",   30, "teclado.jpg",   true);
        Producto p4  = new Producto(4L,  "Monitor Samsung 24\"", 300.00, "Monitor Full HD",        15, "monitor.jpg",   true);
        Producto p5  = new Producto(5L,  "Auriculares Sony",     80.00,  "Auriculares bluetooth",  20, "auriculares.jpg",true);
        Producto p6  = new Producto(6L,  "Remera Algodón",       15.00,  "Remera 100% algodón",   100, "remera.jpg",    true);
        Producto p7  = new Producto(7L,  "Jeans Slim Fit",       40.00,  "Jean elastizado",        60, "jeans.jpg",     true);
        Producto p8  = new Producto(8L,  "Campera Inflable",    120.00,  "Campera impermeable",    25, "campera.jpg",   true);
        Producto p9  = new Producto(9L,  "Silla Gamer",         200.00,  "Silla ergonómica",        8, "silla.jpg",     true);
        Producto p10 = new Producto(10L, "Lámpara LED",          18.00,  "Lámpara de escritorio",  40, "lampara.jpg",   true);

        // Asociar productos a categorías
        electronica.agregarProducto(p1);
        electronica.agregarProducto(p2);
        electronica.agregarProducto(p3);
        electronica.agregarProducto(p4);
        electronica.agregarProducto(p5);
        ropa.agregarProducto(p6);
        ropa.agregarProducto(p7);
        ropa.agregarProducto(p8);
        hogar.agregarProducto(p9);
        hogar.agregarProducto(p10);

        // Colección de todos los productos
        Set<Producto> todosLosProductos = new HashSet<>();
        todosLosProductos.add(p1);
        todosLosProductos.add(p2);
        todosLosProductos.add(p3);
        todosLosProductos.add(p4);
        todosLosProductos.add(p5);
        todosLosProductos.add(p6);
        todosLosProductos.add(p7);
        todosLosProductos.add(p8);
        todosLosProductos.add(p9);
        todosLosProductos.add(p10);

        // ── Usuarios ─────────────────────────────────────────────────────────────
        Usuario usuario1 = new Usuario(1L, "Carlos",  "García",  "carlos@mail.com",  "3511234567", "pass123", Rol.USUARIO);
        Usuario usuario2 = new Usuario(2L, "Luciana", "Martínez","luciana@mail.com", "3519876543", "pass456", Rol.ADMIN);

        // ── Pedidos ──────────────────────────────────────────────────────────────
        Pedido pedido1 = new Pedido(1L, LocalDate.of(2025, 3, 10), Estado.CONFIRMADO, FormaPago.TARJETA);
        pedido1.addDetallePedido(1, p1);
        pedido1.addDetallePedido(2, p2);
        pedido1.addDetallePedido(1, p3);

        Pedido pedido2 = new Pedido(2L, LocalDate.of(2025, 4, 5), Estado.TERMINADO, FormaPago.EFECTIVO);
        pedido2.addDetallePedido(3, p6);
        pedido2.addDetallePedido(1, p7);

        Pedido pedido3 = new Pedido(3L, LocalDate.of(2025, 5, 20), Estado.PENDIENTE, FormaPago.TRANSFERENCIA);
        pedido3.addDetallePedido(1, p9);
        pedido3.addDetallePedido(2, p10);
        pedido3.addDetallePedido(1, p5);

        // Asignar pedidos a usuarios
        usuario1.agregarPedido(pedido1);
        usuario1.agregarPedido(pedido2);
        usuario1.agregarPedido(pedido3);

        // usuario2 tiene menos pedidos para poder comparar cuál tiene más
        Pedido pedido4 = new Pedido(4L, LocalDate.of(2025, 5, 25), Estado.PENDIENTE, FormaPago.TARJETA);
        pedido4.addDetallePedido(1, p4);
        pedido4.addDetallePedido(2, p8);
        usuario2.agregarPedido(pedido4);

        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        // ── Punto 4: mostrar un producto, listado de productos y pedidos del usuario con más pedidos ──
        System.out.println("=== UN PRODUCTO ===");
        System.out.println(p1);

        System.out.println("\n=== LISTADO DE PRODUCTOS ===");
        todosLosProductos.forEach(System.out::println);

        Usuario usuarioConMasPedidos = usuarios.stream()
                .max(Comparator.comparingInt(u -> u.getPedidos().size()))
                .orElseThrow();

        System.out.println("\n=== USUARIO CON MÁS PEDIDOS ===");
        System.out.println(usuarioConMasPedidos);

        System.out.println("\n=== PEDIDOS DE " + usuarioConMasPedidos.getNombre().toUpperCase() + " ===");
        usuarioConMasPedidos.getPedidos().forEach(System.out::println);

        // ── Punto 5: instanciar producto duplicado y comparar con la colección ──
        System.out.println("\n=== COMPARACIÓN DE PRODUCTO DUPLICADO ===");
        Producto productoDuplicado = new Producto(99L, "Notebook HP", 850.00, "Copia de prueba", 0, "copia.jpg", false);
        System.out.println("Producto nuevo (mismos nombre y precio que p1): " + productoDuplicado);

        boolean encontrado = false;
        for (Producto p : todosLosProductos) {
            boolean sonIguales = p.equals(productoDuplicado);
            System.out.println("  ¿" + p.getNombre() + " equals productoNuevo? -> " + sonIguales);
            if (sonIguales) encontrado = true;
        }
        System.out.println("\nResultado: el producto duplicado " + (encontrado ? "YA EXISTE" : "NO EXISTE") + " en la colección.");

        // Intentar agregar el duplicado al Set (no debería agregarse)
        int tamañoAntes = todosLosProductos.size();
        todosLosProductos.add(productoDuplicado);
        int tamañoDespues = todosLosProductos.size();
        System.out.println("Tamaño del Set antes de agregar duplicado: " + tamañoAntes);
        System.out.println("Tamaño del Set después de agregar duplicado: " + tamañoDespues);
        System.out.println("El Set " + (tamañoAntes == tamañoDespues ? "rechazó el duplicado correctamente." : "aceptó el elemento (no era duplicado)."));
    }
}
