package Escenario3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Main procesador = new Main();
        if (procesador.generarReportes()) {
            System.out.println("Archivos de reporte generados con éxito.");
        } else {
            System.err.println("Error al generar archivos de reporte.");
        }
    }

    public boolean generarReportes() {
        try {
            Map<String, Integer> ventasPorVendedor = obtenerVentasPorVendedor();
            crearReporteVentas(ventasPorVendedor);

            Map<String, Integer> ventasPorProducto = obtenerVentasPorProducto();
            crearReporteVentasProducto(ventasPorProducto);

            return true;
        } catch (IOException e) {
            System.err.println("Error al generar archivos de reporte: " + e.getMessage());
            return false;
        }
    }

    private Map<String, Integer> obtenerVentasPorVendedor() throws IOException {
        Map<String, Integer> ventasPorVendedor = new HashMap<>();
        List<String> archivosVentas = obtenerArchivosEnDirectorio("ventas");
        for (String archivoVentas : archivosVentas) {
            int ventasTotales = obtenerTotalVentas(archivoVentas);
            String nombreVendedor = archivoVentas.substring(archivoVentas.indexOf("_") + 1, archivoVentas.indexOf(".txt"));
            ventasPorVendedor.put(nombreVendedor, ventasTotales);
        }
        return ventasPorVendedor;
    }

    private List<String> obtenerArchivosEnDirectorio(String directorio) throws IOException {
        try (Stream<String> stream = Files.walk(new File(directorio).toPath())
                .filter(Files::isRegularFile)
                .map(p -> p.toFile().getName())) {
            return stream.collect(Collectors.toList());
        }
    }

    private int obtenerTotalVentas(String archivoVentas) throws IOException {
        List<String> lineas = Files.readAllLines(new File("ventas/" + archivoVentas).toPath());
        return lineas.size();
    }

    private void crearReporteVentas(Map<String, Integer> ventasPorVendedor) throws IOException {
        FileWriter escritor = new FileWriter("reporte_ventas.csv");
        ventasPorVendedor.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    try {
                        escritor.write(entry.getKey() + ";" + entry.getValue() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        escritor.close();
    }

    private Map<String, Integer> obtenerVentasPorProducto() throws IOException {
        Map<String, Integer> ventasPorProducto = new HashMap<>();
        List<String> lineas = Files.readAllLines(new File("productos.txt").toPath());
        for (String linea : lineas) {
            String[] partes = linea.split(";");
            ventasPorProducto.put(partes[1], 0);
        }
        List<String> archivosVentas = obtenerArchivosEnDirectorio("ventas");
        for (String archivoVentas : archivosVentas) {
            List<String> lineasVentas = Files.readAllLines(new File("ventas/" + archivoVentas).toPath());
            for (String venta : lineasVentas) {
                String[] partes = venta.split(";");
                String nombreProducto = partes[0];
                if (ventasPorProducto.containsKey(nombreProducto)) {
                    int ventasActuales = ventasPorProducto.get(nombreProducto);
                    ventasPorProducto.put(nombreProducto, ventasActuales + 1);
                }
            }
        }
        return ventasPorProducto;
    }

    private void crearReporteVentasProducto(Map<String, Integer> ventasPorProducto) throws IOException {
        FileWriter escritor = new FileWriter("reporte_ventas_productos.csv");
        ventasPorProducto.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    try {
                        escritor.write(entry.getKey() + ";" + entry.getValue() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        escritor.close();
    }
}
