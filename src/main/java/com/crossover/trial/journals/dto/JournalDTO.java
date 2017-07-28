package com.crossover.trial.journals.dto;

import java.io.Serializable;
import java.util.Date;

public class JournalDTO implements Serializable{

    private static final long serialVersionUID = -8385803222254643478L;

    private String name;

    private Date publishDate;

    private String publisherName;

    private String categoryName;

    public JournalDTO(String name, Date publishDate, String publisherName, String categoryName) {
        this.name = name;
        this.publishDate = publishDate;
        this.publisherName = publisherName;
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JournalDTO that = (JournalDTO) o;

        if (!name.equals(that.name)) return false;
        if (!publishDate.equals(that.publishDate)) return false;
        if (!publisherName.equals(that.publisherName)) return false;
        return categoryName.equals(that.categoryName);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + publishDate.hashCode();
        result = 31 * result + publisherName.hashCode();
        result = 31 * result + categoryName.hashCode();
        return result;
    }
}
