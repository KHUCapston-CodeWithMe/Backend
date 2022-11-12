package oncoding.concoder;

import oncoding.concoder.service.ProblemService;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class ConcoderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcoderApplication.class, args);
    }

    @Bean
    LazyInitializationExcludeFilter lazyInitializationExcludeFilter() {
        return LazyInitializationExcludeFilter.forBeanTypes(ProblemService.class);
    }
}
