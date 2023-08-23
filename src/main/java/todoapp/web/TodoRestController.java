package todoapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import todoapp.core.todos.domain.Todo;

import java.util.Collections;
import java.util.List;

@Controller
public class TodoRestController {

    @RequestMapping("/api/todos")
    @ResponseBody
    public List<Todo> list() {
        return Collections.emptyList();
    }
}
