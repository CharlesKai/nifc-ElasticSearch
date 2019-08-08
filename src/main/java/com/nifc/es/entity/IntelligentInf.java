package com.nifc.es.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName IntelligentInf
 * @Description 智能资讯实体类
 * @Author charlesYan
 * @Date 2019/7/24 19:04
 * @Version 1.0
 **/

// indexName 为关联的索引库的名称(必须小写)      type 为创建索引库的类型
@Document(indexName = "nifc", type = "intelligentInf")
public class IntelligentInf implements Serializable{

    private static final long serialVersionUID = -1838668690328733289L;

    //配置 mapping 映射
    @Id
//    @Field(type = FieldType.text, store = true)
    private String id;

    private String infId;

    // index = 分词,store = 存储,analyzer = "存储采用分词器",searchAnalyzer = "查询采用分词器",type = 数据类型
//    @Field(type = FieldType.text, store = true, analyzer = "ik_smart")
//    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String title;

//    @Field(type = FieldType.text, store = true, analyzer = "ik_smart")
//    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String infContent;

    @JsonProperty("crt_tm")
    private Date crtTm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfId() {
        return infId;
    }

    public void setInfId(String infId) {
        this.infId = infId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfContent() {
        return infContent;
    }

    public void setInfContent(String infContent) {
        this.infContent = infContent;
    }

    public Date getCrtTm() {
        return crtTm;
    }

    public void setCrtTm(Date crtTm) {
        this.crtTm = crtTm;
    }

    @Override
    public String toString() {
        return "IntelligentInf{" +
                "id='" + id + '\'' +
                ", infId='" + infId + '\'' +
                ", title='" + title + '\'' +
                ", infContent='" + infContent + '\'' +
                ", crtTm=" + crtTm +
                '}';
    }
}
