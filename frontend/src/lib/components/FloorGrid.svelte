<script lang="ts">
	import type { FloorTable } from '$lib';

	export let tables: FloorTable[] = [];
	export let selectedTableId: number | null = null;

	const stateStyles: Record<FloorTable['state'], string> = {
		recommended: 'border-emerald-400 bg-emerald-100 text-emerald-900',
		available: 'border-sky-300 bg-sky-50 text-sky-900',
		selected: 'border-slate-900 bg-slate-900 text-white',
		unavailable: 'border-amber-300 bg-amber-50 text-amber-900',
		'not-suitable': 'border-rose-300 bg-rose-50 text-rose-900'
	};

	$: maxX = Math.max(6, ...tables.map((table) => table.gridX ?? 1));
	$: maxY = Math.max(4, ...tables.map((table) => table.gridY ?? 1));
</script>

<div class="space-y-4 rounded-3xl border border-slate-200 bg-white/90 p-4 shadow-sm">
	<div class="flex flex-wrap items-center justify-between gap-3">
		<div>
			<h2 class="text-lg font-semibold text-slate-900">Floor plan</h2>
			<p class="text-sm text-slate-500">Tables are placed from backend coordinates so layout stays data-driven.</p>
		</div>
		<div class="flex flex-wrap gap-2 text-xs font-medium">
			<span class="rounded-full bg-emerald-100 px-3 py-1 text-emerald-900">Recommended</span>
			<span class="rounded-full bg-sky-100 px-3 py-1 text-sky-900">Available</span>
			<span class="rounded-full bg-amber-100 px-3 py-1 text-amber-900">Another time</span>
			<span class="rounded-full bg-rose-100 px-3 py-1 text-rose-900">Not suitable</span>
		</div>
	</div>

	{#if tables.length === 0}
		<div class="rounded-3xl border border-dashed border-slate-300 bg-slate-50 px-4 py-10 text-center text-sm text-slate-500">
			No tables to show yet. Pick a zone or run a search to populate the grid.
		</div>
	{:else}
		<!-- TODO: replace the placeholder grid chrome with a richer room visualization after the basic flow settles. -->
		<div class="overflow-auto rounded-3xl border border-slate-200 bg-slate-50 p-4">
			<div
				class="grid min-w-xl gap-3"
				style={`grid-template-columns: repeat(${maxX}, minmax(4.5rem, 1fr)); grid-template-rows: repeat(${maxY}, minmax(4.5rem, 1fr));`}
			>
				{#each tables as table, index (table.tableId)}
					<button
						class={`flex min-h-18 flex-col items-center justify-center rounded-2xl border p-2 text-center text-sm font-medium shadow-sm transition hover:-translate-y-0.5 hover:shadow ${stateStyles[table.tableId === selectedTableId ? 'selected' : table.state]}`}
						style={`grid-column: ${table.gridX ?? 1}; grid-row: ${table.gridY ?? index + 1};`}
						type="button"
						on:click={() => (selectedTableId = table.tableId)}
					>
						<span class="text-base font-semibold">{table.tableNumber}</span>
						<span class="text-xs opacity-80">{table.nSeats} seats</span>
					</button>
				{/each}
			</div>
		</div>
	{/if}
</div>
