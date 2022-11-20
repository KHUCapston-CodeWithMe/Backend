package oncoding.concoder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CompileService.class})
@ActiveProfiles("test")
class CompileServiceTest {
    @Autowired
    private CompileService compileService;

    private final int THREAD_TIMEOUT_SECONDS = 10; // task timeout 설정 시간 (초)

    @Test
    void task_비동기_처리() {
        // given
        String text = "hello world";
        String pythonCode = "print(\""+text+"\", end=\"\")";

        // when
        String result = "";
        Future<String> future = null;
        try {
           future = compileService.run(pythonCode, "");
           result = future.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // then
        assertThat(result).isEqualTo(text);
        assertThat(future).isNotNull();
        assertThat(future).isDone();
        assertThat(future).succeedsWithin(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @Test
    void 특정_시간을_넘어가는_task_timeout_처리() {
        // given
        String timeoutCode = "while True:\n" + "    a = 1;";

        // when, then
        assertThatThrownBy(()-> compileService.run(timeoutCode, "").get())
                .isInstanceOf(InterruptedException.class);
    }

    @Test
    void 에러_발생하는_task_처리() {
        // given
        String errorCode = "print(";

        // when
        String result = "";
        Future<String> future = null;
        try {
            future = compileService.run(errorCode, "");
            result = future.get();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        // then
        assertThat(result).contains("Error"); // Error 내용 반환
        assertThat(future).isNotNull();
        assertThat(future).isDone(); // Task 자체는 시간 내 완료
        assertThat(future).succeedsWithin(THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

}