function addTask() {
    const taskInput = document.getElementById("taskInput");
    const taskList = document.getElementById("taskList");

    if (taskInput.value.trim() !== "") {
        const listItem = document.createElement("li");
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";

        checkbox.addEventListener("change", function() {
            listItem.style.textDecoration = this.checked ? "line-through" : "none";
        });
		
		// 삭제 버튼 추가
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "-";
        deleteButton.classList.add("delete");
		
		// 삭제 버튼 클릭 시 항목 삭제
       deleteButton.addEventListener("click", function() {
       taskList.removeChild(listItem);
       });

        listItem.appendChild(checkbox);
        listItem.appendChild(document.createTextNode(taskInput.value));
		// 리스트 아이템에 삭제 버튼 추가
		listItem.appendChild(deleteButton);
		
        taskList.appendChild(listItem);

        taskInput.value = "";
    }
	
}

// 엔터키로 할 일 추가
document.getElementById("taskInput").addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        addTask();
    }
});

// 전체 삭제 기능
function clearAllTasks() {
    const taskList = document.getElementById("taskList");
    taskList.innerHTML = ""; // 목록의 모든 항목 삭제
}