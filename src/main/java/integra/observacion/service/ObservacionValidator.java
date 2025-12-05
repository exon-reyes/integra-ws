package integra.observacion.service;

import integra.globalexception.BusinessRuleException;
import integra.observacion.ObservacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObservacionValidator {
    private final ObservacionRepository observacionRepository;

    public void checkCambioEstatus(Integer id, Integer estatusId) {
        if (observacionRepository.esEstatusFinal(id)) {
            throw new BusinessRuleException("El estatus ya no puede ser modificado");
        }
    }
}
