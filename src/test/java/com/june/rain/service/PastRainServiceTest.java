package com.june.rain.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.june.rain.web.PastRainController;
import com.june.rain.web.dto.PastRainResponseDto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PastRainServiceTest {


    @Test
    public void HTML데이터를_가져온다() throws IOException, ParseException {
        String url = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";
        //1)날짜에 따라 하루 데이터 가져오기 html parsing
        //1-1) 10분 데이터 시 조회날짜 한페이지에 61개. 필요한 데이터 144. 하루는 3번(2.3번) 루프
        //1-2) 시간 10분 시 조회날짜 컨버팅 필요
        String nextMinute = "202007230000";
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar cal = Calendar.getInstance();

        try {

            Date date = fm.parse(nextMinute);
            System.out.println("date --- > " + date);
            cal.setTime(date);
            cal.add(Calendar.MINUTE, -610);

            cal.add(Calendar.DATE, -1);
            nextMinute = fm.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

       // Document doc = Jsoup.connect(url+"?202007232200&0&MINDB_10M&273&a&K").get();
        //2)데이터 가공

        //3)DTO담기

        System.out.println("시간 2page----> " + fm.format(cal.getTime()));

    }
}
