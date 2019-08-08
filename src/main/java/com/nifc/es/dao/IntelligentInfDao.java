package com.nifc.es.dao;

import com.nifc.es.entity.IntelligentInf;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ClassName IntelligentInfDao
 * @Description 智能资讯Dao
 * @Author charlesYan
 * @Date 2019/7/24 18:57
 * @Version 1.0
 **/

// IntelligentInf 资讯       Long 主键类型
//@Repository
public interface IntelligentInfDao extends ElasticsearchRepository<IntelligentInf,String> {


//    Page<IntelligentInf> findByTitleAndPage(String title, Pageable pageable);
    List<IntelligentInf> findByTitle(String title);
    List<IntelligentInf> findByInfContent(String content);
    List<IntelligentInf> findByTitleOrInfContent(String title, String content);
    List<IntelligentInf> findByTitleOrInfContent(String title, String content, Pageable pageable);


}
