package com.example.hostelmanager1.controller;

import com.example.hostelmanager1.dto.DebitInfo;
import com.example.hostelmanager1.dto.MemberStatis;
import com.example.hostelmanager1.dto.SpendingDto;
import com.example.hostelmanager1.entity.Member;
import com.example.hostelmanager1.entity.MemberSpending;
import com.example.hostelmanager1.entity.Spending;
import com.example.hostelmanager1.repository.MemberRepository;
import com.example.hostelmanager1.repository.MemberSpendingRepository;
import com.example.hostelmanager1.repository.SpendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    private final MemberRepository memberRepository;
    private final SpendingRepository spendingRepository;
    private final MemberSpendingRepository memberSpendingRepository;
    @Autowired
    private HttpServletRequest servletRequest;

    @GetMapping("/")
    public String home(){
        servletRequest.getSession().setAttribute("member", null);
        return "home";
    }

    @GetMapping("/home")
    public String homePage(@RequestParam("username") String username, Model model) {
//        if(servletRequest.getSession().getAttribute("username") == null){
        username = username.toLowerCase(Locale.ROOT);
            Member member = memberRepository.findByUsername(username);
            if(member != null){
                servletRequest.getSession().setAttribute("username", member.getUsername());
                servletRequest.getSession().setAttribute("member", member);
                servletRequest.getSession().setAttribute("fullname", member.getFullname());
                model.addAttribute("name", member.getFullname());
                model.addAttribute("username", member.getUsername());
                return "home";
            } else {
                return "index";
            }
//        } else {
//            model.addAttribute("name", servletRequest.getSession().getAttribute("fullname"));
//            return "home";
//        }
    }

    @GetMapping("spending")
    @ResponseBody
    public List<Spending> getAllSpending() {
        List<Spending> sp = spendingRepository.getAllSpending();
        Collections.sort(sp, Comparator.comparing(Spending::getModifiedDate));
        return spendingRepository.getAllSpending();
    }

    @PostMapping("spending")
    @ResponseBody
    public String saveSpending(@RequestBody SpendingDto spendingDto) {
        System.out.println(spendingDto);
        String spendingId = spendingDto.getIdSpending() == null || spendingDto.getIdSpending().isEmpty() ? UUID.randomUUID().toString() : spendingDto.getIdSpending();
        List<MemberSpending> memberSpendings = new ArrayList<>();
        if (spendingDto.getIdSpending() != null && !spendingDto.getIdSpending().isEmpty()) {
            List<MemberSpending> memberSpendingList = memberSpendingRepository.findAllBySpendingId(spendingDto.getIdSpending());
            if (!memberSpendingList.isEmpty()) {
                memberSpendingRepository.deleteAll(memberSpendingList);
            }
        }

        Date createDate = spendingDto.getIdSpending() == null || spendingDto.getIdSpending().isEmpty() ? new Date() : spendingRepository.findById(spendingId).get().getCreatedDate();

        spendingDto.getUse().forEach(o -> {
            Member member = memberRepository.getById(Integer.parseInt(o));
            MemberSpending memberSpending = MemberSpending.builder()
                    .id(UUID.randomUUID().toString())
                    .spendingId(spendingId)
                    .member(member)
                    .build();
            memberSpendings.add(memberSpending);
        });
        Member member = (Member) servletRequest.getSession().getAttribute("member");
        if(spendingDto.getIdSpending() != null && !spendingDto.getIdSpending().isEmpty()){
            Spending spendingOld = spendingRepository.getById(spendingId);
            member = spendingOld.getMember();
        }
        Spending spending = Spending.builder()
                .id(spendingId)
                .expenses(spendingDto.getExpenses())
                .price(Integer.parseInt(spendingDto.getPrice()))
                .member(member)
                .createdDate(spendingDto.getIdSpending() == null || spendingDto.getIdSpending().isEmpty() ? new Date() : createDate)
                .modifiedBy(((Member) servletRequest.getSession().getAttribute("member")).getFullname())
                .modifiedDate(new Date())
                .note(spendingDto.getNote())
                .build();

        spendingRepository.save(spending);
        memberSpendingRepository.saveAll(memberSpendings);

        return "";
    }

    @GetMapping("spending-detail")
    @ResponseBody
    public Spending getSpendingById(@RequestParam("id") String id) {
        Spending spending = spendingRepository.findById(id).get();
        Member member = (Member) servletRequest.getSession().getAttribute("member");
        if(member != null && member.getId().equals(spending.getMember().getId())){
            spending.setDelete(true);
        } else {
            spending.setDelete(false);
        }
        return spending;
    }

    @DeleteMapping("spending")
    @ResponseBody
    public String deleteSpending(@RequestParam("id") String id) {
        List<MemberSpending> memberSpendings = memberSpendingRepository.findAllBySpendingId(id);
        memberSpendingRepository.deleteAll(memberSpendings);
        spendingRepository.deleteById(id);
        return "SUCCESS";
    }

    @GetMapping("statis")
    @ResponseBody
    public List<MemberStatis> loadStatis() {
        List<MemberStatis> memberStatis = new ArrayList<>();
        List<Member> members = memberRepository.findAll();
        members.forEach(o -> {
            List<DebitInfo> debitInfos = new ArrayList<>();
            members.forEach(o1 -> {
                if(!o1.getId().equals(o.getId())){
                    debitInfos.add(DebitInfo.builder()
                            .creditorId(o1.getId())
                            .total(0)
                            .creditor(o1.getFullname())
                            .build());
                }
            });
            memberStatis.add(MemberStatis.builder()
                    .id(o.getId())
                    .totalSpending(0)
                    .fullName(o.getFullname())
                    .debitInfos(debitInfos)
                    .debitTotal(0)
                    .img(o.getImg())
                    .build());
        });
        List<Spending> spendings = spendingRepository.findAll();
        spendings.forEach(o -> {
            Integer memberId = o.getMember().getId();
            Integer totalSpending = o.getPrice();
            List<Integer> memberIds = new ArrayList<>();
            o.getMemberSpendings().forEach(s -> {
                memberIds.add(s.getMember().getId());
            });

            memberStatis.forEach(m -> {
                if(memberIds.contains(m.getId())){
                    m.getDebitInfos().forEach(debitInfo -> {
                        if(debitInfo.getCreditorId().equals(memberId)){
                            Integer total = debitInfo.getTotal();
                            debitInfo.setTotal(total + o.getPrice() / memberIds.size());
                        }
                    });
                }
                if (m.getId().equals(memberId)) {
                    m.totalSpending += totalSpending;
                    m.fullName = o.getMember().getFullname();
                }
            });

        });

        memberStatis.forEach(me -> {
            memberStatis.forEach(other -> {
                if(other.getId() != me.getId()){
                    other.getDebitInfos().forEach(otherDebitInfo -> {
                        if(otherDebitInfo.getCreditorId() == me.getId()){
                            me.getDebitInfos().forEach(meDebitInfo -> {
                                if (meDebitInfo.getCreditorId() == other.getId()) {
                                    if(meDebitInfo.getTotal() < otherDebitInfo.getTotal()){
                                        otherDebitInfo.setTotal(otherDebitInfo.getTotal() - meDebitInfo.getTotal());
                                        meDebitInfo.setTotal(0);
                                    } else if(meDebitInfo.getTotal() > otherDebitInfo.getTotal()){
                                        meDebitInfo.setTotal(meDebitInfo.getTotal() - otherDebitInfo.getTotal());
                                        otherDebitInfo.setTotal(0);
                                    } else {
                                        meDebitInfo.setTotal(0);
                                        otherDebitInfo.setTotal(0);
                                    }
                                }
                            });
                        }
                    });
                }
            });
        });

        memberStatis.forEach(o -> {
            AtomicReference<Integer> total = new AtomicReference<>(0);
            o.getDebitInfos().forEach(o1 -> {
                total.updateAndGet(v -> v + o1.getTotal());
            });
            o.setDebitTotal(total.get());
        });
        return memberStatis;
    }
}
