# SpatialMatch

# Desarrollo de un Sistema Web Orientado a Objetos para la Evaluación de Aptitudes Espaciales

**Universidad Americana (UAM)** **Asignatura:** Programación Orientada a Objetos (POO)  
**Grupo:** 9

**Equipo de Desarrollo:**
1. José Alessandro Torres Parodi
2. Cesar Eduardo Pérez Saavedra
3. Gunter Ian Zamora Bonilla
4. Marlon Emanuel Aguilar Ortega

---

## 1. Resumen Ejecutivo del Proyecto

El presente proyecto consiste en el diseño e implementación de una aplicación web transaccional orientada a la gestión y ejecución de pruebas psicotécnicas (específicamente el Test Espacial de Figuras Idénticas). El sistema está fundamentado en el paradigma de Programación Orientada a Objetos (POO) y hace uso del framework **OpenXava** para automatizar la generación de la interfaz de usuario y gestionar la capa de persistencia de datos a través de JPA (Java Persistence API).

El objetivo principal es proveer una plataforma que no solo valide respuestas de opción múltiple, sino que registre métricas telemétricas precisas (timestamps) de la interacción del usuario para su posterior análisis de rendimiento.

## 2. Arquitectura y Stack Tecnológico

El sistema se ha construido bajo una arquitectura multicapa, apoyándose fuertemente en el modelado del dominio (Domain-Driven Design) propuesto por OpenXava.

* **Lenguaje de Programación:** Java SE
* **Java Development Kit (JDK):** Eclipse Temurin 21 (Adoptium)
* **Framework Principal:** OpenXava 7.7 (Arquitectura basada en componentes de negocio)
* **Motor de Persistencia:** Hibernate (Implementación de JPA)
* **Gestor de Dependencias y Construcción:** Apache Maven (`pom.xml`)
* **Arquetipo Base:** `org.openxava:openxava-archetype-spanish`
* **Identificadores del Proyecto:**
    * `groupId`: `ni.edu.uam`
    * `artifactId`: `SpatialMatch`
* **Entorno de Desarrollo Integrado (IDE):** IntelliJ IDEA

## 3. Especificación del Modelo de Dominio (Entidades JPA)

Para dar soporte a la lógica de negocio y garantizar la integridad referencial, el modelo de datos se estructura en las siguientes clases persistentes (Entidades):

* **`Usuario`**: Gestiona el control de acceso y los roles del sistema. Separa lógicamente a los usuarios con privilegios de creación/administración de los usuarios en rol de evaluación (evaluados).
* **`Pregunta`**: Modela el reactivo del test. Encapsula los atributos de la matriz gráfica (la figura a evaluar), las cinco alternativas (A, B, C, D, E), la clave de la respuesta correcta y una relación `ManyToOne` hacia el `Usuario` autor.
* **`PruebaEvaluacion`**: Representa la instancia o sesión de una prueba asignada a un usuario específico. Actúa como entidad agregadora para consolidar la calificación final y el reporte de retroalimentación.
* **`RegistroRespuesta`**: Entidad transaccional crítica. Establece una relación asociativa entre la `PruebaEvaluacion` y la `Pregunta`. Encapsula la opción seleccionada por el evaluado y los atributos de trazabilidad temporal mediante el estereotipo `@Stereotype("DATETIME")`.

## 4. Requerimientos Funcionales Implementados

### 4.1. Módulo de Administración de Banco de Preguntas
* Operaciones CRUD (Create, Read, Update, Delete) para los reactivos del Test Espacial.
* Asignación obligatoria de la respuesta correcta y del autor intelectual de la pregunta.

### 4.2. Motor de Evaluación y Captura de Eventos (Cronometría)
El sistema registra marcas de tiempo exactas para cada transición de estado durante la ejecución de la prueba:
* `marcaTiempoInicio`: Captura el instante (`Timestamp`) en el que la interfaz renderiza la pregunta.
* `marcaTiempoRespuesta`: Instante en el que ocurre el evento de selección de una opción (A, B, C, D o E).
* `marcaTiempoNavegacion`: Instante de ejecución de la acción "Siguiente".
* `marcaTiempoFinalizacion`: Instante de la confirmación final del envío del test.
* **Nota Técnica:** La diferencia algorítmica entre estos atributos permite calcular la latencia de respuesta y el tiempo neto de resolución por reactivo.

### 4.3. Algoritmo de Calificación y Retroalimentación
Al desencadenarse la acción "Finalizar", el sistema ejecuta una rutina interna que:
1. Compara las opciones seleccionadas (`MetricaInteraccion`) con las claves correctas (`Pregunta`).
2. Tabula los aciertos y errores.
3. Calcula el tiempo promedio por respuesta y el tiempo global invertido.
4. Genera un reporte diagnóstico de aptitud espacial basado en los criterios de precisión y velocidad de procesamiento.

