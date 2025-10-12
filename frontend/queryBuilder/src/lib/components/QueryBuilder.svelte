<script lang="js">
	import { onMount } from "svelte";
	import { executeQuery, getTableColumns, getTables } from "../../api";

    let tables = $state([]);
    let selectedTable = $state('');
    let columns = $state([]);
    let selectedColumns = $state([]);
    let filters = $state([]);
    let results = $state({query:'', rows:[]});
    let error = $state('');

    const operators = ['>', '<', '=', 'like', '!='];

    onMount(async () => {
        try {
            tables = await getTables();
            console.log('Tables:', tables);
        } catch (err) {
            console.error('Error fetching tables:', err);
            tables = ['Orders', 'Customers', 'Products'];
        }
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


<h1>Database : Test DB</h1>
  <div class="section">
      <select bind:value={selectedTable} on:change={handleTableChange}>
          <option value="" disabled>-- Choose a table --</option>
          {#each tables as table}
              <option value={table}>{table}</option>
          {/each}
      </select>
  </div>

  <!-- Column selection -->
  {#if columns.length > 0}
      <div class="section">
          <label class="section-label">Columns to display:</label>
          <div class="checkbox-group">
              {#each columns as column}
                  <label class="checkbox-label">
                      <input type="checkbox" bind:group={selectedColumns} value={column}>
                      <span>{column}</span>
                  </label>
              {/each}
          </div>
      </div>
  {/if}

  <!-- Filters creation -->
  {#if columns.length > 0}
      <div class="section">
          <h3>Filters</h3>
          <p class="section-subtitle">Select columns to filter with:</p>
          {#each filters as filter, i}
              <div class="filter-row">
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
                  <button class="remove-btn" on:click={() => removeFilter(i)}>Remove</button>
              </div>
          {/each}
          <button class="add-btn" on:click={addFilter}>Add Filter</button>
      </div>
  {/if}

  <!-- Generate Query button -->
  {#if columns.length > 0}
      <div class="section">
          <button class="generate-btn" on:click={generateQuery}>Generate Query</button>
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

  <!-- Results table -->
  {#if results.rows.length > 0}
      <div class="section results">
          <h3>Query Results</h3>
          <table>
              <thead>
                  <tr>
                      {#each selectedColumns as column}
                          <th>{column}</th>
                      {/each}
                  </tr>
              </thead>
              <tbody>
                  {#each results.rows as row}
                      <tr>
                          {#each (Array.isArray(row) ? row : [row]) as value}
                              <td>{value}</td>
                          {/each}
                      </tr>
                  {/each}
              </tbody>
          </table>
      </div>
  {/if}

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

  <style>
    :root {
        --primary-color: #4A90E2; /* Soft blue */
        --text-primary: #333333;
        --text-secondary: #666666;
        --background: #F7F8FA; /* Light gray */
        --white: #FFFFFF;
        --border-color: #E0E4E8;
        --shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        --error-color: #D32F2F;
    }

    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
    }

    body {
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        background-color: var(--background);
        color: var(--text-primary);
        line-height: 1.5;
    }

    h1 {
        font-size: 1.8rem;
        margin: 1.5rem 0;
        text-align: center;
        color: var(--text-primary);
    }

    .container {
        max-width: 800px;
        margin: 0 auto;
        padding: 1.5rem;
        background: var(--white);
        border-radius: 8px;
        box-shadow: var(--shadow);
    }

    .section {
        margin-bottom: 1.5rem;
    }

    .section-label {
        font-size: 1rem;
        font-weight: 600;
        color: var(--text-primary);
        margin-bottom: 0.5rem;
        display: block;
    }

    .section-subtitle {
        font-size: 0.9rem;
        color: var(--text-secondary);
        margin-bottom: 0.75rem;
    }

    select,
    input[type="text"] {
        padding: 0.5rem;
        font-size: 1rem;
        border: 1px solid var(--border-color);
        border-radius: 6px;
        background: var(--white);
        transition: border-color 0.2s, box-shadow 0.2s;
        width: 100%;
        max-width: 200px;
    }

    select:focus,
    input[type="text"]:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
    }

    .checkbox-group {
        display: flex;
        flex-wrap: wrap;
        gap: 1rem;
    }

    .checkbox-label {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        font-size: 0.95rem;
        color: var(--text-primary);
    }

    input[type="checkbox"] {
        accent-color: var(--primary-color);
        width: 1rem;
        height: 1rem;
    }

    .filter-row {
        display: flex;
        gap: 0.5rem;
        align-items: center;
        margin-bottom: 0.75rem;
    }

    .filter-row select,
    .filter-row input[type="text"] {
        flex: 1;
        min-width: 120px;
    }

    button {
        padding: 0.5rem 1rem;
        font-size: 1rem;
        border: none;
        border-radius: 6px;
        background: var(--primary-color);
        color: var(--white);
        cursor: pointer;
        transition: background 0.2s, transform 0.1s;
    }

    button:hover {
        background: #357ABD;
        transform: translateY(-1px);
    }

    button:active {
        transform: translateY(0);
    }

    .remove-btn {
        background: #E57373;
    }

    .remove-btn:hover {
        background: #D32F2F;
    }

    .add-btn {
        background: #43A047;
    }

    .add-btn:hover {
        background: #388E3C;
    }

    .generate-btn {
        background: var(--primary-color);
        padding: 0.75rem 1.5rem;
        font-weight: 600;
    }

    .error-message {
        color: var(--error-color);
        font-size: 0.9rem;
        margin: 1rem 0;
        padding: 0.5rem;
        background: #FFEBEE;
        border-radius: 6px;
        text-align: center;
    }

    .results {
        padding: 1rem;
        background: #E6F0FA;
        border-radius: 6px;
    }

    .results h3 {
        font-size: 1.2rem;
        margin-bottom: 0.75rem;
        color: var(--text-primary);
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background: var(--white);
        border-radius: 6px;
        overflow: hidden;
        box-shadow: var(--shadow);
    }

    th,
    td {
        padding: 0.75rem;
        text-align: left;
        border-bottom: 1px solid var(--border-color);
    }

    th {
        background: #F0F4F8;
        font-weight: 600;
        color: var(--text-primary);
    }

    tr:nth-child(even) {
        background: #FAFAFA;
    }

    tr:hover {
        background: #E6F0FA;
    }

    .debug {
        background: #F5F5F5;
        padding: 1rem;
        border-radius: 6px;
        margin-top: 2rem;
    }

    .debug h3 {
        font-size: 1.2rem;
        margin-bottom: 0.75rem;
        color: var(--text-primary);
    }

    .debug pre {
        background: var(--white);
        padding: 1rem;
        border: 1px solid var(--border-color);
        border-radius: 6px;
        font-size: 0.9rem;
        white-space: pre-wrap;
        color: var(--text-primary);
    }
</style>