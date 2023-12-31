package todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;
import todoapp.web.convert.TodoToSpreadsheetConverter;
import todoapp.web.model.SiteProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class TodoController {

    private final SiteProperties siteProperties;
    private final TodoFinder finder;

    public TodoController(SiteProperties siteProperties, TodoFinder finder) {
        this.siteProperties = siteProperties;
        this.finder = finder;
    }

    /*
    @ModelAttribute("site")
    public SiteProperties siteProperties() {
        return siteProperties;
    }
    */

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

    @RequestMapping(path = "/todos", produces = "test/csv")
    public void downloadTodos(Model model) {

        model.addAttribute("todos", new TodoToSpreadsheetConverter().convert(finder.getAll()));
    }

    public static class TodoCsvViewResolver implements ViewResolver {


        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            if("todos".equals(viewName)){
                return new TodoCsvView();
            }
            return null;
        }
    }

    public static class TodoCsvView extends AbstractView implements View {

        final Logger logger = LoggerFactory.getLogger(getClass());

        public TodoCsvView() {
            setContentType("text/csv");
        }

        @Override
        protected boolean generatesDownloadContent() {
            return true;
        }

        @Override
        protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
            logger.info("render model as csv content");

            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"todos.csv\"");
            response.getWriter().println("id, title, completed");

            List<Todo> todos = (List<Todo>) model.getOrDefault("todos", Collections.emptyList());
            for (Todo todo : todos) {
                String line = String.format("%d,%s,%s", todo.getId(), todo.getTitle(), todo.isCompleted());
                response.getWriter().println(line);
            }

            response.flushBuffer();
        }
    }
}
