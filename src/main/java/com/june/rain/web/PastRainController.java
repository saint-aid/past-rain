package com.june.rain.web;

import com.june.rain.domain.Rain;
import com.june.rain.service.PastRainService;
import com.june.rain.web.dto.PastRainResponseDto;
import com.june.rain.web.dto.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PastRainController {
   // private final PastRainService pastRainService;
    //@Autowired
    private final PastRainService pastRainService;

    //**조회**//
    @PostMapping("/getRainAll")
    public ResponseEntity<ResponseMessage> getRainAll (
//            @RequestParam(name = "searchDay", required = true, defaultValue = "") String searchDay,
//            @RequestParam(name = "city", required = true, defaultValue = "") String city
            @RequestBody Map paraMap
        ){
        ResponseMessage responseMessage = new ResponseMessage();
        ResponseEntity responseEntity = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
        try{
            String searchDay = (String) paraMap.get("searchDay");
            String city = (String) paraMap.get("city");

            System.out.println("con searchDay---> " + searchDay);
            System.out.println("con city---> " + city);
            List<PastRainResponseDto> result = pastRainService.getRainAll(searchDay,city );

            if(result.isEmpty())
                responseMessage.setResult(false);

            responseMessage.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            responseMessage.setCode(500);
            responseMessage.setResult(false);
        }

        return responseEntity;
    }

    //**엑셀다운로드**//
    @GetMapping("/excelDown")
    public void excelDown(
            HttpServletResponse response,
            @RequestParam(name = "searchDay", required = true, defaultValue = "") String searchDay,
            @RequestParam(name = "city", required = true, defaultValue = "") String city
        ) throws Exception{
        pastRainService.excelDown(response, searchDay, city);
    }

}
