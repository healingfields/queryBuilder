<script lang="js">
	import { onMount } from "svelte";
	import { executeQuery, getTableColumns, getTables, uploadSchema } from "../../api";
	import { globalState } from "$lib/globalState.svelte";
    let props = $props();
    
    let databaseName = $state(null);
    let tables = $derived(globalState.tables);
    let selectedTable = $state('');
    let columns = $state([]);
    let selectedColumns = $state([]);
    let filters = $state([]);
    let results = $state({query:'', rows:[]});
    let error = $state('');

    const operators = ['>', '<', '=', 'like', '!='];

    onMount(async () => {
      /*  try {
            tables = await getTables();
            console.log('Tables:', tables);
        } catch (err) {
            console.error('Error fetching tables:', err);
            tables = ['Orders', 'Customers', 'Products'];
        }*/
    });

    async function handleTableChange() {
        try {
            columns = await getTableColumns(selectedTable);
            console.log('Columns for', selectedTable, ':', columns);
            selectedColumns = [];
            filters = [];
            results = {query:'', rows:[]};
            error = '';
        } catch (err) {
            console.error('Error fetching columns:', err);
            columns = ['product', 'amount', 'date'];
        }
    }

    function addFilter() {
        filters = [...filters, { column: '', operator: '', value: '' }];
    }

    function removeFilter(index) {
        filters = filters.filter((_, i) => i !== index);
    }

    async function generateQuery() {
        if (!selectedTable) {
            error = 'No table selected';
            return;
        }
        if (selectedColumns.length === 0) {
            error = 'At least one column must be selected';
            return;
        }

        const payload = {
            table: selectedTable,
            columns: selectedColumns,
            filters: filters.filter(f => f.column && f.operator && f.value)
        };

        try {
            const data = await executeQuery(payload);
            console.log('Query results:', data);
            results = data;
            error = '';
        } catch (err) {
            console.error('Error generating query:', err);
            error = err.message;
            results = {query:'', rows:[]};
        }
    }
</script>

<h1>Database : {databaseName}</h1>
  <div>
      <select bind:value={selectedTable} on:change={handleTableChange}>
          <option value="" disabled>-- Choose a table --</option>
          {#each tables as table}
              <option value={table}>{table}</option>
          {/each}
      </select>
  </div>

  <!-- Column selection -->
  {#if columns.length > 0}
      <div >
          <label >Columns to display:</label>
          <div >
              {#each columns as column}
                  <label >
                      <input type="checkbox" bind:group={selectedColumns} value={column}>
                      <span>{column}</span>
                  </label>
              {/each}
          </div>
      </div>
  {/if}

  <!-- Filters creation -->
  {#if columns.length > 0}
      <div >
          <h3>Filters</h3>
          <p> Select columns to filter with:</p>
          {#each filters as filter, i}
              <div>
                  <select bind:value={filter.column}>
                      <option value="" disabled>-- Column --</option>
                      {#each columns as column}
                          <option value={column}>{column}</option>
                      {/each}
                  </select>
                  <select bind:value={filter.operator}>
                      <option value="" disabled>-- Operator --</option>
                      {#each operators as op}
                          <option value={op}>{op}</option>
                      {/each}
                  </select>
                  <input type="text" bind:value={filter.value} placeholder="Value">
                  <button on:click={() => removeFilter(i)}>Remove</button>
              </div>
          {/each}
          <button on:click={addFilter}>Add Filter</button>
      </div>
  {/if}

  <!-- Generate Query button -->
  {#if columns.length > 0}
      <div class="section">
          <button on:click={generateQuery}>Generate Query</button>
      </div>
  {/if}

  <!-- Error message -->
  {#if error}
      <div class="error-message">{error}</div>
  {/if}

  <div>
    <label for="">Generated query:</label>
    <span style="color: blueviolet;font-family:'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;font-weight:500">{results.query}</span>
  </div>

<!--
  Debug section to show all state 
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
  -->
