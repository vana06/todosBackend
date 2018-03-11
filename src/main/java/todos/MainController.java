package todos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path="/api")
public class MainController {
    @Autowired// This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path="/add", consumes = "application/json") // Map ONLY POST Requests
    public @ResponseBody String addNewItem (@RequestBody List<TodosItem> todosItems) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Iterable<TodosItem> it = userRepository.findAll();

        L: for (TodosItem dbItem: it){
            long id = dbItem.getId();
            for (TodosItem item: todosItems) {
                if (item.getId() == id) {
                    userRepository.save(item);
                    todosItems.remove(item);
                    //System.out.println("update " + id + " " + dbItem.getText());
                    continue L;
                }
            }
            //System.out.println("delete" + id + " " + dbItem.getText());
            userRepository.delete(dbItem);
        }

        for (TodosItem item: todosItems){
            userRepository.save(item);
        }

        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<TodosItem> getAllItems() {
        // This returns a JSON or XML with the users
        return userRepository.findAllByOrderByIdAsc();
    }
}
