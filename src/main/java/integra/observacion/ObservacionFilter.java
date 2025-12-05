package integra.observacion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ObservacionFilter {
    private Integer supervisorId;
    private Integer unidadId;
    private Integer areaId;
    private Integer departamentoResponsableId;
    private Integer departamentoColaboradorId;
    private Integer departamentoGeneraId;
    private Integer zonaId;
    private String prioridad;
    private Integer tipoObservacionId;
    private Integer estatusId;
    private String folio;
    private Integer usuarioCreadorId;
    private LocalDateTime creadoDesde;
    private LocalDateTime creadoHasta;
    private Integer pagina;
    private Integer filas;
    private Boolean privado;

}