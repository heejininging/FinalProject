package com.sungjin.jobfair.service;

import com.sungjin.jobfair.command.*;
import com.sungjin.jobfair.pagination.Criteria;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface UserService {

    //################## 로그인 관련(login) Service #######################
    public UserVO login(UserVO vo);
    public UserVO info(String id);


    //################## QnA 관련 Service #######################
        //큐앤에이 답변 등록
    public void qnaRegist(QnAVO vo);
        //큐앤에이 목록 가져오기
    public ArrayList<QnAVO> getQnAList(Criteria cri);
        //큐앤에이 상세페이지 데이터 가져오기
    public QnAVO getQnADetail(int qa_num);
        //큐앤에이 수정
    public int uQnAModi(QnAVO vo);
        //큐앤에이 기업 데이터 전달하기
//    public QnAVO uQnABtnClick();


    //################## 채용공고(jobPost) 관련 Service #######################
        //채용공고 목록 가져오기
    public ArrayList<EmpVO> getJobPostList();
        //채용공고 검색 목록 가져오기
    public ArrayList<EmpVO> getJobPostSrc(EmpSearchVO vo);
        //기업이 입력한 채용공고 내용 유저의 채용공고 상세페이지에 뿌리기 (박희진 작성중)
//    public ArrayList<EmpVO> EmpRegistInfo();


    //################## 마이페이지(MyPage) Service #######################
        //이력서 정보 가져오기
    public ArrayList<ResumeVO> resumeInfo();
        //이력서 삭제버튼 누르면 데이터 삭제하기
    public void deleteResume(int res_num);


    //################## 이력서(Resume) 관련 Service #######################
        // 1) 이력서 -인적사항- 등록
    public void regResume(ResumeVO resVO);
        // 2) 이력서 -학력- 등록
    public void regResEdu(EduVO eduVO);
        // 3) 이력서 -경력- 등록
    public void regResWe(WeVO weVO);
        // 4) 이력서 -자격증- 등록
    public void regResCert(CertVO certVO);



    //####################### 페이지네이션 #######################
    //페이지 처리
//    public ArrayList<QnAVO> getPage(Criteria cri);

    //전체 게시글 가져오기
//    public int getQnATotal();


    //페이지네이션
//    public Map<String, Object> uQnAListAxios(Criteria cri);

    public int uQnAGetTotal(Criteria cri);

}