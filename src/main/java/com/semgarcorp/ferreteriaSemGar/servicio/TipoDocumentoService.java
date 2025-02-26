package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoDocumento;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    // Metodo para listar todos los tipos de documento
    public List<TipoDocumento> listar() {
        return tipoDocumentoRepository.findAll();
    }

    // Metodo para obtener un tipo de documento por su ID
    public TipoDocumento obtenerPorId(Integer id) {
        return tipoDocumentoRepository.findById(id).orElse(null);
    }

    // Metodo para guardar un nuevo tipo de documento
    public TipoDocumento guardar(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }

    // Metodo para actualizar un tipo de documento existente
    public TipoDocumento actualizar(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);  // Esto persiste los cambios del tipo de documento existente
    }

    // Metodo para eliminar un tipo de documento por su ID
    public void eliminar(Integer id) {
        tipoDocumentoRepository.deleteById(id);
    }
}