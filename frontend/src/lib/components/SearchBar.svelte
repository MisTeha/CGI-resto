<script lang="ts">
	import { createEventDispatcher } from 'svelte';

	import { DURATION_OPTIONS, TIME_OPTIONS, type SearchParams, type Zone } from '$lib';

	export let search: SearchParams;
	export let zones: Zone[] = [];
	export let loading = false;

	const dispatch = createEventDispatcher<{ submit: void }>();
</script>

<form class="space-y-4 rounded-3xl border border-slate-200 bg-white/90 p-4 shadow-sm" on:submit|preventDefault={() => dispatch('submit')}>
	<!-- TODO: swap these controls to DaisyUI / shadcn-svelte primitives once the interaction model stabilizes. -->
	<div class="flex items-start justify-between gap-3">
		<div>
			<p class="text-sm font-semibold text-slate-900">Find a table</p>
			<p class="text-sm text-slate-500">Start with the bare minimum filters and keep the structure easy to change.</p>
		</div>
		<button class="rounded-2xl bg-slate-900 px-4 py-2 text-sm font-medium text-white transition hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-400" disabled={loading || !search.zoneId} type="submit">
			{loading ? 'Searching…' : 'Search tables'}
		</button>
	</div>

	<div class="grid gap-3 md:grid-cols-2 xl:grid-cols-6">
		<label class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">Date</span>
			<input bind:value={search.date} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none ring-0 transition focus:border-slate-400" required type="date" />
		</label>

		<label class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">Time</span>
			<select bind:value={search.time} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400">
				{#each TIME_OPTIONS as option}
					<option value={option}>{option}</option>
				{/each}
			</select>
		</label>

		<label class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">Duration</span>
			<select bind:value={search.durationMinutes} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400">
				{#each DURATION_OPTIONS as option}
					<option value={option.value}>{option.label}</option>
				{/each}
			</select>
		</label>

		<label class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">Zone</span>
			<select bind:value={search.zoneId} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400">
				<option disabled value={0}>Select a zone</option>
				{#each zones as zone}
					<option value={zone.id}>{zone.name}</option>
				{/each}
			</select>
		</label>

		<label class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">People</span>
			<input bind:value={search.partySize} class="w-full rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2 outline-none transition focus:border-slate-400" min="1" type="number" />
		</label>

		<div class="space-y-2 text-sm text-slate-600">
			<span class="font-medium text-slate-900">Preferences</span>
			<div class="flex flex-wrap gap-2">
				<label class="inline-flex items-center gap-2 rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2">
					<input bind:checked={search.windowPreferred} class="h-4 w-4 rounded border-slate-300" type="checkbox" />
					<span>Window</span>
				</label>
				<label class="inline-flex items-center gap-2 rounded-2xl border border-slate-200 bg-slate-50 px-3 py-2">
					<input bind:checked={search.privacyPreferred} class="h-4 w-4 rounded border-slate-300" type="checkbox" />
					<span>Privacy</span>
				</label>
			</div>
		</div>
	</div>
</form>
