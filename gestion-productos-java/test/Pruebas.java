import java.math.BigDecimal;

/** Comprobaciones ejecutables sin bibliotecas externas. */
public class Pruebas {
    private static int total;
    private static void comprobar(boolean condicion, String caso) {
        if (!condicion) throw new AssertionError(caso);
        total++;
    }
    private static void rechaza(Runnable accion, String caso) {
        try { accion.run(); } catch (IllegalArgumentException esperado) { total++; return; }
        throw new AssertionError(caso);
    }
    public static void main(String[] args) {
        Producto producto = new Producto(" p001 ", "Cuaderno", 45.50, 10);
        comprobar(producto.getCodigo().equals("P001"), "Código normalizado");
        comprobar(producto.calcularValorInventario().compareTo(new BigDecimal("455.00")) == 0, "Valor inicial");
        producto.agregarExistencias(5);
        comprobar(producto.getExistencias() == 15, "Entrada");
        producto.retirarExistencias(3);
        comprobar(producto.getExistencias() == 12, "Salida");
        producto.actualizarPrecio(50);
        comprobar(producto.calcularValorInventario().compareTo(new BigDecimal("600.00")) == 0, "Nuevo precio");
        rechaza(() -> producto.retirarExistencias(13), "Stock insuficiente");
        comprobar(producto.getExistencias() == 12, "Rechazo sin modificar stock");
        rechaza(() -> producto.agregarExistencias(0), "Entrada cero");
        rechaza(() -> producto.agregarExistencias(-1), "Entrada negativa");
        rechaza(() -> producto.retirarExistencias(0), "Salida cero");
        rechaza(() -> producto.retirarExistencias(-1), "Salida negativa");
        rechaza(() -> producto.actualizarPrecio(-1), "Precio negativo");
        rechaza(() -> producto.actualizarPrecio(Double.NaN), "NaN");
        rechaza(() -> producto.actualizarPrecio(Double.POSITIVE_INFINITY), "Infinito");
        rechaza(() -> new Producto("", "Nombre", 1, 0), "Código vacío");
        rechaza(() -> new Producto("P", " ", 1, 0), "Nombre vacío");
        rechaza(() -> new Producto("P", "Nombre", 1, -1), "Stock negativo");
        Inventario inventario = new Inventario("Prueba");
        comprobar(inventario.calcularValorTotal().compareTo(BigDecimal.ZERO) == 0, "Inventario vacío");
        inventario.agregarProducto(producto);
        comprobar(inventario.buscarProducto(" p001 ") == producto, "Búsqueda normalizada");
        comprobar(inventario.buscarProducto("X") == null, "No encontrado");
        rechaza(() -> inventario.agregarProducto(new Producto("p001", "Duplicado", 1, 1)), "Duplicado");
        rechaza(() -> inventario.agregarProducto(null), "Objeto nulo");
        inventario.agregarProducto(new Producto("P002", "Lápiz", 8.50, 20));
        comprobar(inventario.calcularValorTotal().compareTo(new BigDecimal("770.00")) == 0, "Total");
        producto.retirarExistencias(12);
        comprobar(producto.getExistencias() == 0, "Retiro completo");
        Producto maximo = new Producto("MAX", "Máximo", 0, Integer.MAX_VALUE);
        rechaza(() -> maximo.agregarExistencias(1), "Desbordamiento");
        Producto decimal = new Producto("DEC", "Decimal", 0.10, 3);
        comprobar(decimal.calcularValorInventario().compareTo(new BigDecimal("0.30")) == 0, "Precisión decimal");
        decimal.actualizarPrecio(1.235);
        comprobar(decimal.getPrecio().compareTo(new BigDecimal("1.24")) == 0, "Redondeo");
        System.out.println("Pruebas aprobadas: " + total);
    }
}
