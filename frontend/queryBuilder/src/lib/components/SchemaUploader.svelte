<script lang="js">
    import { globalState } from "$lib/globalState.svelte";
	import { getTables, uploadSchema } from "../../api";

    let databaseName = $state(null);
    let error = $state('');
    
    async function handleSchemaUpload(event){
        const file = event.target.files[0]
        if(file){
            const res = await uploadSchema(file);
            databaseName = res.data.name;
            const resTab = await getTables();
        if(resTab){
            globalState.step = 'generate';
            globalState.tables = resTab;
        }
        }else{
            error = "error uploading the file";
        }
    }
</script>

<div>
    <h1>Upload database Schema</h1>
    <div>
        <label>Upload Schema SQL file</label>
        <input type="file"  accept=".sql" on:change={handleSchemaUpload} />
    </div>
    {#if databaseName}
        <h2>Database: {databaseName}</h2>
    {/if}
    {#if error}
        <div class="error">{error}</div>
    {/if}
</div>