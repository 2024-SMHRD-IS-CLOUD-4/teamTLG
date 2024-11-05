let dayCount = 0;
let cardCounter = 0;
let draggedColumn = null;
let draggedCard = null;

function createColumns(days) {
    const board = document.getElementById('kanban-board');
    board.innerHTML = ''; // 기존 열 초기화
    dayCount = days;

    for (let i = 1; i <= dayCount; i++) {
        addDayColumn(i); // 각 날짜에 대해 열 추가
    }

    initializeDragAndDrop(); // 드래그 앤 드롭 초기화
}

function addDayColumn(dayNumber) {
    const board = document.getElementById('kanban-board');

    const column = document.createElement('div');
    column.className = 'kanban-column';
    column.id = `day-${dayNumber}`;

    const headerContainer = document.createElement('div');
    headerContainer.className = 'header-container';

    const header = document.createElement('h2');
    header.textContent = `Day ${dayNumber}`;
    headerContainer.appendChild(header);

    const deleteColumnButton = document.createElement('button');
    deleteColumnButton.className = 'delete-column-button';
    deleteColumnButton.textContent = 'X';
    deleteColumnButton.onclick = () => deleteColumn(column); // 열 삭제
    headerContainer.appendChild(deleteColumnButton);

    column.appendChild(headerContainer);

    const editButton = document.createElement('button');
    editButton.className = 'edit-button';
    editButton.textContent = 'Edit';
    editButton.onclick = () => toggleEditMode(column, editButton); // 편집 모드 전환
    column.appendChild(editButton);

    const itemsContainer = document.createElement('div');
    itemsContainer.className = 'kanban-items';
    column.appendChild(itemsContainer);

    const addButton = document.createElement('button');
    addButton.className = 'add-card-button';
    addButton.textContent = "+ Add Card";
    addButton.onclick = () => addCard(column.id); // 카드 추가
    column.appendChild(addButton);

    column.setAttribute('draggable', 'true');
    column.addEventListener('dragstart', dragStartColumn);
    column.addEventListener('dragend', dragEndColumn);

    board.appendChild(column);
}

function addCard(columnId) {
    const column = document.getElementById(columnId).querySelector('.kanban-items');

    const card = document.createElement('div');
    card.className = 'kanban-card';

    const uniqueId = `${columnId}-card-${cardCounter++}`;
    card.dataset.id = uniqueId;

    const textarea = document.createElement('textarea');
    textarea.className = 'card-input';
    textarea.placeholder = 'Enter event title';
    textarea.wrap = 'soft';
    textarea.addEventListener('input', autoResize); // 자동 크기 조절

    card.appendChild(textarea);
    column.appendChild(card);
    textarea.focus();

    // 블러 이벤트로 카드 제목 저장 후 세부 페이지로 이동할 수 있도록 클릭 이벤트 추가
    textarea.addEventListener('blur', () => {
        if (textarea.value.trim() !== '') {
            card.textContent = textarea.value;
            card.addEventListener('click', openDetailPage); // 클릭 시 세부 페이지 이동
        } else {
            card.remove(); // 내용이 없으면 카드 삭제
        }
    });

    textarea.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); // Enter키 입력 방지
        }
    });

    card.setAttribute('draggable', 'true');
    card.addEventListener('dragstart', dragStart);
    card.addEventListener('dragend', dragEnd);
}

function openDetailPage(event) {
    const card = event.currentTarget;
    const uniqueId = card.dataset.id;
    const url = `kb_sub.jsp?id=${uniqueId}`; // 세부 페이지 URL
    window.open(url, '_blank'); // 새 탭에서 열기
}

function deleteColumn(column) {
    if (confirm("해당 날짜를 지우시겠습니까? 해당 날의 일정이 전부 삭제됩니다!")) {
        column.remove(); // 열 삭제
        updateDayNumbers(); // 열 번호 업데이트
        initializeDragAndDrop(); // 드래그 앤 드롭 초기화
    }
}

function addNewDay() {
    dayCount++;
    addDayColumn(dayCount); // 새로운 열 추가
    initializeDragAndDrop(); // 드래그 앤 드롭 초기화
}

function updateDayNumbers() {
    const columns = document.querySelectorAll('.kanban-column');
    dayCount = columns.length;

    columns.forEach((column, index) => {
        const dayNumber = index + 1;
        const header = column.querySelector('h2');
        header.textContent = `Day ${dayNumber}`;
        column.id = `day-${dayNumber}`; // 열 ID 업데이트

        const addButton = column.querySelector('.add-card-button');
        addButton.onclick = () => addCard(column.id); // 카드 추가 핸들러 업데이트
    });
}

function toggleEditMode(column, editButton) {
    const isEditing = editButton.textContent === 'Edit';
    editButton.textContent = isEditing ? 'Save' : 'Edit'; // 버튼 텍스트 전환

    const cards = column.querySelectorAll('.kanban-card');
    cards.forEach(card => {
        if (isEditing) {
            const currentText = card.textContent;
            card.innerHTML = '';
            const input = document.createElement('textarea');
            input.className = 'card-input';
            input.value = currentText;
            input.addEventListener('input', autoResize);

            const deleteButton = document.createElement('button');
            deleteButton.className = 'delete-button';
            deleteButton.textContent = 'X';
            deleteButton.onclick = () => card.remove(); // 카드 삭제

            card.appendChild(input);
            card.appendChild(deleteButton);

            card.removeEventListener('click', openDetailPage); // 클릭 이벤트 제거
        } else {
            const input = card.querySelector('textarea');
            if (input && input.value.trim() !== '') {
                card.textContent = input.value; // 입력값으로 카드 텍스트 업데이트
                card.addEventListener('click', openDetailPage); // 클릭 이벤트 다시 추가
            }
        }
    });
}

function autoResize(event) {
    const textarea = event.target;
    textarea.style.height = 'auto'; // 높이 초기화
    textarea.style.height = `${textarea.scrollHeight}px`; // 내용에 맞게 높이 조정
}

function dragStart(event) {
    draggedCard = event.target;
    event.dataTransfer.setData("text/plain", event.target.textContent);
    setTimeout(() => {
        draggedCard.style.display = "none"; // 드래그 시 카드 숨기기
    }, 0);
}

function dragEnd(event) {
    setTimeout(() => {
        draggedCard.style.display = "block"; // 드래그 끝난 후 카드 보이기
        draggedCard = null; // 드래그 카드 초기화
    }, 0);
}

function dragOver(event) {
    event.preventDefault();
    const target = event.target.closest('.kanban-card') || event.target.closest('.kanban-items');

    if (target && draggedCard) {
        const bounding = target.getBoundingClientRect();
        const offset = event.clientY - bounding.top;

        if (target.classList.contains('kanban-card')) {
            if (offset < bounding.height / 2) {
                target.parentNode.insertBefore(draggedCard, target);
            } else {
                target.parentNode.insertBefore(draggedCard, target.nextSibling);
            }
        } else if (target.classList.contains('kanban-items')) {
            target.appendChild(draggedCard);
        }
    }
}

function drop(event) {
    event.preventDefault();
    const target = event.target.closest('.kanban-card') || event.target.closest('.kanban-items');

    if (target && draggedCard) {
        if (target.classList.contains("kanban-items")) {
            target.appendChild(draggedCard);
        } else if (target.classList.contains("kanban-card")) {
            const bounding = target.getBoundingClientRect();
            const offset = event.clientY - bounding.top;

            if (offset < bounding.height / 2) {
                target.parentNode.insertBefore(draggedCard, target);
            } else {
                target.parentNode.insertBefore(draggedCard, target.nextSibling);
            }
        }
    } else {
        draggedCard.style.display = "block"; // 드래그 후 카드 보이기
        draggedCard = null;
    }
}

function dragStartColumn(event) {
    draggedColumn = event.target;
    event.dataTransfer.effectAllowed = 'move';
    setTimeout(() => {
        draggedColumn.style.display = 'none'; // 드래그 시 열 숨기기
    }, 0);
}

function dragEndColumn(event) {
    setTimeout(() => {
        draggedColumn.style.display = "block"; // 드래그 후 열 보이기
        draggedColumn = null;
        updateDayNumbers(); // 열 번호 업데이트
    }, 0);
}

function dragOverColumn(event) {
    event.preventDefault();
    const targetColumn = event.target.closest('.kanban-column');
    if (targetColumn && draggedColumn !== targetColumn) {
        const bounding = targetColumn.getBoundingClientRect();
        const offset = event.clientX - bounding.left;

        if (offset > bounding.width / 2) {
            targetColumn.parentNode.insertBefore(draggedColumn, targetColumn.nextSibling);
        } else {
            targetColumn.parentNode.insertBefore(draggedColumn, targetColumn);
        }
    }
}

function initializeDragAndDrop() {
    document.querySelectorAll('.kanban-items').forEach(itemsContainer => {
        itemsContainer.addEventListener('dragover', dragOver);
        itemsContainer.addEventListener('drop', drop);
    });

    document.querySelectorAll('.kanban-column').forEach(column => {
        column.addEventListener('dragstart', dragStartColumn);
        column.addEventListener('dragend', dragEndColumn);
        column.addEventListener('dragover', dragOverColumn);
    });
}

// 페이지가 로드될 때 칸반 보드 초기화
document.addEventListener("DOMContentLoaded", () => {
    // 필요한 초기화 코드 추가
});
