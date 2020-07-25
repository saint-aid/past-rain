'use strict'

const main = {
    init : function(){

        const _this = this;
        document.querySelector('#btn-search').addEventListener('click',()=>{
            _this.search();
        })
    },
    search : function () {
        const data = {
           //date : document.getElementById('#date').valueOf()
        };
        $.ajax({
            type : 'GET',
            url : '/api/v1/posts' + search,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            //data: JSON.stringify(data)
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (){
            alert(JSON.stringify(error));
        })
    },
};

main.init();

