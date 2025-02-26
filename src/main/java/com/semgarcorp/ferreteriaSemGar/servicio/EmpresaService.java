package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Empresa;
import com.semgarcorp.ferreteriaSemGar.repositorio.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepositorio;

    public EmpresaService(EmpresaRepository empresaRepositorio) {
        this.empresaRepositorio = empresaRepositorio;
    }

    // Metodo para listar todas las empresas
    public List<Empresa> listar() {
        return empresaRepositorio.findAll();
    }

    // Metodo para obtener una empresa por su ID
    public Empresa obtenerPorId(Integer id) {
        return empresaRepositorio.findById(id).orElse(null);
    }

    // Metodo para guardar una nueva empresa
    public Empresa guardar(Empresa empresa) {
        return empresaRepositorio.save(empresa);
    }

    // Metodo para actualizar una empresa existente
    public Empresa actualizar(Empresa empresa) {
        return empresaRepositorio.save(empresa);  // Esto persiste los cambios de la empresa existente
    }

    // Metodo para eliminar una empresa por su ID
    public void eliminar(Integer id) {
        empresaRepositorio.deleteById(id);
    }
}