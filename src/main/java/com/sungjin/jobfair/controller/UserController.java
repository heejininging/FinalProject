package com.sungjin.jobfair.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.sungjin.jobfair.PageGate;
import com.sungjin.jobfair.pagination.Criteria;
import com.sungjin.jobfair.pagination.PageVO;
import org.springframework.boot.Banner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sungjin.jobfair.command.*;
import com.sungjin.jobfair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/jobfair")
public class UserController {

    //인터페이스 타입 선언
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    //application.properties에서 경로 가져오기
    @Value("${project.uploadpath}")
    private String uploadpath;

    //**********************************************이력서**********************************************
        //파일 생성 시 날짜 별로 폴더 생성 후 저장할 경로 생성
    public String makeDir() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String now = sdf.format(date);

        System.out.println("uploadpath = " + uploadpath);
        String path = uploadpath + "\\" + now;
        File file = new File(path);
        if (file.exists() == false) {
            file.mkdir();
        }
        return path;
    }
        //이력서 내용가져오기
    @PostMapping(value = "/resumeInfo")
    public ArrayList<ResumeVO> resumeInfo(Model model) {

        ArrayList<ResumeVO> list = userService.resumeInfo();
        model.addAttribute("list", list);

        return list;
    }

        //이력서 삭제하기
    @GetMapping(value = "/deleteResume")
    public void deleteResume(@RequestParam("res_num") int res_num) {

        userService.deleteResume(res_num);

    }
        //이력서 등록
    @PostMapping(value = "/regResume",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String regResume(@RequestPart("res_img") MultipartFile file,
                            @RequestPart("resData") ObjectNode node,
                            ResumeVO resumeVO,
                            ArrayList<EduVO> eduList,
                            ArrayList<WeVO> weList,
                            ArrayList<CertVO> certList) {

        //파일 객체 분해 및 경로+이름 지정
        String pic_name = file.getOriginalFilename();
        String pic_path = makeDir();
        String pic_uuid = UUID.randomUUID().toString();
        String saveName = pic_path + "/" + pic_uuid + "_" + pic_name;
        System.out.println(pic_name);
        System.out.println("pic_uuid = " + pic_uuid);
        System.out.println(pic_path);
        ObjectMapper mapper = new ObjectMapper();
        try {
            //넘어온 이미지파일 먼저 생성
            File save = new File(saveName);
            file.transferTo(save);
            //Json을 객체로 변환
            resumeVO = mapper.treeToValue(node.get("resInfo"), ResumeVO.class);
            resumeVO.setRes_picName(pic_name);
            resumeVO.setRes_picPath(pic_path);
            resumeVO.setRes_picUuid(pic_uuid);
            ArrayList tmpEduList = mapper.treeToValue(node.get("eduInfo"), ArrayList.class);
            ArrayList tmpWeList = mapper.treeToValue(node.get("weInfo"), ArrayList.class);
            ArrayList tmpCertList = mapper.treeToValue(node.get("certInfo"), ArrayList.class);
            eduList = mapper.convertValue((node.get("eduInfo")), new TypeReference<ArrayList<EduVO>>(){});
            weList = mapper.convertValue((node.get("weInfo")), new TypeReference<ArrayList<WeVO>>(){});
            certList = mapper.convertValue((node.get("certInfo")), new TypeReference<ArrayList<CertVO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }

        //인적사항 insert
        userService.regResume(resumeVO);
        int res_num = resumeVO.getRes_num();
        String user_id = resumeVO.getUser_id();

        for (EduVO edu : eduList) {
            edu.setRes_num(res_num);
            edu.setEdu_grades("4.0");
            edu.setEdu_totalGrades("4.5");
            System.out.println("edu = " + edu);
            userService.regResEdu(edu);
        }
        for (WeVO we : weList) {
            we.setRes_num(res_num);
            userService.regResWe(we);
        }
        for (CertVO cert : certList) {
            cert.setRes_num(res_num);
            userService.regResCert(cert);
        }

        return "success";
    }


    //**********************************************큐앤에이 **********************************************
        //큐앤에이 등록
    @PostMapping(value = "/qnaRegist")
    public String qnaRegist(@RequestBody QnAVO vo) {
        userService.qnaRegist(vo);
        return "success";
    }
        //큐앤에이 목록
    @PostMapping(value = "/getQnAList")
    public ArrayList<QnAVO> getQnAList(Criteria cri) {

        ArrayList<QnAVO> list = userService.getQnAList(cri);
        System.out.println(list.toString());

        return list;
    }
        //큐앤에이 상세페이지 데이터 불러오기
    @GetMapping(value = "/getQnADetail")
    public QnAVO getQnADetail(@RequestParam("qa_num") int qa_num) {

        QnAVO vo = userService.getQnADetail(qa_num);
        System.out.println("유저VO");
        System.out.println(vo);

        return vo;
    }
        //큐앤에이 수정
    @PostMapping ( "/uQnAModi")
    public int uQnAModi(@RequestBody QnAVO vo) {
        int a = userService.uQnAModi(vo);
        System.out.println("글수정");
        System.out.println(vo.toString());

        return a;
    }

    //채용상세공고에서 질문하기 버튼 누를경우 기업정보 넘기는 메서드
    //(채용공고정보) jpl번호를 기준으로 jpl테이블에서 데이터를 불러오는 메서드
//    @PostMapping("/uQnABtnClick")
//        public QnAVO uQnABtnClick(){
//            QnAVO vo = userService.uQnABtnClick();
//            System.out.println(vo);
//            return vo;
//        }


    //**********************************************채용공고**********************************************
        //채용공고 목록
    @PostMapping(value = "/getJobPostList")
    public ArrayList<EmpVO> getJobPostList(Model model) {

        ArrayList<EmpVO> list = userService.getJobPostList();
        model.addAttribute("list", list);
//        System.out.println(list.toString());

        return list;
    }
        //채용공고 검색
    @PostMapping(value="/getJobPostSrc",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ArrayList<EmpVO> getJobPostSrc(@RequestBody ObjectNode node,
                                          ArrayList<EmpSearchVO> jpl_duty,
                                          ArrayList<EmpSearchVO> jpl_workHistory,
                                          ArrayList<EmpSearchVO> jpl_workForm,
                                          ArrayList<EmpSearchVO> jpl_education,
                                          ArrayList<EmpSearchVO> jpl_conditions,
                                          ArrayList<EmpSearchVO> jpl_certificate,
                                          ArrayList<EmpSearchVO> jpl_salary,
                                          ArrayList<EmpSearchVO> jpl_locationSi,
                                          ArrayList<EmpSearchVO> jpl_locationGu
    ) {
        //System.out.println(vo);
        //System.out.println("검색메서드"+list.toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        //System.out.println(mapper.);
        System.out.println("node = " + node);

        ArrayList<EmpVO> empvo = new ArrayList<>();

        try {
            System.out.println(1);
            ArrayList tmp_jpl_duty = mapper.treeToValue(node.get("jpl_duty"), ArrayList.class);
            ArrayList tmp_jpl_workHistory = mapper.treeToValue(node.get("jpl_workHistory"), ArrayList.class);
            ArrayList tmp_jpl_workForm = mapper.treeToValue(node.get("jpl_workForm"), ArrayList.class);
            ArrayList tmp_jpl_education = mapper.treeToValue(node.get("jpl_education"), ArrayList.class);
            ArrayList tmp_jpl_conditions = mapper.treeToValue(node.get("jpl_conditions"), ArrayList.class);
            ArrayList tmp_jpl_certificate = mapper.treeToValue(node.get("jpl_certificate"), ArrayList.class);
            ArrayList tmp_jpl_salary = mapper.treeToValue(node.get("jpl_salary"), ArrayList.class);
            ArrayList tmp_jpl_locationSi = mapper.treeToValue(node.get("jpl_locationSi"), ArrayList.class);
            ArrayList tmp_jpl_locationGu = mapper.treeToValue(node.get("jpl_locationGu"), ArrayList.class);

            System.out.println(tmp_jpl_duty.toString());

            jpl_duty = mapper.convertValue(tmp_jpl_duty.toString(), new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_workHistory = mapper.convertValue(tmp_jpl_workHistory, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_workForm = mapper.convertValue(tmp_jpl_workForm, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_education = mapper.convertValue(tmp_jpl_education, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_conditions = mapper.convertValue(tmp_jpl_conditions, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_certificate = mapper.convertValue(tmp_jpl_certificate, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_salary = mapper.convertValue(tmp_jpl_salary, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_locationSi = mapper.convertValue(tmp_jpl_locationSi, new TypeReference<ArrayList<EmpSearchVO>>(){});
            jpl_locationGu = mapper.convertValue(tmp_jpl_locationGu, new TypeReference<ArrayList<EmpSearchVO>>(){});

            System.out.println(jpl_duty);
            for(EmpSearchVO vo : jpl_duty){
                System.out.println(vo);
                userService.getJobPostSrc(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empvo;
    }

        //기업이 작성한 채용공고 내용 가져오는 메서드 (박희진 작성중)
//    @PostMapping( value = "EmpRegistInfo")
//    public ArrayList<EmpVO> EmpRegistInfo(Model model){
//
//        ArrayList<EmpVO> list = userService.EmpRegistInfo();
//        model.addAttribute("list", list);
//        System.out.println("controller탐");
//
//        return list;
//    }


    //**********************************************페이지네이션**********************************************
//    @PostMapping(value = "/getQnATotal")
////    public int getQnATotal() {
////       int total = userService.getQnATotal();
////        System.out.println(total);
////        return total;
////    }



    //페이지네이션

//    public ResponseEntity <Map<String, Object>> uQnAListAxios(Criteria cri) {
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("pageVOList", userService.getQnAList(cri));
//        map.put("totalPage", userService.uQnAGetTotal(cri));
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }

    @GetMapping("/uQnAListAxios")
    public PageGate list(Criteria cri) {

        int total = userService.uQnAGetTotal(cri);
        PageVO pageVO = new PageVO(cri, total);

        ArrayList<QnAVO> list = userService.getQnAList(cri);

        PageGate pageGate = new PageGate(list, pageVO);

        System.out.println(list.toString());

        return pageGate;
    }

    @PostMapping("/uQnAGetTotal")
    public int uQnAGetTotal(Criteria cri) {
        int total = userService.uQnAGetTotal(cri);

        return total;
    }





}