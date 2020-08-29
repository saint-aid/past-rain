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
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PastRainServiceTest {


    @Test
    public void 시간을_가공한다() throws ParseException {
        //1)날짜에 따라 하루 데이터 가져오기 html parsing
        //1-1) 10분 데이터 시 조회 날짜 한페이지에 61개. 필요한 데이터 144. 하루는 3번(2.3번) 루프
        //1-2) 시간 10분 시 조회 날짜 컨버팅
        String searchStDay = "202007250000";
        String searchEdDay = "202007220130";
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmm");
        Date d1 = fm.parse(searchStDay);
        Date d2 = fm.parse(searchEdDay);
        long diffDay = (d1.getTime() - d2.getTime())/(24*60*60*1000);

        System.out.println("타임 0-1 --> " + d1.getTime() /(24*60*60*1000) );
        System.out.println("타임 0-2 --> " + d2.getTime() /(24*60*60*1000) );
        System.out.println("타임 1--> " + (d1.getTime() - d2.getTime())/(24*60*60*1000) );
        System.out.println("타임 2--> " + (d1.getTime() - d2.getTime())/(60*60*1000) );
        System.out.println("타임 3--> " + (d1.getTime() - d2.getTime())/(60*1000) );
        int loopDt = Math.round((diffDay*144)/61) + 1; //루프수 구하기

        Calendar cal = Calendar.getInstance();
        String minuteList[] = new String[loopDt];
        try {
            for(int i=0; i<minuteList.length; i++){
                Date date = fm.parse(searchStDay);
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
        System.out.println("==========doc======= " + doc);
        //2)개체 정보 가져오기(tr 객체를 가져온다 61개)
        Elements els = doc.select(".text");
        //System.out.println("==========els======= " + els);
        List<PastRainResponseDto> rainList = new ArrayList<>();
        for (Element el: els) {
            String dt = el.child(0).child(0).attr("href");// 날짜 변수값 찾기
            int indexDt = dt.indexOf(","); //반환 인덱스
            String searchDays = dt.substring(indexDt +1,indexDt+9); // 날짜값 세팅
            System.out.println("==== "+dt + " ====");
            System.out.println("1==== "+dt.substring(indexDt +1,indexDt+9) + " ====1");
            PastRainResponseDto dto = new PastRainResponseDto(
                    Rain.builder()
                            .searchDay(searchDays)
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
            //System.out.println("==============================================================\n\n");
        }
        System.out.println("======rainList======= : " + rainList.toString());

    }

    @Test
    public void HTML데이터를_인코딩후_가져온다() throws IOException {
        String baseUrl = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";
        String searchDay = "20200723000000";
        String city = "273";
        String urls = baseUrl+ searchDay + "&0&MINDB_10M&" + city + "&m&K";
        Document doc =  Jsoup.parse(new URL(urls).openStream(), "euc-kr", urls);

        System.out.println("============================dom==============================");
        System.out.println(doc.toString());

    }

    @Test
    public void HTML데이터를_인코딩후_셀렉터값을_가져온다() throws IOException {
        String baseUrl = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";
        String searchDay = "20200723000000";
        String city = "273";
        String urls = baseUrl+ searchDay + "&0&MINDB_10M&" + city + "&m&K";
        Document doc =  Jsoup.parse(new URL(urls).openStream(), "euc-kr", urls);
        Elements els = doc.select(".text");

        System.out.println("==========================================================");
        System.out.println(els);
        System.out.println("==========================================================");

    }
}
