TecnoMovil Data
Proyecto desarrollado para la actividad EA2. Creación de repositorio, de la Unidad 2 de Tecnología en Desarrollo de Software. La solución procesa registros simulados de un sistema de transporte urbano y aplica operaciones del paradigma funcional en Java.
¿Qué hace el programa?
El programa genera 10.000 registros de prueba y realiza los seis análisis planteados en el caso de estudio:
1. Cuenta las entradas registradas en cada estación.
2. Identifica las horas con mayor movimiento.
3. Organiza las rutas según la cantidad de entradas.
4. Muestra una secuencia de estaciones asociada a cada usuario.
5. Calcula el tiempo promedio entre registros consecutivos de un mismo usuario.
6. Marca las rutas que superan un límite de entradas definido para la prueba.
Los registros son simulados; no representan pasajeros ni recorridos reales. Para la prueba de sobrecarga se utiliza un límite de 2.000 entradas por ruta. Es un valor de referencia para comprobar la operación, no una capacidad real del sistema de transporte.
Requisitos
- Java JDK 17 o superior.
- Windows si se desea utilizar los archivos .bat.
- No se requieren frameworks ni librerías externas.
Organización del proyecto
EA2_TecnoMovilData/
├── src/main/java/com/tecnomovil/Main.java
├── evidencias/
│   ├── captura_ejecucion.png
│   └── salida_ejecucion.txt
├── compilar.bat
├── ejecutar.bat
├── .gitignore
└── README.md
Cómo compilar y ejecutar
Opción 1. Usar los archivos por lotes de Windows
Incluí compilar.bat y ejecutar.bat como una forma práctica de repetir los pasos de compilación y ejecución desde Windows. Al abrir compilar.bat, se crea la carpeta out si hace falta y se ejecuta javac para compilar el archivo fuente. Después, ejecutar.bat inicia la clase principal con java.

Esta fue una decisión de organización para facilitar las pruebas: en lugar de escribir los mismos comandos cada vez, puedo ejecutar cada paso con un archivo. Los .bat no son un requisito de la actividad, no reemplazan el código Java y no contienen la lógica del programa. Son instrucciones de consola guardadas para reutilizarlas.
Opción 2. Escribir los comandos manualmente
También puedo realizar el mismo proceso desde una terminal ubicada en la carpeta principal del proyecto. Para compilar, ejecuto:
javac -encoding UTF-8 -d out src/main/java/com/tecnomovil/Main.java
Y para ejecutar el programa:
java -cp out com.tecnomovil.Main
La primera instrucción compila el código fuente y guarda las clases generadas en out. La segunda le indica a Java dónde están esas clases y cuál es la clase principal que debe iniciar. En Windows, los archivos .bat automatizan esos mismos comandos; no hacen algo diferente.
Conceptos funcionales aplicados
- Registro inmutable: se utiliza record Registro para reunir el identificador del usuario, la ruta, la estación, la acción y la fecha y hora.
- Streams: permiten encadenar operaciones para filtrar, agrupar, ordenar, transformar y contar registros.
- Expresiones lambda: se usan para expresar condiciones y acciones directamente en las operaciones de los Streams.
- Funciones de análisis: cada requerimiento se resuelve en un método separado que recibe los registros y devuelve un resultado.
- Colecciones no modificables: los resultados se entregan como mapas o listas no modificables, y los análisis no alteran la lista original.
- Composición: detectarSobrecarga reutiliza el resultado de rutasMasUtilizadas en lugar de repetir el conteo.
Las operaciones se ejecutan con stream() normal. La solución evita modificar datos compartidos y puede servir como base para ensayar operaciones paralelas cuando el análisis y el volumen de datos lo justifiquen; esta entrega no afirma haber medido una mejora de rendimiento con parallelStream().
Datos de prueba y resultados
Se generan 10.000 registros para 100 usuarios, cuatro rutas y seis estaciones. Las marcas de tiempo son simuladas. En la ejecución guardada como evidencia se obtuvo un promedio de 14 minutos entre registros consecutivos del mismo usuario y la ruta RUTA-01 superó el umbral simulado de 2.000 entradas.

El promedio representa la diferencia temporal entre registros consecutivos del mismo usuario en los datos de prueba; no equivale necesariamente al tiempo real de desplazamiento entre estaciones.
Archivos principales
- src/main/java/com/tecnomovil/Main.java: código fuente completo.
- compilar.bat: automatiza la compilación en Windows.
- ejecutar.bat: inicia el programa en Windows.
- evidencias/: contiene la captura y la salida de la ejecución de prueba.
Repositorio
Enlace al repositorio personal: https://github.com/Candro25/EA2-CREACION-REPOSITORIO
