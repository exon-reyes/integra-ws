package integra.config.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfiguracionService {

    private final ParametroRepository parametroRepository;

    public Map<String, Object> cargarParametrosGlobales() {
        Map<String, Object> parametros = new HashMap<>();
        List<ParametrosAppEntity> lista = parametroRepository.findAll();

        for (ParametrosAppEntity p : lista) {
            parametros.put("app.config." + p.getNombre(), p.getValor());
        }
        return parametros;
    }
}
