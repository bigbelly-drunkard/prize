package org.egg.model.DO;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {
    private Long id;

    private String customerNo;

    private BigDecimal score;

    private BigDecimal gold;

    private BigDecimal loadFactor;

    private String wxMiniOpenId;

    private String zfbOpenId;

    private String wxOpenId;

    private String nickName;

    private String headUrl;

    private String customerStatus;

    private String customerType;

    private Date memberExpire;

    private Date createdDate;

    private Date modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getGold() {
        return gold;
    }

    public void setGold(BigDecimal gold) {
        this.gold = gold;
    }

    public BigDecimal getLoadFactor() {
        return loadFactor;
    }

    public void setLoadFactor(BigDecimal loadFactor) {
        this.loadFactor = loadFactor;
    }

    public String getWxMiniOpenId() {
        return wxMiniOpenId;
    }

    public void setWxMiniOpenId(String wxMiniOpenId) {
        this.wxMiniOpenId = wxMiniOpenId == null ? null : wxMiniOpenId.trim();
    }

    public String getZfbOpenId() {
        return zfbOpenId;
    }

    public void setZfbOpenId(String zfbOpenId) {
        this.zfbOpenId = zfbOpenId == null ? null : zfbOpenId.trim();
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId == null ? null : wxOpenId.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl == null ? null : headUrl.trim();
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus == null ? null : customerStatus.trim();
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType == null ? null : customerType.trim();
    }

    public Date getMemberExpire() {
        return memberExpire;
    }

    public void setMemberExpire(Date memberExpire) {
        this.memberExpire = memberExpire;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}