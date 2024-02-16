package shop.mtcoding.blog.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

//댓글쓰기, 댓글삭제, 댓글 목록보기
@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final HttpSession session;
    private final ReplyRepository replyRepository;

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable int id){

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginform";
        }

        Reply reply=replyRepository.findById(id);
        //댓글 없거나, 댓글 주인이 아니거나, 댓글 주인이거나
        System.out.println(reply);

        if(reply==null){
            return "error/404";
        }

        if (reply.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        replyRepository.deleteById(id);

        return "redirect:/board/"+reply.getBoardId();
    }

    @PostMapping("/reply/save")
    public String write(ReplyRequest.WriteDTO requestDTO){
        System.out.println(requestDTO);
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginform";
        }

        //유효성검사(내가 하기)


        //핵심코드
        replyRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/board/"+requestDTO.getBoardId();
    }
}
