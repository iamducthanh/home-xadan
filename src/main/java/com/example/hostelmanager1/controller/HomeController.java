package com.example.hostelmanager1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/8/2022 2:31 PM
 */
@Controller
public class HomeController {
 @GetMapping("/")
 public String homePage(){
  return "home";
 }
}
