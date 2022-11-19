package oncoding.concoder.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompileServiceTest {
    @Autowired
    private CompileService compileService;

    @Test
    void async_task_executor_실행() {
        for (int i = 0; i<20; i++){
            try {
                compileService.run("print('hello world')");
            }
            catch (Exception e) {
                fail();
            }
        }
    }

}