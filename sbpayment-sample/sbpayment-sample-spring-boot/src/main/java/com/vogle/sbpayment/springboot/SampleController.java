package com.vogle.sbpayment.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Payment controller
 *
 * @author Allan Im
 */
@Controller
public class SampleController {

    @GetMapping("/checkout")
    public String checkout(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "checkout";
    }
}
