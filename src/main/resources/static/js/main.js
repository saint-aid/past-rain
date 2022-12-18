
const main = (() => {
    `use strict`
    document.querySelector('#btn-search').addEventListener('click', () => {
        main.search();
    });
    document.querySelector('#btn-excel').addEventListener('click', () => {
        main.excelDownload();
    });

    return {
        search: function () {
            const param = {
                city: document.querySelector('#city').value,
                searchStDay: document.querySelector('#searchStDay').value,
                searchEdDay: document.querySelector('#searchEdDay').value,
            };
            // $(".m_bg").show(); //배경 //jQuery 걷어내기
            // $(".loadingImg").show(); //배경 //jQuery 걷어내기
            document.querySelector('.m_bg').display = 'block'
            document.querySelector('.loadingImg').display = 'block'

            fetch('/getRainAll', {
                method: 'POST',
                body: JSON.stringify(param),
                headers: {'Content-Type': 'application/json;charset=utf-8;'}
            })
                .then(response => response.json()) //json 객체로 반환
                .then(result => {
                    $(".m_bg").hide(); //배경 //jQuery 걷어내기
                    $(".loadingImg").hide(); //배경 //jQuery 걷어내기
                    const list = result.data;

                    let subTitle = list[0].subTitle;
                    console.log('subtitle ---', document.querySelector('#subTitle'));
                    document.querySelector('#subTitle').innerHTML = subTitle;

                    document.querySelector('#tbody tr').remove();
                    let html = "";
                    list.forEach( item => {
                        html += `<tr>
                                     <td>${item.searchDay}</td>
                                     <td>${item.rainHm}</td>
                                     <td>${item.rainYn}</td>
                                     <td>${item.rain15m}</td>
                                     <td>${item.rain60m}</td>
                                     <td>${item.rain3h}</td>
                                     <td>${item.rain6h}</td>
                                     <td>${item.rain12h}</td>
                                     <td>${item.temperature}</td>
                                 </tr>`
                    })
                    document.querySelector('#tbody').innerHTML = html;
                })
                .catch(error => console.error(error));
        },
        excelDownload: function () {
            if (!confirm("엑셀다운로드를 시작합니다.\n계속하시겠습니까?")) return;
            $(".m_bg").show(); //배경 //jQuery 걷어내기
            $(".loadingImg").show(); //배경 //jQuery 걷어내기
            const paraMap = {
                city: document.querySelector('#city').value,
                searchStDay: document.querySelector('#searchStDay').value,
                searchEdDay: document.querySelector('#searchEdDay').value,
            };
            console.log("엑셀 다운로드 !!!!! 시작 ", paraMap);
            fetch('/excelDown', {
                method: 'POST',
                body: JSON.stringify(paraMap),
                headers: {'Content-Type': 'application/json;charset=utf-8;'}
            })
                .then(response => response.blob())
                .then(_data => {
                    $(".m_bg").hide(); //배경 //jQuery 걷어내기
                    $(".loadingImg").hide(); //배경 //jQuery 걷어내기
                    //console.log(_data);
                    const _blob = new Blob([_data], {type: 'ms-vnd/excel'});
                    //--가상다운--//
                    const link = document.createElement('a');
                    link.href = window.URL.createObjectURL(_blob);
                    link.download = 'rainy_excel.xls';
                    link.click();
                })
                .catch(error => console.error(error));
        }
    }
})()
