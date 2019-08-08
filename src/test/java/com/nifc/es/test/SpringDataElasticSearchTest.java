package com.nifc.es.test;

import com.nifc.es.entity.IntelligentInf;
import com.nifc.es.service.IntelligentInfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName SpringDataElasticSearchTest
 * @Description ES测试类
 * @Author charlesYan
 * @Date 2019/7/24 19:21
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-es.xml")//获取配置文件
public class SpringDataElasticSearchTest {


    @Autowired
    private IntelligentInfService infService;//注入操作智能资讯的Service

    @Autowired
    private ElasticsearchTemplate esTemplate;//ES操作模板类


    //创建索引库
    @Test
    public void createIndex() throws Exception {
        //创建索引库并配置映射关系
        esTemplate.createIndex(IntelligentInf.class);
        //配置已创建好索引库的映射关系
        //template.putMapping(IntelligentInf.class);
    }

    /**
     * 测试新增文档
     */
    @Test
    public void testAddDocument() throws Exception {
        //创建一个IntelligentInf对象
        IntelligentInf inf = new IntelligentInf();
        inf.setInfId("1");
        inf.setTitle("标题");
        inf.setInfContent("标题内容");
        //把文档写入索引库
        infService.save(inf);
    }


    /**
     * 测试修改文档
     */
    @Test
    public void testUpdateDocumentById(){
        IntelligentInf article = new IntelligentInf();
        article.setInfId("1");
        article.setTitle("elasticSearch 5.6.8版本发布");
        article.setInfContent("ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎");
        infService.update(article);
    }


    /**
     * 测试删除文档
     */
    @Test
    public void testDeleteDocumentById() throws Exception {
        //删除指定 id 的文档
        infService.deleteById("1");
        //全部删除
        //infService.deleteAll();
    }


    /**
     * 测试批量新增文档
     */
    @Test
    public void testAddBatchDocuments(){
        //构建资讯集合
        ArrayList<IntelligentInf> infList = new ArrayList<IntelligentInf>();
        for(int i = 2; i<10; i++){
            IntelligentInf inf = new IntelligentInf();
            inf.setInfId(i + "");
            inf.setTitle("资讯标题" + i);
            inf.setInfContent("资讯内容有很多" + i);

        }
        //批量新增文档
        infService.saveAll(infList);

    }

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAllDocuments(){
        Iterable<IntelligentInf> iterable = infService.findAll();
        Iterator<IntelligentInf> iterator = iterable.iterator();
        while (iterator.hasNext()){
            IntelligentInf inf = iterator.next();
            System.out.println(inf);
        }
    }

    /**
     * 测试分页查询所有
     */
    @Test
    public void testFindAllDocumentsByPage(){
        // 创建分页对象，从0开始，表示第一页，每页查询5条数据
        PageRequest request = PageRequest.of(0, 5);
        Page<IntelligentInf> page = infService.findAll(request);
        List<IntelligentInf> contentList = page.getContent();
        for (IntelligentInf intelligentInf : contentList) {
            System.out.println(intelligentInf.toString());
        }

    }

    /**
     * 测试根据 Entity 中定义的 ik 分词器拆分的字段来查询
     */
    @Test
    public void testFindByTitle(){
        List<IntelligentInf> infList = infService.findByTitle("检索查询");
        infList.stream().forEach(a-> System.out.println(a));
    }

    /**
     * 测试根据 Entity 中定义的 ik 分词器拆分的字段来高亮查询
     */
    @Test
    public void testHighLightFindByTitle(){
        String field = "title";
        String searchMessage = "数据查询检索解决方案";
        List<IntelligentInf> infList = infService.highLightFindByTitle(field,searchMessage);
        infList.stream().forEach(a-> System.out.println(a));
    }

    /**
     * 测试拼音汉字组合查询
     */
    @Test
    public void testFindByCombination(){
        String title = "查询索引";
        String infContent = "同步配置";
        List<IntelligentInf> infList = infService.findByCombination(title, infContent);
        infList.stream().forEach(a -> System.out.println(a));
    }

    @Test
    public void testFindByTitleAndPage(){
        // 创建分页对象，从0开始，表示第一页，每页查询5条数据
        PageRequest request = PageRequest.of(0, 5);
//        String title = "查询检索插件";
        String title = "检索插件";
        Page<IntelligentInf> infPage = infService.findByTitleAndPage(title, request);
        //总条数
        int total = infPage.getTotalPages();
        System.out.println("总页数：" + total);
        //遍历
        infPage.forEach(inf -> System.out.println("inf：" + inf));

    }
}
