package oncoding.concoder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CompileDTO {
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class CreateRequest {
        private String code;
        private String input;
    }

}
