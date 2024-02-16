package shop.mtcoding.blog.reply;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name="reply_tb")
@Data
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String comment;
    private int boardId;
    private int userId;

    private LocalDateTime createdAt;
}
