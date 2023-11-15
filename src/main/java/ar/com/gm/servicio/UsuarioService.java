package ar.com.gm.servicio;

import ar.com.gm.dao.UsuarioDAO;
import ar.com.gm.domain.Rol;
import ar.com.gm.domain.Usuario;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class UsuarioService implements UserDetailsService{
    
    @Autowired
    private UsuarioDAO usuarioDAO;
    
    //@Autowired
    //private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.findByUsername(username);
        
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        } else {
            var roles = new ArrayList<GrantedAuthority>();
            
            for (Rol rol : usuario.getRoles()) {
                roles.add(new SimpleGrantedAuthority(rol.getNombre()));
            }
            
            String password = usuario.getPassword();

        // Crear un objeto UserDetails con el nombre de usuario, contraseña encriptada y roles
            UserDetails userDetails = new User(usuario.getUsername(), password, roles);

            return userDetails;
        }
    }
    
}
