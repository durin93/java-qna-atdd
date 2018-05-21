package codesquad.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

public class QuestionAcceptanceTest extends AcceptanceTest{

	@Autowired
	QuestionRepository questionRepository;
	
	@Test
	public void createForm() {
		ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	public void createSuccess_login() {
		HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
				.addParameter("title", "test제목")
				.addParameter("contents", "test내용").build();
		
        User loginUser = defaultUser();
		ResponseEntity<String> response =  basicAuthTemplate(loginUser).postForEntity("/questions", request, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
		
		assertThat(questionRepository.findById(3L).isPresent(), is(true));
		assertThat(questionRepository.findById(4L).isPresent(), is(false));
		assertThat(response.getHeaders().getLocation().getPath(), is("/questions"));
	}
	
	@Test
	public void createFail_no_login() {
		HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
				.addParameter("title", "test제목")
				.addParameter("contents", "test내용").build();
		
		ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);
		
		assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
//		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
}
