const BASE_URL = "http://localhost:8080";

export async function getTables(){
    const res = await fetch(`${BASE_URL}/tables`);
    return res.json();
}

export async function getTableColumns(table){
    const res = await fetch(`${BASE_URL}/columns?table=${table}`);
    return res.json();
}

export async function executeQuery(query){
    const res = await fetch(`${BASE_URL}/query`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(query)
    });
    return res.json();
}