document
    .getElementById('index-button-showEditPage')
    .addEventListener('click', () => {
        window.location.href = 'pages/editor.html';
    });

document.getElementById('index-input-bookYearSearch').max = new Date().getFullYear();
document.getElementById('index-input-bookYearSearch').min = 0;

document
    .getElementById('index-form-filter')
    .addEventListener('submit', async function ( event ) {
        event.preventDefault();
        let keyWord = document.getElementById('index-input-bookTitleSearch').value;
        let year = document.getElementById('index-input-bookYearSearch').value;
        let forKidsYes = document.getElementById('index-input-bookKidsSearch-yes').checked;
        let forKidsNo = document.getElementById('index-input-bookKidsSearch-no').checked;
        let forKidsNull = document.getElementById('index-input-bookKidsSearch-null').checked;

        let forKids;
        if (forKidsNull) forKids = null;
        if (forKidsYes) forKids = true;
        if (forKidsNo) forKids = false;
        if(keyWord.includes(' ') || keyWord.includes('?') || keyWord.includes('&')) {
            alert('Keyword search may not contain any space, question mart or "&" character!');
        }else await updateCatalogList(keyWord, year, forKids);
    });