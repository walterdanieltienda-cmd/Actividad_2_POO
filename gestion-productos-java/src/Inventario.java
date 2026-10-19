import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Administra objetos Producto sin códigos duplicados. Los datos viven en memoria. */
public class Inventario {
    private final String nombre;
    private final List<Producto> productos;

    /** Construye un inventario vacío con nombre obligatorio. */
    public Inventario(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio.");
        this.nombre = nombre.trim();
        this.productos = new ArrayList<>();
    }

    /** Incorpora un objeto Producto si su código no existe. */
    public void agregarProducto(Producto producto) {
        if (producto == null) throw new IllegalArgumentException("El producto es obligatorio.");
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("El código ya está registrado.");
        }
        productos.add(producto);
    }

    /** Encuentra un producto por código o devuelve null si no existe. */
    public Producto buscarProducto(String codigo) {
        if (codigo == null) return null;
        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo.trim())) return producto;
        }
        return null;
    }

    /** Suma el valor de las existencias de todos los productos. */
    public BigDecimal calcularValorTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (Producto producto : productos) total = total.add(producto.calcularValorInventario());
        return total;
    }

    /** Reúne el detalle de cada objeto y el valor total del inventario. */
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder("Inventario: " + nombre + "\n");
        if (productos.isEmpty()) reporte.append("Sin productos registrados.\n");
        for (Producto producto : productos) reporte.append(producto.obtenerResumen()).append('\n');
        reporte.append("Valor total MXN: ").append(calcularValorTotal().toPlainString()).append('\n');
        return reporte.toString();
    }
}
