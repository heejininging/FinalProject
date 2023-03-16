package com.sungjin.jobfair.service;

import com.sungjin.jobfair.command.*;

import java.util.ArrayList;

public interface UserService {
    public UserVO login(UserVO vo);
    //큐앤에이 답변 등록
    public void qnaRegist(QnAVO vo);
    //큐앤에이 목록 가져오기
    public ArrayList<QnAVO> getQnAList();
    public UserVO info(String id);

    //채용공고 목록 가져오기
    public ArrayList<EmpVO> getJobPostList();
    //채용공고 검색 목록 가져오기
    public ArrayList<EmpVO> getJobPostSrc(EmpSearchVO vo);


    //큐앤에이 상세페이지 데이터 가져오기
    public QnAVO getQnADetail(int qa_num);
    //이력서 정보 가져오기
    public ArrayList<ResumeVO> resumeInfo();
}