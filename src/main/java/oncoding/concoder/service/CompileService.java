package oncoding.concoder.service;

import java.io.IOException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;
import oncoding.concoder.etc.Language;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompileService {
    private final SimpMessagingTemplate template;

    private Runner runner;

    @Async("taskExecutor")
    public void run(String roomId, Language lang, String code, String input, String testCaseId) {

        log.info(Thread.currentThread().getName()+" thread run()...");
        String random = UUID.randomUUID().toString();

        if (lang.equals(Language.PYTHON)) {
            runner = new PythonRunner(random);
        } else if (lang.equals(Language.CPP)) {
            runner = new CppRunner(random);
        }

        try {
            CompileDto.Response result = runner.run(roomId, code, input, testCaseId);
            template.convertAndSend("/sub/compile/"+ roomId, result);
        } catch (InterruptedException e) {
            template.convertAndSend("/sub/compile/"+ roomId,
                    new CompileDto.Response(testCaseId, "[Error] timeout!", -1L));
        } catch (IOException e) {
            template.convertAndSend("/sub/compile/"+ roomId,
                    new CompileDto.Response(testCaseId, "[Error] "+e.getMessage(), -1L));
        } finally {
            runner.deleteFile(random);
        }
    }
}
