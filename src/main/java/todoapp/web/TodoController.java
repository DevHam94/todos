package todoapp.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {

    private final SiteProperties siteProperties;

    public TodoController(SiteProperties siteProperties) {
        this.siteProperties = siteProperties;
    }

    @ModelAttribute("site")
    public SiteProperties siteProperties() {
        return siteProperties;
    }

    @RequestMapping("/todos")
    public void todos() throws Exception {
        //model.addAttribute("site", siteProperties);

        //SiteProperties site = new SiteProperties();
        //site.setAuthor(environment.getProperty("site.author"));
        //site.setDescription("스프링 MVC 할 일 관리 앱");

//        ModelAndView mav = new ModelAndView();
//        mav.addObject("site", siteProperties);
//        mav.setViewName("todos");

        //ViewResolver viewResolver = new InternalResourceViewResolver();
        //View view = viewResolver.resolveViewName("todos", null);;

        // viewName = "todos"
        // prefix => classpath:/templates/
        // suffix => .html
        // fullViewName = classpath:/templates/todos.html
        //return "todos";
    }
}
