package com.june.rain.service;

import com.june.rain.domain.Rain;
import com.june.rain.web.dto.PastRainResponseDto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PastRainServiceTest {


    @Test
    public void 시간을_가공한다() {
        //1)날짜에 따라 하루 데이터 가져오기 html parsing
        //1-1) 10분 데이터 시 조회날짜 한페이지에 61개. 필요한 데이터 144. 하루는 3번(2.3번) 루프
        //1-2) 시간 10분 시 조회날짜 컨버팅 필요
        String nextMinute = "202007232350";
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar cal = Calendar.getInstance();
        String[] minuteList = new String[3];
        try {
            for(int i=0; i<minuteList.length; i++){
                Date date = fm.parse(nextMinute);
                cal.setTime(date);
                cal.add(Calendar.MINUTE, -(i*610));
                minuteList[i] = fm.format(cal.getTime());
                cal.clear();
            }
            System.out.println(Arrays.toString(minuteList));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void HTML데이터를_가져온다() throws IOException, ParseException {
        String url = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";
        String searchDay = "20200723000000";
        String city = "273";
        Document doc = Jsoup.connect(url+searchDay+"&0&MINDB_10M&"+city+"&m&K").get();
        //2)개체 정보 가져오기(tr 객체를 가져온다 61개)
        Elements els = doc.select(".text");
        List<PastRainResponseDto> rainList = new ArrayList<>();
        for (Element el: els) {
            PastRainResponseDto dto = new PastRainResponseDto(
                    Rain.builder()
                            .searchDay("20200723000000")
                            .rainHm(el.children().get(0).text())
                            .rainYn(el.children().get(1).text())
                            .rain15m(el.children().get(2).text())
                            .rain60m(el.children().get(3).text())
                            .rain3h(el.children().get(4).text())
                            .rain6h(el.children().get(5).text())
                            .rain12h(el.children().get(6).text())
                            .rain24h(el.children().get(7).text())
                            .temperature(el.children().get(8).text())
                            .build()
            );
            //list에 넣기
            rainList.add(dto);
            System.out.println("==============================================================\n\n");
        }
        System.out.println("======rainList======= : " + rainList.toString());

    }

//    @Test
//    public void 가져온_HTML데이터를_가공한다(){
//
//    }
}
