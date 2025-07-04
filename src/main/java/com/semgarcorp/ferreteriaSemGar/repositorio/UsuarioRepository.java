    package com.semgarcorp.ferreteriaSemGar.repositorio;

    import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
    import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
        Optional<Usuario> findByNombreUsuario(String nombreUsuario);
        
        // Buscar usuario por trabajador
        Optional<Usuario> findByTrabajador(Trabajador trabajador);
    }