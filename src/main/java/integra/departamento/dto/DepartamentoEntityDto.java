package integra.departamento.dto;

import integra.departamento.entity.DepartamentoEntity;

import java.io.Serializable;

/**
 * DTO for {@link DepartamentoEntity}
 */
public record DepartamentoEntityDto(Integer id, String nombre) implements Serializable {
}