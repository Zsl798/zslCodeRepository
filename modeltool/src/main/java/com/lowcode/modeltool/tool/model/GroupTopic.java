package com.lowcode.modeltool.tool.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2023/02/24
 * @desc : 话题分组
 */
@JsonPropertyOrder({
        "id",
        "defKey",
        "defName",
})
public class GroupTopic implements Serializable,Cloneable{
    private String id;
    private String defKey;
    private String defName;
    private List<String> refEntities = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefKey() {
        return defKey;
    }

    public void setDefKey(String defKey) {
        this.defKey = defKey;
    }

    public String getDefName() {
        return defName;
    }

    public void setDefName(String defName) {
        this.defName = defName;
    }

    public List<String> getRefEntities() {
        return refEntities;
    }

    public void setRefEntities(List<String> refEntities) {
        this.refEntities = refEntities;
    }
}
