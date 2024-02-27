package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

// user로 테이블명을 만들면, 키워드여서 안만들어질 수 있다. _tb 컨벤션 지키자.
@Table(name="love_tb", uniqueConstraints = {
        @UniqueConstraint(
                name="love_uk",
                columnNames={"board_id","user_id"}
        )
})
@Data
@Entity
public class Love {
    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략
    private Integer id;

    private Integer boardId;
    private Integer UserId;
    private Timestamp createdAt;
}
