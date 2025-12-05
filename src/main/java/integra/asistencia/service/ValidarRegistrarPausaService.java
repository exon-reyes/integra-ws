package integra.asistencia.service;

import integra.asistencia.repository.PausaModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ValidarRegistrarPausaService {
    private final PausaModelRepository repository;

    public void execute(Integer command) {
        repository.findFirstByAsistencia_Empleado_IdAndFinNullOrderByInicioDesc(command)
                .ifPresent(p -> {
                    throw new IllegalStateException("Ya existe una pausa activa para este empleado");
                });
    }
}
