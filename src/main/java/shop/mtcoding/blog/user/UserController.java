package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor // final이 붙은 애들에 대한 생성자를 만들어줌
@Controller
public class UserController {

    @GetMapping("/companyMain")
    public String companyMain(){
        return "company/companyMain";
    }

    @GetMapping("/uploadForm")
    public String uploadForm(){
        return "company/uploadForm";
    }

    @GetMapping("/employeeResumes")
    public String employeeResumes() {
        return "employee/employeeResumes";
    }

    @GetMapping("/resumeDetail")
    public String resumeDetail() {
        return "employee/resumeDetail";
    }

    @GetMapping("/saveResumeForm")
    public String saveResumeForm() {
        return "employee/saveResumeForm";
    }

    @GetMapping("/updateResumeForm")
    public String updateResumeForm() {
        return "employee/updateResumeForm";
    }

    @GetMapping("/companyResumes")
    public String companyResumes() {
        return "company/companyResumes";
    }

    @GetMapping("/appliedResumeDetail")
    public String appliedResumeDetail() {
        return "company/appliedResumeDetail";
    }

    @GetMapping("/postDetail")
    public String postDetail() {
        return "employee/postDetail";
    }
}
