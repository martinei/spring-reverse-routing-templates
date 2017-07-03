package de.martinei.routing;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {


    @GetMapping("/OneParam/{id}")
    @ResponseBody
    public ResponseEntity<String> oneParam(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Just a dummy to link to");
    }


    @GetMapping("/objectParam/{id}")
    @ResponseBody
    public ResponseEntity<String> objectTypedParam(@PathVariable("object") Object object) {
        return ResponseEntity.ok("Just a dummy to link to");
    }

    @GetMapping("/TwoParams/{id}/and/{more}")
    @ResponseBody
    public ResponseEntity<String> mixedTarget(@PathVariable("id") Long id, @PathVariable("more") String more) {

        return ResponseEntity.ok("Just a dummy to link to");
    }
}
