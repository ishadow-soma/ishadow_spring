package com.soma.ishadow.domains.user;

import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user_audio.UserAudio;
import com.soma.ishadow.requests.PatchUserReq;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static com.soma.ishadow.utils.PasswordEncoding.passwordEncoding;
import static io.micrometer.core.instrument.util.StringUtils.isEmpty;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "myPoint")
    private Long myPoint;

    @Column(name = "sns")
    private String sns;

    @Column(name = "purposeOfUse")
    private String purposeOfUse;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "lastLoginAt")
    private Timestamp lastLoginAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "withdrawalContent")
    private String withdrawalContent;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserConvertor userConvertor;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserAudio> userAudios = new HashSet<>();

    public void userAudeoAdd(UserAudio userAudio) {
        userAudios.add(userAudio);
    }

    public User(Long userId, String name, String email, String password, int age, String gender, Long myPoint, String sns, String purposeOfUse, Timestamp createdAt, Timestamp lastLoginAt, Timestamp updateAt, Status status, String withdrawalContent) {

        //checkArgument(userId < 0,"invalid userId");
        //checkNotNull(name,"name must be provided");
        //checkNotNull(email,"email_address must be provided");
        //checkNotNull(password,"password must be provided");
        //checkNotNull(birthday,"birthday must be provided");
        //checkNotNull(phoneNumber,"phone_number must be provided");
        //checkNotNull(gender,"gender must be provided");
        //checkNotNull(sns,"sns must be provided");

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.myPoint = myPoint;
        this.sns = sns;
        this.purposeOfUse = defaultIfNull(purposeOfUse,"NONE");
        this.createdAt = (Timestamp) defaultIfNull(createdAt,now());
        this.lastLoginAt = lastLoginAt;
        this.updateAt = updateAt;
        this.status = defaultIfNull(status,Status.YES);
        this.withdrawalContent = defaultIfNull(withdrawalContent,"NONE");
    }

    public User() {

    }

    /**
     * sha512는 org.apache.commons.codec.digest 사용
     * @param credentials
     * @return
     */

    public void login(String credentials) {
        if (isEmpty(credentials) || !passwordEncoding(credentials).equals(password)) {
            throw new IllegalStateException("Could not login to " + email);
        }

    }



    public User deleteUserConvertor(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
        this.status = Status.NO;
        this.updateAt = Timestamp.valueOf(now());
        return this;
    }


    public User updatePasswordConvertor(String password) {
        this.password = passwordEncoding(password);
        return this;
    }

    public User updateUserConvertor(PatchUserReq patchUserReq) {

        if( patchUserReq.getName() != null) {
            this.name = patchUserReq.getName();
        }

        if( patchUserReq.getAge() != 0) {
            this.age = patchUserReq.getAge();
        }

        if( patchUserReq.getGender() != null) {
            this.gender = patchUserReq.getGender();
        }

        if( patchUserReq.getPurposeOfUse() != null) {
            this.purposeOfUse = patchUserReq.getPurposeOfUse();
        }

        this.updateAt = Timestamp.valueOf(now());
        return this;
    }

    public void afterLoginSuccess() {
        lastLoginAt = Timestamp.valueOf(now());
    }

    public Long getUserId() {
        return userId;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public Long getMyPoint() {
        return myPoint;
    }

    public String getSns() {
        return sns;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getLastLoginAt() {
        return lastLoginAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Status getStatus() {
        return status;
    }

    public String getWithdrawalContent() {
        return withdrawalContent;
    }

    public int getAge() {
        return age;
    }

    static public class Builder {

        private Long userId;
        private String name;
        private String email;
        private String password;
        private int age;
        private String gender;
        private Long myPoint;
        private String sns;
        private String purposeOfUse;
        private Timestamp createdAt;
        private Timestamp lastLoginAt;
        private Timestamp updateAt;
        private Status status;
        private String withdrawalContent;

        public Builder() {};

        public Builder(User user) {
            this.userId = user.userId;
            this.name = user.name;
            this.email = user.email;
            this.password = user.password;
            this.gender = user.gender;
            this.age = user.age;
            this.myPoint = user.myPoint;
            this.sns = user.sns;
            this.purposeOfUse = user.purposeOfUse;
            this.createdAt = user.createdAt;
            this.lastLoginAt = user.lastLoginAt;
            this.updateAt = user.updateAt;
            this.status = user.status;
            this.withdrawalContent = user.withdrawalContent;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }


        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder myPoint(Long myPoint) {
            this.myPoint = myPoint;
            return this;
        }

        public Builder sns(String sns) {
            this.sns = sns;
            return this;
        }

        public Builder purposeOfUse(String purposeOfUse) {
            this.purposeOfUse = purposeOfUse;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLoginAt(Timestamp lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder updateAt(Timestamp updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder withdrawalContent(String withdrawalContent) {
            this.withdrawalContent = withdrawalContent;
            return this;
        }


        public User build() {
            return new User(userId, name, email, password, age ,gender, myPoint, sns, purposeOfUse, createdAt, lastLoginAt, updateAt, status, withdrawalContent);
        }
    }
}
