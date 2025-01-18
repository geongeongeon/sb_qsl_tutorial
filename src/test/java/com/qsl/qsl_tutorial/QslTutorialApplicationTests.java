package com.qsl.qsl_tutorial;

import com.qsl.qsl_tutorial.boundedContext.user.entity.SiteUser;
import com.qsl.qsl_tutorial.boundedContext.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	@Test
	@DisplayName("전체 회원 오래된 순")
	void t6() {
		List<SiteUser> users = userRepository.getQslUserOrderByIdAsc();

		SiteUser user1 = users.get(0);

		assertThat(user1.getId()).isEqualTo(1L);
		assertThat(user1.getUsername()).isEqualTo("user1");
		assertThat(user1.getPassword()).isEqualTo("{noop}1234");
		assertThat(user1.getEmail()).isEqualTo("user1@test.com");

		SiteUser user2 = users.get(1);

		assertThat(user2.getId()).isEqualTo(2L);
		assertThat(user2.getUsername()).isEqualTo("user2");
		assertThat(user2.getPassword()).isEqualTo("{noop}1234");
		assertThat(user2.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("검색, List 리턴, 검색 대상 : username, email")
	void t7() {
		// 검색 : username
		List<SiteUser> users = userRepository.searchQsl("user1");

		assertThat(users.size()).isEqualTo(1);

		SiteUser user = users.get(0);

		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getUsername()).isEqualTo("user1");
		assertThat(user.getPassword()).isEqualTo("{noop}1234");
		assertThat(user.getEmail()).isEqualTo("user1@test.com");

		// 검색 : username
		users = userRepository.searchQsl("user2");

		assertThat(users.size()).isEqualTo(1);

		user = users.get(0);

		assertThat(user.getId()).isEqualTo(2L);
		assertThat(user.getUsername()).isEqualTo("user2");
		assertThat(user.getPassword()).isEqualTo("{noop}1234");
		assertThat(user.getEmail()).isEqualTo("user2@test.com");
	}

	@Test
	@DisplayName("검색, Page 리턴, id ASC, pageSize = 1, page = 1")
	void t8() {
		long totalCount = userRepository.count();
		int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
		int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
		int page = 1; // 현재 페이지 -> 2번째 페이지를 의미
		String kw = "user";

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("id")); // id 기준 오름차순
		// sorts.add(Sort.Order.desc("name")) // name 기준 내림차순
		Pageable pageable = PageRequest.of(1, pageSize, Sort.by(sorts)); // 한 페이지당 몇 개까지 보여질 것인가
		Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

		assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
		assertThat(usersPage.getNumber()).isEqualTo(page);
		assertThat(usersPage.getSize()).isEqualTo(pageSize);

		List<SiteUser> users = usersPage.get().toList();
		assertThat(users.size()).isEqualTo(pageSize);

		SiteUser user = users.get(0);

		assertThat(user.getId()).isEqualTo(2L);
		assertThat(user.getUsername()).isEqualTo("user2");
		assertThat(user.getPassword()).isEqualTo("{noop}1234");
		assertThat(user.getEmail()).isEqualTo("user2@test.com");
	}
}
