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
var message;
var url = "vss";	
var page_type;
var week;

function takePage(page_type, par) {
    msg_type = "getPage";
    message = "cmd=takePage&getPage=" + page_type;
    if (page_type === "editWeek") {
        msg_type = "tasks";
        week = par;
        message = "cmd=takeTaskPage&getPage=" + page_type + "&Week_id=" + week;
    }
    xmlHttp.open("POST", url, true);
    xmlHttp.onreadystatechange = readyRequest;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded" );
    xmlHttp.send(message);
}

function getYearTasks(year) {
    msg_type = "tasks";
    message = "cmd=getYearTasks&year=" + year;
    xmlHttp.open("POST", url, true);
    xmlHttp.onreadystatechange = readyRequest;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded" );
    xmlHttp.send(message);
}

function addNewTask(week_id) {
    msg_type = "tasks";
    task_day = document.getElementById("new_day").options.selectedIndex + 1;
    task_name = document.getElementById("new_taskName").value;
    message = "cmd=new_task&week_id=" + week_id + "&task_day=" + task_day;
    message = message + "&task_name=" + task_name;
    xmlHttp.open("POST", url, true);
    xmlHttp.onreadystatechange = readyRequest;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded" );
    xmlHttp.send(message);
}

function editTask(task_id) {
    msg_type = "tasks";
    task_name = document.getElementById("taskName_" + task_id).value;
    task_finish = document.getElementById("finish_" + task_id).checked;
    message = "cmd=edit_task&task_id=" + task_id + "&task_name=" + task_name;
    if (task_finish) 
        message += "&finish=true";
    else
        message += "&finish=false";
    xmlHttp.open("POST", url, true);
    xmlHttp.onreadystatechange = readyRequest;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded" );
    xmlHttp.send(message);
}
	
/*
 * FUNCTION: readyRequest
 * COMMENT: Получаем ответ от сервера и обрабатываем его
 */
function readyRequest()
{
	if (xmlHttp.status === 500) {
		alert("На сервере возникла ошибка!");
		return;
	}
	if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            var response = xmlHttp.responseText;
            if (msg_type === "getPage") {
		document.close();
                document.write(response);
            }
            if (msg_type === "tasks") {
                document.getElementById("tasks").innerHTML = response;
            }
        }
}