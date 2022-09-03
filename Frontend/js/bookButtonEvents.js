const params = new URLSearchParams(window.location.search);
const isbn = params.get("isbn");
showBookInfo();
showComments();
addEventListenersForBookPage();

function addEventListenersForBookPage () {
    document
        .getElementById('book-form-addReview')
        .addEventListener('submit', async function ( event ) {
            event.preventDefault();
            if(document.getElementById('book-input-reviewNickname').value.includes(' ')) {
                alert('Reviewer nickname cannot contain spaces!');
            }else {
                let review = {}
                const inputs = document.getElementById('book-form-addReview').querySelectorAll('input[type=text], input[type=radio]:checked');
                for (const input of inputs){
                    review[input.name] = input.value;
                }

                review['book'] = { 'isbn' : isbn }

                let response = await postNewReview(review);
                if(response.status < 200 || response.status > 299) {
                    alert(response.message);
                }else {
                    alert('New review successfully added!');
                    showComments();
                }
                event.target.reset();
            }
        });

    document
        .getElementById('book-button-ShowRepoPage')
        .addEventListener('click', () => {
            window.location.href = '../index.html';
        });
}

async function postNewReview(review){
    try {
        const response = await fetch('http://localhost:8080/api/v1/reviews', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(review)
        })

        return await response.json()
    } catch (e) {
        alert (e)
    }
}

async function showBookInfo() {
    try {
        const response = await fetch('http://localhost:8080/api/v1/books/' + isbn);
        const responseJson = await response.json();

        let isForKidsText = 'This book contains text that is safe for kids to read. The content of the book can be easily understood by children.';
        let isNotForKidsText = 'This book should not be read by kids! The context of this book is not safe for children!';

        document.getElementById('bookInfo-title').innerText = responseJson.title;
        document.getElementById('bookInfo-isbn').innerText = responseJson.isbn;
        document.getElementById('bookInfo-year').innerText = responseJson.publishYear;
        document.getElementById('bookInfo-notes').innerText = responseJson.forKids ? isForKidsText : isNotForKidsText;

    } catch (e) {
        alert(e);
    }
}

async function showComments(){
    let commentTable = document.getElementById("book-table-reviews");
    try {
        const response = await fetch("http://localhost:8080/api/v1/reviews?isbn=" + isbn);
        const responseJson = await response.json();

        commentTable.innerHTML = "";

        for(const item of responseJson){
            const tr = document.createElement("tr");

            const tdNickname = document.createElement("td");
            const tdStars = document.createElement("td");
            const tdComment = document.createElement("td");

            tdNickname.innerText = item.nickname;
            for(let i = 0; i < item.starsGiven; i++) {
                tdStars.innerText += '* ';
            }
            tdComment.innerText = item.comment;

            tr.append(tdNickname, tdStars, tdComment);

            commentTable.append(tr)
        }

    } catch (e) {
        alert(e)
    }
}