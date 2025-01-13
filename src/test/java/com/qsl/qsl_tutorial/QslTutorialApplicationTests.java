package com.qsl.qsl_tutorial;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QslTutorialApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("회원 생성")
	void t1() {
		// {noop} : 비밀번호를 암호화하지 않고 그대로 사용
		/*
		SiteUser u1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
		SiteUser u2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");
		*/

		SiteUser u1 = SiteUser.builder()
				.username("user1")
				.password("{noop}1234")
				.email("user1@test.com")
				.build();

		SiteUser u2 = SiteUser.builder()
				.username("user2")
				.password("{noop}1234")
				.email("user2@test.com")
				.build();

		userRepository.saveAll(Arrays.asList(u1, u2));
	}

	@Test
	@DisplayName("1번 회원을 QSL로 가져오기")
	void t2() {
		// SELECT * FROM site_user WHERE id = 1;
		SiteUser u1 = userRepository.getQslUser(1L);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
	}

	@Test
	@DisplayName("2번 회원을 QSL로 가져오기")
	void t3() {
		// SELECT * FROM site_user WHERE id = 2;
		SiteUser u1 = userRepository.getQslUser(2L);

		assertThat(u1.getId()).isEqualTo(2L);
		assertThat(u1.getUsername()).isEqualTo("user2");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
		assertThat(u1.getEmail()).isEqualTo("user2@test.com");
	}
}
