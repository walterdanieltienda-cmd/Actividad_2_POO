import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/** Representa un producto con precio en MXN y existencias enteras. */
public class Producto {
    private final String codigo;
    private final String nombre;
    private BigDecimal precio;
    private int existencias;

    /** Inicializa los atributos y valida la identidad, el precio y el stock. */
    public Producto(String codigo, String nombre, double precio, int existencias) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Código y nombre son obligatorios.");
        }
        if (existencias < 0) throw new IllegalArgumentException("Las existencias no pueden ser negativas.");
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        actualizarPrecio(precio);
        this.existencias = existencias;
    }

    /** Cambia el precio, redondeado a dos decimales, sin aceptar negativos o valores no finitos. */
    public void actualizarPrecio(double nuevoPrecio) {
        if (!Double.isFinite(nuevoPrecio) || nuevoPrecio < 0) {
            throw new IllegalArgumentException("El precio debe ser un número no negativo.");
        }
        this.precio = BigDecimal.valueOf(nuevoPrecio).setScale(2, RoundingMode.HALF_UP);
    }

    /** Suma una entrada positiva y evita exceder la capacidad de un entero. */
    public void agregarExistencias(int cantidad) {
        validarCantidad(cantidad);
        if (cantidad > Integer.MAX_VALUE - existencias) {
            throw new IllegalArgumentException("La cantidad excede la capacidad del inventario.");
        }
        existencias += cantidad;
    }

    /** Resta una salida positiva únicamente cuando hay stock suficiente. */
    public void retirarExistencias(int cantidad) {
        validarCantidad(cantidad);
        if (cantidad > existencias) throw new IllegalArgumentException("Existencias insuficientes.");
        existencias -= cantidad;
    }

    /** Centraliza la regla para entradas y salidas de unidades. */
    private void validarCantidad(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
    }

    /** Calcula precio por existencias con aritmética decimal. */
    public BigDecimal calcularValorInventario() {
        return precio.multiply(BigDecimal.valueOf(existencias));
    }

    /** Permite consultar el código sin modificarlo. */
    public String getCodigo() { return codigo; }
    /** Permite consultar el nombre sin modificarlo. */
    public String getNombre() { return nombre; }
    /** Devuelve el precio como valor decimal inmutable. */
    public BigDecimal getPrecio() { return precio; }
    /** Devuelve las unidades disponibles. */
    public int getExistencias() { return existencias; }

    /** Presenta los datos del producto para el reporte. */
    public String obtenerResumen() {
        return codigo + " | " + nombre + " | Precio MXN: " + precio.toPlainString()
                + " | Existencias: " + existencias + " | Valor MXN: "
                + calcularValorInventario().toPlainString();
    }
}
