package ar.com.gm.web;

import ar.com.gm.domain.Persona;
import ar.com.gm.servicio.PersonaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user) {
        var personas = personaService.listarPersonas();
        log.info("Ejecutando el controlador SpringMVC");
        log.info("Usuario logeado " + user);
        model.addAttribute("personas", personas);
        var saldoTotal = 0D;
        for(var p : personas){
            saldoTotal += p.getSaldo();
        }
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalClientes", personas.size());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/errores/403")
    public String error403() {
        return "errores/403";
    }

    @GetMapping("/agregar")
    public String agregar(Persona persona) {
        return "modificar";
    }

    @GetMapping("/listadoClientes")
    public String listadoClientes(Model model) {
        // Obtiene la lista de personas desde algún servicio o repositorio
        List<Persona> personas = personaService.listarPersonas();
        model.addAttribute("personas", personas);
        return "layout/listadoClientes";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errors) {
        if (errors.hasErrors()) {
            return "modificar";
        } else {
            personaService.guardar(persona);
            return "redirect:/";
        }
    }

    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona, Model model) {
        persona = personaService.encontrarPersona(persona);
        if (persona != null){
        model.addAttribute("persona", persona);
        return "modificar";
        }
        else{
            log.info("Persona null");
            return "redirect:/";
        }
    }

    @GetMapping("/eliminar")
    public String eliminar(Persona persona) {
        personaService.eliminar(persona);
        return "redirect:/";
    }

}
