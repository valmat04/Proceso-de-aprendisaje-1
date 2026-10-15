# ayquipaValentino — Recuperación de humedales

Proyecto Maven para Valentino Ayquipa, basado en `docs/EPPractico8254PAR.docx`, sección 8254. Incluye HUB01 y HUB02. Requiere **JDK 21** y **PostgreSQL**. No contiene contraseñas ni tokens configurados.

## Abrir y ejecutar en IntelliJ IDEA

1. Descomprime el ZIP. En IntelliJ elige **File → Open** y selecciona la carpeta `ayquipaValentino` o su `pom.xml`. Confía en el proyecto e importa Maven.
2. En **File → Project Structure → Project SDK**, selecciona JDK 21. Para Maven puedes usar el Wrapper incluido o el Maven integrado de IntelliJ. Espera a que descargue las dependencias.
3. En PostgreSQL, con tu usuario administrador, crea la base **una sola vez**:
   ```sql
   CREATE DATABASE "BDParcialAyquipa";
   ```
   Si ya existe, úsala sin borrarla. Las comillas conservan las mayúsculas del nombre. Hibernate crea las tablas al iniciar; no crea la base de datos.
4. Abre `AyquipaValentinoApplication.java`. En **Run → Edit Configurations → Environment variables**, añade:

   | Variable | Valor que debes configurar |
   | --- | --- |
   | `DB_URL` | `jdbc:postgresql://localhost:5432/BDParcialAyquipa` |
   | `DB_USERNAME` | Tu usuario de PostgreSQL, por ejemplo `postgres` |
   | `DB_PASSWORD` | La contraseña de ese usuario |
   | `JWT_SECRET` | Un secreto aleatorio de al menos 32 bytes, exclusivo para tu entorno |
   | `INSPECTOR_USERNAME` | `inspector` (también es el valor predeterminado) |
   | `INSPECTOR_PASSWORD` | Una contraseña privada elegida por ti |
   | `DEMO_SEED` | `true` para cargar datos demostrativos en una BD sin registros; `false` para empezar sin datos |

   No compartas la configuración de ejecución si contiene secretos. El proyecto no carga automáticamente archivos `.env`.
5. Ejecuta la clase principal. El puerto predeterminado es 8080; puedes cambiarlo con `PORT`.
6. En tu navegador local abre `http://localhost:8080/swagger-ui/index.html`.

El ZIP incluye código fuente y Maven Wrapper. No incluye el JDK ni PostgreSQL. IntelliJ descargará las dependencias y necesitará conexión a Internet. En Windows puedes usar `mvnw.cmd`; en Linux/macOS, `./mvnw`.

## Operaciones

| Método | Ruta | Acceso | Resultado |
| --- | --- | --- | --- |
| POST | `/zones` | Público, sin token | 201 y objeto creado; 400 si faltan campos o el área no es positiva |
| POST | `/auth/login` | Público | 200 con JWT válido por una hora; 401 con credenciales incorrectas |
| GET | `/monitors` | JWT con rol `INSPECTOR` | 200 con fecha y cantidad; `[]` sin monitoreos; 401 sin token válido; 403 sin rol |

El enunciado no fija la ruta de registro de zonas; se eligió `/zones`. Las fechas JSON usan `dd/MM/yyyy`. Se conservó literalmente la clave **`"cantidad "` con espacio final**, tal como aparece en el ejemplo del documento. Si el docente confirma que ese espacio es un error tipográfico, hay que ajustar `MonitorReportDTO`, su prueba y la colección Postman de forma consistente.

### HUB01 con Swagger

En `POST /zones`, elige **Try it out**, ingresa y ejecuta:

```json
{
  "name": "Sector totoral",
  "location": "Humedal, ribera norte",
  "areaHectares": 12.5,
  "interventionDate": "14/10/2026",
  "status": "EN RESTAURACIÓN"
}
```

No uses Authorize para demostrar el acceso público. Repite con cinco zonas coherentes; la colección de Postman también incluye cinco ejemplos. Un área cero/negativa o un campo obligatorio vacío devuelve 400 y mensajes de validación. El identificador lo genera PostgreSQL; no se solicita en el DTO de entrada.

### HUB02 con Postman

Importa `postman/ayquipaValentino.postman_collection.json`. Configura `baseUrl` con tu puerto, y el valor **local** de `inspectorPassword` con tu contraseña. No exportes los valores privados ni el token generado.

Ejecuta **Login INSPECTOR**: la colección guarda temporalmente el JWT en la variable `token`. Luego ejecuta **Reporte con INSPECTOR**. El reporte usa una consulta **SQL nativa** con `GROUP BY record_date`, no JPQL ni un Query Method. Con los datos demostrativos recién cargados devuelve:

```json
[
  {"date": "12/10/2026", "cantidad ": 2},
  {"date": "13/10/2026", "cantidad ": 2},
  {"date": "14/10/2026", "cantidad ": 1}
]
```

La colección incluye peticiones sin token y con datos inválidos. La prueba automatizada adicional verifica el caso de un token válido sin el rol requerido.

## Datos de demostración y PostgreSQL

Con `DEMO_SEED=true`, `DemoDataConfig` carga cinco zonas, cinco especies y cinco monitoreos **sólo si las tres tablas están vacías**. En siguientes arranques no modifica ni duplica esos registros. No elimina datos. Si alguna tabla ya tiene registros, no carga el conjunto demostrativo; esto evita mezclarlo con trabajo previo. El rol `INSPECTOR` se configura en memoria a partir de variables de entorno; no hay contraseñas en las tablas.

Para comprobar la persistencia con pgAdmin o psql, conectado a `BDParcialAyquipa`:

```sql
SELECT id_zona, name, location, area_hectares, intervention_date, status FROM zona ORDER BY id_zona;
SELECT id_especie, common_name, scientific_name, id_zona FROM especie ORDER BY id_especie;
SELECT id_monitoreo, record_date, id_zona FROM monitoreo_ecologico ORDER BY id_monitoreo;
SELECT record_date AS date, COUNT(*) AS cantidad
FROM monitoreo_ecologico GROUP BY record_date ORDER BY record_date;
```

Las entidades usan IDs `Integer`, fechas `LocalDate`, área/calidad/cobertura `Double` y conteos `Integer`. El documento no especifica los tipos exactos ni una unidad para `waterQuality`; esos son tipos de implementación, sin inventar rangos o unidades de validación para esa entidad.

## Pruebas

Crea una base **separada y vacía**, una vez:

```sql
CREATE DATABASE "BDParcialAyquipaTest";
```

En una configuración Maven de IntelliJ, usa los objetivos `verify` y las mismas variables privadas de la aplicación, más:

- `TEST_DB_URL=jdbc:postgresql://localhost:5432/BDParcialAyquipaTest`
- `DEMO_SEED=false`

También puedes ejecutar `./mvnw verify` o `mvnw.cmd verify` desde una terminal con esas variables configuradas. Las pruebas activan el perfil `test` automáticamente y **exigen TEST_DB_URL**, para no dirigirse por defecto a la base de la aplicación. Las operaciones de cada prueba revierten su transacción; no borran tablas ni datos existentes. Usa la BD de prueba vacía para verificar el caso `[]`.

`verify` ejecuta 18 casos contra PostgreSQL real: registro público y persistencia, validaciones, fecha inválida, SQL agrupado, caso vacío, login, JWT inválido/vencido, ausencia de token, rol insuficiente, Swagger y OpenAPI. Para empaquetar sin ejecutar las pruebas: `./mvnw -DskipTests package`. El JAR resultante requiere las mismas variables al ejecutarse.

## Explicación del código

`@RestController` recibe JSON y devuelve respuestas HTTP. `@Valid` comprueba las anotaciones del DTO antes de llamar al servicio. `IZonaService` declara la operación y `ZonaServiceImpl` convierte el DTO en entidad y guarda mediante `ZonaRepository`. `@Entity`, `@Id` y `@GeneratedValue` representan tablas y claves generadas en PostgreSQL. `@ManyToOne` y `@JoinColumn` conectan especies y monitoreos con una zona.

El flujo es **Controller → Service → Repository → PostgreSQL**. Los controladores dependen de interfaces de servicio. El reporte reutiliza la proyección de la consulta SQL y expone sólo los campos del DTO, sin serializar entidades relacionadas. `@Transactional` delimita las operaciones y el reporte es de sólo lectura.

Spring Security deja público el registro de zonas y restringe el reporte a `INSPECTOR`. El login valida la contraseña mediante BCrypt y emite un JWT firmado con HS256. Spring Security valida firma y vencimiento y convierte el claim `roles` en autoridades. No se agregaron endpoints CRUD de especies o monitoreos porque el documento no los solicita.

No se proporcionaron ejemplos de clase, por lo que debes comparar esta implementación con las convenciones específicas que use tu docente. Los DTO usan records de Java 21 y la validación JWT usa el soporte estándar de Spring Security. Revisa `ENUNCIADO.md` y el DOCX original antes de adaptar requisitos.

## Evidencias para la evaluación

Consulta `docs/VALIDACION.md` para los resultados ejecutados. No hay capturas inventadas. Obtén tus propias capturas: Swagger mostrando la petición sin token y respuesta 201; validaciones 400; Postman mostrando el reporte autenticado y su JSON; pgAdmin mostrando los cinco registros. No captures ni compartas contraseñas, JWT, el contenido de Authorize o variables privadas. Debes poder explicar y demostrar personalmente el código.
