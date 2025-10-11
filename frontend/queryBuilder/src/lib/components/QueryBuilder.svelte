<script lang="js">
	import { onMount } from "svelte";
	import { getTableColumns, getTables } from "../../api";

    let tables = $state([]);
    let selectedTable= $state('');
    let columns = $state([]);
    let selectedColumns = $state([]);
    let filters = $state([])
    let selectedFilters = $state([]);
    const results = $state([]);


    const operators = [['>', '<', '=', 'like', '!=']];

    onMount(async () => {
        tables = await getTables();
    });

    async function handleTableChange(){
        columns = await getTableColumns(selectedTable);
        selectedColumns = [];
    }
    function removeFilter(index) {
        filters =  filters = filters.filter((_, i) => i !== index);
    }
    
    function addFilter(){
        filters.push({column: '', operator: '', value: ''});
    }
</script>


<h1>Database : Test DB</h1>
<div class="container">

    <!-- table selection --> 
    <div>
        <select bind:value={selectedTable} onchange="{handleTableChange}">
          <option value="" disabled>-- Choose a table --</option>
            {#each tables as table}
            <option value={table}>{table}</option>
            {/each}
        </select>
      </div>

    <!-- column selection -->
      {#if columns.length > 0}
      <div>
        <label>Columns to display:</label><br />    
        {#each columns as column}
            <label>
                <input type="checkbox" bind:group={selectedColumns} value={column}>
                {column}
            </label>
        {/each}
        </div>
      {/if}

    <!-- filters creation -->
     {#if columns.length > 0}
        <h3>Filters:</h3>
        <p>Select columns to filter with:</p>
        {#each filters as filter, i}
        <div>
            <select bind:value={filter.column}>
                <option>-- COLUMMN</option>
                {#each columns as column}
                <option value={column}>{column}</option>
                {/each}
            </select>

            <select bind:value={filter.operator}>
                <option value="=">=</option>
                <option value=">">&gt;</option>
                <option value="<">&lt;</option>
                <option value="!=">!=</option>
            </select>

            <input type="text" bind:value={filter.value}>
            <button onclick={() => removeFilter(i)}>remove Filter</button>
        </div>
        {/each}
        <button onclick={addFilter}>Add Filter</button> 
    {/if}
</div>
  <!-- Debug section to show all state -->
  <div class="debug">
    <h3>Current State</h3>
    <pre>
      tables: {JSON.stringify(tables, null, 2)}
      selectedTable: {JSON.stringify(selectedTable, null, 2)}
      columns: {JSON.stringify(columns, null, 2)}
      selectedColumns: {JSON.stringify(selectedColumns, null, 2)}
      filters: {JSON.stringify(filters, null, 2)}
      results: {JSON.stringify(results, null, 2)}
    </pre>
  </div>
