# Validación ejecutada

Estas comprobaciones se ejecutaron en el entorno de desarrollo con JDK 21.0.12.1, Maven 3.9.11 y PostgreSQL 17.11. No son capturas de IntelliJ, Swagger ni Postman.

- `mvn verify`: BUILD SUCCESS. 18 casos ejecutados, 0 fallos, 0 errores y 0 omitidos.
- Las pruebas usan PostgreSQL real mediante el perfil test, MockMvc y la cadena real de Spring Security. Cada prueba revierte su transacción.
- Se comprobó el Maven Wrapper con `./mvnw --version`: Maven 3.9.11.
- Se arrancó el JAR empaquetado y se realizaron peticiones HTTP reales: Swagger 200; registro público 201; área cero 400; login 200; reporte con INSPECTOR 200; reporte sin token 401.
- Se reinició la aplicación con los datos demostrativos habilitados. El reporte siguió mostrando 2, 2 y 1 monitoreos por fecha, sin duplicarse.
- Consulta directa a PostgreSQL: 6 zonas (cinco de demostración y una creada en la petición HTTP), 5 especies y 5 monitoreos. La zona creada por HTTP existe en la base de datos.

`http-results.json` contiene las respuestas y conteos resumidos, sin JWT ni credenciales. Los datos de la máquina de pruebas no viajan en el ZIP: para crearlos en tu PostgreSQL usa `DEMO_SEED=true` en una base vacía y registra zonas mediante Swagger.

No se ejecutó IntelliJ ni la aplicación de escritorio de Postman en este entorno. La colección se validó como JSON y está preparada para importarse. Abre el proyecto y obtén evidencias personales con tus propias herramientas, sin exponer secretos.
