# TecnoMovil Data

Proyecto de la actividad EA2 de la Unidad 2.

## Descripcion

El programa procesa registros simulados de transporte urbano. Incluye conteos por estacion, horas de mayor movimiento, rutas mas utilizadas, patrones de viaje, tiempo promedio entre registros y deteccion de rutas con alta cantidad de entradas.

## Requisitos

- Java JDK 17 o superior.
- No necesita frameworks ni librerias externas.

## Ejecucion

Desde esta carpeta, compilar:

```bash
javac -encoding UTF-8 -d out src/main/java/com/tecnomovil/Main.java
```

Ejecutar:

```bash
java -cp out com.tecnomovil.Main
```

En Windows tambien se pueden usar `compilar.bat` y `ejecutar.bat`.

## Datos de prueba

Se generan 10.000 registros simulados para 100 usuarios, cuatro rutas y seis estaciones. No son datos reales.

El limite para marcar una ruta como critica es de 2.000 entradas. Es un valor usado para la prueba.

## Archivos

- `src/main/java/com/tecnomovil/Main.java`: codigo fuente.
- `evidencias/`: salida de la ejecucion.
- `compilar.bat`: compila el programa en Windows.
- `ejecutar.bat`: ejecuta el programa en Windows.
