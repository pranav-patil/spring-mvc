package com.library.spring.config;

import com.library.spring.security.config.SecurityConfig;
import com.library.spring.web.formatter.DateFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @see <a href="http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html">Tutorial: Using Thymeleaf</a>
 */
@EnableWebMvc
@Configuration
@Import({SwaggerConfig.class, DatabaseConfig.class, SchedulerConfig.class})
@PropertySource("classpath:config/application.properties")
@ComponentScan({ "com.library.spring.web", "com.library.spring.security" })
public class SpringWebConfig implements WebMvcConfigurer {

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("/images/");
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/secure/view/batchschedule").setViewName("batchschedule");
		registry.addViewController("/public/view/login").setViewName("login");
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/messages");
		return messageSource;
	}

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		registry.addFormatter(dateFormatter());
	}

	@Bean
	public DateFormatter dateFormatter() {
		return new DateFormatter();
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		//viewResolver.setViewNames(new String[] {".html", ".xhtml"});
		return viewResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver(){
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(true);
		return templateResolver;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertyConfigurer.setIgnoreUnresolvablePlaceholders(true);
		return propertyConfigurer;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
 }