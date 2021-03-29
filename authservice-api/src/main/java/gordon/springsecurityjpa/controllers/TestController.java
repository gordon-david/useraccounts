package gordon.springsecurityjpa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class TestController {

    @GetMapping("/gettest")
    public static ResponseEntity<?> GetTest(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posttest")
    public static ResponseEntity<?> PostTest(){
        return ResponseEntity.ok().build();
    }
}
