package com.korat.cart.service;

import com.github.abel533.entity.Example;
import com.korat.cart.bean.Cart;
import com.korat.cart.bean.Item;
import com.korat.cart.bean.User;
import com.korat.cart.mapper.CartMapper;
import com.korat.cart.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 购物车业务层
 *
 * @author solar
 * @date 2017/7/4
 */
@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ItemService itemService;
    /**
     * 添加商品到购物车
     *
     * @param itemId
     */
    public void addItemToCart(Long itemId) {
        User user= UserThreadLocal.get();
        Cart cart=new Cart();
        cart.setItemId(itemId);
        cart.setUserId(user.getId());
        cart = cartMapper.selectOne(cart);
        Cart record=new Cart();
        // 购物车中没有这个商品
        if (cart == null) {
            Item item = itemService.queryItemByItemId(itemId);
            record.setItemId(itemId);
            record.setUserId(user.getId());
            record.setItemTitle(item.getTitle());
            record.setItemPrice(item.getPrice());
            record.setNum(1);
            record.setCreated(new Date());
            record.setUpdated(record.getCreated());
        //    插入到数据库
            this.cartMapper.insert(record);
        }else {
        //    购物车中有这件商品
            record.setNum(cart.getNum() + 1);
            record.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    /**
     * 查询购物车
     *
     * @return
     */
    public List<Cart> queryCartList() {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
        //solartodo 分页
        User user=UserThreadLocal.get();
        example.createCriteria().andEqualTo("userId", user.getId());
        return this.cartMapper.selectByExample(example);
    }
}
