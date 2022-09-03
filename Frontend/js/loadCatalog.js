updateCatalogList('', '', null);

async function updateCatalogList(keyWord, year, forKids){
    try {
        let query = '';

        if(keyWord !== '' || year !== '' || forKids != null) query += '?';

        if(keyWord !== '') query += (query.slice(-1) === '?' ? ('title=' + keyWord) : ('&title=' + keyWord));
        if(year !== '') query += (query.slice(-1) === '?' ? ('publishYear=' + year) : ('&publishYear=' + year));
        if(forKids != null) query += (query.slice(-1) === '?' ? ('forKids=' + forKids) : ('&forKids=' + forKids));

        let catalogList = document.getElementById('index-section-catalog');
        catalogList.innerText = '';
        const response = await fetch('http://localhost:8080/api/v1/books' + query);
        const responseJson = await response.json();

        for(const item of responseJson){
            const book =  document.createElement('div');
            const image = document.createElement('img');
            const title = document.createElement('h1');
            const isbn = document.createElement('h3');
            const kids = document.createElement('p');
            const year = document.createElement('p');

            book.className = 'book';
            title.innerText = item.title;
            isbn.innerText = 'ISBN: ' + item.isbn;
            kids.innerText = item.forKids ? 'Readable by kids' : 'Should not be read by kids!';
            year.innerText = item.publishYear;
            year.className = 'book-publishYear';
            image.src = 'https://game-icons.net/icons/ffffff/transparent/1x1/willdabeast/white-book.png'; // <--- Image by Willdabeast, https://game-icons.net/1x1/willdabeast/white-book.html
            image.alt = 'Book Icon'

            book.append(image, title, isbn, kids, year);
            book.addEventListener('click', ()  => {
                window.location.href = 'pages/book.html?isbn=' + item.isbn;
            });

            catalogList.append(book);
        }

    } catch (e) {
        alert(e);
    }
}