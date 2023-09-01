package todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import todoapp.commons.web.servlet.ExecutionTimeHandlerInterceptor;
import todoapp.commons.web.servlet.LoggingHandlerInterceptor;
import todoapp.commons.web.view.CommaSeparatedValuesView;
import todoapp.core.todos.domain.Todo;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.security.web.servlet.RolesVerifyHandlerInterceptor;
import todoapp.security.web.servlet.UserSessionHandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring Web MVC 설정
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserSessionRepository userSessionRepository;

    public WebMvcConfiguration() {
        logger.debug("스프링 MVC 설정자가 생성됩니다.");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionHandlerMethodArgumentResolver(userSessionRepository));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingHandlerInterceptor());
        registry.addInterceptor(new ExecutionTimeHandlerInterceptor());
        registry.addInterceptor(new RolesVerifyHandlerInterceptor(userSessionRepository));
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //registry.viewResolver(new TodoController.TodoCsvViewResolver());

        // registry.enableContentNegotiation();
        // 위와 같이 직접 설정하면, 스프링부트가 구성한 ContentNegotiatingViewResolver 전략이 무시된다.
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 리소스 핸들러를 등록, 정적 자원을 처리할 수 있습니다.

        // https://localhost:8080/assets/css/todos.css

        // 서블릿 컨텍스트 경로에서 정적 자원 제공
//        registry.addResourceHandler("/assets/**").addResourceLocations("assets/");

        // 파일 경로에서 정적 자원 제공
        //registry
        //        .addResourceHandler("/assets/**")
        //        .addResourceLocations("file:/Users/arawn/springrunner/workspace/todos/files/assets");

        // 클래스패스 경로에서 정적 자원 제공
        // registry.addResourceHandler("/assets/**").addResourceLocations("classpath:assets/");

        //registry
//                .addResourceHandler("/assets/**")
//                .addResourceLocations(
//                        "assets/",
//                        "file:/Users/arawn/springrunner/workspace/todos/files/assets",
//                        "classpath:assets/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new Converter<Todo,String>(){
            @Override
            public String convert(Todo source) {
                return source.toString();
            }
        });
        converters.add(new ObjectToStringHttpMessageConverter(null));
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        commonsRequestLoggingFilter.setIncludeHeaders(true);
        commonsRequestLoggingFilter.setIncludePayload(true);
        commonsRequestLoggingFilter.setIncludeClientInfo(true);

        FilterRegistrationBean<CommonsRequestLoggingFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(commonsRequestLoggingFilter);
        filter.setUrlPatterns(Collections.singletonList("/*"));

        return filter;
    }

    @Bean(name = "todos")
    public CommaSeparatedValuesView todoCsvView() {
        return new CommaSeparatedValuesView();
    }

    /**
     * 스프링부트가 생성한 ContentNegotiatingViewResolver를 조작할 목적으로 작성된 컴포넌트
     */
    @Configuration
    public static class ContentNegotiationCustomizer {

        @Autowired
        public void configurer(ContentNegotiatingViewResolver viewResolver) {
            List<View> defaultViews = new ArrayList<>(viewResolver.getDefaultViews());
            defaultViews.add(new CommaSeparatedValuesView());
            defaultViews.add(new MappingJackson2JsonView());

            viewResolver.setDefaultViews(defaultViews);
        }

    }

}
