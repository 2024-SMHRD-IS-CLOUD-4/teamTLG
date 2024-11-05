const state = {
    likeCount: 0,
    liked: false,
    comments: []
};

function toggleLike() {
    state.liked = !state.liked;
    state.likeCount += state.liked ? 1 : -1;
    renderLike();
}

function renderLike() {
    const likeCountDisplay = document.querySelector(".like-count");
    const likeButton = document.querySelector(".like-button");
    
    likeCountDisplay.textContent = state.likeCount;
    likeButton.style.color = state.liked ? "#ff3b3b" : "#ff6b6b";
}

function submitComment() {
    const commentInput = document.querySelector(".comment-input");
    const commentText = commentInput.value.trim();
    
    if (commentText) {
        state.comments.push(commentText);
        renderComments();
        
        commentInput.value = "";
        document.querySelector(".comment-counter").textContent = "0/150";
    } else {
        alert("댓글을 입력하세요.");
    }
}

function renderComments() {
    const commentList = document.querySelector(".comment-list");
    const commentCountDisplay = document.querySelector(".comment-count");
    
    commentList.innerHTML = ""; 
    state.comments.forEach(comment => {
        const commentElement = document.createElement("div");
        commentElement.className = "comment";
        commentElement.textContent = comment;
        commentList.appendChild(commentElement);
    });
    
    commentCountDisplay.textContent = state.comments.length;
}

document.addEventListener("DOMContentLoaded", () => {
    renderLike();
    renderComments();
});

const commentInput = document.querySelector(".comment-input");
const commentCounter = document.querySelector(".comment-counter");

commentInput.addEventListener("input", () => {
    commentCounter.textContent = `${commentInput.value.length}/150`;
});
