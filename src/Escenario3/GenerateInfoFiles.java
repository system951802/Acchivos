package Escenario3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {

    public static void main(String[] args) {
        GenerateInfoFiles generador = new GenerateInfoFiles();
        if (generador.generarArchivosPrueba()) {
            System.out.println("Archivos de prueba generados con éxito.");
        } else {
            System.err.println("Error al generar archivos de prueba.");
        }
    }

    public boolean generarArchivosPrueba() {
        try {
            generarInfoVendedores(10); // Generar información de vendedores
            generarInfoProductos(20); // Generar información de productos
            for (int i = 0; i < 10; i++) {
                generarVentasVendedor(5, "Vendedor" + i, i); // Generar ventas de prueba para cada vendedor
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al generar archivos de prueba: " + e.getMessage());
            return false;
        }
    }

    private void generarVentasVendedor(int cantidadVentas, String nombre, long id) throws IOException {
        Random random = new Random();
        FileWriter escritor = new FileWriter("ventas_" + id + ".txt");
        for (int i = 0; i < cantidadVentas; i++) {
            escritor.write("IDProducto" + (random.nextInt(10) + 1) + ";" + (random.nextInt(10) + 1) + "\n");
        }
        escritor.close();
    }

    private void generarInfoProductos(int cantidadProductos) throws IOException {
        FileWriter escritor = new FileWriter("productos.txt");
        Random random = new Random();
        for (int i = 1; i <= cantidadProductos; i++) {
            escritor.write("IDProducto" + i + ";Producto" + i + ";" + (random.nextInt(1000) + 1) + "\n");
        }
        escritor.close();
    }

    private void generarInfoVendedores(int cantidadVendedores) throws IOException {
        FileWriter escritor = new FileWriter("vendedores.txt");
        Random random = new Random();
        for (int i = 1; i <= cantidadVendedores; i++) {
            escritor.write("TipoDocumento;NúmeroDocumento;NombresVendedor" + i + ";ApellidosVendedor" + i + "\n");
        }
        escritor.close();
    }
}
