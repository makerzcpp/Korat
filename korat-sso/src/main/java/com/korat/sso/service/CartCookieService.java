package com.korat.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korat.sso.domain.Cart;
import com.korat.sso.domain.User;
import com.korat.sso.mapper.CartMapper;
import com.korat.sso.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 登录的时候持久化cookie中的数据
 *
 * @author solar
 * @date 2017/7/10
 */
@Service(value = "SSO_CARTCOOKIESERVICE")
public class CartCookieService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserService userService;
    @Autowired
    private CartMapper cartMapper;

    /**
     * 持久化cookie中的数据
     * @param request
     * @param cookie_name
     * @param userName
     */
    public void persistenceCart(HttpServletRequest request, String cookie_name, String userName) {
        String value = CookieUtils.getCookieValue(request, cookie_name, true);
        try {
            List<Cart> cartList = objectMapper.readValue(value, objectMapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
            User user = userService.queryUserByUsername(userName);
            if (user != null) {
                for (Cart cart : cartList) {
                    cart.setUserId(user.getId());
                    cartMapper.insert(cart);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
