// 일정 데이터를 서버에 저장하는 AJAX 함수
function saveScheduleData(id, data) {
    $.ajax({
        url: '/api/schedule',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(Object.assign({ id: id }, data)),
        success: function(response) {
            console.log("데이터 저장 성공:"+ response);
            alert('일정이 저장되었습니다!');
        },
        error: function(error) {
            console.error('데이터 저장 실패:'+ error);
            alert('일정 저장에 실패했습니다.');
        }
    });
}

// 일정 데이터를 서버에서 불러오는 AJAX 함수
function loadScheduleData(id, callback) {
    $.ajax({
        url: '/api/schedule?id='+ id,
        type: 'GET',
        success: function(data) {
            console.log('데이터 불러오기 성공:', data);
            callback(data);
        },
        error: function(error) {
            console.error('데이터 불러오기 실패:', error);
            alert('일정 데이터를 불러오지 못했습니다.');
        }
    });
}
