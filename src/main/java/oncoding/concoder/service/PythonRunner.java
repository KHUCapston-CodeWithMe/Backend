package oncoding.concoder.service;

import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;

import java.io.*;
import java.nio.file.Paths;

@Slf4j
public class PythonRunner extends Runner {

    protected PythonRunner(String random) {
        super(random);
    }

    private static String getPythonFilePath(String name) {
        return Paths.get(String.format("%s.py", name)).toString();
    }

    @Override
    public void writeFile(String name, String content) throws IOException {
        File file = new File(getPythonFilePath(name));

        writeContent(content, file);
    }

    @Override
    public void deleteFile(String name) {
        File pyFile = new File(getPythonFilePath(name));

        if (!pyFile.exists()) {
            log.error("Error: python file {} doesn't exist!", getPythonFilePath(name));
        }
        if (!pyFile.delete()) {
            log.error("Error: python file {} not deleted!", getPythonFilePath(name));
        }
    }

    @Override
    public CompileDto.Response run(String roomId, String code, String input, String testCaseId)
            throws IOException, InterruptedException {

        // process start
        writeFile(random, code);

        String pythonCommand = "python3 " + getPythonFilePath(random);

        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", pythonCommand);

        return inputAndOutput(input, testCaseId, processBuilder);
    }
}
