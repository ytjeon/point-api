var dataDetail01Table ={
    tableObj : null,
    columnArrayDetail01: [
        { field:"pointKey"            ,title:"pointKey",  hozAlign:"center", width: 120},
        { field:"pointAccuType"       ,title:"포인트 종류",  hozAlign:"center", width: 150},
        { field:"orderNo"            ,title:"적립 주문번호",  hozAlign:"center", width: 180},
        { field:"accuDate"          ,title:"적립일자",   hozAlign:"center", width: 180},
        { field:"expireDate"        ,title:"만료일자",   hozAlign:"center", width: 180},
        { field:"accuPoint"           ,title:"적립금액",   hozAlign:"center", width: 180,topCalc:"sum", topCalcFormatter:"money",topCalcFormatterParams:{precision:0,}, formatter:"money", formatterParams:{precision:0,symbol:"", symbolAfter:false} },
        { field:"accuCancelYn"        ,title:"적립상태",   hozAlign:"center", width: 180},
        { field:"balancePoint"       ,title:"포인트 잔액",   hozAlign:"center", width: 180,topCalc:"sum", topCalcFormatter:"money",topCalcFormatterParams:{precision:0,}, formatter:"money", formatterParams:{precision:0,symbol:"", symbolAfter:false} },
    ],


    init: function (){
        this.tableObj = tabulatorlib.makeSimpleInfoTable('#detailTable01', this.columnArrayDetail01 );
        this.bindingEvent();
    },

    clearData: function (){
        if(this.tableObj != null)  this.tableObj.clearData()
    },
    setData: function (data){
        this.tableObj.replaceData(data);
    },

    bindingEvent:function (){

        $('#searchBtn').on('click', function(e){
            e.preventDefault();
            dataDetail01Table.search();
        });
    },

    search: function (){
        let userNo = $('#userNo').val()

        $.ajax({
            method: 'GET',
            url: '/api/point/getBalancePointByUserNo?userNo=' + userNo,
            contentType: "application/json",
            success: function (res){
                let resCode = res.resultCode
                let resMsg = res.resultMessage
                if(res.resultCode === '0000'){
                    data = res.data.pointAccuMstDtoList
                    data.forEach(function (e){
                        e.pointAccuType = (e.pointAccuType === '1')?"일반":"관리자 수기지급"
                        e.accuCancelYn  = (e.accuCancelYn === 'N')?"적립":"적립취소"
                    })
                    dataDetail01Table.setData(data);

                    let userName = res.data.userName
                    let balancePoint= res.data.balancePoint
                    $('#userName').text(userName)
                    $('#balancePoint').text(balancePoint)
                } else{
                    let alertMsg = `오류코드:[${resCode}]\n오류메시지:${resMsg}`
                    alert(alertMsg)
                }
            },
            error: function (){
                alert("조회를 실패하였습니다.");
            }
        });
    }
}
