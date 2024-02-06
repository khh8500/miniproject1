package shop.mtcoding.blog.board;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

public class BoardRequest {
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
    }
}
