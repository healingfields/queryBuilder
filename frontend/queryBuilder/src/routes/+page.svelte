<script lang="js">
    import QueryBuilder from "$lib/components/QueryBuilder.svelte";
	import SchemaUploader from "$lib/components/SchemaUploader.svelte"; 
    import { globalState } from "$lib/globalState.svelte";
    $inspect(globalState, (globalState) => console.log(globalState))
    
</script>

{#if globalState.step == 'upload'}
    <SchemaUploader/>
{:else if globalState.step == 'generate'}
    <QueryBuilder/>
{/if}