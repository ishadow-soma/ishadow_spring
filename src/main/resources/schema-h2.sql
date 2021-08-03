-- 테이블 순서는 관계를 고려하여 한 번에 실행해도 에러가 발생하지 않게 정렬되었습니다.

-- AUDIO Table Create SQL
CREATE TABLE AUDIO
(
    audioId            BIGINT          NOT NULL    AUTO_INCREMENT ,
    audioName          VARCHAR(200)    NULL        ,
    audioLength        BIGINT          NULL        ,
    audioType          VARCHAR(45)     NULL        ,
    audioURL           TEXT            NULL        ,
    audioChannel       INT             NULL        ,
    audioSampling      INT             NULL        ,
    audioCapacity      BIGINT          NULL        ,
    audioSpeakerCount  INT             NULL        ,
    createdAt          TIMESTAMP       NOT NULL    ,
    updateAt           TIMESTAMP       NULL        ,
    status             VARCHAR(10)     NOT NULL    ,
    CONSTRAINT PK_AUDIO PRIMARY KEY (audioId)
);


-- POINT Table Create SQL
CREATE TABLE POINT
(
    pointId      BIGINT         NOT NULL    AUTO_INCREMENT ,
    pointAmount  BIGINT         NOT NULL    ,
    pointPrice   BIGINT         NOT NULL    ,
    pointImageURL TEXT          NOT NULL    ,
    createdAt    TIMESTAMP      NOT NULL    ,
    updateAt     TIMESTAMP      NULL        ,
    status       VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_POINT PRIMARY KEY (pointId)
);


-- USER Table Create SQL
CREATE TABLE USER
(
    userId             BIGINT          NOT NULL    AUTO_INCREMENT ,
    loginId            VARCHAR(30)     NOT NULL    ,
    name               VARCHAR(30)     NOT NULL    ,
    email              VARCHAR(50)     NOT NULL    ,
    password           VARCHAR(30)     NOT NULL    ,
    birthday           VARCHAR(10)     NOT NULL    ,
    phoneNumber        VARCHAR(13)     NOT NULL    ,
    gender             VARCHAR(10)     NOT NULL    ,
    myPoint            BIGINT          NOT NULL    DEFAULT 0 ,
    sns                VARCHAR(10)     NOT NULL    ,
    purposeOfUse       VARCHAR(100)    NOT NULL    DEFAULT 'NONE' ,
    createdAt          TIMESTAMP       NOT NULL    ,
    lastLoginAt        TIMESTAMP       NULL    ,
    updateAt           TIMESTAMP       NULL        ,
    status             VARCHAR(10)     NOT NULL    DEFAULT 'Y' ,
    withdrawalContent  VARCHAR(100)    NOT NULL    DEFAULT 'NONE' ,
    CONSTRAINT PK_USER PRIMARY KEY (userId)
);


-- ORDER Table Create SQL
CREATE TABLE ORDERS
(
    orderId    BIGINT         NOT NULL    AUTO_INCREMENT ,
    pointId    BIGINT         NOT NULL    ,
    userId     BIGINT         NOT NULL    ,
    createdAt  TIMESTAMP      NOT NULL    ,
    updateAt   TIMESTAMP      NULL        ,
    status     VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_ORDER PRIMARY KEY (orderId)
);

ALTER TABLE ORDERS
    ADD CONSTRAINT FK_ORDER_pointId_POINT_pointId FOREIGN KEY (pointId)
        REFERENCES POINT (pointId) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE ORDERS
    ADD CONSTRAINT FK_ORDER_userId_USER_userId FOREIGN KEY (userId)
        REFERENCES USER (userId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- SENTENCE_EN Table Create SQL
CREATE TABLE SENTENCE_EN
(
    sentenseId          BIGINT         NOT NULL    AUTO_INCREMENT ,
    audioId             BIGINT         NOT NULL    ,
    sentenseContent     TEXT           NOT NULL    DEFAULT 'NONE',
    sentenseStartTime   FLOAT          NOT NULL    ,
    sentenseEndTime     FLOAT          NOT NULL    ,
    sentenseSpeaker     VARCHAR(10)    NOT NULL    ,
    sentenseConfidence  DOUBLE         NOT NULL    ,
    createdAt           TIMESTAMP      NOT NULL    ,
    updateAt            TIMESTAMP      NULL        ,
    status              VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_SCRIPT_EN PRIMARY KEY (sentenseId)
);

ALTER TABLE SENTENCE_EN
    ADD CONSTRAINT FK_SENTENCE_EN_audioId_AUDIO_audioId FOREIGN KEY (audioId)
        REFERENCES AUDIO (audioId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- CATEGORY Table Create SQL
CREATE TABLE CATEGORY
(
    categoryId    BIGINT         NOT NULL    AUTO_INCREMENT ,
    categoryName  VARCHAR(50)    NOT NULL    ,
    createdAt     TIMESTAMP      NOT NULL    ,
    updateAt      TIMESTAMP      NULL        ,
    status        VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (categoryId)
);


-- AUDIO_USER Table Create SQL
CREATE TABLE AUDIO_USER
(
    userId     BIGINT         NOT NULL    ,
    audioId    BIGINT         NOT NULL    ,
    createdAt  TIMESTAMP      NOT NULL    ,
    updateAt   TIMESTAMP      NULL        ,
    status     VARCHAR(10)    NOT NULL
);

ALTER TABLE AUDIO_USER
    ADD CONSTRAINT FK_AUDIO_USER_userId_USER_userId FOREIGN KEY (userId)
        REFERENCES USER (userId) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE AUDIO_USER
    ADD CONSTRAINT FK_AUDIO_USER_audioId_AUDIO_audioId FOREIGN KEY (audioId)
        REFERENCES AUDIO (audioId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- REPEAT_SENTENCE Table Create SQL
CREATE TABLE REPEAT_SENTENCE
(
    repeatSentenseId  BIGINT         NOT NULL    AUTO_INCREMENT ,
    audioId           BIGINT         NOT NULL    ,
    sentenseId        BIGINT         NOT NULL    ,
    userId            BIGINT         NOT NULL    ,
    sentenseSaveType  VARCHAR(10)    NOT NULL    ,
    createdAt         TIMESTAMP      NOT NULL    ,
    updateAt          TIMESTAMP      NULL        ,
    status            VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_REPEAT_SECTION PRIMARY KEY (repeatSentenseId)
);


ALTER TABLE REPEAT_SENTENCE
    ADD CONSTRAINT FK_REPEAT_SENTENCE_audioId_AUDIO_audioId FOREIGN KEY (audioId)
        REFERENCES AUDIO (audioId) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPEAT_SENTENCE
    ADD CONSTRAINT FK_REPEAT_SENTENCE_sentenseId_SENTENCE_EN_sentenseId FOREIGN KEY (sentenseId)
        REFERENCES SENTENCE_EN (sentenseId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- AUDIO_CATEGORY Table Create SQL
CREATE TABLE AUDIO_CATEGORY
(
    categoryId  BIGINT         NOT NULL    ,
    audioId     BIGINT         NOT NULL    ,
    createdAt   TIMESTAMP      NOT NULL    ,
    updateAt    TIMESTAMP      NULL        ,
    status      VARCHAR(10)    NOT NULL
);


ALTER TABLE AUDIO_CATEGORY
    ADD CONSTRAINT FK_AUDIO_CATEGORY_categoryId_CATEGORY_categoryId FOREIGN KEY (categoryId)
        REFERENCES CATEGORY (categoryId) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE AUDIO_CATEGORY
    ADD CONSTRAINT FK_AUDIO_CATEGORY_audioId_AUDIO_audioId FOREIGN KEY (audioId)
        REFERENCES AUDIO (audioId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- PAYMENT Table Create SQL
CREATE TABLE PAYMENT
(
    paymentId     BIGINT         NOT NULL    AUTO_INCREMENT ,
    orderId       BIGINT         NOT NULL    ,
    paymentPG     VARCHAR(45)    NOT NULL    ,
    paymentType   VARCHAR(45)    NOT NULL    ,
    paymentData   TEXT           NOT NULL    ,
    paymentPrice  BIGINT         NOT NULL    ,
    paymentIp     VARCHAR(45)    NOT NULL    ,
    createdAt     TIMESTAMP      NOT NULL    ,
    updateAt      TIMESTAMP      NULL        ,
    status        VARCHAR(10)    NOT NULL    ,
    CONSTRAINT PK_PAYMENT PRIMARY KEY (paymentId)
);


ALTER TABLE PAYMENT
    ADD CONSTRAINT FK_PAYMENT_orderId_ORDER_orderId FOREIGN KEY (orderId)
        REFERENCES ORDERS (orderId) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- SENTENCE_KR Table Create SQL
CREATE TABLE SENTENCE_KR
(
    sentenseId       BIGINT         NOT NULL    ,
    sentenseContent  VARCHAR(45)    NOT NULL    DEFAULT 'NONE' ,
    createdAt        TIMESTAMP      NOT NULL    ,
    updateAt         TIMESTAMP      NULL        ,
    status           VARCHAR(10)    NOT NULL
);

ALTER TABLE SENTENCE_KR
    ADD CONSTRAINT FK_SENTENCE_KR_sentenseId_SENTENCE_EN_sentenseId FOREIGN KEY (sentenseId)
        REFERENCES SENTENCE_EN (sentenseId) ON DELETE RESTRICT ON UPDATE RESTRICT;


