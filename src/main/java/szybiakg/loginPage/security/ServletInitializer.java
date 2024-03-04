package szybiakg.loginPage.security;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import szybiakg.loginPage.TimeAndAttendanceSystemApplication;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TimeAndAttendanceSystemApplication.class);
	}

}
