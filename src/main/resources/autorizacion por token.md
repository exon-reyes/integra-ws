Flujo de Inicio de Sesión (Login)
🔹 Requerimientos

El backend debe autenticar al usuario y generar un JWT que contenga:

Los roles del usuario.

Los permisos especiales (solo los que sean explícitamente asignados al usuario).

🔹 Respuesta esperada del backend

En la respuesta del login, el backend debe enviar además una carga completa de todos los permisos asociados a esos
roles, para que el frontend (UI) pueda controlar visibilidad (por ejemplo, habilitar botones, secciones, etc.).

Esta lista completa de permisos solo se envía una vez en el login y se usa únicamente para fines visuales en la UI.

2️⃣ Flujo de Solicitudes Posteriores (Requests)
🔹 Descripción

En las peticiones posteriores, el frontend solo envía el JWT, que contiene los roles y los permisos especiales (no la
lista completa de permisos).

El backend debe interceptar cada petición, validar el JWT, y reconstruir la lista completa de permisos a partir de los
roles contenidos en el token.

🔹 Requerimientos técnicos

Para optimizar el rendimiento, el backend debe tener precargada en memoria una estructura (por ejemplo):

Map<String, List<String>> rolePermissions

Esta estructura contendrá los permisos agrupados por rol.

🔹 Funcionalidad esperada del Interceptor de seguridad

El Interceptor o Filter de seguridad debe realizar las siguientes acciones:

Verificar la validez del token (firma, expiración, issuer, etc.).

Obtener los roles y permisos especiales del JWT.

Cargar de memoria los permisos correspondientes a esos roles.

Fusionar estos permisos con los permisos especiales contenidos en el token.

Autorizar o denegar la petición antes de llegar al controlador, según las reglas de negocio.

3️⃣ Entregable esperado
🔹 Implementación solicitada

Generar el Interceptor (o Filter) completo en Spring Boot que implemente la lógica anterior.

Debe incluir:

Validación del JWT.

Extracción de roles y permisos.

Reconstrucción de permisos desde un mapa en memoria.

Aplicación de autorización previa al acceso a los controladores.

🔹 Estructura recomendada de clases o componentes

JwtProvider → Encargado de generar y validar JWT.

PermissionRegistry → Contiene el mapa de permisos agrupados por rol (cacheado en memoria).

JwtInterceptor → Interceptor o filtro de seguridad que aplica la validación y reconstrucción de permisos.

AuthController → Punto de entrada del login, encargado de generar el token y devolver los permisos completos.

🔹 Requisitos de calidad

El código debe:

Seguir buenas prácticas de arquitectura (capas separadas, responsabilidad única).

Ser thread-safe.

Cumplir con los principios SOLID.

Estar documentado de forma clara y mantenible.