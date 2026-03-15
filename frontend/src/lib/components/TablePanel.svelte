

<!-- AGENDI GENEREERITUD KOOD-->
<!-- Komponente ei kirjutanud ma ise, kuid hoolitsesin selle eest, et
  	 ülesehitus oleks loogiline ja kood ülalpeetav 
	-->
<script lang="ts">
	import type { FloorTable } from '$lib';
	import { Badge } from '$lib/components/ui/badge';
	import { Button } from '$lib/components/ui/button';
	import {
		Card,
		CardContent,
		CardDescription,
		CardFooter,
		CardHeader,
		CardTitle
	} from '$lib/components/ui/card';
	import { Input } from '$lib/components/ui/input';
	import { Label } from '$lib/components/ui/label';
	import * as Select from '$lib/components/ui/select';

	let {
		table = null,
		selectedDate = '',
		selectedTime = $bindable(''),
		availableTimes = [],
		customerName = $bindable(''),
		customerPhone = $bindable(''),
		submitting = false,
		message = '',
		mobile = false,
		canBook = true,
		onBook,
		onClose
	}: {
		table?: FloorTable | null;
		selectedDate?: string;
		selectedTime: string;
		availableTimes?: string[];
		customerName: string;
		customerPhone: string;
		submitting?: boolean;
		message?: string;
		mobile?: boolean;
		canBook?: boolean;
		onBook?: () => void;
		onClose?: () => void;
	} = $props();
	let selectedTimeValue = $state('');

	$effect(() => {
		if (selectedTimeValue !== selectedTime) selectedTimeValue = selectedTime;
	});

	function handleBookSubmit(event: SubmitEvent) {
		event.preventDefault();
		onBook?.();
	}

	function handleSelectedTimeChange(value: string) {
		selectedTimeValue = value;
		selectedTime = value;
	}

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

<Card class={`card border border-base-300 bg-base-100 shadow-sm ${mobile ? 'max-h-[85vh]' : 'h-full'}`}>
	<CardHeader class="space-y-2">
		<div class="flex items-start justify-between gap-3">
			<div>
				<CardTitle class="text-base">Table details</CardTitle>
				<CardDescription>Inspect table properties and place a booking.</CardDescription>
			</div>
			{#if mobile}
					<Button class="btn btn-sm" type="button" variant="outline" onclick={() => onClose?.()}>Close</Button>
			{/if}
		</div>
	</CardHeader>

	{#if !table}
		<CardContent class="flex flex-1 items-center justify-center">
			<div class="alert border border-dashed border-base-300 bg-base-200/50 text-sm">
				<span>Select a table from the grid to inspect details and test booking.</span>
			</div>
		</CardContent>
	{:else}
		<CardContent class="space-y-4 overflow-auto">
			<div class="rounded-box bg-base-200/50 p-4">
				<div class="mb-3 flex flex-wrap items-start justify-between gap-3">
					<div>
						<h2 class="text-xl font-semibold">Table {table.tableNumber}</h2>
						<p class="text-sm text-muted-foreground">{table.zoneName}</p>
					</div>
					<Badge class="badge badge-neutral" variant="secondary">{availabilityLabel(table.state)}</Badge>
				</div>

				<div class="grid gap-2 text-sm sm:grid-cols-2">
					<div class="stats w-full bg-base-100 shadow-none">
						<div class="stat px-3 py-2">
							<div class="stat-title text-xs">Seats</div>
							<div class="stat-value text-base">{table.nSeats}</div>
						</div>
					</div>
					<div class="stats w-full bg-base-100 shadow-none">
						<div class="stat px-3 py-2">
							<div class="stat-title text-xs">Window</div>
							<div class="stat-value text-base">{table.nextToWindow ? 'Yes' : 'No'}</div>
						</div>
					</div>
					<div class="stats w-full bg-base-100 shadow-none">
						<div class="stat px-3 py-2">
							<div class="stat-title text-xs">Privacy</div>
							<div class="stat-value text-base">{table.privacyScore ?? '—'}</div>
						</div>
					</div>
					<div class="stats w-full bg-base-100 shadow-none">
						<div class="stat px-3 py-2">
							<div class="stat-title text-xs">Date</div>
							<div class="stat-value truncate text-base">{selectedDate}</div>
						</div>
					</div>
				</div>
			</div>

			<form class="space-y-4" onsubmit={handleBookSubmit}>
				<div class="form-control space-y-2">
					<Label for="booking-time">Time</Label>
					<Select.Root type="single" value={selectedTimeValue} onValueChange={handleSelectedTimeChange}>
						<Select.Trigger class="w-full bg-base-100" id="booking-time">
							{selectedTimeValue || 'Select time'}
						</Select.Trigger>
						<Select.Content class="border-base-300 bg-base-100 shadow-lg">
							{#each availableTimes as time}
								<Select.Item value={time} label={time} />
							{/each}
						</Select.Content>
					</Select.Root>
					<p class="text-xs text-muted-foreground">TODO: replace placeholder quarter-hour options with backend-provided slots.</p>
				</div>

				<div class="form-control space-y-2">
					<Label for="customer-name">Customer name</Label>
					<Input bind:value={customerName} class="input input-bordered w-full" id="customer-name" placeholder="Jane Doe" required type="text" />
				</div>

				<div class="form-control space-y-2">
					<Label for="customer-phone">Customer phone</Label>
					<Input bind:value={customerPhone} class="input input-bordered w-full" id="customer-phone" placeholder="+37255512345" required type="tel" />
				</div>

				{#if message}
					<div class="alert alert-info text-sm">{message}</div>
				{/if}

				<CardFooter class="p-0">
					<Button class="btn btn-primary w-full" disabled={submitting || !canBook} type="submit">
						{submitting ? 'Booking…' : canBook ? 'Book this table' : 'Table does not fit current party'}
					</Button>
				</CardFooter>
			</form>
		</CardContent>
	{/if}
</Card>
