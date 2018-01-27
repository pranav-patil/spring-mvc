package com.library.spring.config;

import com.library.spring.security.filter.CORSFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
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

	/**
	 * @see <a href="http://www.logicbig.com/how-to/spring-mvc/spring-customizing-default-error-resolver/">Customizing Spring default HandlerExceptionResolvers functionality</a>
	 */
	@Override
	protected FrameworkServlet createDispatcherServlet (WebApplicationContext webApplicationContext) {
		DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);
		//setting this flag to true will throw NoHandlerFoundException instead of 404 page
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}
}