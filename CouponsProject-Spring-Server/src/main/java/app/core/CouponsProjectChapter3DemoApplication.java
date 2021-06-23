package app.core;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import app.core.filters.LoginFilter;
import app.core.sessions.SessionContextManager;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
//http://localhost:8080/swagger-ui.html
public class CouponsProjectChapter3DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponsProjectChapter3DemoApplication.class, args);
//		ApplicationContext ctx = SpringApplication.run(CouponsProjectChapter3DemoApplication.class, args);

	}

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("app.core"))
				.paths(PathSelectors.ant("/api/**")).build();
	}

	@Bean
	public FilterRegistrationBean<LoginFilter> filterRegistration(SessionContextManager sessionContext) {
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<LoginFilter>();
		LoginFilter loginFilter = new LoginFilter(sessionContext);
		filterRegistrationBean.setFilter(loginFilter);
		filterRegistrationBean.addUrlPatterns("/api/admin/*", "/api/company/*", "/api/customer/*", "/api/client*");
		return filterRegistrationBean;
	}

//================================================
// email = "admin@admin.com";
// password = "admin";
//================================================

}
