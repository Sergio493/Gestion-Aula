# Instrucciones para agentes

## Contexto del proyecto

- Gestion-Aula es una API REST en Java 21 con Spring Boot, Spring Data JPA y MySQL.
- El código de la aplicación está en `src/main/java/es/sergioe/gestionaula`.
- Separa responsabilidades entre `controller`, `service`, `repository` y `model`.
- La documentación del proyecto y las respuestas dirigidas al equipo deben estar en español.

## Cambios de código

- Sigue las convenciones y patrones existentes; limita los cambios al alcance solicitado.
- Conserva las respuestas HTTP y los mensajes de error coherentes con los controladores y servicios existentes.
- Comprueba las referencias y las reglas de negocio antes de persistir reservas.
- No añadas dependencias ni cambies la configuración de base de datos sin necesidad.
- No incluyas credenciales, secretos ni datos reales en el código o la documentación.
- Si una propuesta de IA no está respaldada por el código o por una decisión explícita del equipo, preséntala como propuesta, no como hecho.
- No inventes prompts, decisiones, errores ni resultados de pruebas de sesiones anteriores. Identifica claramente los ejemplos y las limitaciones de información.

## Validación

- Ejecuta `./gradlew test` cuando el entorno tenga Java 21 y la configuración de MySQL requerida.
- Si la validación no puede ejecutarse por falta de infraestructura o configuración, indícalo sin afirmar que pasó.
- Añade o actualiza pruebas para cubrir cambios de comportamiento cuando sea viable.
