package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId; //게시글 작성자 id
        private String username;
        private Boolean boardOwner; //소문자 boolean은 getter가 안만들어짐 //매핑클래스를 쓰는게 좋다는데 매핑클래스가 뭐여

        public void isBoardOwner(User session){
            if (session == null){
                boardOwner =false;
            }else {
                boardOwner = session.getId() ==userId;
            }

        }
    }

    @Data
    public static class ReplyDTO{
        private Integer id;
        private Integer userId;
        private String comment;
        private String username;
        private Boolean replyOwner;

        public ReplyDTO(Object[] ob, User sessionUser){
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];

            if(sessionUser == null){
                replyOwner = false;
            }else{
                replyOwner = sessionUser.getId()==userId;
            }
        }
    }
}
