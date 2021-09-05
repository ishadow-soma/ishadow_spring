package com.soma.ishadow.domains.category;

import com.soma.ishadow.domains.category_video.CategoryVideo;
import com.soma.ishadow.domains.enums.Status;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId", nullable = false, updatable = false)
    private Long categoryId;

    @Column(name = "categoryName", nullable = false, updatable = false)
    private String categoryName;

    @Column(name = "createAt")
    private Timestamp createdAt;

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Status getStatus() {
        return status;
    }

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CategoryVideo> categoryVideos = new HashSet<>();


    @Builder
    public Category(Long categoryId, String categoryName, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public Category() {

    }
}
