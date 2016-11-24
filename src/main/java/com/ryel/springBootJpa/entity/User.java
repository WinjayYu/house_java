package com.ryel.springBootJpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryel.springBootJpa.annotations.Exclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * APP 注册用户
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //用户是否被用户关注
    @Transient
    private Boolean watchIs  = false;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Exclude
    private String password;

    private String description;

    // 英文名
    private String ename;

    // 国籍
    private String nationality;

    // 性别
    private String sex;

    // 手机
    private String tel;

    // 医院
    private String hospital;

    // 职务
    private String post;

    // 职称
    private String titles;

    // 科室
    private String department;

    @Column(name = "img_url")
    private String imgUrl;

    // e_mail
    private String email;

    // 备注
    private String remark;

    //用户类型 -1 主讲人 -2主席 1 普通注册用户
    private Integer type;

    @Transient
    private Boolean thisForum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 环信服务器用户名
     */
    @Column(name = "im_username")
    private String imUsername;

    /**
     * 环信服务器密码
     */
    @Column(name = "im_password")
    private String imPassword;


    public User(){
    }

    public User(Integer id){
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e_mail) {
        this.email = e_mail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getWatchIs() {
        return watchIs;
    }

    public void setWatchIs(Boolean watchIs) {
        this.watchIs = watchIs;
    }

    public Boolean getThisForum() {
        return thisForum;
    }

    public void setThisForum(Boolean thisForum) {
        this.thisForum = thisForum;
    }

    public String getImUsername() {
        return imUsername;
    }

    public void setImUsername(String imUsername) {
        this.imUsername = imUsername;
    }

    public String getImPassword() {
        return imPassword;
    }

    public void setImPassword(String imPassword) {
        this.imPassword = imPassword;
    }
}
