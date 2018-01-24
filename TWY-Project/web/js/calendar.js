/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * COMMENT: Переменные
 */
var xmlHttp = new XMLHttpRequest();
var msg_type;


function sendCalQuery(message) {
    xmlHttp.open("POST", url, true);
    xmlHttp.onreadystatechange = readyCalRequest;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded" );
    xmlHttp.send(message);
}


function getYearCalendar(year) {
    msg_type = "calendar";
    message = "cmd=getYearCalendar&year=" + year;
    sendCalQuery(message);
}

function show_cal(cal_id) {
    msg_type = "cal_entry";
    message = "cmd=getCalendarEntry&id=" + cal_id;
    alert("Запрос объекта календаря");
}



/*
 * FUNCTION: readyCalRequest
 * COMMENT: Получаем ответ от сервера и обрабатываем его
 */
function readyCalRequest()
{
	if (xmlHttp.status === 500) {
		alert("На сервере возникла ошибка! Смотри журнал событий сервера.");
		return;
	}
	if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            var result;
            var response = xmlHttp.responseText;
            if (msg_type === "calendar") {
                json_array = response.split(": :");
                cal_array = [];
                for ( i = 1; i < json_array.length; i ++) {
                    cal_array[i-1] = JSON.parse(json_array[i], parseCal);
                }
                for (i = 0; i < cal_array.length; i++) {
                    result += HTMLCalendar(cal_array[i]);
                }
            }
            
            
            document.getElementById("calendar").innerHTML = result;
        }
}

function HTMLCalendar(json_obj) {
    html = "<div class=\"time_object\" onClick=\"show_cal(";
    html += json_obj.id;
    html += ")\">";
    html += "<div class=\"time_object_begin\">";
    html += json_obj.begin_date.getDate();
    html += "</div>";
    html += "<div class=\"time_object_end\">";
    html += json_obj.end_date.getDate();
    html += "</div>";
    html += "<div class=\"time_object_entry\">";
    html += json_obj.entry;
    html += "</div>";
    html += "<div class=\"time_object_task\">";
    html += "Задача: ";
    html += json_obj.task_entry;
    html += "</div>";
    html += "</div>";
    return html;
}


function parseCal(key, value) {
    if (key == "begin_date") return new Date(value);
    if (key == "end_date") return new Date(value);
    return value;
}