package oncoding.concoder.service;

import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Timer;

@Slf4j
public class PythonRunner extends Runner {

    private final String random;

    public PythonRunner(String random) {
        this.random = random;
    }

    @Override
    public void writeFile(String name, String content) throws IOException {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        FileWriter fw = new FileWriter(file);
        try (BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(content);
        }
    }

    @Override
    public void deleteFile(String name) {
        File file = new File(Paths.get(String.format("%s.py", name)).toString());

        if (!file.exists()) return;
        if (file.delete()) return;
        System.out.println("Error : file "+ Paths.get(String.format("file %s.py", name)).toString() +" not deleted!");
    }

    @Override
    public CompileDto.Response run(String roomId, String code, String input, String testCaseId)
            throws IOException, InterruptedException {

        // set timer to timeout
        Timer timer = setTimeoutTimer(Thread.currentThread());

        // process start
        writeFile(random, code);
        ProcessBuilder processBuilder = new ProcessBuilder("python3",
                Paths.get(String.format("%s.py", random)).toString());
        long startTime = System.nanoTime();
        Process process = processBuilder.start();

        // write input
        OutputStream stdin = process.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(stdin));
        bw.write(input);
        bw.close();

        // process end
        int exitCode = process.waitFor();
        long time = System.nanoTime()-startTime;
        timer.cancel();

        // read output
        InputStream stdout = exitCode!=0 ? process.getErrorStream() : process.getInputStream();
        BufferedReader br =  new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));

        return getOutput(br, exitCode, time, testCaseId);
    }
}
