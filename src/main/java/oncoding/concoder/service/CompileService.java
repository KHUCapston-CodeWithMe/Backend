package oncoding.concoder.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;

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

    public String getOutput(BufferedReader bufferedReader, boolean success) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        if (!success) {
            System.out.println("비정상 종료");
        }
        else {
            System.out.println("정상 종료");
        }
        return sb.toString();
    }

    public String run(String code) throws IOException, InterruptedException {
        String random = UUID.randomUUID().toString();
        try {
            writeFile(random, code);

            ProcessBuilder processBuilder = new ProcessBuilder("python3",
                Paths.get(String.format("%s.py", random)).toString());
            Process process = processBuilder.start();
            ProcessHandle.Info processInfo = process.info();
            int exitCode = process.waitFor();

            BufferedReader br;
            br = exitCode!=0 ? new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))
                : new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            return getOutput(br, exitCode==0);
        }
        finally {
            deleteFile(random);
        }

    }
}
