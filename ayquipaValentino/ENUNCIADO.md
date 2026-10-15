# Enunciado proporcionado: EPPractico8254PAR.docx

Transcripción de texto; consultar el original en docs/ para el diagrama y su formato.

Universidad Peruana de Ciencias Aplicadas

Arquitectura de Aplicaciones Web

Examen Parcial

2026-20

Sedes: 8254

Indicaciones: 

La tolerancia de ingreso del examen es de 15 minutos.

Se puede utilizar cualquier tipo de IA.

El tiempo del examen práctico es de 60 minutos.

Puede utilizar los materiales de clase.

Es individual.

El plagio será penalizado con la anulación del examen.

Parte Práctica: (15 puntos) Se solicita elabore una API Backend que debe contar con las siguientes características:

Tecnologías 

Spring Boot

Spring DataJPA

Spring Security

Base de Datos – PostgreSQL

DTO

Swagger

Postman

API REST para gestionar la recuperación de humedales

Se solicita desarrollar una API REST para una organización ambiental que restaura humedales afectados por la contaminación y la expansión urbana. La aplicación permitirá administrar las zonas de intervención, las especies nativas y los monitoreos de recuperación ecológica.

HUB01: Registrar Zona 

Como desarrollador quiero registrar una zona para gestionarlas

Criterios de aceptación:

Se registra una zona, todos los campos son obligatorios.

De enviar un campo vacío, muestra mensaje de validación; así como el área debe ser positiva.

Muestra el código HTTP 201 con el objeto creado.

Se debe utilizar Swagger para su demostración personal.

El método no pide token.

Se deben registrar como mínimo 5 filas con datos coherentes.

El estudiante debe demostrar personalmente el funcionamiento de la API.

Utiliza el estilo de programación de clase.

La base de datos utilizada será PostgreSQL y se llamará BDParcialApellidoPaterno.

El nombre del proyecto es su apellidoPaternoPrimerNombre

HUB02: Cantidad de monitoreo por fecha 

Como administrador, quiero visualizar la cantidad de monitoreos por fecha para gestionarlos

Criterios de aceptación:

Se debe mostrar la fecha y la cantidad de monitoreos realizados.

De no existir registros muestra el array vacío.

Se debe utilizar Postman para la demostración personal.

El resultado debe mostrarse en formato JSON.

La ruta de método es:/monitors

El usuario con rol INSPECTOR podrá visualizar el reporte

La consulta debe realizarse mediante una consulta nativa SQL.

Se deben registrar como mínimo 5 filas con datos coherentes.

El estudiante debe demostrar personalmente el funcionamiento de la API.

Utiliza el estilo de programación de clase.

La base de datos utilizada será PostgreSQL y se llamará BDParcialApellidoPaterno.

El nombre del proyecto es su apellidoPaternoPrimerNombre

JSON:

[

{

 "date": "14/10/2026", 

"cantidad ": 100

}

]

Diagrama de base de datos

Zona

Especie

MonitoreoEcologico

idZona (PK)

idEspecie (PK)

idMonitoreo (PK)

name

commonName

recordDate

location

scientificName

waterQuality

areaHectares

conservationStatus

vegetationCoverage

interventionDate

ecologicalRole

faunaCount

status

idZona (FK)

observations

idZona (FK)

Rúbrica de Evaluación

Criterio de calificación

Excelente

Promedio

Deficiente

HUB01. Funcionalidad 1 

La funcionalidad 01- HUB01 solicitada en la historia de usuario se desarrolla al 100%, y cumple con todos los criterios de aceptación. La HU funciona al 100%.

El estilo de programación alinea 100% con lo desarrollado en clase.

Registra la funcionalidad solicitada en la historia de usuario, pero no cumple con todos los criterios de aceptación.

La HU funciona con algunos detalles faltantes. Usa código o estilo de programación no usada en clase.

No registra, tiene errores, no funciona.

No implementa o usa un estilo de programación no realizado en clase.

4 puntos

2 puntos

0 puntos

PreguntaHUB01

Responde sin problema y sin demora, explica el código lo manipula y sustenta lo realizado.

Se demora, duda, tiene miedo manipular código; responde con algunas imprecisiones.

No responde, se queda callado, se demora y responde con rodeos y no se enfoca en la pregunta formulada

4 puntos

2 puntos

0 puntos

HUB02. Consulta 1 

Muestra los datos solicitados según el criterio de aceptación de la HUB02.

Funciona al 100% lo solicitado.

El estilo de programación alinea 100% con lo desarrollado en clase.

Muestra los datos solicitados con inconsistencias según el criterio de aceptación de la HUB02.

La HU funciona con algunos detalles faltantes. Usa código o estilo de programación no usada en clase.

No muestra, no funciona.

No implementa o usa un estilo de programación no realizado en clase.

4 puntos

2 puntos

0 puntos

PreguntaHUB02

Responde sin problema y sin demora, explica el código lo manipula y sustenta lo realizado.

Se demora, duda, tiene miedo manipular código; responde con algunas imprecisiones.

No responde, se queda callado, se demora y responde con rodeos y no se enfoca en la pregunta formulada

3 puntos

1.5 puntos

0 puntos