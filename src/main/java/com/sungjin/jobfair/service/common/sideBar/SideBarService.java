package com.sungjin.jobfair.service.common.sideBar;

import com.sungjin.jobfair.command.MenuVO;

import java.util.ArrayList;
import java.util.Map;

public interface SideBarService {
    //메뉴 아코디언에 들어갈 서브메뉴 목록 받아오기
    ArrayList<MenuVO> getMenu(String menu_id, String menu_auth);
}
