package com.example.hostelmanager1.controller;

import com.example.hostelmanager1.entity.Member;
import com.example.hostelmanager1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/8/2022 2:31 PM
 */
@Controller
@RequiredArgsConstructor
public class HomeController {
 private final MemberService memberService;

 @GetMapping("/")
 @ResponseBody
 public List<Member> homePage(){
  return memberService.findAllMembers();
 }
}
