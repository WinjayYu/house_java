package com.ryel.springBootJpa.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    List list ;

    Page page ;


    public PageVo(org.springframework.data.domain.Page page){
        this.page = new Page(page.getTotalElements(),page.getTotalPages(),page.getNumber()+1);
        this.list = page.getContent();
    }



    private class  Page {

        Long totalNum;

        Integer totalPage;

        Integer currentPage;

        public Page(Long totalNum,Integer totalPage,Integer currentPage){
            this.totalNum = totalNum;
            this.totalPage = totalPage;
            this.currentPage = currentPage;
        }

    }
}

