package com.sungjin.jobfair.controller;

import com.sungjin.jobfair.command.CompanyVO;
import com.sungjin.jobfair.command.EmpVO;
import com.sungjin.jobfair.command.QnAVO;
import com.sungjin.jobfair.command.UserVO;
import com.sungjin.jobfair.pagination.Criteria;
import com.sungjin.jobfair.service.AdminService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/jobfair")
public class AdminController {

    @Autowired
    @Qualifier("adminService")
    private AdminService adminService;

    //################## 메인 #######################
    @PostMapping(value = "/getAuth")
    public ArrayList<UserVO> getAuth(Model model){
        ArrayList<UserVO> list = adminService.getAuth();
        
        return list;
    }

    //전체 가져오기
    @PostMapping(value = "/getAllData")
    public ArrayList<EmpVO> getAllData(Model model, @RequestBody CompanyVO vo){

        String com_num = vo.getCom_num();
        ArrayList<EmpVO> list = adminService.getAllData(com_num);
        System.out.println(list.toString());

        model.addAttribute("list", list);

        return list;
    }

    //전체 채용정보 페이지네이션
    @PostMapping(value = "/getEmpList")
    public ArrayList<EmpVO> getEmpList(Criteria cri) {

        ArrayList<EmpVO> list = adminService.getEmpList(cri);
        System.out.println(list.toString());

        return list;
    }

    //################## 통계 #######################

    //user의 정보를 가져와서 성비통계나타내보기
    @PostMapping(value = "/getUserInfo")
    public ArrayList<UserVO> getUserInfo(Model model){

        ArrayList<UserVO> list = adminService.getUserInfo();
        model.addAttribute("list", list);

        return list;
    }
}
