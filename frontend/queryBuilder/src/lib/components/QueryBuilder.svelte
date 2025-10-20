<script lang="js">  
	import { executeQuery, getTableColumns, getTables, uploadSchema } from "../../api";
	import { globalState } from "$lib/globalState.svelte";
    
    let databaseName = $state(null);
    let tables = $derived(globalState.tables);
    let selectedTable = $state('');
    let columns = $state([]);
    let selectedColumns = $state([]);
    let filters = $state([]);
    let results = $state({query:'', rows:[]});
    let error = $state('');

    const operators = ['>', '<', '=', 'like', '!='];

    let availableOperators = {};
    let availableValues = {};

    async function handleTableChange() {
        try {
            columns = await getTableColumns(selectedTable);
            console.log('Columns for', selectedTable, ':', columns);
            selectedColumns = [];
            filters = [];
            results = {query:'', rows:[]};
            error = '';

            columns.forEach(column => {
                switch (column.type) {
                    case 'BOOLEAN':
                        availableOperators[column.name] = ['=', '!='];
                        availableValues[column.name] = ['true', 'false']; 
                        break;
                    case 'DOUBLE','INT':
                        availableOperators[column.name] = ['=', '!=', '<', '>'];
                        availableValues[column.name] = []; 
                        break;
                    case 'VARCHAR (255)':
                        availableOperators[column.name] = ['=', '!=', 'like'];
                        availableValues[column.name] = []; 
                        break;
                    case 'DATE':
                        availableOperators[column.name] = ['=', '!=', '<', '>'];
                        availableValues[column.name] = [];
                        break;
                    default:
                        availableOperators[column.name] = operators;
                        availableValues[column.name] = [];
                        break;
                }
            })
            console.log(availableOperators);
            console.log(availableValues);
            
        } catch (err) {
            console.error('Error fetching columns:', err);
            columns = [];
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
    function handleColumnChange(filter, i){
        const selectedColumn = columns.find(col => col.name === filter.column)
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
                      <input type="checkbox" bind:group={selectedColumns} value={column.name}>
                      <span>{column.name}</span>
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
                          <option value={column.name}>{column.name}</option>
                      {/each}
                  </select>
                  <select bind:value={filter.operator}>
                      <option value="" disabled>-- Operator --</option>
                      {#each availableOperators[filter.column] || [] as op}
                          <option value={op}>{op}</option>
                      {/each}
                  </select>
                  {#if availableValues[filter.column]?.length > 0}
                  <select bind:value={filter.value}>
                    <option value="" disabled>-- Value --</option>
                    {#each availableValues[filter.column] || [] as val}
                        <option value={val}>{val}</option>
                    {/each}
                </select>
                {:else}
                  <input type="text" bind:value={filter.value} placeholder="Value">
                    {/if}
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
