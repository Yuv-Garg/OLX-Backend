package net.olxApplication.Exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

public class NotExist extends RuntimeException {

    public NotExist(String message) {
        super( message);
    }

    @Data
    @NoArgsConstructor
    public static class NotFoundResponse{
        private LocalDateTime timeStamp = LocalDateTime.now();
        private int status;
        private String error;

        public NotFoundResponse(String error, int status){
            this.error = error;
            this.status = status;
        }
    }
}
