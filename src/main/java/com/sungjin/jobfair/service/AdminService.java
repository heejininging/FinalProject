package com.sungjin.jobfair.service;

import com.sungjin.jobfair.command.CompanyVO;
import com.sungjin.jobfair.command.EmpVO;

import java.util.ArrayList;
import java.util.Map;

public interface AdminService {

    //**********************************************채용공고**********************************************
    public ArrayList<EmpVO> getEmpData();
//    기업 정보가져오기
    public ArrayList<CompanyVO> getComData(String com_num);

    //기업이랑 채용정보 조인해서 가져오기

}
