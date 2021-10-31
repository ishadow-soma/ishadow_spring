package com.soma.ishadow;

import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.services.VideoService;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class TextFileTest {
    public static void main(String[] args) throws Exception {

        String sentences = IOUtils.toString(Objects.requireNonNull(TextFileTest.class.getResourceAsStream("/TEST/test.txt")), "UTF-8");
        sentences = sentences.replaceAll(",",".").replaceAll(" --> ","-->");
        String[] tests = sentences.split("\r\n\r\n");


        for(int i = 0; i < tests.length; i++) {


            String[] test = tests[i].split("\n");
            String[] times = test[1].split("-->");
            String startTime = times[0];
            String endTime = times[1];
            StringBuilder content = new StringBuilder();
            for(int j = 2; j < test.length; j++) {
                content.append(test[j]);
            }

            SentenceEn sentenceEn = new SentenceEn.Builder()
                    .video(null)
                    .content(content.toString())
                    .startTime(startTime)
                    .endTime(endTime)
                    .speaker("NONE")
                    .confidence("NONE")
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .status(Status.YES)
                    .build();
            System.out.println(sentenceEn);
        }

    }
}
