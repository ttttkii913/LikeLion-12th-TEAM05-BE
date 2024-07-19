package org.likelion.likelion_12th_team05;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> Test() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
