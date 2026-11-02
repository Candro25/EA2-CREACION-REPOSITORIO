package com.tecnomovil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public record Registro(String idUsuario, String ruta, String estacion,
                           String accion, LocalDateTime timestamp) {
        public Registro {
            Objects.requireNonNull(idUsuario);
            Objects.requireNonNull(ruta);
            Objects.requireNonNull(estacion);
            Objects.requireNonNull(accion);
            Objects.requireNonNull(timestamp);

            if (!accion.equals("entrada") && !accion.equals("salida")) {
                throw new IllegalArgumentException("La accion debe ser entrada o salida.");
            }
        }
    }

    public static Map<String, Long> afluenciaPorEstacion(List<Registro> registros) {
        return registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Registro::estacion, TreeMap::new,
                                Collectors.counting()),
                        Collections::unmodifiableMap));
    }

    public static Map<Integer, Long> horasPico(List<Registro> registros) {
        Map<Integer, Long> conteo = registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(
                        r -> r.timestamp().getHour(),
                        TreeMap::new,
                        Collectors.counting()));

        return conteo.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (a, b) -> a, LinkedHashMap::new),
                        Collections::unmodifiableMap));
    }

    public static Map<String, Long> rutasMasUtilizadas(List<Registro> registros) {
        Map<String, Long> conteo = registros.stream()
                .filter(r -> r.accion().equals("entrada"))
                .collect(Collectors.groupingBy(
                        Registro::ruta, Collectors.counting()));

        return conteo.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (a, b) -> a, LinkedHashMap::new),
                        Collections::unmodifiableMap));
    }

    public static Map<String, List<String>> patronesPorUsuario(List<Registro> registros) {
        Map<String, List<String>> resultado = registros.stream()
                .sorted(Comparator.comparing(Registro::timestamp))
                .collect(Collectors.groupingBy(
                        Registro::idUsuario,
                        TreeMap::new,
                        Collectors.mapping(Registro::estacion, Collectors.toList())));

        Map<String, List<String>> copia = resultado.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> List.copyOf(e.getValue()),
                        (a, b) -> a,
                        TreeMap::new));

        return Collections.unmodifiableMap(copia);
    }

    public static OptionalDouble tiempoPromedioEntreEstaciones(List<Registro> registros) {
        List<Long> minutos = registros.stream()
                .collect(Collectors.groupingBy(Registro::idUsuario))
                .values().stream()
                .flatMap(lista -> {
                    List<Registro> ordenados = lista.stream()
                            .sorted(Comparator.comparing(Registro::timestamp))
                            .toList();

                    return IntStream.range(1, ordenados.size())
                            .mapToObj(i -> Duration.between(
                                    ordenados.get(i - 1).timestamp(),
                                    ordenados.get(i).timestamp()).toMinutes());
                })
                .filter(m -> m >= 0)
                .toList();

        if (minutos.isEmpty()) {
            return OptionalDouble.empty();
        }

        return minutos.stream().mapToLong(Long::longValue).average();
    }

    public static Map<String, String> detectarSobrecarga(
            List<Registro> registros, long limiteEntradas) {
        return rutasMasUtilizadas(registros).entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> e.getValue() > limiteEntradas ? "CRITICA" : "NORMAL"));
    }

    public static List<Registro> crearDatosSimulados(int cantidad) {
        List<String> usuarios = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> String.format("U%03d", i))
                .toList();

        List<String> rutas = List.of("RUTA-01", "RUTA-02", "RUTA-03", "RUTA-04");
        List<String> estaciones = List.of(
                "Centro", "Universidad", "Parque",
                "Terminal Norte", "Hospital", "Estadio");

        LocalDateTime inicio = LocalDateTime.of(2026, 10, 20, 5, 0);

        return IntStream.range(0, cantidad)
                .mapToObj(i -> new Registro(
                        usuarios.get(i % usuarios.size()),
                        rutas.get((i * 7 + i / 13) % rutas.size()),
                        estaciones.get((i * 3 + i / 9) % estaciones.size()),
                        i % 3 == 0 ? "salida" : "entrada",
                        inicio.plusMinutes((i / usuarios.size()) * 14L)))
                .toList();
    }

    public static void main(String[] args) {
        List<Registro> registros = crearDatosSimulados(10_000);

        System.out.println("TECNOMOVIL DATA");
        System.out.println("Registros: " + registros.size());

        System.out.println("\nAfluencia por estacion:");
        afluenciaPorEstacion(registros).forEach((estacion, cantidad) ->
                System.out.println(estacion + ": " + cantidad));

        System.out.println("\nHoras con mayor movimiento:");
        horasPico(registros).entrySet().stream().limit(5).forEach(e ->
                System.out.println(String.format("%02d:00", e.getKey())
                        + ": " + e.getValue()));

        System.out.println("\nRutas mas utilizadas:");
        rutasMasUtilizadas(registros).forEach((ruta, cantidad) ->
                System.out.println(ruta + ": " + cantidad));

        System.out.println("\nPatrones de viaje:");
        patronesPorUsuario(registros).entrySet().stream().limit(3).forEach(e ->
                System.out.println(e.getKey() + ": "
                        + e.getValue().stream().limit(8).toList()));

        System.out.println("\nTiempo promedio entre registros:");
        OptionalDouble promedio = tiempoPromedioEntreEstaciones(registros);
        if (promedio.isPresent()) {
            System.out.printf("%.2f minutos%n", promedio.getAsDouble());
        } else {
            System.out.println("No hay suficientes registros.");
        }

        System.out.println("\nEstado de las rutas:");
        detectarSobrecarga(registros, 2_000).forEach((ruta, estado) ->
                System.out.println(ruta + ": " + estado));

        System.out.println("\nLa lista conserva " + registros.size() + " registros.");
    }
}
