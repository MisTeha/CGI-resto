<script lang="ts">
	import { createEventDispatcher } from 'svelte';

	import type { FloorTable } from '$lib';

	export let table: FloorTable | null = null;
	export let selectedDate = '';
	export let selectedTime = '';
	export let availableTimes: string[] = [];
	export let customerName = '';
	export let customerPhone = '';
	export let submitting = false;
	export let message = '';
	export let mobile = false;
	export let canBook = true;

	const dispatch = createEventDispatcher<{ book: void; close: void }>();

	function availabilityLabel(state: FloorTable['state']) {
		switch (state) {
			case 'recommended':
				return 'Best match right now';
			case 'available':
				return 'Available at requested time';
			case 'unavailable':
				return 'Seats fit, but timing needs adjusting';
			case 'not-suitable':
				return 'Likely too small for this party';
			case 'selected':
				return 'Currently selected';
		}
	}
</script>

<div class={`flex h-full flex-col rounded-3xl border border-slate-200 bg-white/95 p-4 shadow-sm ${mobile ? 'max-h-[85vh]' : ''}`}>
	<div class="flex items-start justify-between gap-3">
		<div>
			<p class="text-sm font-semibold text-slate-900">Table details</p>
			<p class="text-sm text-slate-500">Keep this panel thin for now and evolve it as the booking flow gets clearer.</p>
		</div>
		{#if mobile}
			<button class="rounded-full border border-slate-200 px-3 py-1 text-sm text-slate-600" type="button" on:click={() => dispatch('close')}>
				Close
			</button>
		{/if}
	</div>

	{#if !table}
		<div class="mt-4 flex flex-1 items-center justify-center rounded-3xl border border-dashed border-slate-300 bg-slate-50 px-4 text-center text-sm text-slate-500">
			Select a table from the grid to inspect details and test the booking form.
		</div>
	{:else}
		<div class="mt-4 space-y-4 overflow-auto">
			<div class="space-y-3 rounded-3xl bg-slate-50 p-4">
				<div class="flex flex-wrap items-start justify-between gap-3">
					<div>
						<h2 class="text-xl font-semibold text-slate-900">Table {table.tableNumber}</h2>
						<p class="text-sm text-slate-500">{table.zoneName}</p>
					</div>
					<span class="rounded-full bg-slate-900 px-3 py-1 text-xs font-medium text-white">{availabilityLabel(table.state)}</span>
				</div>

				<div class="grid gap-3 text-sm text-slate-600 sm:grid-cols-2">
					<div class="rounded-2xl bg-white px-3 py-2">
						<p class="text-xs uppercase tracking-wide text-slate-400">Seats</p>
						<p class="mt-1 font-medium text-slate-900">{table.nSeats}</p>
					</div>
					<div class="rounded-2xl bg-white px-3 py-2">
						<p class="text-xs uppercase tracking-wide text-slate-400">Window</p>
						<p class="mt-1 font-medium text-slate-900">{table.nextToWindow ? 'Yes' : 'No'}</p>
					</div>
					<div class="rounded-2xl bg-white px-3 py-2">
						<p class="text-xs uppercase tracking-wide text-slate-400">Privacy</p>
						<p class="mt-1 font-medium text-slate-900">{table.privacyScore ?? '—'}</p>
					</div>
					<div class="rounded-2xl bg-white px-3 py-2">
						<p class="text-xs uppercase tracking-wide text-slate-400">Date</p>
						<p class="mt-1 font-medium text-slate-900">{selectedDate}</p>
					</div>
				</div>
			</div>

			<form class="space-y-4" on:submit|preventDefault={() => dispatch('book')}>
				<div class="space-y-2 text-sm text-slate-600">
					<label class="font-medium text-slate-900" for="booking-time">Time</label>
					<select bind:value={selectedTime} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400" id="booking-time">
						{#each availableTimes as time}
							<option value={time}>{time}</option>
						{/each}
					</select>
					<p class="text-xs text-slate-500">TODO: replace these placeholder quarter-hour options with backend-provided slot data.</p>
				</div>

				<div class="space-y-2 text-sm text-slate-600">
					<label class="font-medium text-slate-900" for="customer-name">Customer name</label>
					<input bind:value={customerName} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400" id="customer-name" placeholder="Jane Doe" required type="text" />
				</div>

				<div class="space-y-2 text-sm text-slate-600">
					<label class="font-medium text-slate-900" for="customer-phone">Customer phone</label>
					<input bind:value={customerPhone} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400" id="customer-phone" placeholder="+37255512345" required type="tel" />
				</div>

				{#if message}
					<p class="rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-600">{message}</p>
				{/if}

				<button class="w-full rounded-2xl bg-slate-900 px-4 py-3 text-sm font-medium text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-400" disabled={submitting || !canBook} type="submit">
					{submitting ? 'Booking…' : canBook ? 'Book this table' : 'Table does not fit current party'}
				</button>
			</form>
		</div>
	{/if}
</div>
