/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lowcode.modeltool.tool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lowcode.modeltool.tool.fisok.raw.kit.StringKit;

import java.io.Serializable;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2021/6/12
 * @desc : 列字段信息
 * 来源于siner结构json文件:entities.fields
 */
@JsonPropertyOrder({
        "rowNo",
        "defKey",
        "defName",
        "comment",
        "domain",
        "type",
        "len",
        "scale",
        "primaryKey",
        "notNull",
        "autoIncrement",
        "defaultValue",
        "hideInGraph",
})
public class ColumnField implements Serializable,Cloneable {
    @JsonIgnore
    private TableEntity tableEntity;
    private int rowNo;              //行号，从1开始
    private String defKey;          //字段代码
    private String defName;         //字段名称
    private String comment = "";    //字段注释说明
    private String domain = "";                     //数据域（暂时留空，前端自行匹配）
    private String type = "";       //字段数据类型，如varchar
    private Integer len = null;     //字段长度，如32
    private Integer scale = null;   //字段小数位数据
    private String typeFullName = "";//类型+长度+小数位数
    private Boolean primaryKey = Boolean.FALSE;     //是否主键
    private String primaryKeyName = "";
    private Boolean notNull = Boolean.FALSE;        //是否允许为空
    private String notNullName = "";
    private Boolean autoIncrement = Boolean.FALSE;  //是否自增
    private String autoIncrementName = "";
    private String refDict = "";               //引用数据字典
    private String defaultValue = "";               //默认值
    private Boolean hideInGraph = Boolean.FALSE;    //关系图是否隐藏（第15个之前，默认为true)
    private String attr1 = "";               //扩展属性1
    private String attr2 = "";               //扩展属性2
    private String attr3 = "";               //扩展属性3
    private String attr4 = "";               //扩展属性4
    private String attr5 = "";               //扩展属性5
    private String attr6 = "";               //扩展属性6
    private String attr7 = "";               //扩展属性7
    private String attr8 = "";               //扩展属性8
    private String attr9 = "";               //扩展属性9

    public TableEntity getTableEntity() {
        return tableEntity;
    }

    public void setTableEntity(TableEntity tableEntity) {
        this.tableEntity = tableEntity;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    /**
     * 字段类型+长度+小数位数
     * @return
     */
    public String getTypeFullName() {
        return typeFullName;
    }

    public void setTypeFullName(String typeFullName) {
        this.typeFullName = typeFullName;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getRefDict() {
        return refDict;
    }

    public void setRefDict(String refDict) {
        this.refDict = refDict;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getHideInGraph() {
        return hideInGraph;
    }

    public void setHideInGraph(Boolean hideInGraph) {
        this.hideInGraph = hideInGraph;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public String getNotNullName() {
        return notNullName;
    }

    public void setNotNullName(String notNullName) {
        this.notNullName = notNullName;
    }

    public String getAutoIncrementName() {
        return autoIncrementName;
    }

    public void setAutoIncrementName(String autoIncrementName) {
        this.autoIncrementName = autoIncrementName;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public String getAttr6() {
        return attr6;
    }

    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    public String getAttr7() {
        return attr7;
    }

    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    public String getAttr8() {
        return attr8;
    }

    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    public String getAttr9() {
        return attr9;
    }

    public void setAttr9(String attr9) {
        this.attr9 = attr9;
    }

    public void fillConvertNames(){
        //处理类型名
        StringBuffer buffer = new StringBuffer(type);
        if(len != null && len > 0){
            buffer.append("(").append(len);
            if(scale != null && scale > 0){
                buffer.append(",").append(scale);
            }
            buffer.append(")");
        }
        typeFullName = buffer.toString();

        if(primaryKey != null && Boolean.TRUE.equals(primaryKey)){
            primaryKeyName = "√";
        }
        if(notNull != null && Boolean.TRUE.equals(notNull)){
            notNullName = "√";
        }
        if(autoIncrement != null && Boolean.TRUE.equals(autoIncrement)){
            autoIncrementName = "√";
        }
        if(StringKit.isNotBlank(refDict)){
            Dict dict = lookupDict(tableEntity.getDicts(),refDict);
            String dictText = "";
            if(dict != null){
                dictText = "<字典:"+dict.getDefKey()+">";
            }
            if(StringKit.isNotBlank(comment)
                    && comment.indexOf(dictText) < 0
                    && StringKit.isNotBlank(dictText)){
                comment += (";"+dictText);
            }else{
                comment = dictText;
            }
        }
    }

    public Dict lookupDict(List<Dict> dictList,String dictId){
        for(Dict dict : dictList){
            if(dictId.equals(dict.getId())){
                return dict;
            }
        }
        return null;
    }
}
