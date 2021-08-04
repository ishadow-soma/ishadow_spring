package com.soma.ishadow.domains.user;

import javax.persistence.*;
import java.sql.Timestamp;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "user_convertor")
public class UserConvertor {

    @Id
    private Long userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "convertorCount")
    private int convertorCount;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    public UserConvertor() {}

    public UserConvertor(Long userId, User user, int convertorCount) {
        this.userId = userId;
        this.user = user;
        this.convertorCount = convertorCount;
    }

    public void updateConversion(int count) {
        this.convertorCount = count;
        this.updateAt = Timestamp.valueOf(now());
    }

    public Long getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public int getConvertorCount() {
        return convertorCount;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

}
