	let dayCount = 0;
	let cardCounter = 0;
	let draggedColumn = null;
	let draggedCard = null;
	
	function createColumns(days) {
		const board = document.getElementById('kanban-board');
		board.innerHTML = '';
		dayCount = days;
	
		for (let i = 1; i <= dayCount; i++) {
			addDayColumn(i);
		}
	
		initializeDragAndDrop();
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
		deleteColumnButton.onclick = () => deleteColumn(column);
		headerContainer.appendChild(deleteColumnButton);
	
		column.appendChild(headerContainer);
	
		const editButton = document.createElement('button');
		editButton.className = 'edit-button';
		editButton.textContent = 'Edit';
		editButton.onclick = () => toggleEditMode(column, editButton);
		column.appendChild(editButton);
	
		const itemsContainer = document.createElement('div');
		itemsContainer.className = 'kanban-items';
		column.appendChild(itemsContainer);
	
		const addButton = document.createElement('button');
		addButton.className = 'add-card-button';
		addButton.textContent = "+ Add Card";
		addButton.onclick = () => addCard(column.id);
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
		textarea.addEventListener('input', autoResize);
	
		card.appendChild(textarea);
		column.appendChild(card);
		textarea.focus();
	
		// 블러 이벤트로 카드 제목 저장 후 세부 페이지로 이동
		textarea.addEventListener('blur', () => {
			if (textarea.value.trim() !== '') {
				card.textContent = textarea.value;
	
				// 카드의 개요를 로컬 저장소에 저장
				/*const scheduleData = {
					title: textarea.value,
					type: "",
					date: "",
					location: "",
					description: ""
				};
				localStorage.setItem(uniqueId, JSON.stringify(scheduleData));*/
	
				// 카드 클릭 시 세부 계획 페이지로 이동
				card.addEventListener('click', openDetailPage);
			} else {
				card.remove();
			}
		});
	
		textarea.addEventListener('keypress', (event) => {
			if (event.key === 'Enter') {
				event.preventDefault();
			}
		});
	
		card.setAttribute('draggable', 'true');
		card.addEventListener('dragstart', dragStart);
		card.addEventListener('dragend', dragEnd);
	}
	
	function openDetailPage(event) {
		const card = event.currentTarget;
		const uniqueId = card.dataset.id;
		const url = `kb_sub.jsp?id=${uniqueId}`;
		window.open(url, '_blank');
	}
	
	function deleteColumn(column) {
		if (confirm("해당 날짜를 지우시겠습니까? 해당 날의 일정이 전부 삭제됩니다!")) {
			column.remove();
			updateDayNumbers();
			initializeDragAndDrop();
		}
	}
	
	function addNewDay() {
		dayCount++;
		addDayColumn(dayCount);
		initializeDragAndDrop();
	}
	
	function updateDayNumbers() {
		const columns = document.querySelectorAll('.kanban-column');
		dayCount = columns.length;
	
		columns.forEach((column, index) => {
			const dayNumber = index + 1;
			const header = column.querySelector('h2');
			header.textContent = `Day ${dayNumber}`;
			column.id = `day-${dayNumber}`;
	
			const addButton = column.querySelector('.add-card-button');
			addButton.onclick = () => addCard(column.id);
		});
	}
	
	function toggleEditMode(column, editButton) {
		const isEditing = editButton.textContent === 'Edit';
		editButton.textContent = isEditing ? 'Save' : 'Edit';
	
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
				deleteButton.onclick = () => card.remove();
	
				card.appendChild(input);
				card.appendChild(deleteButton);
	
				card.removeEventListener('click', openDetailPage);
			} else {
				const input = card.querySelector('textarea');
				if (input && input.value.trim() !== '') {
					card.textContent = input.value;
					card.addEventListener('click', openDetailPage);
				}
			}
		});
	}
	
	function autoResize(event) {
		const textarea = event.target;
		textarea.style.height = 'auto';
		textarea.style.height = `${textarea.scrollHeight}px`;
	}
	
	function dragStart(event) {
		draggedCard = event.target;
		event.dataTransfer.setData("text/plain", event.target.textContent);
		setTimeout(() => {
			draggedCard.style.display = "none";
		}, 0);
	}
	function dragEnd(event) {
		setTimeout(() => {
			draggedCard.style.display = "block";
			draggedCard = null;
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
			draggedCard.style.display = "block";
			draggedCard = null;
		}
	}
	function dragStartColumn(event) {
		draggedColumn = event.target;
		event.dataTransfer.effectAllowed = 'move';
		setTimeout(() => {
			draggedColumn.style.display = 'none';
		}, 0);
	}
	function dragEndColumn(event) {
		setTimeout(() => {
			draggedColumn.style.display = "block";
			draggedColumn = null;
			updateDayNumbers();
		}, 0); 1
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
	
	let currentPage = 1;
	const commentsPerPage = 10;
	
	document.addEventListener("DOMContentLoaded", () => {
		loadComments();
		document.getElementById("add-comment").onclick = function() {
			addComment();
		};
	});
	
	function loadComments() {
		const comments = JSON.parse(localStorage.getItem("comments")) || [];
		const commentSection = document.getElementById('comments');
		commentSection.innerHTML = '';
	
		const sortedComments = [...comments].reverse();
		const paginatedComments = sortedComments.slice(
			(currentPage - 1) * commentsPerPage,
			currentPage * commentsPerPage
		);
	
		paginatedComments.forEach((comment, index) => {
			const commentDiv = document.createElement('div');
			commentDiv.className = 'comment';
	
			const contentSpan = document.createElement('span');
			contentSpan.textContent = `${comment.user}: ${comment.content}`;
	
			const likeButton = document.createElement('button');
			likeButton.innerHTML = `❤️ ${comment.likes || 0}`;
			likeButton.className = 'like-button';
			likeButton.style.width = "50px";
			likeButton.onclick = () => toggleLike(index + (currentPage - 1) * commentsPerPage);
	
			commentDiv.appendChild(contentSpan);
			commentDiv.appendChild(likeButton);
			commentSection.appendChild(commentDiv);
		});
	
		displayPagination(comments.length);
	}
	
	function displayPagination(totalComments) {
		const pagination = document.getElementById("pagination");
		pagination.innerHTML = '';
	
		const totalPages = Math.ceil(totalComments / commentsPerPage);
	
		for (let i = 1; i <= totalPages; i++) {
			const pageButton = document.createElement("button");
			pageButton.textContent = i;
			pageButton.className = i === currentPage ? "active-page" : "";
			pageButton.onclick = () => {
				currentPage = i;
				loadComments();
			};
			pagination.appendChild(pageButton);
		}
	}
	
	// 댓글부분 js
	function addComment() {
		const content = document.getElementById('comment-content').value;
		const user = "사용자";
	
		if (content.trim()) {
			/*const comments = JSON.parse(localStorage.getItem("comments")) || [];*/
			comments.unshift({ user, content, likes: 0 });
/*			localStorage.setItem("comments", JSON.stringify(comments));
*/	
			document.getElementById('comment-content').value = '';
			currentPage = 1;
			loadComments();
		} else {
			alert("댓글 내용을 입력하세요!");
		}
	}
	
	
	function toggleLike(index) {
		let comments = JSON.parse(localStorage.getItem("comments")) || [];
		const likedComments = JSON.parse(localStorage.getItem("likedComments")) || {};
	
		const originalIndex = comments.length - 1 - index;
	
		if (comments[originalIndex]) {
			if (likedComments[originalIndex]) {
				comments[originalIndex].likes = Math.max(0, (comments[originalIndex].likes || 1) - 1);
				delete likedComments[originalIndex];
			} else {
				comments[originalIndex].likes = (comments[originalIndex].likes || 0) + 1;
				likedComments[originalIndex] = true;
			}
	
			localStorage.setItem("comments", JSON.stringify(comments));
			localStorage.setItem("likedComments", JSON.stringify(likedComments));
	
			loadComments();
		} else {
			console.error("해당 인덱스에 댓글이 없습니다.");
		}
	}
	
	function localDelete(){
		window.localStorage.clear();
	}
	
	document.getElementById('local').addEventListener('click',localDelete);
	
	const days = parseInt(prompt("며칠동안 여행하시나요?"), 10);
	if (days > 0) {
		createColumns(days);
	}
	1