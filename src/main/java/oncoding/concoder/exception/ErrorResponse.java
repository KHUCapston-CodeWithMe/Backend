package oncoding.concoder.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private int status;
  private String error;
  private String code;
  private String message;


  @Builder
  public ErrorResponse(ErrorCode errorCode){
    this.status = errorCode.getStatus().value();
    this.error= errorCode.getStatus().name();
    this.code = errorCode.name();
    this.message = errorCode.getMessage();
  }

  @Builder
  public ErrorResponse(int status, String error, String code, String message) {
    this.status = status;
    this.error = error;
    this.code = code;
    this.message = message;
  }

  public ErrorResponse(String defaultMessage) {
    this.message = defaultMessage;
  }

}
