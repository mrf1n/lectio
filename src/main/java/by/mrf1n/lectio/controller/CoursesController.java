package by.mrf1n.lectio.controller;

import by.mrf1n.lectio.model.User;
import by.mrf1n.lectio.model.course.Course;
import by.mrf1n.lectio.repository.UserRepository;
import by.mrf1n.lectio.repository.coursse.CourseRepository;
import by.mrf1n.lectio.service.LectioUiService;
import by.mrf1n.lectio.service.validation.LectioInputValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/courses")
public class CoursesController {

    private final LectioUiService lectioUiService;
    private final LectioInputValidationService inputValidationService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CoursesController(LectioUiService lectioUiService,
                             LectioInputValidationService inputValidationService,
                             CourseRepository courseRepository,
                             UserRepository userRepository) {
        this.lectioUiService = lectioUiService;
        this.inputValidationService = inputValidationService;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/catalog")
    public String getCatalogPage(Model model, Authentication authentication) {
        lectioUiService.fillRolesModelByLogin(authentication.getName(), model);
        return "courses/catalog";
    }

    @GetMapping("/new")
    public String getNewCoursePage(Model model, Authentication authentication) {
        lectioUiService.fillRolesModelByLogin(authentication.getName(), model);
        model.addAttribute("course", new Course());
        return "courses/new";
    }

    @PostMapping("/add")
    public String getAddCourse(@ModelAttribute("course") Course course,
                               BindingResult result,
                               Model model,
                               Authentication authentication) {
        lectioUiService.fillRolesModelByLogin(authentication.getName(), model);
        if (inputValidationService.validateCourseName(course, result)) return "courses/new";
        Optional<User> user = userRepository.findByLogin(authentication.getName());
        if (user.isPresent()) {
            user.ifPresent(course::setCreator);
            Set<User> users = Collections.singleton(user.get());
            course.setTeachers(users);
            Course save = courseRepository.save(course);
            return "redirect:/courses/" + save.getId();
        }
        return "courses/new";
    }

    @GetMapping("/{id}")
    public String getCoursePage(Model model,
                                @PathVariable Long id,
                                Authentication authentication) {
        lectioUiService.fillRolesModelByLogin(authentication.getName(), model);
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/course";
        }
        return "redirect:/error";
    }

    @GetMapping("/{id}/plan")
    public String getPlanOfCoursePage(Model model,
                                      @PathVariable Long id,
                                      Authentication authentication) {
        lectioUiService.fillRolesModelByLogin(authentication.getName(), model);
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("course_page", "plan");
            return "courses/course";
        }
        return "redirect:/error";
    }
}
