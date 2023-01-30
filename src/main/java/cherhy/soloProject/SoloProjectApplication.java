package cherhy.soloProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SoloProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoloProjectApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		return () -> Optional.of(UUID.randomUUID().toString());
	}

////스프링 시큐리티를 쓰는 경우에는 SecurityContext를 통해 세션 정보들을 가져와서 넣어주면 된다
//	@Bean
//	public AuditorAware<String> auditorProvider() {
//		return () -> {
//			ServletRequestAttributes attr
//					= (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//
//			String currentUser = (String)attr.getRequest().getSession().getAttribute(Sessions.SESSION_ID);
//
//			if(currentUser != null)
//				return Optional.of(currentUser);
//			else
//				return Optional.of("Anonymous");
//		};
//	};

}
