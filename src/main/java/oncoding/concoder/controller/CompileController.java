package oncoding.concoder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import jdk.incubator.vector.VectorOperators.Test;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;
import oncoding.concoder.dto.CompileDto.Response;
import oncoding.concoder.service.CompileService;
import org.json.simple.JSONObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/compile")
@RequiredArgsConstructor
public class CompileController {
    private final CompileService compileService;
    private final SimpMessagingTemplate template;

    // TODO: 실행 요청 받기
    @MessageMapping("/compile/{roomId}")
    public void compileByTestcases(@DestinationVariable final String roomId, JSONObject obj) {
        String code = (String) obj.get("code");
        // Redis 로부터 roomId 로 가져오기
        List<CompileDto.Testcase> testcases = new ArrayList<>();
        for (int idx = 0; idx<testcases.size(); idx++){
            CompileDto.Testcase testcase = testcases.get(idx);
            try {
                compileService.run(roomId, code, testcase.getInput(), idx);
            }
            catch (InterruptedException e) {
                log.info("Testcase "+idx+" timeout!");
                template.convertAndSend("/sub/compile/"+ roomId,
                    new CompileDto.Response(idx, "[Error] timeout!", -1L));
            }
            catch (IOException e) {
                log.info("Testcase "+idx+" error! "+ Arrays.toString(e.getStackTrace()));
                template.convertAndSend("/sub/compile/"+ roomId,
                    new CompileDto.Response(idx, "[Error] "+e.getMessage(), -1L));
            }
        }

    }

    // TODO: 테케 실행 완료마다 뿌리기

//    @PostMapping("")
//    public List<CompileDto.Response> test(@RequestBody CompileDto.CreateRequest req) {
//            List<CompileDto.Response> responses = new ArrayList<>();
//            for (int i = 0; i<req.getInputs().size(); i++) {
//                try {
//                    responses.add(compileService.run(req.getCode(), req.getInputs().get(i)).get());
//                }
//                catch (ExecutionException | InterruptedException e) {
//                    responses.add(new CompileDto.Response("[Error] timeout", -1L));
//                }
//                catch (IOException e) {
//                    responses.add(new CompileDto.Response("[Error] "+e.getMessage(), -1L));
//                }
//            }
//            return responses;
//
//    }

}
