package com.library.spring.config;

import com.library.spring.security.filter.CORSFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @see <a href="https://stackoverflow.com/questions/35258758/getservletconfigclasses-vs-getrootconfigclasses-when-extending-abstractannot">getServletConfigClasses() vs getRootConfigClasses()</a>
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected Filter[] getServletFilters() {
		Filter [] singleton = { new CORSFilter()};
		return singleton;
	}
}