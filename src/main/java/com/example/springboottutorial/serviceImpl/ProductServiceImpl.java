package com.example.springboottutorial.serviceImpl;

import com.example.springboottutorial.models.Cart;
import com.example.springboottutorial.models.Order;
import com.example.springboottutorial.models.Product;
import com.example.springboottutorial.repositories.ProductRepositories;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Service
public class ProductServiceImpl {

    private ProductRepositories productRepositories;

    @Autowired
    public ProductServiceImpl(ProductRepositories productRepositories) {
        this.productRepositories = productRepositories;
    }

    public Supplier<List<Product>> findAllProducts = ()-> productRepositories.findAll();

    public void addProductToCart(Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart;
        if (session.getAttribute("cart") !=null){
            cart = (Cart) session.getAttribute("cart");
            cart.setProductIds(cart.getProductIds()+","+id);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);
        }
        else {
            cart = Cart.builder().productIds(id.toString())
                    .userId((Long) session.getAttribute("UserID")).build();
            session.setAttribute("cart", cart);
            session.setAttribute("cartItems", cart.getProductIds().split(",").length);

        }
    }

    public void checkOutCart(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        List<Product> productList = new ArrayList<>();
        List<String> productIds = Arrays.stream(cart.getProductIds().split(",")).toList();
        productIds.forEach(id->{
            productList.add(productRepositories.findById(Long.parseLong(id)).orElseThrow(()-> new NullPointerException("No such product found with ID : "+ id)));
        });

        final BigDecimal[] totalPrice = {new BigDecimal(0)};
        productList.forEach(product -> totalPrice[0] = totalPrice[0].add(product.getPrice()));
        model.addAttribute("totalPrice", "Total Price: $" + totalPrice[0]);
        session.setAttribute("cart", null);
        Order order = Order.builder()
                .productList(productList)
                .userId((Long) session.getAttribute("UserID"))
                .totalPrice(totalPrice[0])
                .build();
        session.setAttribute("order", order);
        model.addAttribute("order", order);
    }


}
