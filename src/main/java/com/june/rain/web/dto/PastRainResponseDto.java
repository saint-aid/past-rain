package com.june.rain.web.dto;

import com.june.rain.domain.Rain;
import lombok.*;

@NoArgsConstructor
@Getter
public class PastRainResponseDto {
    //==조회날짜==//
    private String searchDay;
    //==시 분==//
    private String rainHm;
    //==강수 유무==//
    private String rainYn;
    //==측정 15분==//
    private String rain15m;
    //==측정 60분==//
    private String rain60m;
    //==측정 3시간==//
    private String rain3h;
    //==측정 6시간==//
    private String rain6h;
    //==측정 12시간==//
    private String rain12h;
    //==강수일==//
    private String rain24h;
    //==기온==//
    private String temperature;
    //==풍향1==//
    //==풍향1-1==//
    //==풍속==//
    //==풍향10==//
    //==풍속10-1==//
    //==습도==//
    //==해면기압==//

    public PastRainResponseDto(Rain entity) {
        this.searchDay = entity.getSearchDay();
        this.rainHm = entity.getRainHm();
        this.rainYn = entity.getRainYn();
        this.rain15m = entity.getRain15m();
        this.rain60m = entity.getRain60m();
        this.rain3h = entity.getRain3h();
        this.rain6h = entity.getRain6h();
        this.rain12h = entity.getRain12h();
        this.rain24h = entity.getRain24h();
        this.temperature = entity.getTemperature();
    }

}

