package integra.credenciales.service;

import integra.credenciales.query.CuentaEntityDto;
import integra.credenciales.repository.CuentaEntityRepository;
import integra.credenciales.repository.TipoCuentaRepository;
import integra.credenciales.request.FiltroCuenta;
import integra.model.TipoCuenta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CuentaQueryService {
    private final CuentaEntityRepository cuentaEntityRepository;
    private final TipoCuentaRepository tipoCuentaReposistory;

    public List<CuentaEntityDto> obtenerCuentasPorFiltro(FiltroCuenta filtro) {
        return cuentaEntityRepository.findAllWithRelations(CredencialSpecifications.conFiltro(filtro));
    }

    public List<TipoCuenta> obtenerTiposCuenta() {
        return this.tipoCuentaReposistory.findBy(TipoCuenta.class);
    }
}
