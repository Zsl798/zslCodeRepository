package com.lowcode.modeltool.apimodel.projectInfo.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 建模信息表
 * @author zhangsonglin
 * @since 1.0
 * @version 2023年4月24日
 * @version 2023年4月24日
 */
@Table(name = "BDAP_MODEL_PROJECT_INFO")
public class ProjectInfoVO {

    /**
     * 构造方法
     */
    public ProjectInfoVO() {

    }

    /** 序列化ID */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "PROJECT_ID", length = 32, precision = 0)
    private String projectId;

    /**
     * 项目名称
     */
    @Column(name = "PROJECT_NAME", length = 50, precision = 0)
    private String projectName;

    /**
     * 项目描述
     */
    @Column(name = "REMARK", length = 200, precision = 0)
    private String remark;

    /**
     * 项目定义内容
     */
    @Column(name = "CONTENT", length = 65535, precision = 0)
    private String content;

    /**
     * 创建人
     */
    @Column(name = "CREATE_ID", length = 32, precision = 0)
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", precision = 0)
    private Timestamp createTime;

    /**
     * 更新人
     */
    @Column(name = "UPDATE_ID", length = 32, precision = 0)
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME", precision = 0)
    private Timestamp updateTime;

    /**
     * @return 获取 主键 属性值
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId 设置 主键 属性值为参数值 projectId
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return 获取 项目名称 属性值
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName 设置 项目名称 属性值为参数值 projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return 获取 项目描述 属性值
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark 设置 项目描述 属性值为参数值 remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 获取 项目定义内容 属性值
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 设置 项目定义内容 属性值为参数值 content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return 获取 创建人 属性值
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * @param createId 设置 创建人 属性值为参数值 createId
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * @return 获取 创建时间 属性值
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime 设置 创建时间 属性值为参数值 createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 获取 更新人 属性值
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * @param updateId 设置 更新人 属性值为参数值 updateId
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * @return 获取 更新时间 属性值
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime 设置 更新时间 属性值为参数值 updateTime
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 建模项目信息表VO的toString方法
     *
     * @return 建模项目信息表VO的toString值
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
