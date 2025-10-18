const BASE_URL = "http://localhost:8080";

export async function getTables(){
    const res = await fetch(`${BASE_URL}/tables`);
    return res.json();
}

export async function getTableColumns(table){
    const res = await fetch(`${BASE_URL}/columns?tableName=${table}`);
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

export async function uploadSchema(file){
    const formData = new FormData();
    formData.append('file', file);
    const res = await fetch(`${BASE_URL}/schema/upload-sql`, {
        method: 'POST',
        body: formData 
    });
    if (!res.ok) {
        throw new Error('Failed to upload schema');
    }
    const data = await res.json();
    if(!data){
        throw new Error('No data returned from server');
    }
    return {
        success: true,
        data,
    };
}