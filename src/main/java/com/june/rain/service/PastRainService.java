package com.june.rain.service;

import com.june.rain.domain.Rain;
import com.june.rain.web.dto.PastRainResponseDto;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@NoArgsConstructor
@Service
public class PastRainService {

    private static String BaseUrl = "https://www.weather.go.kr/cgi-bin/aws/nph-aws_txt_min_guide_test?";

    //**조회데이터를 구해온다**/
    public List<PastRainResponseDto> getRainAll(String searchStDay, String searchEdDay,String city) {
        //String city = "273";
        List<PastRainResponseDto> rainList = new ArrayList<>(); // 초기화
        try {
            //1.==가공된 시간을 구해온다==//
            String[] days =  getSearchTime(searchStDay, searchEdDay);
            int idx = 1; //총 루프수()
            for(String min : days){
                //System.out.println("min ------- > " + min);
                //2.==url을 구하고 HTML 정보를 가져온다==/
                //인코딩 문제로 parsing 가져옴
                Document doc = Jsoup.parse(new URL(getUrls(min,city)).openStream(), "euc-kr", getUrls(min,city));
                //3)개체 정보 가져오기(tr 객체를 가져온다 61개)
                Elements els = doc.select(".text");
                for (Element el: els) {
                    if(idx == days.length ){ //마지막 배열 중 end 시간과 같으면 break
                        //--break 문을 사용하기 위한 날짜변환 -10분 --//
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                        Date endTime = format.parse(searchEdDay);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(endTime);
                        cal.add(Calendar.MINUTE, -10); //해당시간에 break 되기 때문에 전 시간과 비교

                        String hhmm = format.format(cal.getTime()).substring(8);
                        String hhmm2 = el.children().get(0).text().replace(":","");

                        if(hhmm.equals(hhmm2)) break; //총 루프수 넘으면 중단
                    }
                    PastRainResponseDto dto = new PastRainResponseDto(
                            Rain.builder() //setter 없음
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
                }
                idx++;//총 배열 수
            }
            //System.out.println("======rainList======= : " + rainList.toString());
            System.out.println("======size:rainList:size======= : " + rainList.size());

        }catch (Exception e){
            e.printStackTrace();
        }
        return rainList;
    }

    //==시간을 구한다==//
    public String[] getSearchTime(String searchStDay, String searchEdDay) throws Exception {
        //1)날짜에 따라 하루 데이터 가져오기 html parsing
            //1-1) 10분 데이터 시 조회 날짜 한페이지에 61개. 필요한 데이터 144. 하루는 3번(2.3번) 루프
            //1-2) 시간 10분 시 조회 날짜 컨버팅
        //String transStDay = searchStDay.substring(0,8);
        //String transEdDay = searchEdDay.substring(0,8);
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmm");
        Date d1 = fm.parse(searchStDay);
        Date d2 = fm.parse(searchEdDay);
        long diffDay = (d1.getTime() - d2.getTime())/(24*60*60*1000);
        int loopDt = Math.round((diffDay*144)/61)+ 1; //루프수 구하기

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
        return minuteList;
    }
    
    //==URL을 구해온다==//
    public String getUrls(String days, String city) {
        //String date = "&"+days; //서칭 날짜
        String standMin = "&0"; //서칭된 값의 기준
        String rangeMin = "&MINDB_10M"; //10분단위
        String cities = "&"+city; //도시(문경 273)
        String realTime = "&m&K"; //실시간(a)

        return BaseUrl + days + standMin + rangeMin + cities + realTime;
    }

    //**엑셀다운로드**//
    public void excelDown(HttpServletResponse response, String searchStDay,String searchEdDay,String city) throws Exception {

        //목록조회
        List<PastRainResponseDto> excelList = getRainAll(searchStDay, searchEdDay,city);

        // 워크북 생성
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("관측자료");
        Row row = null;
        Cell cell = null;
        int rowNo = 0;

        // 테이블 헤더용 스타일
        CellStyle headStyle = wb.createCellStyle();

        // 가는 경계선을 가집니다.
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경색은 회색입니다.
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(HorizontalAlignment.CENTER);


        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);


        // 헤더 생성
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("조회일");
        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("시:분");
        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수");
        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수15");
        cell = row.createCell(4);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수60");
        cell = row.createCell(5);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수3H");
        cell = row.createCell(6);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수6H");
        cell = row.createCell(7);
        cell.setCellStyle(headStyle);
        cell.setCellValue("강수12H");
        cell = row.createCell(8);
        cell.setCellStyle(headStyle);
        cell.setCellValue("일강수");
        cell = row.createCell(9);
        cell.setCellStyle(headStyle);
        cell.setCellValue("기온");


        // 데이터 부분 생성
        for(PastRainResponseDto dto : excelList) {
            row = sheet.createRow(rowNo++);

            cell = row.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getSearchDay());
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRainHm());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRainYn());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain15m());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain60m());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain3h());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain6h());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain12h());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getRain24h());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(dto.getTemperature());
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=rain.xls");

        // 엑셀 출력
        try {
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            wb.close();
        }
    }
}
