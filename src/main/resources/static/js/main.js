`use strict`

const main = {
    init : function(){
        document.querySelector('#btn-search').addEventListener('click',()=>{
            this.search();
        });
        document.querySelector('#btn-excel').addEventListener('click',()=>{
            this.excelDownload();
        });
        // $('#datetimepicker2').datetimepicker({
        //     locale: 'ru'
        // });
    },
    search : function () {
        let param = {
            city :  document.querySelector('#city').value,
            searchStDay : document.querySelector('#searchStDay').value ,
            searchEdDay : document.querySelector('#searchEdDay').value ,
        };
        console.log("data -----------------> " , JSON.stringify(param));
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/getRainAll", true);
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        xhr.send(JSON.stringify(param));
        try{
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        console.log("작업내용 작성");
                        const response = JSON.parse(xhr.responseText);
                        const list = response.data;
                        //let tbody = document.querySelector('#tbody');

                        $('#tbody tr').remove(); //jQuery 걷어내기
                        for(let i in list){

                            //let tr = document.createElement("tr");
                            //let td1 = document.createElement("td");​
                            //td1.innerHTML = ""+list[i].searchDay;
                            //tbody.appendChild(tr);

                            let html ="";
                            html += "<tr>";
                            html +="<td>"+list[i].searchDay+"</td>";
                            html +="<td>"+list[i].rainHm+"</td>";
                            html +="<td>"+list[i].rainYn+"</td>";
                            html +="<td>"+list[i].rain15m+"</td>";
                            html +="<td>"+list[i].rain60m+"</td>";
                            html +="<td>"+list[i].rain3h+"</td>";
                            html +="<td>"+list[i].rain6h+"</td>";
                            html +="<td>"+list[i].rain12h+"</td>";
                            html +="<td>"+list[i].temperature+"</td>";
                            html +="</tr>";

                            $('#tbody').append(html); //jQuery 걷어내기
                        }
                    }
                }
            };
        }catch (e) {
            console.log('Caught Exception: ' + e.description);
        }
    },
    excelDownload : () => {
        if(!confirm("엑셀다운로드를 시작합니다.\n계속하시겠습니까?")) return;
        $(".m_bg").show(); //배경 //jQuery 걷어내기
        const paraMap = {
            city :  document.querySelector('#city').value,
            searchStDay : document.querySelector('#searchStDay').value ,
            searchEdDay : document.querySelector('#searchEdDay').value ,
        };
        console.log("엑셀 다운로드 !!!!! 시작 ", paraMap);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/excelDown", true);
        xhr.responseType = 'blob';
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        xhr.send(JSON.stringify(paraMap));
        xhr.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
                $(".m_bg").hide(); // 배경 //jQuery 걷어내기
                console.log("다운로드!!");
                var _data = this.response;
                var _blob = new Blob([_data], {type: 'ms-vnd/excel'});
                //--가상다운--//
                var link = document.createElement('a');
                link.href = window.URL.createObjectURL(_blob);
                link.download = 'text.xls';
                link.click();
            }
        }
    },
};


main.init();

