# Proyecto Moviles- Guía de Instalación

¡Bienvenido al proyecto Moviles! Aquí encontrarás las instrucciones detalladas para configurar tanto el backend como el frontend de la aplicación.

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalados los siguientes componentes:

### Backend
- [Composer](https://getcomposer.org/download/): Descarga e instala Composer.

### Frontend
- [Android Studio](https://developer.android.com/studio): Descarga e instala Android Studio.
- [JDK](https://www.oracle.com/mx/java/technologies/downloads/): Descarga e instala JDK.

## Configuración del Backend

1. Clona el repositorio desde el siguiente enlace: [URL del Repositorio Backend](url_del_repositorio_backend).

2. Abre el proyecto en tu editor de código preferido.

3. Copia el archivo `.env.example` y pégalolo como un nuevo archivo llamado `.env`.

4. Modifica la línea `DB_PASSWORD=` en el archivo `.env` con la contraseña de tu base de datos.

5. Abre una terminal en la carpeta del backend y ejecuta los siguientes comandos:

   ```bash
   composer install
   php artisan migrate
   php artisan serve --host=0.0.0.0 --port=8000
   ```

   Esto instalará las dependencias, migrará la base de datos y pondrá en marcha el servidor.

## Configuración del Frontend

1. Descarga e instala [Android Studio](https://developer.android.com/studio) y [JDK](https://www.oracle.com/mx/java/technologies/downloads/).

2. Abre una terminal y ejecuta `ipconfig` para obtener la dirección IP de tu máquina.

3. Abre el proyecto en Android Studio.

4. Modifica la variable `IP` en el archivo `Config.kt` con la dirección IP obtenida en el paso anterior.

5. Ejecuta el proyecto en un dispositivo físico o emulador.

## Nota Importante

No es necesario realizar los pasos anteriores si solo deseas probar la aplicación. Puedes instalar la aplicación directamente utilizando el archivo `app-install.apk`.

¡Listo! Ahora deberías tener el backend y frontend configurados y funcionando correctamente. Si encuentras algún problema, por favor, consulta la sección de problemas comunes en el repositorio o ponte en contacto con el equipo de soporte.


