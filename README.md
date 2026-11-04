# Gestion-Aula

API REST para gestionar aulas, profesores, franjas horarias y la lógica de reservas de aulas. Está desarrollada con Java 21, Spring Boot, Spring Data JPA y MySQL; Gradle se utiliza para la compilación y las pruebas.

## Estado y alcance

Actualmente hay operaciones CRUD para aulas (`/aulas`), profesores (`/profesores`) y franjas horarias (`/franjas-horarias`). El listado de aulas admite los filtros opcionales `ordenadores` y `capacidad`; el filtro de capacidad devuelve aulas con capacidad superior al valor indicado.

Existe un servicio de reservas que valida la fecha, las referencias a aula/profesor/franja y la disponibilidad del aula para una fecha y franja. Sin embargo, en el código actual no hay un controlador REST de reservas, por lo que esa lógica no está expuesta como una API HTTP. Tampoco hay una interfaz de usuario.

## Requisitos y ejecución

- Java 21.
- Una instancia de MySQL y una base de datos creada para la aplicación.
- Gradle Wrapper incluido en el repositorio.

La configuración versionada solo establece el nombre de la aplicación. Antes de iniciar la aplicación hay que proporcionar URL, usuario y contraseña de MySQL mediante variables de entorno, por ejemplo:

```bash
SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/gestion_aula' \
SPRING_DATASOURCE_USERNAME='usuario' \
SPRING_DATASOURCE_PASSWORD='contraseña' \
./gradlew bootRun
```

Configura las credenciales para tu entorno; no las añadas al repositorio. La configuración actual no define una estrategia de creación o actualización del esquema, así que debe prepararse la base de datos de acuerdo con las entidades JPA antes de usar la aplicación.

Para ejecutar las pruebas:

```bash
./gradlew test
```

La prueba existente solo comprueba la carga del contexto de Spring. Como la aplicación depende de MySQL y no incluye una base de datos embebida ni una configuración de pruebas específica, las pruebas pueden requerir una base de datos accesible.

## Rutas disponibles

| Recurso | Ruta | Operaciones |
|---|---|---|
| Aulas | `/aulas` | Listar y filtrar, consultar por ID, crear, actualizar y eliminar |
| Profesores | `/profesores` | Listar, consultar por ID, crear, actualizar y eliminar |
| Franjas horarias | `/franjas-horarias` | Listar, consultar por ID, crear, actualizar y eliminar |

Las rutas para reservas no están implementadas en los controladores actuales. Las entidades principales son `Aula` (nombre, capacidad y disponibilidad de ordenadores), `Profesor` (nombre, apellidos y correo), `FranjaHoraria` (hora de inicio y fin) y `Reserva` (fecha y relaciones con los otros tres recursos).

## Problemas y límites identificados

- La lógica de reservas existe en `ReservaService`, pero no hay una ruta HTTP que permita utilizarla.
- La configuración de MySQL no está incluida, por lo que el arranque depende de que cada entorno aporte sus propios parámetros de conexión.
- La prueba disponible verifica únicamente que se carga el contexto; no cubre operaciones CRUD, validaciones ni conflictos de reservas.
- La clase del controlador de profesores se llama `ProfersorController` (con un error tipográfico), aunque su ruta sí es `/profesores`.
- Las entidades de aula y profesor no tienen validaciones explícitas de campos en sus controladores. La franja horaria sí comprueba que la hora inicial sea anterior a la final.
- La comprobación de disponibilidad de reservas busca coincidencias exactas de aula, fecha y franja. No se observa una restricción de base de datos que evite carreras entre solicitudes concurrentes.

Estas observaciones describen el código presente; no implican que todas se hayan producido o corregido durante una sesión con IA.

## Desarrollo asistido por IA

No se ha incluido un historial de conversaciones, decisiones del equipo ni revisiones previas de código generado por IA. Para no atribuir a sesiones anteriores información que no está disponible:

- **Decisiones observables en el código:** se usa una API REST organizada en controladores, servicios, repositorios y entidades JPA; las comprobaciones de negocio de reservas se concentran en `ReservaService`; y la persistencia prevista es MySQL. No puede afirmarse, sin el historial, que estas decisiones fueran consecuencia de una propuesta concreta de IA.
- **Prompts:** los siguientes son ejemplos representativos que podrían utilizarse para continuar el desarrollo; no se presentan como transcripciones exactas de prompts anteriores.

  > Revisa `ReservaService` y propone pruebas unitarias para validar fechas, referencias inexistentes y conflictos de disponibilidad. No cambies código hasta explicar los casos límite.

  > Añade un controlador REST para reservas siguiendo las convenciones existentes. Expón las operaciones disponibles y conserva los códigos HTTP y validaciones del servicio.

- **Errores cometidos por la IA:** no hay un registro verificable de errores producidos por IA en el material del repositorio. Por tanto, no es posible enumerarlos con rigor. Si se usa IA durante el desarrollo, documenta cada error comprobado con el cambio propuesto, el problema observado y la corrección aplicada; no registres como hecho una posibilidad o una sugerencia descartada.

## Estructura del código

```text
src/main/java/es/sergioe/gestionaula/
├── controller/   # Endpoints REST
├── model/        # Entidades JPA
├── repository/   # Repositorios Spring Data
└── service/      # Lógica de negocio de reservas
```
