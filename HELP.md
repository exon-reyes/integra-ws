DOCUMENTACIÓN DETALLADA DE LA PLATAFORMA DE GESTIÓN DE AUDITORÍAS Y SUPERVISIÓN

1. ANÁLISIS DEL ENTORNO
   La plataforma está diseñada para una empresa con una red de farmacias y una estructura jerárquica clara de
   supervisión y departamentos de apoyo. Su propósito principal es centralizar y estandarizar el registro, seguimiento y
   resolución de observaciones de auditoría, visitas de supervisión y reportes internos, asegurando la trazabilidad de
   cada acción y proporcionando métricas de rendimiento clave para la gestión estratégica.
   La plataforma busca:
   • Mejorar la comunicación entre farmacias, supervisores, auditores y departamentos de apoyo.
   • Incrementar la eficiencia operativa mediante la automatización de notificaciones y asignaciones.
   • Garantizar consistencia y calidad en el manejo de incidencias y observaciones.
   • Facilitar la generación de informes y dashboards estratégicos para la toma de decisiones.
   Roles y Actores Clave
   • Farmacias: Generan reportes internos de incidencias y problemas detectados en su operación.
   • Supervisores (Ventas): Realizan visitas de supervisión, generan reportes, monitorean unidades asignadas y validan
   la resolución de observaciones.
   • Auditores (Auditoría Interna): Ejecutan auditorías programadas, registran observaciones y reportes de auditoría.
   • Jefe de Auditores: Supervisa el desempeño del equipo de auditoría y garantiza la correcta ejecución de las
   auditorías.
   • Super Supervisor: Obtiene una visión estratégica global del estado operativo y de los reportes en toda la red de
   farmacias.
   • Departamentos de Apoyo (Inventario, Sistemas TI, Mantenimiento): Son responsables de atender y resolver las
   observaciones asignadas, pudiendo generar reportes internos propios.

________________________________________

2. ANÁLISIS DE REQUERIMIENTOS
   Requerimientos Funcionales (RF)
1. RF-01: Registro de observaciones y reportes por parte de Auditores, Supervisores, Farmacias y Departamentos de Apoyo.
2. RF-02: Cada observación debe estar vinculada a un Folio Único, generado automáticamente por el sistema o ingresado
   manualmente.
3. RF-03: Asignación automática de la observación a un único departamento responsable, según tipo de incidencia.
4. RF-04: Notificación automática al departamento responsable cuando se asigna una nueva observación.
5. RF-05: Los departamentos responsables pueden actualizar el estado de una observación a En Proceso o Resuelta.
6. RF-06: Adjuntar evidencia (fotos, documentos, enlaces) a cada observación y a su resolución.
7. RF-07: Panel de control para Supervisores y Auditores con listado de observaciones pendientes por farmacia.
8. RF-08: Capacidad para cerrar folios de auditoría/visita, evitando registros adicionales.
9. RF-09: Dashboards estratégicos para Jefe de Auditores y Super Supervisor con métricas de desempeño y tiempos de
   resolución.
10. RF-10: Supervisores pueden definir la visibilidad de un reporte para otros departamentos, incluso si no son
    responsables.
11. RF-11: Módulo de Colaboración para comentarios y adjuntos de departamentos con visibilidad compartida.
12. RF-12: Posibilidad de asignar sub-tareas a otros departamentos colaboradores.
13. RF-13: Reportes internos levantados por departamentos son visibles para Supervisores y Auditores de la farmacia
    correspondiente.
14. RF-14: Control de visibilidad de reportes para farmacias, mostrando solo los aprobados por el creador.
15. RF-15: Normalización de tipos de observación mediante un catálogo precargado, asegurando consistencia en los datos.
16. RF-16: Detección automática de duplicados: alerta cuando existe un reporte pendiente del mismo tipo para la misma
    farmacia y departamento responsable.
17. RF-17: Registro automático de un historial de seguimiento de cada acción sobre una observación: cambios de estado,
    comentarios, adjuntos y asignación de subtareas, incluyendo fecha y usuario responsable.
    Requerimientos No Funcionales (RNF)
    • RNF-01 (Seguridad): Implementación de un sistema de roles y permisos estricto para garantizar acceso controlado a
    cada funcionalidad.
    • RNF-02 (Usabilidad): Interfaz intuitiva, consistente y fácil de usar, adaptada a todos los roles.
    • RNF-03 (Rendimiento): Respuesta rápida del sistema, incluso con gran volumen de observaciones y usuarios
    simultáneos.
    • RNF-04 (Escalabilidad): Soporte para expansión de la red de farmacias, usuarios y volumen de datos sin degradación
    del rendimiento.

________________________________________

3. CASOS DE USO Y FLUJO DE TRABAJO
   CU-01: Auditor Inicia y Cierra una Auditoría
   • Actor: Auditor.
   • Descripción: El auditor visita una farmacia, genera un folio de auditoría, registra observaciones con evidencia y
   cierra el folio al finalizar.
   • Flujo:
1. Inicia auditoría → Folio generado en estado En Proceso.
2. Registra observaciones → Asignación automática al departamento responsable.
3. Adjunta evidencia → Fotos, documentos o enlaces.
4. Cierra el folio → Observaciones continúan activas hasta su resolución.
   CU-02: Supervisor Inicia y Cierra una Visita
   • Actor: Supervisor.
   • Descripción: Registro de observaciones durante visita de supervisión, asignación de departamento responsable y
   cierre del folio.
   CU-03: Farmacia Levanta Reporte Interno
   • Actor: Farmacia.
   • Descripción: Registro de incidencias internas que requieren atención de un departamento de apoyo.
   CU-04: Departamento de Apoyo Levanta Reporte Interno
   • Actor: Departamento de Apoyo.
   • Descripción: Registro de incidencias detectadas en campo, asignación a supervisor y notificación automática.
   CU-05: Departamento Responsable Resuelve una Observación
   • Actor: Departamento Responsable.
   • Descripción: Seguimiento, documentación y resolución de observaciones asignadas.
   CU-06: Departamento Colabora en Observación de Otro
   • Actor: Departamento Colaborador.
   • Descripción: Añade comentarios y evidencia a observaciones asignadas a otro departamento.
   CU-07: Auditor Consulta Historial de Farmacia
   • Actor: Auditor.
   • Descripción: Revisión de observaciones pendientes antes de iniciar nueva auditoría.
   CU-08: Supervisor Monitorea Unidades
   • Actor: Supervisor.
   • Descripción: Visualización y seguimiento de todas las observaciones de sus unidades en un dashboard filtrable por
   farmacia.
   CU-09: Super Supervisor Monitorea a Nivel Global
   • Actor: Super Supervisor.
   • Descripción: Acceso a métricas consolidadas, tendencias y desempeño sin necesidad de visualizar reportes
   individuales.
   CU-10: Detección Automática de Duplicados
   • Actor: Cualquier usuario que registre observaciones.
   • Flujo:
1. Usuario inicia registro → selecciona farmacia, tipo de observación y departamento.
2. Sistema verifica existencia de reporte similar.
3. Alertas y opciones para revisar o continuar el registro.
   CU-11: Registro de Historial Automático
   • Actor: Sistema.
   • Descripción: Cada acción sobre una observación queda registrada con usuario, fecha y descripción, garantizando
   trazabilidad completa.

________________________________________

4. ESCENARIOS DE PRUEBA DE ESCRITORIO
   Escenario A: Proceso de Auditoría y Resolución
   Paso Actor Acción Sistema Resultado Esperado
   1 Auditor Consulta historial de Farmacia Z Muestra 2 observaciones pendientes Auditor comprende el contexto previo
   2 Auditor Crea nueva auditoría Folio AUD-003 en estado "En Proceso"    Folio generado correctamente
   3 Auditor Registra observación "Falla de Router", asigna a Sistemas TI, no visible para farmacia Notificación enviada
   a Sistemas TI Farmacia no visualiza observación
   4 Auditor Registra observación "Desorden en Almacén", asigna a Inventario, visible para farmacia Notificación enviada
   a Inventario Farmacia visualiza observación
   5 Supervisor Accede a panel Muestra 4 observaciones pendientes de Farmacia Z Supervisor ve panorama completo
   6 Sistemas TI Resuelve observación de router Estado actualizado a "Resuelta"    Todos los paneles reflejan resolución
   Escenario B: Detección de Duplicados
   Paso Actor Acción Sistema Resultado Esperado
   1 Auditor Registra "Equipo dañado" en Farmacia Beta Observación registrada y notificación enviada Observación activa
   2 Supervisor Registra el mismo problema días después Sistema verifica duplicado Detección de observación existente
   3 Supervisor Recibe alerta de duplicado Presenta opción de revisar o continuar Evita duplicidad de registros
   Escenario C: Seguimiento Completo de una Observación
   Paso Actor Acción Registro en Historial Comentarios
   1 Auditor Crea observación asignada a Mantenimiento    "Observación creada"    Notificación enviada
   2 Empleado Mantenimiento Cambia estado a "En Proceso"    "Estado cambiado de 'Pendiente' a 'En Proceso'"    Inicia
   resolución
   3 Empleado Mantenimiento Agrega comentario    "Comentario añadido"    Documenta solicitud de ayuda
   4 Técnico de Sistemas Añade evidencia    "Evidencia adjuntada"    Documenta causa de problema
   5 Empleado Mantenimiento Marca como "Resuelta"    "Estado cambiado de 'En Proceso' a 'Resuelta'"    Cierre de tarea
   registrado

________________________________________

5. MÓDULOS PROPUESTOS
   • Módulo de Registro: Punto de entrada único para todas las observaciones y reportes.
   • Módulo de Gestión de Acciones Correctivas: Seguimiento y resolución de observaciones y reportes.
   • Módulo de Paneles de Control (Dashboards): Visualización personalizada por rol con métricas clave.
   • Módulo de Colaboración: Permite comunicación, comentarios y adjuntos entre departamentos en un mismo reporte.

________________________________________

6. ROLES PROPUESTOS
   • Auditor: Genera auditorías, registra observaciones y cierra folios.
   • Jefe de Auditores: Supervisa desempeño y cumplimiento de auditorías.
   • Supervisor: Realiza visitas, registra reportes y monitorea farmacias asignadas.
   • Super Supervisor: Gestión estratégica global, revisión de métricas y tendencias.
   • Farmacia: Genera reportes internos de incidencias.
   • Departamentos Responsables: Atienden observaciones y reportes asignados, colaboran y documentan resolución.

________________________________________

7. FLUJO DE TRABAJO GENERAL
1. Registro: Cualquier actor (Auditor, Supervisor, Farmacia o Departamento) crea un reporte u observación.
2. Asignación: El sistema asigna automáticamente al departamento responsable y envía notificaciones.
3. Seguimiento: El departamento responsable gestiona, documenta avances y cambia estados según resolución.
4. Colaboración: Otros departamentos pueden añadir comentarios, evidencia o realizar subtareas asignadas.
5. Monitoreo: Supervisores y auditores revisan el progreso en dashboards filtrables. Jefe de Auditores y Super
   Supervisor acceden a métricas globales.
6. Cierre: La observación se cierra cuando la solución es verificada y documentada.
7. Historial Automático: Todas las acciones quedan registradas con usuario, fecha y descripción, garantizando
   trazabilidad completa.

________________________________________

