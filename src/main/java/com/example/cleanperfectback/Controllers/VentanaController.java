package com.example.cleanperfectback.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class VentanaController {

    @RequestMapping("/")
    public String getVentana2() {
        return "updated_index";
    }

    @RequestMapping("/entrada")
    public String getVentana3() {
        return "entrada";
    }
}
