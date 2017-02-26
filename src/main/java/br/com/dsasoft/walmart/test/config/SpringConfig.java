package br.com.dsasoft.walmart.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.mongodb.MongoClient;

@Configuration
@ComponentScan(basePackages = { "br.com.dsasoft.walmart.test.resources",
		"br.com.dsasoft.walmart.test.service" })
@PropertySource("classpath:config/mongodb.properties")
@EnableWebMvc
@EnableSwagger2
public class SpringConfig extends WebMvcConfigurerAdapter {
	
	@Value("${mongodb.url}")
	private String mongodbUrl;

	@Value("${mongodb.port}")
	public Integer mongoPort;

	@Bean
	public MongoClient mongoTemplate() throws Exception {
		MongoClient mongo = new MongoClient(mongodbUrl, mongoPort);
		return mongo;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations(
				"classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations(
				"classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/");
	}

}
