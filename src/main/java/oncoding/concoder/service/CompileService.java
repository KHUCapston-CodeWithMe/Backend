package oncoding.concoder.service;

import io.lettuce.core.protocol.AsyncCommand;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompileService {

    public void writeFile(String name, String content) throws IOException {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        FileWriter fw = new FileWriter(file);
        try (BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(content.substring(1, content.length()-1));
        }
    }

    /*
    파일 존재할 경우 : 삭제 (삭제 제대로 처리되지 않을 경우 print)
    파일 존재하지 않을 경우 : 무시
    */
    public void deleteFile(String name) {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        if (!file.exists()) return;
        if (file.delete()) return;
        System.out.println("Error : file "+ Paths.get(String.format("file %s.py", name)).toString() +" not deleted!");
    }

    public String getOutput(BufferedReader bufferedReader, int exitCode, long time) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        if (exitCode!=0) {
            log.info("run failed with exit code " + exitCode + " time : " + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS));
        }
        else {
            log.info("run success with exit code "+ exitCode + " time: " + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS));
        }
        return sb.toString();
    }

    @Async("taskExecutor")
    public void run(String code) throws IOException, InterruptedException {
        log.info("CompileService.run()...");
        String random = UUID.randomUUID().toString();
        try {
            writeFile(random, code);

            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                Paths.get(String.format("%s.py", random)).toString());
            long startTime = System.nanoTime();
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            long time = System.nanoTime()-startTime;
            BufferedReader br;
            br = exitCode!=0 ? new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))
                : new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            getOutput(br, exitCode, time); // TODO : 워크스페이스 Id 받아서 socket spread
        }
        finally {
            deleteFile(random);
        }

    }
}
