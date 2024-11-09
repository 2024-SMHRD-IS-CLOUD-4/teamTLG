let dayCount = 0;
let cardCounter = 0;
let draggedColumn = null;
let draggedCard = null;

$(document).ready(function() {
	// URL에서 tr_idx 값을 가져옵니다.
	const urlParams = new URLSearchParams(window.location.search);
	const tr_idx = urlParams.get('tr_idx');

	if (tr_idx) {
		loadKanbanBoard(tr_idx); // tr_idx에 맞는 Kanban 보드를 로드합니다.
	} else {
		console.error('tr_idx가 제공되지 않았습니다.');
	}
});

// 초기 데이터를 서버에서 가져와 Kanban 보드에 설정하는 함수
function loadKanbanBoard(tr_idx) {
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'GET',
		data: { tr_idx: tr_idx },
		dataType: 'json',
		success: function(data) {
			console.log("로드된 데이터:", data);
			const kanbanBoard = $('#kanban-board');
			kanbanBoard.empty(); // 기존 보드 내용을 지웁니다.

			data.forEach(day => {
				const dayId = `day-${day.col_idx}`;

				// Day 컬럼 생성
				if (!$(`#${dayId}`).length) {
					addDayColumn(day.col_idx, day.col_title);
				}

				// 카드가 존재하고, 실제로 빈 배열이 아닌 경우에만 추가
				if (day.cards && Array.isArray(day.cards) && day.cards.length > 0) {
					day.cards.forEach(card => {
						// 카드의 고유 ID를 기반으로 중복 추가 방지
						const cardId = `${day.col_idx}-card-${card.card_idx}`;
						if (!$(`#${cardId}`).length) {
							addCard(day.col_idx, card.card_idx, card.card_title);
						}
					});
				}
			});

			// 새로 생성된 요소들에 드래그 앤 드롭 기능 초기화
			initializeDragAndDrop();
			updateDayNumbers(); // 컬럼 순서에 따라 Day 번호 업데이트
		},
		error: function(xhr, status, error) {
			console.error('데이터를 로드하는 중 오류가 발생했습니다:', error);
		}
	});
}

let latestColIdx = null; // 최근 생성된 컬럼의 col_idx를 저장하기 위한 변수

function addNewDay() {
	console.log("컬럼생성되는 addNewDay 호출"); // 호출되는지 확인
	dayCount++;

	// 현재 보드에 있는 컬럼 개수를 기준으로 새로운 컬럼의 순서를 정합니다.
	const currentColumnCount = $('.kanban-column').length;
	const newColOrder = currentColumnCount + 1; // 새 컬럼은 현재 컬럼 개수 + 1 번째 위치가 됩니다.

	// 서버에 새로운 Day 컬럼 저장 요청
	$.ajax({
		url: '/TravelMate/KanbanController', // KanbanController의 URL입니다.
		type: 'POST',
		data: JSON.stringify({
			action: 'createColumn',
			tr_idx: tr_idx, // 현재 여행 계획 ID
			col_title: `Day ${dayCount}`, // 고유한 컬럼 제목으로 설정
			col_order: newColOrder // 클라이언트에서 결정한 새로운 컬럼 순서
		}),
		contentType: 'application/json',
		success: function(response) {
			if (response && response.col_idx) {
				// 서버에서 받아온 col_idx를 기반으로 컬럼 추가
				addDayColumn(response.col_idx, `Day ${newColOrder}`); // 새로운 열 추가
				console.log('새로운 컬럼이 서버에 저장되었습니다:', response);

				// 최근 생성된 컬럼의 col_idx를 전역 변수에 저장합니다.
				latestColIdx = response.col_idx;
			}
		},
		error: function(xhr, status, error) {
			console.error('새로운 컬럼 저장 중 오류가 발생했습니다:', error);
			console.error('상태:', status);
			console.error('응답:', xhr.responseText);
		}
	});
}

function addCard(col_idx = null, card_idx = null, card_title = "") {

	console.log('addcard 함수 호출됨, col_idx : ', col_idx);
	// col_idx가 주어지지 않은 경우 최근 생성된 컬럼의 col_idx를 사용합니다.
	if (!col_idx && latestColIdx) {
		col_idx = latestColIdx;
	}

	const itemsContainer = $(`#Day${col_idx} .kanban-items`);

	if (itemsContainer.length === 0) {
		console.error('카드를 추가할 대상 컬럼을 찾을 수 없습니다.');
		return;
	}

	const card = $('<div>').addClass('kanban-card').attr('draggable', 'true');

	// 임시 card_idx 생성 (서버로부터 받아온 이후 업데이트될 예정)
	if (!card_idx) {
		card_idx = `temp-${Date.now()}`;
	}
	card.attr('data-id', card_idx);

	if (card_title) {
		// 이미 존재하는 카드일 경우 (DB에서 불러온 카드)
		card.text(card_title);
	} else {
		// 새 카드 생성 시
		const textarea = $('<textarea>').addClass('card-input').attr('placeholder', 'Enter event title');
		textarea.on('input', autoResize);

		textarea.on('blur', function() {
			if (textarea.val().trim() !== '') {
				card.text(textarea.val()); // 카드에 텍스트 설정

				// 서버에 카드 저장 요청
				$.ajax({
					url: '/TravelMate/KanbanController',
					type: 'POST',
					data: JSON.stringify({
						action: 'createCard',
						card_title: textarea.val(),
						col_idx: col_idx,
						card_order: itemsContainer.children().length + 1 // 현재 컬럼의 카드 개수 + 1로 card_order 설정
					}),
					contentType: 'application/json; charset=UTF-8',
					success: function(response) {
						console.log('카드가 성공적으로 서버에 저장되었습니다:', response);
						// 서버로부터 생성된 card_idx를 받아와 업데이트
						if (response.card_idx) {
							card.attr('data-id', response.card_idx);
						} else {
							console.error('서버 응답에 card_idx가 없습니다.');
						}
					},
					error: function(xhr, status, error) {
						console.error('카드를 서버에 저장하는 중 오류가 발생했습니다:', error);
						console.error('응답:', xhr.responseText);
					}
				});

				// blur 이벤트 후 클릭 이벤트 추가
				card.on('click', function(event) {
					if (!textarea.is(":focus")) { // textarea가 포커스를 잃었을 때만 이동
						const cardId = card.data('id');
						openDetailPage(cardId);
					}
				});
			} else {
				card.remove(); // 내용이 없을 경우 카드 삭제
			}
		});

		card.append(textarea);
		textarea.focus();
	}

	itemsContainer.append(card);

	// 드래그 앤 드롭 이벤트 추가
	card.on('dragstart', dragStart);
	card.on('dragend', dragEnd);
}





function autoResize(event) {
	const textarea = event.target;
	textarea.style.height = 'auto'; // 높이 초기화
	textarea.style.height = `${textarea.scrollHeight}px`; // 내용에 맞게 높이 조정
}

function addDayColumn(col_idx, col_title) {
	console.log("addDayColumn 호출됨 - col_idx:", col_idx, ", col_title:", col_title);

	const kanbanBoard = $('#kanban-board');
	const columnId = `Day ${col_idx}`;
	if ($(`#${columnId}`).length > 0) {
		console.error('중복된 컬럼 ID가 발견되었습니다:', columnId);
		return;
	}

	const column = $('<div>').addClass('kanban-column').attr('id', `Day${col_idx}`).attr('draggable', 'true');
	const header = $('<h2>').text(col_title);
	const itemsContainer = $('<div>').addClass('kanban-items');

	const editButton = $('<button>').addClass('edit-button').text('Edit');
	editButton.on('click', function() { toggleEditMode(column, editButton); });

	const addButton = $('<button>').addClass('add-card-button').text("+ Add Card");
	addButton.on('click', function() { addCard(col_idx); });

	column.append(header).append(editButton).append(itemsContainer).append(addButton);

	// 컬럼에 드래그 앤 드롭 이벤트 추가
	column.on('dragstart', dragStartColumn)
		.on('dragend', dragEndColumn)
		.on('dragover', dragOverColumn)
		.on('drop', dropColumn);

	kanbanBoard.append(column);
}





// 특정 카드 클릭 시 새로운 페이지로 이동하는 함수
function openDetailPage(cardId) {
	const url = `kb_sub.jsp?id=${cardId}`; // 세부 페이지 URL
	window.open(url, '_blank'); // 새 탭에서 열기
}

// 컬럼 순서를 기준으로 Day 번호 업데이트
function updateDayNumbers() {
	const columns = document.querySelectorAll('.kanban-column');
	columns.forEach((column, index) => {
		const header = column.querySelector('h2');
		header.textContent = `Day ${index + 1}`;
	});
}

// 드래그 앤 드롭 초기화 함수
function initializeDragAndDrop() {
	// 카드 요소에 드래그 이벤트 추가
	document.querySelectorAll('.kanban-card').forEach(card => {
		card.addEventListener('dragstart', dragStart);
		card.addEventListener('dragend', dragEnd);
	});

	// 컬럼 요소에 드래그 이벤트 추가
	document.querySelectorAll('.kanban-column').forEach(column => {
		column.addEventListener('dragstart', dragStartColumn);
		column.addEventListener('dragend', dragEndColumn);
		column.addEventListener('dragover', dragOverColumn);
		column.addEventListener('drop', dropColumn);
	});

	// 카드 컨테이너에 드래그 오버 및 드롭 이벤트 추가
	document.querySelectorAll('.kanban-items').forEach(itemsContainer => {
		itemsContainer.addEventListener('dragover', dragOver);
		itemsContainer.addEventListener('drop', drop);
	});
}

// 드래그 시작
function dragStart(event) {
	draggedCard = event.currentTarget;
	event.dataTransfer.effectAllowed = "move";
	event.dataTransfer.setData("text/plain", event.target.id);
	draggedCard.style.opacity = "0.5"; // 드래그 중 카드 투명도 조정
	draggedCard.style.display = ""; // 드래그 시 보이게 설정 (혹시 이전 설정이 남아있을 경우 대비)
	console.log("dragStart:", draggedCard);
}

// 드래그 종료
function dragEnd(event) {
	if (draggedCard) {
		draggedCard.style.opacity = "1"; // 드래그가 끝나면 카드 다시 보이기
		draggedCard.style.display = "block"; // 드래그 후 카드가 사라지지 않도록 display를 block으로 설정
		console.log("dragEnd - Card restored:", draggedCard);
		draggedCard = null; // draggedCard 초기화
	}
}
// 드래그 중인 상태
function dragOver(event) {
	event.preventDefault();
	const target = event.target.closest('.kanban-card') || event.target.closest('.kanban-items');
	console.log("dragOver - Target:", target);

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
		const bounding = target.getBoundingClientRect();
		const offset = event.clientY - bounding.top;

		if (target.classList.contains("kanban-card")) {
			if (offset < bounding.height / 2) {
				target.parentNode.insertBefore(draggedCard, target);
			} else {
				target.parentNode.insertBefore(draggedCard, target.nextSibling);
			}
		} else if (target.classList.contains("kanban-items")) {
			target.appendChild(draggedCard);
		}

		saveCardOrder(target.closest('.kanban-items')); // 이동 후 카드 순서 업데이트 함수 호출
		draggedCard.style.opacity = "1";
		draggedCard.style.display = "block"; // 카드가 사라지지 않도록 display를 block으로 설정
		draggedCard = null;
	}
}

// 카드 순서 업데이트 함수
function saveCardOrder(itemsContainer) {
	const cards = itemsContainer.querySelectorAll('.kanban-card');
	const updatedOrder = [];
	const colIdx = itemsContainer.closest('.kanban-column').getAttribute('id').replace('Day', '');

	cards.forEach((card, index) => {
		const cardIdx = card.getAttribute('data-id');
		updatedOrder.push({
			card_idx: parseInt(cardIdx),
			col_idx: parseInt(colIdx),
			card_order: index + 1
		});
	});

	// 서버에 카드 순서 업데이트 요청
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		data: JSON.stringify({
			action: 'updateCardOrder',
			cards: updatedOrder
		}),
		contentType: 'application/json; charset=UTF-8',
		success: function(response) {
			console.log('카드 순서가 성공적으로 업데이트되었습니다:', response);
		},
		error: function(xhr, status, error) {
			console.error('카드 순서를 업데이트하는 중 오류가 발생했습니다:', error);
		}
	});
}


// 드래그 시작 시 컬럼 이벤트 수정
function dragStartColumn(event) {
	draggedColumn = event.target;
	if (event.dataTransfer) {
		event.dataTransfer.effectAllowed = 'move';
	}
	setTimeout(() => {
		if (draggedColumn) {
			draggedColumn.style.display = 'none'; // 드래그 시 열 숨기기
		}
	}, 0);
}
// 드래그 종료 시 컬럼 이벤트 수정
function dragEndColumn(event) {
	setTimeout(() => {
		if (draggedColumn) {
			draggedColumn.style.display = "block"; // 드래그 후 열 보이기
			updateDayNumbers(); // 이동 후 순서 업데이트
			saveColumnOrder(); // 순서 업데이트 함수 호출
		}
		draggedColumn = null;
	}, 0);
}

function dragOverColumn(event) {
	event.preventDefault();
}

function dropColumn(event) {
	event.preventDefault();
	if (draggedColumn) {
		const targetColumn = event.target.closest('.kanban-column');
		if (targetColumn) {
			const bounding = targetColumn.getBoundingClientRect();
			const offset = event.clientX - bounding.left;
			if (offset > bounding.width / 2) {
				targetColumn.parentNode.insertBefore(draggedColumn, targetColumn.nextSibling);
			} else {
				targetColumn.parentNode.insertBefore(draggedColumn, targetColumn);
			}
		}
		draggedColumn.style.display = "block"; // 드래그 후 열 보이기
		updateDayNumbers(); // 이동 후 순서 업데이트
		saveColumnOrder(); // 순서 업데이트 함수 호출
		draggedColumn = null;
	}
}

// 컬럼 순서 업데이트 함수
function saveColumnOrder() {
	const columns = document.querySelectorAll('.kanban-column');
	const updatedOrder = [];

	columns.forEach((column, index) => {
		const colIdx = column.getAttribute('id').replace('Day', '');
		updatedOrder.push({
			col_idx: parseInt(colIdx),
			col_order: index + 1
		});
	});

	// 서버에 컬럼 순서 업데이트 요청
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		data: JSON.stringify({
			action: 'updateColumnOrder',
			columns: updatedOrder
		}),
		contentType: 'application/json; charset=UTF-8',
		dataType: 'json',
		success: function(response) {
			console.log('컬럼 순서가 성공적으로 업데이트되었습니다:', response);
		},
		error: function(xhr, status, error) {
			console.error('컬럼 순서를 업데이트하는 중 오류가 발생했습니다:', error);
		}
	});
}