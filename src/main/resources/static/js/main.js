'use strict'

const main = {
    init : function(){
        console.log('1111111111111111111111111111111111111111111n1t')
        document.querySelector('#btn-search').addEventListener('click',()=>{
            this.search();
        });
        document.querySelector('#btn-excel').addEventListener('click',()=>{
            if(!confirm("엑셀다운로드를 시작합니다.\n계속하시겠습니까?")) return;
            location.href = "/excelDown";
        });
    },
    search : function () {
        const data = {
           //date : document.getElementById('#date').valueOf()
        };

        const xhr = new XMLHttpRequest();
        xhr.open("GET", "/getRainAll", true);
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        xhr.send(null);
        try{
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        console.log("작업내용 작성");
                        const response = JSON.parse(xhr.responseText);
                        const list = response.data;

                        for(let i in list){
                            //let tbody = document.querySelector('#tbody');
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

                            $('#tbody').append(html);
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

