

<!-- AGENDI GENEREERITUD KOOD-->
<!-- Komponente ei kirjutanud ma ise, kuid hoolitsesin selle eest, et
  	 ülesehitus oleks loogiline ja kood ülalpeetav 
	-->
<script lang="ts">
	import type { FloorTable } from '$lib';
	import { Badge } from '$lib/components/ui/badge';
	import { Card, CardContent, CardHeader, CardTitle } from '$lib/components/ui/card';

	let {
		tables = [],
		zoneName = '',
		zoneHours = '',
		selectedTableId = $bindable<number | null>(null)
	}: {
		tables: FloorTable[];
		zoneName?: string;
		zoneHours?: string;
		selectedTableId: number | null;
	} = $props();

	const stateStyles: Record<FloorTable['state'], string> = {
		recommended: 'border-success bg-success text-success-content',
		available: 'border-info bg-info text-info-content',
		selected: 'border-neutral bg-neutral text-neutral-content ring-2 ring-primary',
		unavailable: 'border-warning bg-warning text-warning-content',
		'not-suitable': 'border-error bg-error text-error-content'
	};

	const maxX = $derived(Math.max(6, ...tables.map((table) => table.gridX ?? 1)));
	const maxY = $derived(Math.max(4, ...tables.map((table) => table.gridY ?? 1)));
</script>

<Card class="card border border-base-300 bg-base-100 shadow-sm">
	<CardHeader class="space-y-3">
		<div class="flex flex-wrap items-center justify-between gap-3">
			<CardTitle class="text-lg">Floor plan</CardTitle>
			<div class="flex flex-wrap gap-2 text-xs">
				<Badge class="badge badge-success" variant="outline">Recommended</Badge>
				<Badge class="badge badge-info" variant="outline">Available</Badge>
				<Badge class="badge badge-warning" variant="outline">Another time</Badge>
				<Badge class="badge badge-error" variant="outline">Not suitable</Badge>
			</div>
		</div>
		<div class="space-y-1">
			{#if zoneName}
				<p class="text-sm text-muted-foreground">
					Zone: <span class="font-medium text-base-content">{zoneName}</span>
				</p>
			{/if}
			{#if zoneHours}
				<p class="text-sm text-muted-foreground">
					Open: <span class="font-medium text-base-content">{zoneHours}</span>
				</p>
			{/if}
			<p class="text-sm text-muted-foreground">Tables use backend coordinates so the floor stays data-driven.</p>
		</div>
	</CardHeader>
	<CardContent>
		{#if tables.length === 0}
			<div class="alert border border-dashed border-base-300 bg-base-200/50">
				<span class="text-sm">No tables to show yet. Pick a zone or run a search to populate the grid.</span>
			</div>
		{:else}
			<div class="overflow-auto rounded-box border border-base-300 bg-base-200/40 p-4">
				<div
					class="grid min-w-xl gap-3"
					style={`grid-template-columns: repeat(${maxX}, minmax(4.5rem, 1fr)); grid-template-rows: repeat(${maxY}, minmax(4.5rem, 1fr));`}
				>
					{#each tables as table, index (table.tableId)}
						<button
							class={`btn h-auto min-h-18 w-full flex-col gap-0.5 whitespace-normal border p-2 text-center text-xs shadow-sm ${stateStyles[table.tableId === selectedTableId ? 'selected' : table.state]}`}
							style={`grid-column: ${table.gridX ?? 1}; grid-row: ${table.gridY ?? index + 1};`}
							type="button"
							onclick={() => (selectedTableId = table.tableId)}
						>
							<span class="text-sm font-semibold">{table.tableNumber}</span>
							<span class="opacity-85">{table.nSeats} seats</span>
						</button>
					{/each}
				</div>
			</div>
		{/if}
	</CardContent>
</Card>
