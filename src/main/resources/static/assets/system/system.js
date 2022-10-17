let dataTable = null
$(document).ready(function () {
    dataTable = $("#datatable").DataTable({
        columns: [
            { data: 'expenses' },
            { data: 'price' },
            { data: 'use' },
            { data: 'createBy' },
            { data: 'createdDate' },
            { data: 'modifyBy' },
            { data: 'modifiedDate' },
            { data: 'action' },
        ],
        order: [[6, 'desc']],
    })
    loadListSpending()
});
function loadListSpending(){
    $.ajax({
        type: "GET",
        url: "spending",
        error: function () {
            console.log("error")
        },
        success: function (dataOut) {
            let data = new Array();
            console.log(dataOut)
            for(let i=0;i<dataOut.length;i++){
                let use = '';
                dataOut[i].memberSpendings.forEach((o) => {
                    use += o.member.fullname + ', '
                })
                data.push({
                    expenses: dataOut[i].expenses,
                    price: dataOut[i].price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + 'đ',
                    use: use,
                    createBy: dataOut[i].member.fullname,
                    createdDate: new Date(dataOut[i].createdDate).toLocaleDateString("en-US"),
                    modifyBy: dataOut[i].modifiedBy,
                    modifiedDate: new Date(dataOut[i].modifiedDate).toLocaleDateString("en-US") + ' ' + new Date(dataOut[i].modifiedDate).getHours() +':' + new Date(dataOut[i].modifiedDate).getMinutes(),
                    action: '<button onclick="onUpdateChiTieu(\''+dataOut[i].id+'\')" class="btn btn-primary"><i class="bi bi-pencil-square"></i></button>',
                })
            }

            console.log(data)
            dataTable.clear().draw();
            dataTable.rows.add(data).draw();
            console.log("hello")
        },
    });

    loadStatis()
}

function addChiTieu(){
    let khoanChi = document.getElementById('khoanChi').value
    let giaTien = document.getElementById('giaTien').value
    let note = document.getElementById('note').value
    let use = new Array();
    $('#addDat')[0].checked ? use.push($('#addDat')[0].value) : ''
    $('#addThanh')[0].checked ? use.push($('#addThanh')[0].value) : ''
    $('#addThuy')[0].checked ? use.push($('#addThuy')[0].value) : ''

    let spendingDto = {
        expenses: khoanChi,
        price: giaTien,
        use: use,
        note: note,
    }

    console.log(spendingDto)

    let check = validate(spendingDto);
    console.log(check)

    if(check){
        $.ajax({
            type: "POST",
            url: "spending",
            contentType: "application/json",
            data: JSON.stringify(spendingDto),
            dataType: 'json',
            error: function (error) {
                console.log(error)
                loadListSpending()
                document.getElementById('addChiTieu').style.display = 'none'
            },
            success: function (dataOut) {
                document.getElementById('addChiTieu').style.display = 'none'
                loadListSpending()
            },
        });
    }
}

function onUpdateChiTieu(id){
    document.getElementById('idDetele').setAttribute('idDelete', id)
    $.ajax({
        type: "GET",
        url: "spending-detail?id=" + id,
        error: function () {
            console.log("error")
        },
        success: function (dataOut) {
            console.log(dataOut)
            if(dataOut.delete){
                document.getElementById('idDetele').style.display = ''
            } else {
                document.getElementById('idDetele').style.display = 'none'
            }
            document.getElementById('editChiTieu').style.display = ''
            $('#idSpending')[0].value = id;
            $('#editKhoanChi')[0].value = dataOut.expenses;
            $('#editGiaTien')[0].value = dataOut.price;
            $('#editNote')[0].value = dataOut.note;

            dataOut.memberSpendings.forEach(o => {
                o.member.id == 1 ? $('#editThanh')[0].checked = true : ''
                o.member.id == 2 ? $('#editDat')[0].checked = true : ''
                o.member.id == 3 ? $('#editThuy')[0].checked = true : ''
            })
        },
    });
}

function updateChiTieu(){
    let khoanChi = document.getElementById('editKhoanChi').value
    let giaTien = document.getElementById('editGiaTien').value
    let note = document.getElementById('editNote').value
    let idSpending = document.getElementById('idSpending').value
    let use = new Array();
    $('#editDat')[0].checked ? use.push($('#editDat')[0].value) : ''
    $('#editThanh')[0].checked ? use.push($('#editThanh')[0].value) : ''
    $('#editThuy')[0].checked ? use.push($('#editThuy')[0].value) : ''

    let spendingDto = {
        idSpending: idSpending,
        expenses: khoanChi,
        price: giaTien,
        use: use,
        note: note,
    }

    console.log(spendingDto)
    let check = validate(spendingDto);
    if(check){
        $.ajax({
            type: "POST",
            url: "spending",
            contentType: "application/json",
            data: JSON.stringify(spendingDto),
            dataType: 'json',
            error: function (error) {
                console.log(error)
                loadListSpending()
                document.getElementById('editChiTieu').style.display = 'none'
            },
            success: function (dataOut) {
                document.getElementById('editChiTieu').style.display = 'none'
                loadListSpending()
            },
        });

    }
}

function deleteChiTieu(elm){
    let idSpending = elm.getAttribute('idDelete');
    if(window.confirm("Có chắc muốn xóa không?")){
        $.ajax({
            type: "DELETE",
            url: "spending?id=" + idSpending,
            error: function () {
                console.log("error")
            },
            success: function (dataOut) {
                document.getElementById('editChiTieu').style.display = 'none'
                loadListSpending()
            },
        });
    }
}

function loadStatis(){
    $.ajax({
        type: "GET",
        url: "statis",
        error: function () {
            console.log("error")
        },
        success: function (dataOut) {
            console.log(dataOut)
            $('#statis')[0].innerHTML = '';
            let statisContainer = $('#statis')[0];
            for (let data of dataOut) {
                statisContainer.innerHTML += '<div class="col-xl-4 col-md-6">\n' +
                    '                        <div class="card mini-stats">\n' +
                    '                            <div class="p-3 mini-stats-content">\n' +
                    '                                <div class="mb-4">\n' +
                    '                                    <div class="float-end text-end">\n' +
                    '                                        <h6 class="mt-0 mb-3 text-white">'+data.fullName+'</h6>\n' +
                    '                                        <p class="text-white-50">Tổng chi: '+data.totalSpending.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'đ</p>\n' +
                    '                                    </div>\n' +
                    '                                    <img src="assets/img/'+data.img+'" width="54" height="54" style="border-radius: 50%;" alt="" srcset="">\n' +
                    '                                    <!-- <span class="peity-pie" data-peity=\'{ "fill": ["rgba(255, 255, 255, 0.8)", "rgba(255, 255, 255, 0.2)"]}\' data-width="54" data-height="54">5/8</span> -->\n' +
                    '                                </div>\n' +
                    '                            </div>\n' +
                    '                            <div class="mx-3">\n' +
                    '                                <div class="card mb-0 border p-3 mini-stats-desc">\n' +
                    '                                    <div class="d-flex">\n' +
                    '                                        <h6 class="mt-0 mb-3">Nợ</h6>\n' +
                    '                                        <h5 class="ms-auto mt-0">'+data.debitTotal.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'đ</h5>\n' +
                    '                                    </div>\n' +
                    '                                    <p class="text-muted mb-0">'+data.debitInfos[0].creditor+': '+data.debitInfos[0].total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'đ</p>\n' +
                    '                                    <p class="text-muted mb-0">'+data.debitInfos[1].creditor+': '+data.debitInfos[1].total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'đ</p>\n' +
                    '                                </div>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                    </div>';

            }

            document.getElementById('preloader').style.display = 'none'
        },
    });
}

function validate(spendingDto) {
    if(spendingDto.expenses.trim().length === 0){
        alert("Nhập khoản chi vào!");
        return false;
    } else if(spendingDto.price.trim().length === 0){
        alert("Nhập giá vào!");
        return false;
    } else if(!/^\d+$/.test(spendingDto.price)){
        alert("Giá là số chứ!");
        return false;
    } else if(Number(spendingDto.price) < 0) {
        alert("Mua gì mà âm!");
        return false;
    }else if(spendingDto.use.length === 0){
        alert("Chọn người dùng đi!");
        return false;
    } else {
        return true;
    }
}
