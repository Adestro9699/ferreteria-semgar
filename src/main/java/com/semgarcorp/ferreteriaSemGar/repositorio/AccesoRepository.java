    package com.semgarcorp.ferreteriaSemGar.repositorio;


    import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
    import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface AccesoRepository extends JpaRepository<Acceso, Integer> {
        Optional<Acceso> findById(Integer idAcceso);

        // Método personalizado para buscar un acceso por usuario
        Optional<Acceso> findByUsuario(Usuario usuario);
    }
