updateBookList();
updateAuthorList();
addEventListenersForEditorPage();

function addEventListenersForEditorPage(){
    document
    .getElementById('editor-button-ShowRepoPage')
    .addEventListener('click', () => {
        window.location.href = '../index.html';
    });

    document.getElementById('editor-input-option-deleteBook').addEventListener('click', () => {
        document.getElementById('editor-section-deleteArea-author').style.visibility = 'hidden';
        document.getElementById('editor-section-deleteArea-book').style.visibility = 'visible';
        document.getElementById('editor-input-submit-delete').value = 'Delete book!';
        updateBookList();
    });

    document.getElementById('editor-input-option-deleteAuthor').addEventListener('click', () => {
        document.getElementById('editor-section-deleteArea-author').style.visibility = 'visible';
        document.getElementById('editor-section-deleteArea-book').style.visibility = 'hidden';
        document.getElementById('editor-input-submit-delete').value = 'Delete author!';
        updateAuthorList();
    });

    document.getElementById('editor-input-add-year').max = new Date().getFullYear();
    document.getElementById('editor-input-add-year').min = 0;
    document.getElementById('editor-input-add-isbn').min = 1;
    document.getElementById('editor-input-editBook-year').max = new Date().getFullYear();
    document.getElementById('editor-input-editBook-year').min = 0;

    document
        .getElementById('editor-form-addNewAuthor')
        .addEventListener('submit', async function ( event ) {
            event.preventDefault();
            let author = {}
            const inputs = document.getElementById('editor-form-addNewAuthor').querySelectorAll('input[type=text], input[type=radio]:checked');
            for (const input of inputs){
                author[input.name] = input.value;
            }

            let response = await postNewAuthor(author);
            if(response.status < 200 || response.status > 299) {
                alert(response.message);
            }else {
                alert('New author successfully added!');
                await updateAuthorList();
            }
            
            event.target.reset();
    });

    document
        .getElementById('editor-from-addNewBook')
        .addEventListener('submit', async function ( event ) {
            event.preventDefault();
            let book = {}
            const inputs = document.getElementById('editor-from-addNewBook').querySelectorAll('input[type=text], input[type=number], input[type=radio]:checked');
            for (const input of inputs){
                book[input.name] = input.value;
            }

            let authorSelector = document.getElementById('editor-selector-addAuthor-author');
            book['author'] = { 'id' : authorSelector.options[authorSelector.selectedIndex].value}

            let response = await postNewBook(book);
            if(response.status < 200 || response.status > 299) {
                alert(response.message);
            }else{
                await updateBookList();
                alert('New book successfully added!');
            }
            event.target.reset();
    });

    document
        .getElementById('editor-form-editExistingBook')
        .addEventListener('submit', async function ( event ) {
            event.preventDefault();
            let book = {}
            const inputs = document.getElementById('editor-form-editExistingBook').querySelectorAll('input[type=text], input[type=number], input[type=radio]:checked');
            for (const input of inputs){
                book[input.name] = input.value;
            }

            let isbnSelector = document.getElementById('editor-selector-editBook-books');
            book['isbn'] = isbnSelector.options[isbnSelector.selectedIndex].value;

            let response = await putBook(book);

            if(response.status < 200 || response.status > 299) {
                alert(response.message);
            } else {
                await updateBookList();
                alert('New book successfully updated!');
            }
            event.target.reset();
    });

    document
        .getElementById('editor-form-delete')
        .addEventListener('submit', async function ( event ) {
            event.preventDefault();
            
            if (document.getElementById('editor-input-option-deleteAuthor').checked) {
                let authorSelector = document.getElementById('editor-selector-deleteAuthor');
                let authID = authorSelector.options[authorSelector.selectedIndex].value;
        
                await deleteExistingAuthor(authID);
  
                alert('Author was successfully deleted!');

                event.target.reset();
            }else {
                let bookSelector = document.getElementById('editor-selector-deleteBook');
                let isbn = bookSelector.options[bookSelector.selectedIndex].value;
        
                await deleteExistingBook(isbn);

                alert('Book was successfully deleted!');
                event.target.reset();
                document.getElementById('editor-input-option-deleteAuthor').checked = false;
                document.getElementById('editor-input-option-deleteBook').checked = true;
            }

            await updateAuthorList();
            await updateBookList();
    });
}

async function postNewAuthor(author){
    try {
        const response = await fetch('http://localhost:8080/api/v1/authors', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(author)
        })

        return await response.json()
    } catch (e) {
        alert (e)
    }
}

async function deleteExistingAuthor(authorID){
    try {
        await fetch('http://localhost:8080/api/v1/authors/' + authorID, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })

    } catch (e) {
        alert (e)
    }
}

async function deleteExistingBook(isbn){
    try {
        await fetch('http://localhost:8080/api/v1/books/' + isbn, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })

    } catch (e) {
        alert (e)
    }
}

async function postNewBook(book){
    try {
        const response = await fetch('http://localhost:8080/api/v1/books', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        })

        return await response.json();
    } catch (e) {
        alert (e)
    }
}

async function putBook(book){
    try {
        const response = await fetch('http://localhost:8080/api/v1/books/' + book.isbn, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        })

        return await response.json();
    } catch (e) {
        alert (e)
    }
}

async function updateAuthorList(){
    try {
        let authorList = document.getElementById('editor-selector-addAuthor-author');
        let authorDeleteList = document.getElementById('editor-selector-deleteAuthor');
        authorList.innerHTML = '';
        authorDeleteList.innerHTML = '';
        const response = await fetch('http://localhost:8080/api/v1/authors');
        const responseJson = await response.json();

        if(responseJson == '') {
            document.getElementById('editor-input-submit-newBook').disabled = true;
            document.getElementById('editor-input-submit-newBook').title = 'Please add an author first!';
            document.getElementById('editor-input-submit-delete').disabled = true;
            document.getElementById('editor-input-submit-delete').title = 'Please add an author first!';
            document.getElementById('editor-selector-deleteAuthor').disabled = true;
            document.getElementById('editor-selector-deleteAuthor').title = 'Please add an author first!';
            document.getElementById('editor-selector-addAuthor-author').disabled = true;
            document.getElementById('editor-selector-addAuthor-author').title = 'Please add an author first!';
        }else {
            document.getElementById('editor-input-submit-newBook').disabled = false;
            document.getElementById('editor-input-submit-newBook').title = '';
            document.getElementById('editor-input-submit-delete').disabled = false;
            document.getElementById('editor-input-submit-delete').title = '';
            document.getElementById('editor-selector-deleteAuthor').disabled = false;
            document.getElementById('editor-selector-deleteAuthor').title = '';
            document.getElementById('editor-selector-addAuthor-author').disabled = false;
            document.getElementById('editor-selector-addAuthor-author').title = '';
            for(const item of responseJson){
                const option =  document.createElement('option');
                const option2 =  document.createElement('option');
                option.value = item.id;
                option.innerText = item.name;

                option2.value = item.id;
                option2.innerText = item.name;
    
                authorList.append(option);
                authorDeleteList.append(option2);
            }
        }

    } catch (e) {
        alert(e);
    }
}

async function updateBookList(){
    try {
        let bookList = document.getElementById('editor-selector-deleteBook');
        bookList.innerHTML = '';
        let bookEditList = document.getElementById('editor-selector-editBook-books');
        bookEditList.innerHTML = '';
        const response = await fetch('http://localhost:8080/api/v1/books');
        const responseJson = await response.json();

        if (responseJson == '') {
            document.getElementById('editor-selector-deleteBook').disabled = true;
            document.getElementById('editor-selector-deleteBook').title = 'Please add a book first!';
            document.getElementById('editor-input-submit-delete').disabled = true;
            document.getElementById('editor-input-submit-delete').title = 'Please add a book first!';
            document.getElementById('editor-selector-editBook-books').disabled = true;
            document.getElementById('editor-selector-editBook-books').title = 'Please add a book first!';
            document.getElementById('editor-input-submit-editBook').disabled = true;
            document.getElementById('editor-input-submit-editBook').title = 'Please add a book first!';
        }else {
            document.getElementById('editor-selector-deleteBook').disabled = false;
            document.getElementById('editor-selector-deleteBook').title = '';
            document.getElementById('editor-input-submit-delete').disabled = false;
            document.getElementById('editor-input-submit-delete').title = '';
            document.getElementById('editor-selector-editBook-books').disabled = false;
            document.getElementById('editor-selector-editBook-books').title = '';
            document.getElementById('editor-input-submit-editBook').disabled = false;
            document.getElementById('editor-input-submit-editBook').title = '';
            for(const item of responseJson){
                const option =  document.createElement('option');
                option.value = item.isbn;
                option.innerText = item.title;
    
                bookList.append(option);

                const option2 =  document.createElement('option');
                option2.value = item.isbn;
                option2.innerText = item.title;
    
                bookEditList.append(option2);
            }
        }

    } catch (e) {
        alert(e);
    }
}