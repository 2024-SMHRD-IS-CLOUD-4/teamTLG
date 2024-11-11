let dayCount = 0;
let cardCounter = 0;
let latestColIdx = null;
let isEditing = false;


$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const tr_idx = urlParams.get('tr_idx');

    if (tr_idx) {
        loadKanbanBoard(tr_idx); // Kanban 보드 로드
        loadComments(tr_idx); // 댓글 로드
    } else {
        console.error('tr_idx가 제공되지 않았습니다.');
    }

    // 기존의 add-card-button에 대한 클릭 이벤트 바인딩
    $(document).on('click', '.add-card-button', function() {
        const col_idx = $(this).closest('.kanban-column').attr('id').replace('Day', '');
        addNewCard(col_idx);
    });

    // + 버튼 클릭 시 새로운 컬럼 추가
    $('.add-day-button').off('click').on('click', addNewDay);

    // 컬럼을 클릭할 때 col_idx를 가져오는 이벤트 추가
    $(document).on('click', '.kanban-column', function() {
        const colIdx = $(this).data('col-idx'); // data-col-idx로부터 col_idx 가져오기
        console.log("클릭한 컬럼의 col_idx:", colIdx);
    });

    // 카드 클릭 이벤트 핸들러 추가
    $(document).on('click', '.kanban-card', function(event) {
        const column = $(this).closest('.kanban-column');
        const colIdx = column.data('col-idx');
        const cardId = $(this).data('id');

        // Edit 모드일 때는 클릭 방지
        if (isEditing) {
            console.log("Edit 모드에서는 카드 클릭이 차단됩니다.");
            event.stopPropagation(); // 클릭 이벤트 중지
            return;
        }

        console.log("Card clicked, isEditing:", isEditing); // 카드 클릭 시 isEditing 상태 확인

        // Edit 모드가 아닐 때만 상세 페이지로 이동
        openDetailPage(cardId, colIdx);
    });

    // 댓글 추가 버튼 클릭 시 댓글 추가 요청
    $('#add-comment').on('click', function() {
        const commentContent = $('#comment-content').val().trim(); // 댓글 입력 내용
        if (commentContent) {
            addComment(tr_idx, commentContent); // 댓글 추가 함수 호출
        } else {
            alert("댓글을 입력해 주세요.");
        }
    });
});


function loadKanbanBoard(tr_idx) {

	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'GET',
		data: { tr_idx: tr_idx },
		dataType: 'json',
		success: function(data) {
			console.log("로드된 데이터:", data); // 데이터를 확인하여 col_title이 정확히 들어오는지 확인
			const kanbanBoard = $('#kanban-board');
			kanbanBoard.empty();

			// 각 컬럼 데이터에 대해 DB에서 가져온 col_title을 사용하여 컬럼 추가
			data.forEach(day => {
				addDayColumn(day.col_idx, day.col_title); // col_title을 직접 전달
				day.cards.forEach(card => {
					addCard(day.col_idx, card.card_idx, card.card_title);
				});
			});

			initializeSortable();
		},
		error: function(xhr, status, error) {
			console.error('데이터를 로드하는 중 오류가 발생했습니다:', error);
		}
	});
}



// 컬럼 타이틀 업데이트 함수
function updateColumnTitle(col_idx, element) {
	const newTitle = element.textContent.trim();
	if (!newTitle) {
		alert("컬럼 제목을 입력해 주세요.");
		return;
	}

	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'updateColumnTitle',
			col_idx: col_idx,
			col_title: newTitle
		}),
		success: function(response) {
			console.log('컬럼 제목이 성공적으로 업데이트되었습니다:', response);
		},
		error: function(xhr, status, error) {
			console.error('컬럼 제목 업데이트 중 오류가 발생했습니다:', error);
		}
	});
}


function addDayColumn(col_idx, col_title) {
	const kanbanBoard = $('#kanban-board');
	const column = $(`
        <div class="kanban-column" id="Day${col_idx}" data-col-idx="${col_idx}">
            <h2 contenteditable="true" onblur="updateColumnTitle(${col_idx}, this)">${col_title}</h2>
            <button class="edit-button" onclick="toggleEditMode(${col_idx})">Edit</button>
            <button class="delete-column-button">Delete Column</button>
            <div class="kanban-items" id="cards-${col_idx}"></div>
            <button class="add-card-button">+ Add Card</button>
        </div>
    `);

	// 컬럼 삭제 버튼 이벤트 바인딩
	column.find('.delete-column-button').on('click', function() {
		const confirmed = confirm('이 컬럼과 내부의 모든 카드를 삭제하시겠습니까?');
		if (confirmed) {
			deleteColumn(col_idx);
		}
	});

	kanbanBoard.append(column);
}


// 컬럼 삭제 함수
function deleteColumn(col_idx) {
	if (!col_idx) {
		console.error('삭제할 컬럼 ID가 유효하지 않습니다:', col_idx);
		return;
	}

	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'deleteColumn',
			col_idx: col_idx
		}),
		success: function(response) {
			console.log('컬럼이 성공적으로 삭제되었습니다:', response);
			$(`#Day${col_idx}`).remove();
		},
		error: function(xhr, status, error) {
			console.error('컬럼을 삭제하는 중 오류가 발생했습니다:', error);
		}
	});
}

function addNewCard(col_idx) {
	const card_title = prompt("카드 제목을 입력하세요:");
	if (card_title) {
		const card_order = $(`#cards-${col_idx}`).children().length + 1;

		// 서버로 카드 추가 요청
		$.ajax({
			url: '/TravelMate/KanbanController',
			type: 'POST',
			contentType: 'application/json; charset=UTF-8',
			data: JSON.stringify({
				action: 'createCard',
				col_idx: col_idx,
				card_title: card_title,
				card_order: card_order
			}),
			success: function(response) {
				if (response && response.card_idx) {
					// 응답 받은 card_idx로 클라이언트에 카드 추가
					addCard(col_idx, response.card_idx, card_title, card_order);
				} else {
					console.error("카드 추가 실패: 서버에서 card_idx를 받지 못했습니다.");
				}
			},
			error: function(xhr, status, error) {
				console.error("카드를 추가하는 중 오류가 발생했습니다:", error);
			}
		});
	}
}

// 카드 타이틀 업데이트 함수
function updateCardTitle(card_idx, newTitle) {
    if (!newTitle || typeof newTitle !== 'string') {
        console.error("업데이트할 카드 제목이 유효하지 않습니다:", newTitle);
        return;
    }

    // 임시 카드 ID인 경우 서버로 전송하지 않습니다.
    if (typeof card_idx === 'string' && card_idx.startsWith('temp-')) {
        console.log("임시 카드 ID이므로 서버에 전송하지 않습니다:", card_idx);
        return;
    }

    // "Delete" 텍스트가 포함되지 않도록 처리
    const sanitizedTitle = newTitle.replace('Delete', '').trim(); 

    $.ajax({
        url: '/TravelMate/KanbanController',
        type: 'POST',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            action: 'updateCardTitle',
            card_idx: card_idx,
            card_title: sanitizedTitle
        }),
        success: function(response) {
            console.log('카드 제목이 성공적으로 업데이트되었습니다:', response);
        },
        error: function(xhr, status, error) {
            console.error('카드 제목을 업데이트하는 중 오류가 발생했습니다:', error);
        }
    });
}

function addCard(col_idx, card_idx = null, card_title = "", card_order = null) {
    if (!card_title) return;

    // 카드 요소 생성
    const card = $('<div>').addClass('kanban-card')
        .attr('data-id', card_idx || `temp-${Date.now()}`)
        .attr('data-order', card_order);

    // 카드 제목 요소 추가 (여기서만 contenteditable을 적용)
    const cardTitle = $('<div>').addClass('card-title')
        .text(card_title)
        .attr('contenteditable', 'true'); // 제목만 수정 가능하게 함

    // 삭제 버튼 추가 (수정 불가능하도록)
    const deleteButton = $('<button>').addClass('delete-card-button').text('Delete')
        .attr('contenteditable', 'false'); // 버튼은 수정 불가능하게 함

    deleteButton.on('click', function(event) {
        event.stopPropagation(); // 카드 클릭 이벤트와 충돌하지 않도록 클릭 이벤트 중지
        const confirmed = confirm('이 카드를 삭제하시겠습니까?');
        if (confirmed) {
            deleteCard($(this).closest('.kanban-card').data('id'));
        }
    });

    // 카드 클릭 이벤트 처리
    card.on('click', function(event) {
        const column = $(this).closest('.kanban-column');
        const isEditing = column.data('editing'); // Edit 모드 확인
        console.log("Card clicked, isEditing:", isEditing); // 디버깅 로그

        if (isEditing) {
            // Edit 모드일 때만 제목 수정 가능
            cardTitle.attr('contenteditable', 'true').focus();
            event.stopPropagation(); // 다른 클릭 이벤트와 충돌 방지
        } else {
            // Edit 모드가 아닐 때만 상세 페이지로 이동
            openDetailPage($(this).data('id'), col_idx);
        }
    });

    // 카드에 제목과 삭제 버튼 추가
    card.append(cardTitle).append(deleteButton);

    // 카드 추가
    $(`#cards-${col_idx}`).append(card);
}




// 카드 삭제 함수
function deleteCard(card_idx) {
	if (!card_idx) {
		console.error('삭제할 카드 ID가 유효하지 않습니다:', card_idx);
		return;
	}

	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'deleteCard',
			card_idx: card_idx
		}),
		success: function(response) {
			console.log('카드가 성공적으로 삭제되었습니다:', response);
			$(`[data-id='${card_idx}']`).remove();
		},
		error: function(xhr, status, error) {
			console.error('카드를 삭제하는 중 오류가 발생했습니다:', error);
		}
	});
}



function addNewDay() {
	const col_title = "Holiday"; // 기본 이름을 Holiday로 설정

	// 서버에 새로운 컬럼 추가 요청
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'createColumn',
			tr_idx: tr_idx,
			col_title: col_title,
			col_order: $('.kanban-column').length + 1
		}),
		success: function(response) {
			if (response && response.col_idx) {
				addDayColumn(response.col_idx, col_title); // 생성된 컬럼을 화면에 추가
				initializeSortable();
			} else {
				console.error("컬럼 추가 실패: 서버에서 col_idx를 받지 못했습니다.");
			}
		},
		error: function(xhr, status, error) {
			console.error("새로운 컬럼을 추가하는 중 오류가 발생했습니다:", error);
		}
	});
}



function initializeSortable() {
	// 컬럼 이동을 위한 sortable 적용
	$('#kanban-board').sortable({
		handle: 'h2',
		cursor: 'move', // 드래그할 때 커서 모양을 'move'로 설정
		update: function(event, ui) {
			const newOrder = $('#kanban-board .kanban-column').map((index, col) => ({
				col_idx: $(col).attr('id').replace('Day', ''),
				col_order: index + 1
			})).get();

			saveColumnOrder(newOrder); // 컬럼 순서를 서버에 저장
		}
	});

	// 카드 이동을 위한 sortable 적용
	$('.kanban-items').sortable({
		connectWith: '.kanban-items',
		cursor: 'move', // 드래그할 때 커서 모양을 'move'로 설정
		update: function(event, ui) {
			const col_idx = $(this).closest('.kanban-column').attr('id').replace('Day', '');
			const newCardOrder = $(this).children('.kanban-card').map((index, card) => ({
				card_idx: $(card).data('id'),
				card_order: index + 1,
				col_idx: col_idx
			})).get();

			saveCardOrder(newCardOrder); // 카드 순서를 서버에 저장
		}
	});
}


function saveColumnOrder(columns) {
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'updateColumnOrder',
			columns: columns
		}),
		success: function(response) {
			console.log('컬럼 순서가 성공적으로 업데이트되었습니다:', response);
		},
		error: function(xhr, status, error) {
			console.error('컬럼 순서를 업데이트하는 중 오류가 발생했습니다:', error);
		}
	});
}

function saveCardOrder(cards) {
	$.ajax({
		url: '/TravelMate/KanbanController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'updateCardOrder',
			cards: cards
		}),
		success: function(response) {
			console.log('카드 순서가 성공적으로 업데이트되었습니다:', response);
		},
		error: function(xhr, status, error) {
			console.error('카드 순서를 업데이트하는 중 오류가 발생했습니다:', error);
		}
	});
}
// Edit 모드 토글 기능
function toggleEditMode(col_idx) {
    const column = $(`#Day${col_idx}`);

    // Edit 모드 상태 토글
    isEditing = !isEditing;

    console.log("Edit 모드 상태:", isEditing);

    // 각 카드의 제목을 편집 가능하도록 설정
    column.find('.kanban-card').each(function() {
        const card = $(this);
        if (isEditing) {
            // Edit 모드 활성화
            card.attr('contenteditable', 'true').focus();
        } else {
            // Save 모드 활성화 (Edit 모드 비활성화)
            const newTitle = card.text().trim();
            card.attr('contenteditable', 'false');
            if (newTitle && card.data('id')) {
                updateCardTitle(card.data('id'), newTitle);
            }
        }
    });

    // 컬럼 제목도 편집 가능하도록 설정
    const columnTitle = column.find('h2');
    if (isEditing) {
        columnTitle.attr('contenteditable', 'true').focus();
    } else {
        const newColTitle = columnTitle.text().trim();
        columnTitle.attr('contenteditable', 'false');
        if (newColTitle) {
            updateColumnTitle(col_idx, columnTitle[0]);
        }
    }

    // Edit 모드 상태 토글
    column.data('editing', isEditing);
    column.find('.edit-button').text(isEditing ? 'Save' : 'Edit');
}

function openDetailPage(cardId, col_idx) {
	const url = `kb_sub.jsp?id=${cardId}&col_idx=${col_idx}`;
	const options = "width=800,height=1000,top=100,left=200";
	window.open(url, '_blank', options);
}

function updateDayNumbers() {
	$('.kanban-column').each((index, column) => {
		$(column).find('h2').text(`Day ${index + 1}`);
	});
}


//---------------------- 댓글 구현 --------------------------//

// 댓글 불러오기 함수
// 댓글 불러오기 함수
function loadComments(tr_idx) {
	console.log('loadComments 호출 시 tr_idx : ' , tr_idx);
	$.ajax({
		url: '/TravelMate/CommentController',
		type: 'GET',
		data: { tr_idx: tr_idx },
		dataType: 'json',
		success: function(comments) {
			console.log("받은 댓글 데이터:", comments);
			$('#comments').empty(); // 기존 댓글 제거
			comments.forEach(comment => {
				addCommentToDOM(comment); // 댓글을 DOM에 추가
			});
		},
		error: function(xhr, status, error) {
			console.error('댓글을 불러오는 중 오류가 발생했습니다:', error);
		}
	});
}

// 댓글 DOM에 추가하는 함수
function addCommentToDOM(comment) {
	const commentElement = $(` 
        <div class="comment" data-id="${comment.comment_idx}">
            <p>${comment.cmt_content}</p>
            <button class="delete-comment-button">삭제</button>
        </div>
    `);

	// 댓글 삭제 버튼 이벤트
	commentElement.find('.delete-comment-button').on('click', function() {
		const confirmed = confirm('이 댓글을 삭제하시겠습니까?');
		if (confirmed) {
			deleteComment(comment.comment_idx);
		}
	});
	console.log('댓글 dom에 추가 할 데이터 : ', comment)
	$('#comments').append(commentElement);
}


// 댓글 추가 함수
function addComment(tr_idx, cmt_content) {
	console.log("전송할 데이터:", {
		action: 'addComment',
		tr_idx: tr_idx,
		cmt_content: cmt_content
	});

	$.ajax({
		url: '/TravelMate/CommentController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'addComment',
			tr_idx: tr_idx,
			cmt_content: cmt_content
		}),

		success: function(response) {
			console.log("댓글이 성공적으로 추가되었습니다:", response);
			$('#comment-content').val(''); // 댓글 입력 창 비우기
			loadComments(tr_idx); // 댓글 목록 다시 불러오기
		},
		error: function(xhr, status, error) {
			if (xhr.status === 401) {
				alert("로그인이 필요합니다. 로그인 후 다시 시도해 주세요.");
				// 여기서 로그인 페이지로 리다이렉트하거나 로그인 모달을 표시할 수 있어
			} else {
				console.error("댓글 추가 중 오류가 발생했습니다:", error);
			}
		}
	});
}

// 댓글 삭제 함수
function deleteComment(comment_idx) {
	$.ajax({
		url: '/TravelMate/CommentController',
		type: 'POST',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			action: 'deleteComment',
			comment_idx: comment_idx
		}),
		success: function(response) {
			$(`.comment[data-id='${comment_idx}']`).remove();
		},
		error: function(xhr, status, error) {
			console.error('댓글을 삭제하는 중 오류가 발생했습니다:', error);
		}
	});
}