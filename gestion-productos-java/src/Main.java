import java.util.NoSuchElementException;
import java.util.Scanner;

/** Punto de entrada. Sin argumentos ejecuta una demostración; --interactivo abre el menú. */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--interactivo")) ejecutarMenu();
        else ejecutarDemostracion();
    }

    /** Muestra instanciación de objetos, constructores y llamadas a métodos. */
    private static void ejecutarDemostracion() {
        Inventario inventario = new Inventario("Papelería escolar");
        Producto cuaderno = new Producto("P001", "Cuaderno", 45.50, 10);
        Producto lapiz = new Producto("P002", "Lápiz", 8.00, 20);
        cuaderno.agregarExistencias(5);
        cuaderno.retirarExistencias(3);
        lapiz.actualizarPrecio(8.50);
        inventario.agregarProducto(cuaderno);
        inventario.agregarProducto(lapiz);
        System.out.print(inventario.generarReporte());
    }

    /** Lee las opciones y coordina los métodos de Producto e Inventario. */
    private static void ejecutarMenu() {
        Inventario inventario = new Inventario("Mi inventario");
        try (Scanner entrada = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Registrar producto\n2. Agregar existencias\n3. Retirar existencias"
                        + "\n4. Actualizar precio\n5. Buscar producto\n6. Mostrar inventario\n0. Salir");
                if (!entrada.hasNextLine()) return;
                String opcion = entrada.nextLine().trim();
                try {
                    switch (opcion) {
                        case "1":
                            System.out.print("Código: "); String codigo = entrada.nextLine();
                            System.out.print("Nombre: "); String nombre = entrada.nextLine();
                            System.out.print("Precio unitario MXN (sin separador de miles): ");
                            double precio = leerPrecio(entrada);
                            System.out.print("Existencias iniciales: ");
                            int stock = Integer.parseInt(entrada.nextLine().trim());
                            inventario.agregarProducto(new Producto(codigo, nombre, precio, stock));
                            System.out.println("Producto registrado.");
                            break;
                        case "2":
                        case "3":
                            Producto producto = solicitarProducto(entrada, inventario);
                            System.out.print("Cantidad: ");
                            int cantidad = Integer.parseInt(entrada.nextLine().trim());
                            if (opcion.equals("2")) producto.agregarExistencias(cantidad);
                            else producto.retirarExistencias(cantidad);
                            System.out.println("Existencias actualizadas.");
                            break;
                        case "4":
                            Producto encontrado = solicitarProducto(entrada, inventario);
                            System.out.print("Nuevo precio MXN: ");
                            encontrado.actualizarPrecio(leerPrecio(entrada));
                            System.out.println("Precio actualizado.");
                            break;
                        case "5": System.out.println(solicitarProducto(entrada, inventario).obtenerResumen()); break;
                        case "6": System.out.print(inventario.generarReporte()); break;
                        case "0": return;
                        default: System.out.println("Opción no válida.");
                    }
                } catch (NumberFormatException error) {
                    System.out.println("Escribe un número válido. Las existencias deben ser enteras.");
                } catch (IllegalArgumentException error) {
                    System.out.println(error.getMessage());
                } catch (NoSuchElementException error) {
                    return; // Termina limpiamente si se cierra la entrada de consola.
                }
            }
        }
    }

    /** Busca el objeto seleccionado para reutilizarlo en las distintas operaciones. */
    private static Producto solicitarProducto(Scanner entrada, Inventario inventario) {
        System.out.print("Código: ");
        Producto producto = inventario.buscarProducto(entrada.nextLine());
        if (producto == null) throw new IllegalArgumentException("Producto no encontrado.");
        return producto;
    }

    /** Acepta punto o coma decimal, sin separadores de miles. */
    private static double leerPrecio(Scanner entrada) {
        return Double.parseDouble(entrada.nextLine().trim().replace(',', '.'));
    }
}
