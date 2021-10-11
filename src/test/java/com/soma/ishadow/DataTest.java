package com.soma.ishadow;

import com.soma.ishadow.utils.PasswordEncoding;
import org.apache.tomcat.jni.Time;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DataTest {
    public static void main(String[] args) throws Exception {

        for(int i = 0; i <= 10; i++) {
            String password = "test10" + i + "!";
            String test1 = PasswordEncoding.passwordEncoding(password);
            System.out.println(test1);
        }

        Date today = new Date();
        //System.out.println(today);
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String result = df.format(today);

        Thread.sleep(5000);
        Date after = new Date();
        //String result1 = df.format(after);

        //System.out.println(today.getTime());
        System.out.println(after.getTime() / 1000 - today.getTime());

    }
}
