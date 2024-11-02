# vinilos-app-misw4203-2024-15

Vinilos es una aplicación diseñada para visualizar y gestionar información relacionada con la 
música, como álbumes, artistas y coleccionistas. Para esta versión móvil, se utilizará una API REST 
previamente desarrollada.

* [API REST](https://api-backvynils-misw4203-600c0ea84373.herokuapp.com/)

## Integrantes

| Nombre                         | Correo                         |
|--------------------------------|--------------------------------|
| Ian Pablo Beltrán Moreno       | ip.beltran@uniandes.edu.co     |
| Julio Cesar Sánchez Rodríguez  | jc.sanchezr1@uniandes.edu.co   |
| Sergio Augusto Celis Esteban   | s.celise@uniandes.edu.co       |
| Diego Felipe Jaramillo Álvarez | df.jaramilloa1@uniandes.edu.co |

## Prerrequisitos Generales

- Java 17
- Gradle 8.7
- Android Studio (Koala Feature Drop | 2024.1.2) o una versión más reciente

## Construcción y ejecución de la aplicación de forma local

1. Clonar [repositorio](https://github.com/jcsanchezr1/vinilos-app-misw4203-2024-15)
2. Abrir el proyecto con Android Studio
3. Sincronizar el archivo `build.gradle` para establecer las versiones adecuadas de los sistemas en
   su máquina y configurar el entorno para construir la aplicación.
4. Ejecute la aplicación mediante alguna de las siguientes opciones:

- **En el emulador de Android Studio:** Ejecutar la aplicación en un emulador de Android desde
  Android Studio. Crear y seleccionar el emulador, y luego hacer clic en el botón `Run App`.

- **En un dispositivo Android:** También puede utilizar un dispositivo Android real como emulador
  conectándolo a su máquina. Una vez conectado, seleccione el dispositivo y haga clic en el botón `
  Run App` en Android Studio para iniciar la aplicación.

**Nota:** Para la ejecución en un dispositivo Android se debe habilitar la opción de modo desarrollador.

## Ejecución de la aplicación (APK)

1. **Descargar el APK:** Obtener el archivo APK de la aplicación, ubicado en el directorio raíz del
   repositorio.
2. **Transferir el APK al dispositivo:** Conectar el dispositivo Android a la computadora mediante
   un cable USB y copiar el archivo APK al almacenamiento del dispositivo. Alternativamente, enviar 
   el APK al dispositivo a través de correo electrónico o un servicio de almacenamiento en la nube.
3. **Habilitar la instalación de aplicaciones desconocidas:** En el dispositivo, acceder a
   Configuración > Seguridad (o Aplicaciones, según el modelo) y activar la opción para permitir
   la instalación de aplicaciones de fuentes desconocidas.
4. **Instalar la aplicación:** Localizar el archivo APK en el dispositivo, abrirlo y seguir las
   instrucciones para instalarlo.
5. **Ejecutar la aplicación:** Una vez instalada, abrir la aplicación desde el menú de aplicaciones
   del
   dispositivo para comenzar a usarla.

## Ejecutar pruebas automatizadas con Espresso

1. Asegurarse de que el proyecto esté correctamente sincronizado con el archivo `build.gradle`
   configurado para pruebas en Espresso. Verificar que el dispositivo o emulador Android esté
   conectado y listo para recibir las pruebas.
2. En Android Studio, elegir un dispositivo físico conectado o un emulador en el que se ejecutarán
   las pruebas. Esto se puede hacer en la ventana de selección de dispositivos de Android Studio.
3. Navegar al paquete de pruebas en el árbol de archivos del proyecto `src/androidTest`.
4. Hacer clic derecho en la clase o paquete de pruebas que se desea ejecutar y seleccionar `Run '
   nombre_de_la_prueba'` para iniciar las pruebas específicas o Run Tests in `androidTest` para
   ejecutar todas las pruebas E2E.
5. Android Studio abrirá la ventana de resultados de pruebas donde se podrá observar el progreso y
   el estado de cada prueba.
6. Al finalizar la ejecución, revisar el reporte completo de las pruebas en la consola de Android
   Studio, donde se indicarán detalles sobre cada prueba ejecutada, incluyendo los casos exitosos y
   los fallidos.

<img width="1728" alt="Captura de pantalla 2024-11-02 a la(s) 10 03 42" src="https://github.com/user-attachments/assets/16589162-5cd0-4ecc-b2d7-5eaa7d3ad3fe">
