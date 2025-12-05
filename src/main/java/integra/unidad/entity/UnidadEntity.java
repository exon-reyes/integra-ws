package integra.unidad.entity;

import integra.empleado.EmpleadoEntity;
import integra.ubicacion.estado.entity.EstadoEntity;
import integra.ubicacion.zona.entity.ZonaEntity;
import integra.unidad.request.ActualizarUnidad;
import integra.unidad.request.NuevaUnidad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate  // Solo actualiza campos modificados
@Table(name = "unidad")
public class UnidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "clave", nullable = false, length = 10)
    private String clave;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 80)
    @Column(name = "localizacion", length = 80)
    private String localizacion;

    @Size(max = 15)
    @Column(name = "telefono", length = 15)
    private String telefono;

    @ColumnDefault("1")
    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private ZonaEntity zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private EstadoEntity estado;

    @Size(max = 255)
    @ColumnDefault("concat(`clave`, ' ', `nombre`)")
    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Size(max = 255)
    @Column(name = "direccion")
    private String direccion;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ultima_auditoria")
    private LocalDateTime ultimaAuditoria;

    @Column(name = "proxima_auditoria")
    private LocalDateTime proximaAuditoria;

    @ColumnDefault("0")
    @Column(name = "observaciones_pendientes")
    private Integer observacionesPendientes;

    @Column(name = "creado")
    private LocalDate creado;

    @Column(name = "actualizado")
    private LocalDateTime actualizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private EmpleadoEntity supervisor;

    @ColumnDefault("1")
    @Column(name = "requiere_camara")
    private Boolean requiereCamara;

    @Size(max = 5)
    @Column(name = "codigo_autorizacion_kiosco", length = 5)
    private String codigoAutorizacionKiosco;

    @Column(name = "requiere_codigo")
    private Boolean requiereCodigo;

    @ColumnDefault("1")
    @Column(name = "version_kiosco")
    private Integer versionKiosco;

    @Column(name = "tiempo_compensacion")
    private LocalTime tiempoCompensacion;


    public UnidadEntity() {
    }

    public UnidadEntity(Integer id) {
        this.id = id;
    }

    public UnidadEntity(NuevaUnidad params) {
        this.clave = params.getClave();
        this.nombre = params.getNombre();
        this.localizacion = params.getLocalizacion();
        this.telefono = params.getTelefono();
        this.activo = params.getActivo();
        this.zona = new ZonaEntity(params.getIdZona());
        this.estado = new EstadoEntity(params.getIdEstado());
        this.direccion = params.getDireccion();
        this.email = params.getEmail();
        this.creado = LocalDate.now();
    }

    public UnidadEntity(ActualizarUnidad params) {
        this((NuevaUnidad) params);
        this.id = params.getId();
    }
}