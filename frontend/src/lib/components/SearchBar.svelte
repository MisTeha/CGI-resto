

<!-- AGENDI GENEREERITUD KOOD-->
<!-- Komponente ei kirjutanud ma ise, kuid hoolitsesin selle eest, et
  	 ülesehitus oleks loogiline ja kood ülalpeetav 
	-->
<script lang="ts">
	import { DURATION_OPTIONS, type SearchParams, type Zone } from '$lib';
	import { Button } from '$lib/components/ui/button';
	import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '$lib/components/ui/card';
	import { Checkbox } from '$lib/components/ui/checkbox';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import { getSuitableSearchTimes } from '$lib/time-slots';
	import * as Select from '$lib/components/ui/select';

	let {
		search,
		zones = [],
		loading = false,
		onSubmit
	}: {
		search: SearchParams;
		zones?: Zone[];
		loading?: boolean;
		onSubmit?: () => void;
	} = $props();

	let timeValue = $state('');
	let durationValue = $state('');
	let zoneValue = $state('');

	const selectedZone = $derived(zones.find((zone) => String(zone.id) === zoneValue) ?? null);
	const suitableTimeOptions = $derived(getSuitableSearchTimes(selectedZone, search.durationMinutes));

	$effect(() => {
		if (timeValue !== search.time) timeValue = search.time;
		if (durationValue !== String(search.durationMinutes)) durationValue = String(search.durationMinutes);
		if (zoneValue !== (search.zoneId ? String(search.zoneId) : '')) {
			zoneValue = search.zoneId ? String(search.zoneId) : '';
		}
	});

	$effect(() => {
		if (!suitableTimeOptions.includes(search.time)) {
			const fallback = suitableTimeOptions[0] ?? '';
			timeValue = fallback;
			search.time = fallback;
		}
	});

	function handleSubmit(event: SubmitEvent) {
		event.preventDefault();
		onSubmit?.();
	}

	function handleTimeChange(value: string) {
		timeValue = value;
		search.time = value;
	}

	function handleDurationChange(value: string) {
		durationValue = value;
		const parsedDuration = Number(value);
		if (Number.isFinite(parsedDuration)) {
			search.durationMinutes = parsedDuration;
		}
	}

	function handleZoneChange(value: string) {
		zoneValue = value;
		const parsedZone = Number(value);
		if (Number.isFinite(parsedZone)) {
			search.zoneId = parsedZone;
		}
	}
</script>

<Card class="card border border-base-300 bg-base-100 shadow-sm">
	<CardHeader class="flex flex-row flex-wrap items-start justify-between gap-3 space-y-0">
		<div class="space-y-1">
			<CardTitle class="text-base">Find a table</CardTitle>
			<CardDescription>Start with the core filters and keep the flow lightweight.</CardDescription>
		</div>
		<Button class="btn btn-primary" disabled={loading || !search.zoneId} type="submit" form="search-form">
			{loading ? 'Searching…' : 'Search tables'}
		</Button>
	</CardHeader>
	<CardContent>
		<form class="space-y-4" id="search-form" onsubmit={handleSubmit}>
			<div class="grid gap-4 md:grid-cols-2 xl:grid-cols-6">
				<div class="form-control space-y-2">
					<Label for="search-date">Date</Label>
					<Input
						bind:value={search.date}
						class="input input-bordered w-full bg-base-100 [color-scheme:light] [&::-webkit-calendar-picker-indicator]:cursor-pointer [&::-webkit-calendar-picker-indicator]:opacity-80"
						id="search-date"
						required
						type="date"
					/>
				</div>

				<div class="form-control space-y-2">
					<Label for="search-time">Time</Label>
					<Select.Root type="single" value={timeValue} onValueChange={handleTimeChange}>
						<Select.Trigger class="w-full !bg-base-100 !text-base-content" id="search-time">
							{timeValue || 'Select time'}
						</Select.Trigger>
						<Select.Content class="!border-base-300 !bg-base-100 !text-base-content shadow-xl">
							{#each suitableTimeOptions as option}
								<Select.Item class="!bg-base-100 !text-base-content data-[highlighted]:!bg-base-200" value={option} label={option} />
							{/each}
						</Select.Content>
					</Select.Root>
					{#if suitableTimeOptions.length === 0}
						<p class="text-xs text-warning">No suitable start times for this zone and duration.</p>
					{/if}
				</div>

				<div class="form-control space-y-2">
					<Label for="search-duration">Duration</Label>
					<Select.Root type="single" value={durationValue} onValueChange={handleDurationChange}>
						<Select.Trigger class="w-full !bg-base-100 !text-base-content" id="search-duration">
							{DURATION_OPTIONS.find((item) => String(item.value) === durationValue)?.label ?? 'Select duration'}
						</Select.Trigger>
						<Select.Content class="!border-base-300 !bg-base-100 !text-base-content shadow-xl">
							{#each DURATION_OPTIONS as option}
								<Select.Item class="!bg-base-100 !text-base-content data-[highlighted]:!bg-base-200" value={String(option.value)} label={option.label} />
							{/each}
						</Select.Content>
					</Select.Root>
				</div>

				<div class="form-control space-y-2">
					<Label for="search-zone">Zone</Label>
					<Select.Root type="single" value={zoneValue} onValueChange={handleZoneChange}>
						<Select.Trigger class="w-full !bg-base-100 !text-base-content" id="search-zone">
							{zones.find((zone) => String(zone.id) === zoneValue)?.name ?? 'Select zone'}
						</Select.Trigger>
						<Select.Content class="!border-base-300 !bg-base-100 !text-base-content shadow-xl">
							{#each zones as zone}
								<Select.Item class="!bg-base-100 !text-base-content data-[highlighted]:!bg-base-200" value={String(zone.id)} label={zone.name} />
							{/each}
						</Select.Content>
					</Select.Root>
				</div>

				<div class="form-control space-y-2">
					<Label for="search-people">People</Label>
					<Input bind:value={search.partySize} class="input input-bordered w-full" id="search-people" min="1" type="number" />
				</div>

				<div class="form-control space-y-2">
					<Label>Preferences</Label>
					<div class="flex flex-wrap gap-2">
						<div class="flex items-center gap-2 rounded-md border border-base-300 px-3 py-2">
							<Checkbox id="pref-window" bind:checked={search.windowPreferred} />
							<Label class="cursor-pointer" for="pref-window">Window</Label>
						</div>
						<div class="flex items-center gap-2 rounded-md border border-base-300 px-3 py-2">
							<Checkbox id="pref-privacy" bind:checked={search.privacyPreferred} />
							<Label class="cursor-pointer" for="pref-privacy">Privacy</Label>
						</div>
					</div>
				</div>
			</div>
		</form>
	</CardContent>
</Card>
