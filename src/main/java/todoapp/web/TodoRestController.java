package todoapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

import java.util.Collections;
import java.util.List;

@RestController
@Controller
public class TodoRestController {

    private final TodoFinder finder;

    public TodoRestController(TodoFinder finder) {
        this.finder = finder;
    }

    @RequestMapping("/api/todos")
    @ResponseBody
    public List<Todo> list() {
        return finder.getAll();
    }
}
