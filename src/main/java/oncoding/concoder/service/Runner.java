package oncoding.concoder.service;

import lombok.extern.slf4j.Slf4j;
import oncoding.concoder.dto.CompileDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Runner {

    public static final int THREAD_TIMEOUT_SECONDS = 5;

    public Timer setTimeoutTimer (Thread thread) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                thread.interrupt();
                log.info(thread.getName()+" thread timeout!");
            }
        };
        timer.schedule(timerTask, THREAD_TIMEOUT_SECONDS*1000);
        return timer;
    }

    CompileDto.Response getOutput(BufferedReader bufferedReader, int exitCode, long time, String testCaseId) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        boolean first = true;
        while ((line = bufferedReader.readLine()) != null) {
            if (first) first = false;
            else sb.append("\n");
            sb.append(line);
        }
        bufferedReader.close();

        String result = exitCode!=0 ? "failed" : "success";
        log.info("run " + result + " with exit code " + exitCode + " time: " + TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS)+"ms");

        return new CompileDto.Response(testCaseId, sb.toString(),  TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS));
    }

    abstract void writeFile(String name, String content) throws IOException;

    /*
    파일 존재할 경우 : 삭제 (삭제 제대로 처리되지 않을 경우 print)
    파일 존재하지 않을 경우 : 무시
    */
    abstract void deleteFile(String name);

    abstract CompileDto.Response run(String roomId, String code, String input, String testCaseId) throws IOException, InterruptedException;
}
