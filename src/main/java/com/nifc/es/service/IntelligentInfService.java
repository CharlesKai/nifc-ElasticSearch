package com.nifc.es.service;

import com.nifc.es.entity.IntelligentInf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @ClassName IntelligentInfService
 * @Description 智能资讯接口
 * @Author charlesYan
 * @Date 2019/7/24 16:22
 * @Version 1.0
 **/
public interface IntelligentInfService {

    //CRUD
    public void save(IntelligentInf intelligentInf);
    public void update(IntelligentInf intelligentInf);
    public void deleteById(String id);
    public Iterable<IntelligentInf> findAll();

    //批量增加
    public void saveAll(List<IntelligentInf> list);
    //分页查询
    public Page<IntelligentInf> findAll(Pageable pageable);
    //不分词根据标题查询（含分页）
//    Page<IntelligentInf> findByTitleAndPage(String condition, Pageable pageable);

    //根据 Entity 中定义的 ik 分词器拆分的字段来查询
    List<IntelligentInf> findByTitle(String title);
    List<IntelligentInf> findByContent(String content);
    List<IntelligentInf> findByTitleOrContent(String title, String content);
    List<IntelligentInf> findByTitleOrContent(String title, String content, Pageable pageable);

    //测试根据 Entity 中定义的 ik 分词器拆分的字段来高亮查询
    List<IntelligentInf> highLightFindByTitle(String field, String searchMessage);

    //拼接搜索条件
    public List<IntelligentInf> findByCombination(String title, String infContent);

    //分词查询标题并进行分页
    public Page<IntelligentInf> findByTitleAndPage(String title, Pageable pageable);
}
