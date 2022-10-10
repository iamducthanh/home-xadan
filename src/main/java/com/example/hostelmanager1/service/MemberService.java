package com.example.hostelmanager1.service;

/**
 * Description:
 *
 * @author: GMO_ThanhND
 * @version: 1.0
 * @since 10/10/2022 10:16 PM
 */

import com.example.hostelmanager1.entity.Member;
import com.example.hostelmanager1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
 private final MemberRepository repo;
 public List<Member> findAllMembers() {
  return repo.findAll();
 }
}
