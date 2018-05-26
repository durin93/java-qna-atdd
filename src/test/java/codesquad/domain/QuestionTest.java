package codesquad.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.naming.AuthenticationException;

import org.junit.Before;
import org.junit.Test;

import codesquad.CannotDeleteException;

public class QuestionTest {

	private Question question;
	private User user;
	private User otherUser;

	@Before
	public void setUp() {
		user = new User(1, "javajigi", "비밀번호", "이름", "test@mail");
		otherUser = new User(2, "두램", "비밀번호", "이름", "test@mail");
		question = new Question("제목", "내용");
		question.writeBy(user);
	}

	@Test(expected = AuthenticationException.class)
	public void updateFail() throws AuthenticationException {
		Question updatedQuestion = new Question("수정", "수정");
		question.update(updatedQuestion, otherUser);
	}

	@Test
	public void updateSuccess() throws AuthenticationException {
		Question updatedQuestion = new Question("수정", "수정");
		question.update(updatedQuestion, user);
		assertThat(question, is(updatedQuestion));
	}

	@Test
	public void delete본인() throws AuthenticationException, CannotDeleteException{
		question.delete(user);
		assertThat(question.isDeleted(), is(true));
	}
	
	@Test(expected=AuthenticationException.class)
	public void delete다른사람() throws AuthenticationException, CannotDeleteException{
		question.delete(otherUser);
	}

	@Test
	public void delete_댓글존재_모두질문유저꺼() throws AuthenticationException, CannotDeleteException{
		Answer answer = new Answer(1L, user, question, "내용내용");
		question.addAnswer(answer);
		question.delete(user);
		assertThat(question.isDeleted(), is(true));
		assertThat(answer.isDeleted(), is(true));
	}
	
	@Test(expected=CannotDeleteException.class)
	public void delete_댓글존재_다른유저꺼() throws AuthenticationException, CannotDeleteException{
		Answer answer = new Answer(1L, otherUser, question, "내용내용");
		question.addAnswer(answer);
		question.delete(user);
	}
	
	
}
