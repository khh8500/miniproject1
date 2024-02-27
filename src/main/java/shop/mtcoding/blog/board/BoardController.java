package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.love.LoveResponse;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final LoveRepository loveRepository;


    //?title=제목1@
    //title
    //파싱방법이 똑같아서 바로 받을 수 있다 String title, String content 이런식으로

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        //1. 인증체크
        User sessionUser=(User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }
        //2. 권한체크
        Board board = boardRepository.findById(id);

        if(board.getUserId()!=sessionUser.getId()){
            return "error/403";
        }
        //3. 핵심로직
        //update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/{id}";
    }
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request){
        //인증체크
        User sessionUser=(User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }
        //권한 체크
        Board board = boardRepository.findById(id);
        if(board.getUserId()!=sessionUser.getId()){
            return "error/403";
        }
        //모델 위임(id로 board를 조회)
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){
        //1. 인증 안되면 나가
        User sessionUser=(User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        //2. 권한 없으면 나가
        Board board = boardRepository.findById(id);
        if(board.getUserId() != sessionUser.getId()){
            request.setAttribute("status", 403);
            request.setAttribute("msg", "ddd");
            return "error/40x";
        }

        boardRepository.delete(id);

        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){
        //1. 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginform";
        }

        //2. 바디데이터를 확인 및 유효성 검사
        System.out.println(requestDTO);


        if (requestDTO.getTitle().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과하면 안돼요");
            return "error/40x"; //badrequest
        }

        //insert into board.tb(title, content, user_id, created_at) values(?, ?, ?, now());
        boardRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm( HttpServletRequest request) {
        //jsession 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser");


        //값이 null이면 로그인 페이지로 리다이렉션
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        //값이 null이 아니면 /board/saverForm으로 이동
        return "board/saveForm";
    }
    @GetMapping("/board/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    //localhost:8080?page=1 -> page 값이 1
    //localhost:8080 ->page 값이 0
    @GetMapping({ "/" })
    public String index(HttpServletRequest request,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "") String keyword) { //null들어올때 안터지게하기위해 defaul값으로 null를 받는다

        //isEmpty -> null, 공백
        //isBlank -> null, 공백, 화이트 스페이스

        if(keyword.isBlank()) {
            List<Board> boardList=boardRepository.findAll(page);

            int count = boardRepository.count().intValue();
            int namerge = 8 % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", "");
        }else {
            List<Board> boardList = boardRepository.findAll(page, keyword);

            int count = boardRepository.count(keyword).intValue();
            int namerge = 8 % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", keyword);


        }
        return "index";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");


        //1. 모델 진입 - 상세보기 데이터 가져오기
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        List<BoardResponse.ReplyDTO> replyList = replyRepository.findByBoardId(id,sessionUser);

        //2. 페이지 주인 여부 체크 (board와 userId와 sessionUser의 id를 비교)

        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList", replyList);

        if(sessionUser == null){
            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id);
            request.setAttribute("love", loveDetailDTO);
        }else{
            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id, sessionUser.getId());
            request.setAttribute("love", loveDetailDTO);
        }

        return "board/detail";
    }
}
