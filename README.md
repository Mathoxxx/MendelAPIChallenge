# Mendel API Challenge

Este proyecto es una implementación del Mendel API Challenge, construido como un microservicio RESTful para procesar transacciones financieras. La aplicación permite almacenar transacciones en memoria temporales, obtener transacciones específicas, listarlas por categoría y calcular la suma recursiva (incluyendo de forma transitiva el monto de las transacciones hijas) directamente vinculadas a una transacción principal.

## Stack Tecnológico

- **Java 21**: Lenguaje de programación.
- **Spring Boot 3**: Framework principal para el desarrollo ágil de la API.
- **Maven**: Herramienta de gestión de dependencias y empaquetado del proyecto.
- **Lombok**: Reducción de código repetitivo, inyectando métodos comunes (getters, setters, constructores) en tiempo de compilación.
- **JUnit 5 / Mockito**: Librerías base para instrumentación de pruebas unitarias y de integración utilizando el enfoque TDD.
- **Jacoco**: Herramienta de análisis para generar métricas visuales y reportes de cobertura de código.
- **SpringDoc (Swagger)**: Implementación de OpenAI para documentación interactiva de la API, auto-generando un entorno de pruebas visual.
- **Docker & Docker Compose**: Contenerización de la aplicación para aislar el entorno de ejecución, simplificando el proceso de despliegue a un solo comando.

## Características Principales

- Repositorio de datos nativo en memoria utilizando `ConcurrentHashMap`, para cumplir asumiendo la complejidad algorítmica constante garantizada de $O(1)$ subyacente o garantizada $O(n)$ indexaciones. 
- Inicialización e inyección de datos de ejemplo al inicializar la aplicación por medio de un archivo `seed-data.json`, facilitando las pruebas de despliegue en entornos nuevos.
- Alta confiabilidad del código fundamentada y validada iterativamente integrando Test-Driven Development (TDD). 
- Soporte nativo y rápido para despliegue aislado modularizado multi-etapa contenerizado.

## Ejemplos de Peticiones

### 1. Crear una transacción
Registra una transacción provista de un ID en el path. Si el identificador ya existe, lo sobrescribe en el mapa original e indexaciones. Opcionalmente, puede estar vinculada a una transacción padre mediante `parent_id`.
**PUT** `/transactions/{transaction_id}`

*Body:*
```json
{
  "amount": 5000.0,
  "type": "cars"
}
```

### 2. Obtener una transacción por ID
Busca y retorna un objeto de Transacción en base a tu request ID en la base de datos de estructura concurrente.
**GET** `/transactions/{transaction_id}`

*Respuesta (200 OK):*
```json
{
  "id": 10,
  "amount": 5000.0,
  "type": "cars",
  "parentId": null
}
```

### 3. Obtener transacciones por Tipo
Retorna una lista optimizada con los IDs de todas las transacciones pre-indexadas almacenadas correspondientes al tipo exacto provisto.
**GET** `/transactions/types/{type}`

*Respuesta:*
```json
[ 10, 11 ]
```

### 4. Obtener la suma recursiva de una transacción
Calcula la suma global del monto de una transacción específica, acumulado a los subtotales de todas las transacciones vinculadas (hijas) de manera transitiva o anidada.
**GET** `/transactions/sum/{transaction_id}`

*Respuesta:*
```json
{
  "sum": 15000.0
}
```

## Ejecución del Proyecto y Despliegue

### Usando Docker (Recomendado)
Para facilitar el empaquetado no necesitas tener Java ni Maven instalados. Simplemente clona este proyecto, ubícate en la raíz y ejecuta:

```bash
docker-compose up -d --build
```

La aplicación compilará automáticamente en un entorno de dos etapas, construirá los `.jars` subyacentes e interconectará el servicio en: `http://localhost:8080`.
Puedes probar activamente cada request a través la vista interactiva Swagger UI local accesible en: `http://localhost:8080/swagger-ui.html`.

Para detener la ejecución subyacente:
```bash
docker-compose down
```

### Ejecución Local con CLI nativa de Maven
Si prefieres construir y correr la aplicación nativamente:

1. Compilar el proyecto, verificar plugins y ejecutar la suite original de pruebas: `mvn clean install`
2. Ejecutar la app localmente con Java virtual machine: `mvn spring-boot:run`
3. Ejecutar independientemente solo las pruebas y el reporte HTML local de cobertura sobre los ejecutables core (Targets): `mvn clean test jacoco:report`

## Inteligencia Artificial en el Desarrollo

La construcción de este proyecto de microservicio Java contó con la participación directa integral desde su análisis hasta su contenedorización en un marco de **Pair Programming interactivo asistido por IA generativa agéntica**. La asistencia integral destacó en las siguientes ramas clave:
- **Diseño Arquitectónico, Best Practices y Refactorización Continua**: Adopción estricta y delegación interactiva bajo las regulaciones SOLID. El componente de lógica fue fraccionado lógicamente por capas jerárquicas delegadas (Controller, Service, Repository) haciendo uso activo para inyección rápida de estructuras concurrentes, permitiendo solventar proactivamente cualquier issue emergente.
- **Enfoque Unitario de Test-Driven Development (TDD)**: Enrutamiento y visualización transversal guiando dinámicamente el proyecto garantizado mediante la programación en flujo invertido; formulando la falla de la arquitectura de la batería de asserts para tests unitarios/test endpoints completos, refactorizando e infiriendo retroactivamente la lógica mínima estructural necesaria orientada a cumplir todos los pass coverage y resoluciones correctas.
- **DevOps y Documentación Activa (Toolings)**: Integración asistida para empaquetado e inicialización de librerías instrumentadas: Jacoco, automatización en visualizaciones iterativas integradas con Swagger OpenAI 3.0, estructuración agéntica de compilación con Maven Multi-Stages en archivos nativos configurados (Dockerfile) interconectando redes usando directivas de (docker-compose).

En general, la implementación activa delegando operaciones a un esquema agéntico en el flujo de desarrollo ha acelerado progresivamente desde la concepción integral, el planteamiento de requerimientos a la abstracción real funcional del despliegue, mejorando la certitud ante problemas o imprevistos lógicos nativos.
