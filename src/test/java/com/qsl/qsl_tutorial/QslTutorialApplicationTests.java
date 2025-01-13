package com.qsl.qsl_tutorial;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 각 테스트 케이스에 전부 @Transactional을 붙인 것과 같음
// @Test + @Transactional 조합은 자동으로 롤백을 유발시킴
@ActiveProfiles("test")
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

		SiteUser u3 = SiteUser.builder()
				.username("user3")
				.password("{noop}1234")
				.email("user3@test.com")
				.build();

		SiteUser u4 = SiteUser.builder()
				.username("user4")
				.password("{noop}1234")
				.email("user4@test.com")
				.build();

		userRepository.saveAll(Arrays.asList(u3, u4));
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

	@Test
	@DisplayName("모든 회원 수")
	void t4() {
		long count = userRepository.getQslCount();

		assertThat(count).isGreaterThan(0); // 회원 수가 0보다 큰지 확인
		assertThat(count).isEqualTo(2); // 회원 수가 2인지 확인
	}

	@Test
	@DisplayName("가장 오래된 회원")
	void t5() {
		SiteUser user = userRepository.getQslUserOrderByIdAscOne();

		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getUsername()).isEqualTo("user1");
		assertThat(user.getPassword()).isEqualTo("{noop}1234");
		assertThat(user.getEmail()).isEqualTo("user1@test.com");
	}
}
