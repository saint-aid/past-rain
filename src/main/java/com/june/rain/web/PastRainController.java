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
    private final PastRainService pastRainService;

    //**조회**//
    @PostMapping("/getRainAll")
    public ResponseEntity<ResponseMessage> getRainAll (
            //@RequestParam("searchDay") String searchDay,
            //@RequestParam("city") String city
            @RequestBody Map paraMap
        ){
        ResponseMessage responseMessage = new ResponseMessage();
        ResponseEntity responseEntity = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
        try{
            String searchStDay = (String) paraMap.get("searchStDay");
            String searchEdDay = (String) paraMap.get("searchEdDay");
            String city = (String) paraMap.get("city");

//            String searchStDay = "202007250000";
//            String searchEdDay = "202007222330";
//            String city = "273";

            List<PastRainResponseDto> result = pastRainService.getRainAll(searchStDay,searchEdDay,city );

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
    @PostMapping("/excelDown")
    public void excelDown(
            HttpServletResponse response,
//            @PathVariable String cntIdn,
//            @PathVariable String cntIdn
            @RequestBody Map paraMap
        ) throws Exception{
            String searchStDay = (String) paraMap.get("searchStDay");
            String searchEdDay = (String) paraMap.get("searchEdDay");
            String city = (String) paraMap.get("city");
         try{
            pastRainService.excelDown(response, searchStDay,searchEdDay, city);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

}
