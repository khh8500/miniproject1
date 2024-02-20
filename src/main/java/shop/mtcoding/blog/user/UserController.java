package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Script;


@RequiredArgsConstructor // final이 붙은 애들에 대한 생성자를 만들어줌
@Controller
public class UserController {

    // 자바는 final 변수는 반드시 초기화가 되어야함.
    private final UserRepository userRepository;
    private final HttpSession session;
    @PostMapping("/user/update")
    public String update(UserRequest.updateDTO requestDTO, HttpServletRequest request){

        System.out.println(requestDTO);

        User sessionUser=(User) session.getAttribute("sessionUser");
        System.out.println(sessionUser);

        //같은 비밀번호 안받기, 세션에 많은 정보  안담는다고 하니깐 session에서 패스워드 받지 말기
        if (requestDTO.getPassword().equals(userRepository.findById(sessionUser.getId()).getPassword())){
            request.setAttribute("status", 401);
            request.setAttribute("msg", "기존비밀번호가 같아요");
            return "error/40x"; //badrequest
        }

        userRepository.update(requestDTO, sessionUser.getId());
        System.out.println("1");

        User user=userRepository.findById(sessionUser.getId());
        System.out.println(user);

        session.setAttribute("sessionUser", user);
        sessionUser = user;
        System.out.println(sessionUser);

        //로그아웃하고 다시 로그인하기
        session.invalidate();
        return "redirect:/loginForm";

    }

    // 왜 조회인데 post임? 민간함 정보는 body로 보낸다.
    // 로그인만 예외로 select인데 post 사용
    // select * from user_tb where username=? and password=?
@PostMapping("/login")
public String login(UserRequest.LoginDTO requestDTO){


    System.out.println(requestDTO); // toString -> @Data

    String rawPassword= requestDTO.getPassword();
    String encPassword= BCrypt.hashpw(rawPassword, BCrypt.gensalt());//레인보우 테이블에 안털림
    requestDTO.setPassword(encPassword);
    if(requestDTO.getUsername().length() < 3){
        throw new RuntimeException("유저네임 길이가 너무 짧아요"); // ViewResolver 설정이 되어 있음. (앞 경로, 뒤 경로)
    }

    User user = userRepository.findByUsername(requestDTO.getUsername());

    if(BCrypt.checkpw(requestDTO.getPassword(), user.getPassword())){
        throw new RuntimeException("패스워드가 틀렸습니다");
    }

    session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)


    return "redirect:/"; // 컨트롤러가 존재하면 무조건 redirect 외우기
}

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO){ //ResponseBody는 메시지를 리턴
        System.out.println(requestDTO);
        try {
            userRepository.save(requestDTO); // 모델에 위임하기
        }catch (Exception e){
            throw new RuntimeException("아이디가 중복되었어요");
        }
        return "return:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(HttpServletRequest request) {
        User sessionUser=(User) session.getAttribute("sessionUser");
        //권한인증
        if(sessionUser==null){
            return "redirect:/loginForm";
        }
        request.setAttribute("username", userRepository.findById(sessionUser.getId()).getUsername());
        request.setAttribute("email", userRepository.findById(sessionUser.getId()).getEmail());

        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
