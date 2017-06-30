package com.korat.search.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item  {
    @Field("id")
    private Long id;
    @Field("title")
    private String title;
    @Field("sellPoint")
    private String sellPoint;
    @Field("price")
    private Long price;
    @Field("image")
    private String image;
    @Field("cid")
    private Long cid;
    @Field("status")
    private Byte status;
    @Field("updated")
    private String updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint == null ? null : sellPoint.trim();
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sellPoint='" + sellPoint + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", cid=" + cid +
                ", status=" + status +
                ", updated=" + updated +
                '}';
    }
}