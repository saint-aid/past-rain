package com.june.rain.web;

import com.june.rain.service.PastRainService;
import com.june.rain.web.dto.PastRainResponseDto;
import com.june.rain.web.dto.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PastRainController {
   // private final PastRainService pastRainService;

    @Autowired
    PastRainService pastRainService;

    //**조회**//
    @GetMapping("/getRainAll")
    public ResponseEntity<ResponseMessage> getRainAll (
            //@PathVariable String searchDay
        ){
        ResponseMessage responseMessage = new ResponseMessage();
        ResponseEntity responseEntity = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
        try{
            List<PastRainResponseDto> result = pastRainService.getRainAll("202007222350");

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


}
