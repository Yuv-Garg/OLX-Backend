package net.olxApplication.Exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }

    @Data
    @NoArgsConstructor
    public static class BadRequestResponse{
        private LocalDateTime timeStamp = LocalDateTime.now();
        private int status;
        private String error;

        public BadRequestResponse(String error, int status){
            this.error = error;
            this.status = status;
        }
    }
}
