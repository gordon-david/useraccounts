package gordon.api.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ExampleResource {

    // accessible publicly
    @GetMapping("/hometestresource")
    public String home() {
        return ("<h1>Welcome</h1>");
    }

    // accessible by bearers of a jwt with a USER role claim
    @GetMapping("/usertestresource")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    // accessible by bearers of a jwt with an ADMIN role claim
    @GetMapping("/admintestresource")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }
}
