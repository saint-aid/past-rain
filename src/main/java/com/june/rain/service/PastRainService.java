package com.june.rain.service;

import com.june.rain.web.dto.PastRainResponseDto;
import com.june.rain.web.dto.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.List;

@NoArgsConstructor
@Service
public class PastRainService {

    public List<PastRainResponseDto> getRainAll(String searchDay) {
        PastRainResponseDto responseDto = new PastRainResponseDto();
        String url = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";
        try {
            //1)html parsing
            Document doc = Jsoup.connect(url+"?202007232200&0&MINDB_10M&273&a&K").get();
            //2)데이터 가공

            //3)DTO담기

            System.out.println("DOC ===> " + doc);
        }catch (Exception e){
            e.printStackTrace();

        }

        return null;
    }
}
