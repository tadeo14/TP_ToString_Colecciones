# Explicación del Trabajo Práctico — ToString, Equals, HashCode y Colecciones

---

## ¿Qué construimos?

Construimos un sistema de gestión de pedidos de una tienda online. Hay usuarios que hacen pedidos, cada pedido tiene productos, y los productos pertenecen a categorías. Todo esto usando **Programación Orientada a Objetos** en Java.

---

## 1. Los Enums — Valores fijos y controlados

Antes de arrancar con las clases, definimos los valores posibles para ciertos campos. En lugar de usar Strings sueltos como `"ADMIN"` o `"pendiente"` (que se pueden escribir mal), usamos **enums**: listas cerradas de opciones válidas.

```java
// Estado.java
public enum Estado {
    PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO
}

// FormaPago.java
public enum FormaPago {
    TARJETA, TRANSFERENCIA, EFECTIVO
}

// Rol.java
public enum Rol {
    ADMIN, USUARIO
}
```

**¿Por qué?** Porque si mañana alguien escribe `"Pendiente"` en lugar de `"PENDIENTE"`, el compilador no lo va a detectar. Con un enum, si escribís algo que no existe, el programa directamente no compila. Más seguro.

---

## 2. La clase Base — Herencia para evitar repetición

Todas nuestras entidades (Usuario, Producto, Pedido, etc.) comparten tres campos en común:

- `id` → identificador único
- `eliminado` → si el registro fue borrado lógicamente (sin borrarlo de verdad)
- `createdAt` → fecha y hora de creación

En lugar de escribir esos tres campos en cada clase, creamos una clase **abstracta** llamada `Base` que los centraliza.

```java
public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;
    // ...
}
```

**¿Qué significa `abstract`?** Que no podés crear un objeto `new Base()` directamente. Es solo una plantilla. Las clases que la extienden (heredan) reciben automáticamente esos tres campos.

### equals y hashCode en Base

En `Base` también definimos `equals` y `hashCode` basados en el `id`:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Base base = (Base) o;
    return Objects.equals(id, base.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

Esto significa que **dos objetos son iguales si tienen el mismo id**, sin importar el resto de sus datos. Tiene sentido porque en una base de datos, el id es lo que identifica unívocamente a un registro.

---

## 3. La interface Calculable

Una **interface** es un contrato. Cualquier clase que la implemente se compromete a tener ciertos métodos.

```java
public interface Calculable {
    void calcularTotal();
}
```

`Pedido` implementa esta interface porque necesita calcular su total sumando los subtotales de sus detalles. Así nos aseguramos de que cualquier clase que represente algo "calculable" tenga ese método.

---

## 4. Las Entidades — El corazón del sistema

### Producto

```java
public class Producto extends Base {
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private Boolean disponible;
    // ...
}
```

**Identidad (equals/hashCode):** Dos productos son iguales si tienen el **mismo nombre y precio**. Esto es una decisión de diseño: en este negocio, no puede haber dos productos llamados igual con el mismo precio.

```java
@Override
public boolean equals(Object o) {
    Producto producto = (Producto) o;
    return Objects.equals(nombre, producto.nombre) && Objects.equals(precio, producto.precio);
}

@Override
public int hashCode() {
    return Objects.hash(nombre, precio);
}
```

---

### Categoria

Contiene una colección de productos. Usamos `Set<Producto>` (en lugar de `List`) para evitar duplicados:

```java
public class Categoria extends Base {
    private String nombre;
    private String descripcion;
    private Set<Producto> productos;   // ← Set, no List
    // ...
}
```

**Identidad:** Dos categorías son iguales si tienen el **mismo nombre**.

---

### DetallePedido

Representa un renglón dentro de un pedido: "2 unidades de Mouse Logitech = $50".

```java
public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;
    // ...
}
```

El subtotal se calcula automáticamente en el constructor:
```java
this.subtotal = producto.getPrecio() * cantidad;
```

**Identidad:** Dos detalles son iguales si tienen el **mismo producto** (no puede haber dos renglones del mismo producto en un pedido).

---

### Pedido — implements Calculable

```java
public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Set<DetallePedido> detalles;   // ← Set
    // ...
}
```

Tiene tres métodos importantes:

```java
// Agrega un producto al pedido y recalcula el total
public void addDetallePedido(int cantidad, Producto producto) { ... }

// Busca el detalle de un producto específico
public DetallePedido findeDetallePedidoByProducto(Producto producto) { ... }

// Elimina el detalle de un producto y recalcula el total
public void deleteDetallePedidoByProducto(Producto producto) { ... }

// Suma todos los subtotales
@Override
public void calcularTotal() {
    this.total = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
}
```

---

### Usuario

```java
public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contraseña;
    private Rol rol;
    private Set<Pedido> pedidos;   // ← Set
}
```

**Identidad:** Dos usuarios son iguales si tienen el **mismo mail**. Tiene sentido porque el email es único por persona.

---

## 5. toString — ¿Para qué sirve?

Cuando hacés `System.out.println(objeto)`, Java llama automáticamente al método `toString()`. Sin sobreescribirlo, te mostraría algo horrible como `com.tup.programacion3.entities.Producto@3d4eac69`.

Sobreescribiéndolo, podés mostrar la información útil:

```
Producto{id=1, nombre='Notebook HP', precio=850.0, stock=10, disponible=true}
```

Cada clase tiene su propio `toString` mostrando sus campos relevantes.

---

## 6. Set vs List — ¿Por qué usamos Set?

| | List | Set |
|---|---|---|
| Permite duplicados | ✅ Sí | ❌ No |
| Mantiene orden | ✅ Sí | No garantizado |
| Usa equals/hashCode | No para duplicados | ✅ Sí |

Usamos `Set` porque las relaciones del sistema no deben tener duplicados:
- Un pedido no puede tener dos veces el mismo producto
- Una categoría no puede tener dos veces el mismo producto
- Un usuario no puede tener dos veces el mismo pedido

Para que el `Set` sepa qué es un duplicado, necesita que `equals` y `hashCode` estén bien implementados. **Por eso es tan importante el contrato equals/hashCode.**

---

## 7. La clase Main — Todo junto

El Main demuestra los cuatro puntos del práctico:

### Punto 1 a 3 — Instancias requeridas
Se crean: 3 categorías, 10 productos, 3 pedidos con al menos 2 detalles cada uno, 2 usuarios.

### Punto 4 — Mostrar por consola

```java
// Mostrar un producto
System.out.println(p1);

// Mostrar todos los productos
todosLosProductos.forEach(System.out::println);

// Encontrar el usuario con más pedidos y mostrar sus pedidos
Usuario usuarioConMasPedidos = usuarios.stream()
    .max(Comparator.comparingInt(u -> u.getPedidos().size()))
    .orElseThrow();

usuarioConMasPedidos.getPedidos().forEach(System.out::println);
```

### Punto 5 — Demostrar el comportamiento del Set con duplicados

```java
// Creamos un producto con el mismo nombre y precio que p1 (Notebook HP, $850)
Producto productoDuplicado = new Producto(99L, "Notebook HP", 850.00, ...);

// Lo comparamos contra toda la colección
for (Producto p : todosLosProductos) {
    System.out.println(p.getNombre() + " equals productoNuevo? -> " + p.equals(productoDuplicado));
}

// Intentamos agregarlo al Set
todosLosProductos.add(productoDuplicado);
// El Set tiene 10 elementos antes Y después → rechazó el duplicado
```

**Conclusión que demuestra:** Como `equals` de `Producto` compara `nombre` y `precio`, el Set detecta que ya existe un producto con esos valores y no lo agrega. Así funciona el contrato equals/hashCode con las colecciones.

---

## Resumen del flujo de datos

```
Usuario
  └── Set<Pedido>
        └── Set<DetallePedido>
              └── Producto  ←── Categoria
                                  └── Set<Producto>
```

---

## Conceptos clave del TP

| Concepto | Dónde se usa | Por qué |
|---|---|---|
| `toString()` | Todas las clases | Representar objetos como texto legible |
| `equals()` | Todas las clases | Definir cuándo dos objetos son "el mismo" |
| `hashCode()` | Todas las clases | Necesario para que Set funcione correctamente |
| `Set` | Pedido, Usuario, Categoria | Evitar elementos duplicados en colecciones |
| Herencia (`extends`) | Todas las entidades → Base | Reutilizar campos comunes |
| Interface (`implements`) | Pedido → Calculable | Garantizar que ciertos métodos existan |
| Enums | Estado, FormaPago, Rol | Valores fijos y seguros en tiempo de compilación |
