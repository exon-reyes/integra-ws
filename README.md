# Protocolo de Despliegue en Entornos Productivos

Este documento describe el procedimiento estandarizado para la compilación, empaquetado y ejecución de los módulos *
*Front-end (Angular)** y **Back-end (Spring Boot/Java)** en un entorno productivo, garantizando uniformidad, eficiencia
y continuidad operativa.

---

## 1. Materialización del Artefacto de Interfaz de Usuario (Angular)

La compilación del módulo Front-end debe ejecutarse utilizando la configuración de producción, habilitando
optimizaciones como *tree-shaking*, *minificación* y variables contextuales dedicadas al entorno.

### **Comando de compilación (Front-end)**

```bash
ng build --configuration=production
```

El desplegado debe realizarse utilizando exclusivamente el contenido generado en el directorio:

```
dist/
```

---

## 2. Compilación y Empaquetado del Componente de Procesamiento Central (Back-end)

El módulo Back-end utiliza **Maven** para la construcción del artefacto ejecutable **JAR** de Spring Boot.

### **Secuencia de operaciones (Back-end)**

```bash
mvn clean compile package
```

Este comando realiza:

* Limpieza de artefactos previos.
* Compilación del código fuente.
* Integración y empaquetado de dependencias en un único archivo `.jar`.

El artefacto resultante se ubicará normalmente en:

```
target/NOMBRE_EJECUTABLE.jar
```

---

## 3. Ejecución del Servicio y Optimización de la JVM

Una vez generado el artefacto, el servicio Back-end debe iniciarse aplicando parámetros optimizados de la **Java Virtual
Machine (JVM)** para asegurar estabilidad y uso eficiente de memoria, especialmente bajo carga.

### **Parámetros críticos de la JVM**

| Parámetro                       | Valor | Descripción                                                                |
|---------------------------------|-------|----------------------------------------------------------------------------|
| `-Xms512m`                      | 512MB | Asignación inicial de memoria para un arranque estable.                    |
| `-Xmx2g`                        | 2GB   | Límite máximo de heap para soportar picos de tráfico.                      |
| `-XX:+UseG1GC`                  | -     | Activación del Garbage Collector G1, recomendado para latencia predecible. |
| `-Dspring.profiles.active=prod` | prod  | Activación del perfil productivo de Spring Boot.                           |

---

## 4. Secuencia de Inicialización Final

Ejecutar el servicio reemplazando **NOMBRE_EJECUTABLE.jar** con el artefacto generado en la fase de empaquetado:

```bash
java -Xms512m -Xmx2g -XX:+UseG1GC -Dspring.profiles.active=prod -jar NOMBRE_EJECUTABLE.jar
```

---

## 5. Consideraciones Adicionales

* Verificar que el entorno cuente con la versión de Java soportada por el proyecto.
* Asegurar que las variables de entorno del perfil `prod` estén correctamente configuradas.
* Validar accesos, puertos y conectividad con fuentes de datos.

---

## 6. Control de Versiones del Procedimiento

Este protocolo debe actualizarse cuando existan cambios en:

* Estructura del proyecto.
* Parámetros de ejecución.
* Procesos de CI/CD.

---

**Documento generado para estandarizar el proceso de despliegue en producción.**
