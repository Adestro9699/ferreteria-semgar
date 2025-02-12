    package com.semgarcorp.ferreteriaSemGar.repositorio;

    import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
        Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    }