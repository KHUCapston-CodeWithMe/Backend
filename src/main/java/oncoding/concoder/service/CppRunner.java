package oncoding.concoder.service;

import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;

import java.io.*;
import java.nio.file.Paths;

@Slf4j
public class CppRunner extends Runner {

    protected CppRunner(String random) {
        super(random);
    }

    private static String getCppFilePath(String name) {
        return Paths.get(String.format("%s.cpp", name)).toString();
    }

    @Override
    void writeFile(String name, String content) throws IOException {
        File file = new File(getCppFilePath(name));

        writeContent(content, file);
    }

    @Override
    void deleteFile(String name) {
        File cppFile = new File(getCppFilePath(name));
        File binaryFile = new File(name);

        if (!cppFile.exists()) {
            log.error("Error: cpp file {} doesn't exists!", getCppFilePath(name));
        }
        if (!binaryFile.exists()) {
            log.error("Error: binary file {} doesn't exists!", name);
        }

        if (!cppFile.delete()) {
            log.error("Error: cpp file {} not deleted!", getCppFilePath(name));
        }
        if (!binaryFile.delete()) {
            log.error("Error: binary file {} not deleted!", name);
        }
    }

    @Override
    CompileDto.Response run(String roomId, String code, String input, String testCaseId) throws IOException, InterruptedException {

        // process start
        writeFile(random, code);

        // g++로 컴파일 후 바이너리 생성 (C++ 14)
        String compileCommand = "g++ -std=c++14 " + getCppFilePath(random) + " -o " + random;
        String runBinaryCmd = "./" + random;

        ProcessBuilder compileBuilder = new ProcessBuilder("/bin/bash", "-c", compileCommand);
        ProcessBuilder runBinaryBuilder = new ProcessBuilder("/bin/bash", "-c", runBinaryCmd);

        Process compileProcess = compileBuilder.start();

        int compileExitCode = compileProcess.waitFor();
        log.info("compileExitCode = {}", compileExitCode);

        return inputAndOutput(input, testCaseId, runBinaryBuilder);
    }
}
