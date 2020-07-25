package com.june.rain.service;

import com.june.rain.domain.Rain;
import com.june.rain.web.dto.PastRainResponseDto;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

@NoArgsConstructor
@Service
public class PastRainService {
    private static String BaseUrl = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";

    //**조회데이터를 구해온다**/
    public List<PastRainResponseDto> getRainAll(String searchDay) {
        String city = "273";
        List<PastRainResponseDto> rainList = new ArrayList<>(); // 초기화
        try {
            //1.==가공된 시간을 구해온다==//
            String[] days =  getSearchTime(searchDay);
            System.out.println("days :: " + Arrays.toString(days));
            int idx = 1; //총 루프수(하루)

            for(String min : days){
                //System.out.println("min ------- > " + min);
                //2.==url을 구하고 HTML 정보를 가져온다==/
                //System.out.println("getUrls(min,city) ---> " +getUrls(min,city));
                Document doc = Jsoup.connect(getUrls(min,city)).get();

                //3)개체 정보 가져오기(tr 객체를 가져온다 61개)
                Elements els = doc.select(".text");

                for (Element el: els) {

                    if(idx > 144) break;
                    PastRainResponseDto dto = new PastRainResponseDto(
                            Rain.builder()
                                    .searchDay(min.substring(0,8))
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
                    rainList.add(dto); //list에 넣기

                    idx++;//총 루프수(하루)
                }
            }

            System.out.println("======rainList======= : " + rainList.toString());
            System.out.println("======size:rainList:size======= : " + rainList.size());

        }catch (Exception e){
            e.printStackTrace();
        }
        return rainList;
    }

    //==시간을 구한다==//
    private String[] getSearchTime(String nextMinute) {
        //1)날짜에 따라 하루 데이터 가져오기 html parsing
            //1-1) 10분 데이터 시 조회 날짜 한페이지에 61개. 필요한 데이터 144. 하루는 3번(2.3번) 루프
            //1-2) 시간 10분 시 조회 날짜 컨버팅
        //nextMinute = "202007232350";
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
           // System.out.println(Arrays.toString(minuteList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minuteList;
    }
    
    //==URL을 구해온다==//
    private String getUrls(String days, String city) {
        //String date = "&"+days; //서칭 날짜
        String standMin = "&0"; //서칭된 값의 기준
        String rangeMin = "&MINDB_10M"; //10분단위
        String cities = "&"+city; //도시(문경 273)
        String realTime = "&m&K"; //실시간(a)

        return BaseUrl + days + standMin + rangeMin + cities + realTime;
    }
}
