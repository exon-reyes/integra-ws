package integra.unidad.validator;

public interface IUnidadValidator {
    void checkExisteClaveUnidad(String clave);

    void checkExisteTelefono(String telefono);

    void checkExisteNombre(String nombre);

    void checkExisteEmail(String email);
}