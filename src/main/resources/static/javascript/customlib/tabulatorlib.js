var tabulatorlib = {

    /**
     * tabulator 단순 정보 테이블 생성
     * - sum 합계 없음
     */
    makeSimpleInfoTable: function (pEleId,columArray ){
        let eleId =  pEleId;
        if(!pEleId.startsWith("#")){
            eleId = '#' + pEleId;
        }

        return  new Tabulator( eleId , {
            maxHeight: "100%",
            height:"100%",
            selectableRows:1,
            selectableRowsRangeMode:"click",
            columnHeaderVertAlign: "middle",
            layout:"fitColumns",
            columns: columArray,
            placeholder: "No Data",
        });
    },


    /**
     * tabulator row에 checkbox 있는 테이블 생성
     * - sum 합계 없음
     */
    makeRowCheckboxTable: function (pEleId,columArray ){
        let eleId =  pEleId;
        if(!pEleId.startsWith("#")){
            eleId = '#' + pEleId;
        }

        return  new Tabulator( eleId , {
            maxHeight: "100%",
            height:"100%",
            //layout:"fitDataStretch",
            layout:"fitColumns",
            columns: columArray,
            placeholder: "No Data",
            rowHeader:{headerSort:false, resizable: false, frozen:true, headerHozAlign:"center", hozAlign:"center", formatter:"rowSelection", titleFormatter:"rowSelection",width:30,
                cellClick:function(e, cell){
                    cell.getRow().toggleSelect();
            }},
        });
    },
}