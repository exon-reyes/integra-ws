package integra.security;

public final class Autoridades {

    // ================== GENERALES (A) ==================
    public static final String VER_MODULO_GENERALES = "hasAuthority('A')";

    // ------ UNIDADES (AA) ------
    public static final String VER_SUBMODULO_UNIDADES = "hasAuthority('AA')";
    public static final String CONSULTAR_UNIDADES = "hasAuthority('AA1')";
    public static final String EDITAR_UNIDAD = "hasAuthority('AA2')";
    public static final String ELIMINAR_UNIDAD = "hasAuthority('AA3')";
    public static final String CREAR_UNIDAD = "hasAuthority('AA5')";
    public static final String EXPORTAR_UNIDAD = "hasAuthority('AA6')";

    // ------ ZONAS (AB) ------
    public static final String VER_SUBMODULO_ZONAS = "hasAuthority('AB')";
    public static final String CREAR_ZONA = "hasAuthority('AB2')";
    public static final String EDITAR_ZONA = "hasAuthority('AB3')";
    public static final String ELIMINAR_ZONA = "hasAuthority('AB4')";

    // ================== GESTIÓN RRHH (B) ==================
    public static final String VER_MODULO_RRHH = "hasAuthority('B')";

    // ------ EMPLEADOS (BA) ------
    public static final String VER_SUBMODULO_EMPLEADOS = "hasAuthority('BA')";
    public static final String CONSULTAR_INFORME_EMPLEADOS = "hasAuthority('BA1')";
    public static final String VER_INDICADORES_EMPLEADOS = "hasAuthority('BA2')";
    public static final String EXPORTAR_EMPLEADOS = "hasAuthority('BA3')";
    public static final String RESTRINGIR_CONSULTA_SUPERVISOR_EMPL = "hasAuthority('BA4')";

    // ------ INFORME ASISTENCIA (BB) ------
    public static final String VER_SUBMODULO_INFORME_ASISTENCIA = "hasAuthority('BB')";
    public static final String CONSULTAR_INFORME_ASISTENCIA = "hasAuthority('BB1')";
    public static final String RESTRINGIR_CONSULTA_SUPERVISOR_ASIST = "hasAuthority('BB2')";
    public static final String EXPORTAR_ASISTENCIA = "hasAuthority('BB3')";

    // ------ CONFIG OPENTIME (BC) ------
    public static final String VER_SUBMODULO_CONFIG_OPENTIME = "hasAuthority('BC')";
    public static final String CONSULTAR_CONFIGURACION = "hasAuthority('BC1')";
    public static final String ACTIVAR_DESACTIVAR_CAMARA = "hasAuthority('BC2')";
    public static final String MODIFICAR_TIEMPOS_COMP = "hasAuthority('BC3')";
    public static final String APROBAR_CONFIG_PERSONALIZADA = "hasAuthority('BC4')";
    public static final String VER_INDICADORES_USO = "hasAuthority('BC5')";

    // ------ COMPENSACIONES (BD) ------
    public static final String VER_SUBMODULO_COMPENSACIONES = "hasAuthority('BD')";
    public static final String VER_COMPENSACIONES_APLICADAS = "hasAuthority('BD1')";
    public static final String RESTRINGIR_CONSULTA_SUPERVISOR_COMP = "hasAuthority('BD2')";
    public static final String EXPORTAR_COMPENSACIONES = "hasAuthority('BD3')";
    public static final String ACCESO_RELOJ_CHECADOR = "hasAuthority('BD4')";

    // ================== INFRAESTRUCTURA TI (C) ==================
    public static final String ACCESO_MODULO_INFRA = "hasAuthority('C')";

    // ------ ROLES (CA) ------
    public static final String VER_SUBMODULO_ROLES = "hasAuthority('CA')";
    public static final String VER_ROLES_PERMISOS = "hasAuthority('CA1')";
    public static final String ELIMINAR_ROL = "hasAuthority('CA2')";
    public static final String EDITAR_ROL = "hasAuthority('CA3')";
    public static final String CREAR_ROL = "hasAuthority('CA4')";

    // ------ USUARIOS (CB) ------
    public static final String VER_SUBMODULO_USUARIOS = "hasAuthority('CB')";
    public static final String CONSULTAR_USUARIOS = "hasAuthority('CB1')";
    public static final String CREAR_USUARIO = "hasAuthority('CB2')";
    public static final String EDITAR_USUARIO = "hasAuthority('CB3')";
    public static final String DESACTIVAR_USUARIO = "hasAuthority('CB4')";

    // ------ CREDENCIALES (CC) ------
    public static final String VER_SUBMODULO_CREDENCIALES = "hasAuthority('CC')";
    public static final String CONSULTAR_CREDENCIALES = "hasAuthority('CC1')";
    public static final String EDITAR_CREDENCIALES = "hasAuthority('CC2')";
    public static final String ELIMINAR_CREDENCIALES = "hasAuthority('CC3')";
    public static final String EXPORTAR_CREDENCIALES = "hasAuthority('CC4')";
    public static final String CREAR_CREDENCIAL = "hasAuthority('CC5')";
    public static final String CREAR_PROVEEDOR = "hasAuthority('CC6')";
    public static final String CONSULTAR_PROVEEDORES = "hasAuthority('CC7')";
    public static final String EDITAR_PROVEEDOR = "hasAuthority('CC8')";
    public static final String ELIMINAR_PROVEEDOR = "hasAuthority('CC9')";

    private Autoridades() {
        // Evitar instanciación
    }
}