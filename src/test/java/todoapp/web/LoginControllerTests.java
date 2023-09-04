package todoapp.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import todoapp.TodosApplication;
import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.SiteProperties;

import javax.swing.*;
import java.util.Objects;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = TodosApplication.class)
//@WebAppConfiguration
//@SpringJUnitConfig(TodosApplication.class)
//@SpringJUnitWebConfig(TodosApplication.class)
@SpringBootTest
public class LoginControllerTests {

    @Autowired ApplicationContext applicationContext;
    @Autowired UserSessionRepository userSessionRepository;
    @Autowired LoginController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

//    private AnnotationConfigServletWebServerApplicationContext applicationContext;

//    @BeforeEach
//    void setUp() throws Exception {
//        applicationContext = new AnnotationConfigServletWebServerApplicationContext(TodosApplication.class);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        if (Objects.nonNull(applicationContext)) {
//            applicationContext.close();
//        }
//    }

    @Test
    void 스프링컨테이너는_자동클래스탐지로_로그인컨트롤러를_찾아_등록해요() {
//        AnnotationConfigServletWebServerApplicationContext applicationContext =
//                new AnnotationConfigServletWebServerApplicationContext(TodosApplication.class);

        Assertions.assertTrue(applicationContext.containsBean("loginController"));
//        applicationContext.close();
    }

    @Test
    void 인증되지않은_사용자가_로그인화면에_접근하면_로그인화면을_보여준다(@Autowired SiteProperties siteProperties) throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
//
//        UserSessionRepository userSessionRepository = applicationContext.getBean(UserSessionRepository.class);
//        userSessionRepository.set(new UserSession(new User("tester", "")));
//
//        LoginController controller = applicationContext.getBean(LoginController.class);
//        Assertions.assertEquals("login", controller.loginForm());

//        RequestContextHolder.resetRequestAttributes();

        RequestBuilder request = MockMvcRequestBuilders.get("/login");

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.view().name("login"),
                MockMvcResultMatchers.model().attribute("site", siteProperties)
        );
    }

    @Test
    void 인증된_사용자가_로그인화면에_접근화면_할일관리화면으로_전환시킨다() throws Exception {
        userSessionRepository.set(new UserSession(new User("tester", "")));

//        Assertions.assertEquals("redirect:/todos", controller.loginForm());

        RequestBuilder request = MockMvcRequestBuilders.get("/login").with(mockRequest -> {
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
            userSessionRepository.set(new UserSession(new User("tester", "")));
            return mockRequest;
        });

        mockMvc.perform(request).andExpectAll(
            MockMvcResultMatchers.status().is3xxRedirection(),
            MockMvcResultMatchers.view().name("redirect:/todos")
        );

    }
}
