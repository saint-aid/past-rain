`use strict`

const main = {
    init : function(){
        document.querySelector('#btn-search').addEventListener('click',()=>{
            this.search();
        });
        document.querySelector('#btn-excel').addEventListener('click',()=>{
            if(!confirm("엑셀다운로드를 시작합니다.\n계속하시겠습니까?")) return;
            location.href = "/excelDown";
        });
        // $('#datetimepicker2').datetimepicker({
        //     locale: 'ru'
        // });
    },
    search : function () {
        const data = {
            city :  document.querySelector('#city').value,
            searchDay : document.querySelector('#calender').value ,
        };
        console.log("data -----------------> " , data);
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/getRainAll", true);
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        xhr.send(JSON.stringify(data));
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
};



main.init();

