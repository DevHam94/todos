package todoapp.web;

import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTests {

    @Autowired ApplicationContext applicationContext;
    @Autowired UserSessionRepository userSessionRepository;
    @Autowired LoginController controller;

//    MockMvc mockMvc;

    WebClient webClient;
    WebTestClient webTestClient;

    @BeforeEach
    void setUp(@LocalServerPort int port) {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        webClient = WebClient.create("http://localhost:" + port);
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
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

//        Assertions.assertTrue(applicationContext.containsBean("loginController"));
//        applicationContext.close();
        Assertions.assertTrue(applicationContext.containsBean("loginController"));
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
//        RequestBuilder request = MockMvcRequestBuilders.get("/login");
//        mockMvc.perform(request).andExpectAll(
//                MockMvcResultMatchers.status().isOk(),
//                MockMvcResultMatchers.view().name("login"),
//                MockMvcResultMatchers.model().attribute("site", siteProperties)
//        );
        webTestClient.get().uri("/login").exchange().expectAll(
                spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBody()
                        .xpath("//input[@name='username']").exists()
                        .xpath("//input[@name='password']").exists()
                        .xpath("//a[text()='" + siteProperties.getAuthor() + "']").exists()
        );
    }

    @Test
    void 인증된_사용자가_로그인화면에_접근화면_할일관리화면으로_전환시킨다() throws Exception {
//        userSessionRepository.set(new UserSession(new User("tester", "")));
//
////        Assertions.assertEquals("redirect:/todos", controller.loginForm());
//
//        RequestBuilder request = MockMvcRequestBuilders.get("/login").with(mockRequest -> {
//            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
//            userSessionRepository.set(new UserSession(new User("tester", "")));
//            return mockRequest;
//        });
//
//        mockMvc.perform(request).andExpectAll(
//            MockMvcResultMatchers.status().is3xxRedirection(),
//            MockMvcResultMatchers.view().name("redirect:/todos")
//        );
        MultiValueMap<String, String> cookieStore = new LinkedMultiValueMap<>();

        // 1. 사용자가 로그인을 한다
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>(); {
            formData.add("username", "user");
            formData.add("password", "password");
        }

        BodyInserters.FormInserter<String> formBody = BodyInserters.fromFormData(formData);
        webClient.post().uri("/login").body(formBody).exchangeToMono(response -> {
            response.cookies().forEach((name, cookie) -> {
                cookieStore.forEach(cookie -> cookieStore.add(name, cookie.getValue()));
            });
            return Mono.empty();
        }).block();

        // 2. 로그인된 사용자가 로그인 화면 접근시 상황을 검증한다
        webTestClient.get().uri("/login").cookies(cookies -> cookies.addAll(cookieStore).exchange().expectAll(
                spec -> spec.expectStatus().is3xxRedirection(),
                spec -> spec.expectHeader().value(HttpHeaders.LOCATION, StringEndsWith.endsWith("/todos"))
        );
    }
}
