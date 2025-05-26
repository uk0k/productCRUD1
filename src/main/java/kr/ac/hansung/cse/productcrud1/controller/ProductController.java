package kr.ac.hansung.cse.productcrud1.controller;

import jakarta.validation.Valid;
import kr.ac.hansung.cse.productcrud1.entity.Product;
import kr.ac.hansung.cse.productcrud1.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping({"", "/"})
    public String viewHomePage(Model model) {
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "index";
    }

    @GetMapping("/new")
    public String showNewProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "new_product";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable Long id, Model model) {
        Product product = service.get(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return (product.getId() == null) ? "new_product" : "edit_product";
        }

        service.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/products";
    }
}
