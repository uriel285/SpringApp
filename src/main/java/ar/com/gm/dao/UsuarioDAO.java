package ar.com.gm.dao;

import ar.com.gm.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioDAO extends JpaRepository<Usuario, Long>{
    Usuario findByUsername(String username);
}
