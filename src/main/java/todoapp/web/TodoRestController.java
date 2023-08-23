package todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import todoapp.core.todos.application.TodoEditor;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

import java.util.List;
import java.util.Map;

@RestController
@Controller
public class TodoRestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TodoFinder finder;
    private final TodoEditor editor;

    public TodoRestController(TodoFinder finder, TodoEditor editor) {
        this.finder = finder;
        this.editor = editor;
    }

    @GetMapping("/api/todos")
    public List<Todo> list() {
        return finder.getAll();
    }

    @PostMapping("/api/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody CreateTodoCommand command) {
        logger.debug("request command: {}", command);

        editor.create(command.getTitle());
    }

    static class CreateTodoCommand {

        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
