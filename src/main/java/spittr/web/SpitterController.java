package spittr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import spittr.entity.Spitter;
import spittr.data.SpitterRepository;

import javax.validation.Valid;

import java.io.File;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/spitter")
public class SpitterController {

    private SpitterRepository spitterRepository;

    @Autowired
    public SpitterController(SpitterRepository spitterRepository){
        this.spitterRepository = spitterRepository;
    }

    //使用Spring表单，设置了commandName属性之后需要在模型Model中有一个key为spitter的对象
    @RequestMapping(value="/register", method=GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute(new Spitter());
        return "registerForm";
    }

    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(
            @Valid SpitterForm spitterForm,
            Errors error) throws IllegalAccessException, IOException {

        if (error.hasErrors()){
            return "registerForm";
        }

        Spitter spitter = spitterForm.toSpitter();
        spitterRepository.save(spitter);
        MultipartFile profilePicture = spitterForm.getProfilePicture();
        profilePicture.transferTo(new File("D:/ProgramData/logs/" + spitter.getUsername() + ".jpg"));
        return "redirect:/spitter/" + spitter.getUsername();
    }

    @RequestMapping(value="/me", method=GET)
    public String me() {
        System.out.println("ME ME ME ME ME ME ME ME ME ME ME");
        return "home";
    }

    @RequestMapping(value = "/{username}", method = GET)
    public String showSpitterProfile(
            @PathVariable String username, Model model){
        if (!model.containsAttribute("spitter")){
            model.addAttribute(
                    spitterRepository.findByUsername(username));
        }
        return "profile";
    }
}
