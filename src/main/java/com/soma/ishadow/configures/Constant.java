package com.soma.ishadow.configures;

public class Constant {

    public static final Long ACCESS_TOKEN_VALID_TIME = 14 * 24 * 3600 * 1000L;
    public static final String sender = "hr.seongeun@gmail.com";
    public static final String subject = "<IShadow 회원가입 이메일 인증>";
    public static final String baseUrl = "https://ishadow.kr";
    public static Long bookmarkCount = 0L;
    public static final Long DEFAULT_SIZE = 10L;
    public static final String startFilePath = "scp -i /home/ubuntu/key_fair/ishadow_key.pem -r ";
    public static final String endFilePath = " ubuntu@ec2-3-34-122-103.ap-northeast-2.compute.amazonaws.com:/home/ubuntu/";
    public static final String mkdirPath = "ssh -i \"/home/ubuntu/key_fair/ishadow_key.pem\" ubuntu@ec2-3-34-122-103.ap-northeast-2.compute.amazonaws.com";
    public static final String videoBasePath = "http://ec2-3-34-122-103.ap-northeast-2.compute.amazonaws.com/video/";
    public static final String imageBasePath = "http://ec2-3-34-122-103.ap-northeast-2.compute.amazonaws.com/image/";
    public static final String IMAGE_PNG_FORMAT = "png";


}
