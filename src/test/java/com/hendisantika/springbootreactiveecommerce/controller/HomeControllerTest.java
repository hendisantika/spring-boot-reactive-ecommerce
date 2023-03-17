package com.hendisantika.springbootreactiveecommerce.controller;

import com.hendisantika.springbootreactiveecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/18/23
 * Time: 06:24
 * To change this template use File | Settings | File Templates.
 */
@WebFluxTest(value = HomeController.class)
class HomeControllerTests {

    @Autowired
    WebTestClient client;

    @MockBean
    CartService mockCartService;


}
