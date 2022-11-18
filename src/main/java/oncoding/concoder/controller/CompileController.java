package oncoding.concoder.controller;

import lombok.RequiredArgsConstructor;
import oncoding.concoder.service.CompileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compile")
@RequiredArgsConstructor
public class CompileController {
    private final CompileService compileService;

    @PostMapping("")
    public String test(@RequestBody String code) {
        try {
            return compileService.run(code);
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getStackTrace().toString();
        }
    }

}
