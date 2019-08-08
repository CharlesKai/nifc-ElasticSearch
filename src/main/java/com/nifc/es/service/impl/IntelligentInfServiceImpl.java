package com.nifc.es.service.impl;

import com.nifc.es.dao.IntelligentInfDao;
import com.nifc.es.entity.IntelligentInf;
import com.nifc.es.service.IntelligentInfService;
import com.nifc.es.util.DateUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName IntelligentInfServiceImpl
 * @Description 智能智讯接口实现类
 * @Author charlesYan
 * @Date 2019/7/24 16:23
 * @Version 1.0
 **/
@Service
public class IntelligentInfServiceImpl implements IntelligentInfService {

    @Autowired
    private IntelligentInfDao infDao;

    //ES操作模板类
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Iterable<IntelligentInf> findAll() {
        return infDao.findAll();
    }

    @Override
    public void save(IntelligentInf intelligentInf) {
        infDao.save(intelligentInf);
    }

    @Override
    public void update(IntelligentInf intelligentInf) {
        infDao.save(intelligentInf);
    }

    @Override
    public void deleteById(String id) {
        infDao.deleteById(id);

    }

    @Override
    public void saveAll(List<IntelligentInf> list) {
        infDao.saveAll(list);
    }

    @Override
    public Page<IntelligentInf> findAll(Pageable pageable) {
        return infDao.findAll(pageable);
    }

//    @Override
//    public Page<IntelligentInf> findByTitleAndPage(String condition, Pageable pageable) {
//        return infDao.findByTitleAndPage(condition,pageable);
//    }

    @Override
    public List<IntelligentInf> findByTitle(String title) {
        return infDao.findByTitle(title);
    }

    @Override
    public List<IntelligentInf> findByContent(String content) {
        return infDao.findByInfContent(content);
    }

    @Override
    public List<IntelligentInf> findByTitleOrContent(String title, String content) {
        return infDao.findByTitleOrInfContent(title,content);
    }

    @Override
    public List<IntelligentInf> findByTitleOrContent(String title, String content, Pageable pageable) {
        return infDao.findByTitleOrInfContent(title,content,pageable);
    }

    @Override
    public List<IntelligentInf> highLightFindByTitle(String field, String searchMessage) {
        //构建查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //构建查询条件
//                .withQuery(QueryBuilders.termQuery(field, searchMessage))
                .withQuery(QueryBuilders.queryStringQuery(searchMessage).defaultField(field))
                //设置高亮字段
                .withHighlightFields(new HighlightBuilder.Field(field)).build();

        Page<IntelligentInf> page = esTemplate.queryForPage(searchQuery, IntelligentInf.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> Class, Pageable pageable) {
                List<IntelligentInf> infList = new ArrayList<>();
                //命中记录
                SearchHits hits = response.getHits();
                for (SearchHit searchHit : hits) {
                    if (hits.getHits().length <= 0) {
                        return null;
                    }
                    IntelligentInf inf = new IntelligentInf();
                    String highLightMessage = searchHit.getHighlightFields().get(field).fragments()[0].toString();
                    inf.setId(searchHit.getId());
                    inf.setInfId(String.valueOf(searchHit.getSource().get("infId")));
                    inf.setTitle(String.valueOf(searchHit.getSource().get("title")));
                    inf.setInfContent(String.valueOf(searchHit.getSource().get("infContent")));
                    String crtTm = String.valueOf(searchHit.getSource().get("crt_tm"));
                    inf.setCrtTm(DateUtils.convertStringToDate("yyyy-MM-dd'T'HH:mm:ss.SSS Z", crtTm));
                    // 反射调用set方法将高亮内容设置进去
                    try {
                        String setMethodName = parSetName(field);
                        Class<? extends IntelligentInf> poemClazz = inf.getClass();
                        Method setMethod = poemClazz.getMethod(setMethodName, String.class);
                        setMethod.invoke(inf, highLightMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    infList.add(inf);
                }
                if (infList.size() > 0) {
                    AggregatedPage<T> result = new AggregatedPageImpl<T>((List<T>) infList, pageable,
                            response.getHits().getTotalHits());

                    return result;
                }
                return null;
            }
        });

        List<IntelligentInf> infs = page.getContent();
        return infs;
    }


    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return String
     */
    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        if (fieldName.charAt(0) == '_')
            startIndex = 1;
        return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
                + fieldName.substring(startIndex + 1);
    }

    @Override
    public List<IntelligentInf> findByCombination(String title, String infContent) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(structureQuery(title)).build();
        List<IntelligentInf> infList = infDao.search(searchQuery).getContent();
        return infList;
    }

    /**
     * @Author charlesYan
     * @Description 中英文拼接组合查询
     * @Date 14:46 2019/8/6
     * @Param [title]
     * @return org.elasticsearch.index.query.QueryBuilder
     **/
    private DisMaxQueryBuilder structureQuery(String title) {
        //使用dis_max直接取多个query中，分数最高的那个query的分数即可
        DisMaxQueryBuilder disMaxQueryBuilder = QueryBuilders.disMaxQuery();
        //设置权重boost，只搜索匹配title和infContent
        QueryBuilder ikTitleQuery = QueryBuilders.matchQuery("title", title).boost(2f);
        QueryBuilder pinyinTitleQuery = QueryBuilders.matchQuery("title.pinyin", title);
        QueryBuilder ikInfContentQuery = QueryBuilders.matchQuery("infContent", title).boost(2f);
        disMaxQueryBuilder.add(ikTitleQuery);
        disMaxQueryBuilder.add(pinyinTitleQuery);
        disMaxQueryBuilder.add(ikInfContentQuery);
        return disMaxQueryBuilder;
    }

    @Override
    public Page<IntelligentInf> findByTitleAndPage(String title, Pageable pageable) {
        //构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title",title));
//        queryBuilder.withQuery(QueryBuilders.queryStringQuery(title).defaultField("title"));
        //分页查询
        queryBuilder.withPageable(pageable);
        //获取搜索结果
        Page<IntelligentInf> infPage = infDao.search(queryBuilder.build());
        return infPage;
    }
}
