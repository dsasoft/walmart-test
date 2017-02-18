package br.com.dsasoft.walmart.test.config;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/ws/*" };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new CORSFilter() };
	}

}
