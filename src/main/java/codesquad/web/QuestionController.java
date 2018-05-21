package codesquad.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.HttpSessionUtils;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
	@Resource(name = "qnaService")
	private QnaService qnaService;
	
	@GetMapping("/form")
	public String form() {
		return "/qna/form";
	}
	
	@PostMapping("")
	public String create(@LoginUser User loginUser, QuestionDto questionDto) {
		
		if(loginUser.isGuestUser()) {
			return "/user/login_failed";
		}
		
		qnaService.create(loginUser, questionDto.toQuestion());
		return "redirect:/questions";
	}
	
	

}
