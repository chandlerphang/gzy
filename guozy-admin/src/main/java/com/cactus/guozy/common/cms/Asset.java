package com.cactus.guozy.common.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.cactus.guozy.common.BaseDomain;

@Table(name="asset")
public class Asset extends BaseDomain {

	private static final long serialVersionUID = 8198740205776187192L;

	@Column(name="name")
	private String name;

	@Column(name="url")
    private String url;

	@Column(name="title")
    private String title;

	@Column(name="alt_text")
    private String altText;

	@Column(name="mime")
    private String mimeType;

	@Column(name="size")
    private Long fileSize;

	@Column(name="ext")
    private String fileExtension;
    
	@Column(name="date_created")
    private Date dateCreated;
    
	@Column(name="dateUpdated")
    private Date dateUpdated;

    public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String fullUrl) {
        this.url = fullUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
