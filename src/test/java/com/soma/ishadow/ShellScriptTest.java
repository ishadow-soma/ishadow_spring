package com.soma.ishadow;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellScriptTest {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        String s;
        Process p;
        try {
            //이 변수에 명령어를 넣어주면 된다.
            String[] cmd = {"/bin/sh","-c","ps -ef | grep tomcat"};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
    }
}
