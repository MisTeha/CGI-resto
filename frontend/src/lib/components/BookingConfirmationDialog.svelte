

<!-- AGENDI GENEREERITUD KOOD-->
<!-- Komponente ei kirjutanud ma ise, kuid hoolitsesin selle eest, et
  	 ülesehitus oleks loogiline ja kood ülalpeetav 
	-->
<script lang="ts">
	import type { BookingResult } from '$lib';
	import { Button } from '$lib/components/ui/button';
	import {
		Card,
		CardContent,
		CardDescription,
		CardFooter,
		CardHeader,
		CardTitle
	} from '$lib/components/ui/card';

	type ReservationSummary = BookingResult & {
		tableNumber: string;
		zoneName: string;
		durationMinutes: number;
	};

	let {
		open = false,
		reservation = null,
		onClose
	}: {
		open?: boolean;
		reservation?: ReservationSummary | null;
		onClose?: () => void;
	} = $props();

	const dateTimeFormatter = new Intl.DateTimeFormat(undefined, {
		year: 'numeric',
		month: 'short',
		day: 'numeric',
		hour: '2-digit',
		minute: '2-digit'
	});

	function formatDateTime(value: string) {
		const parsed = new Date(value);
		if (Number.isNaN(parsed.valueOf())) {
			return value;
		}

		return dateTimeFormatter.format(parsed);
	}

	function handleClose() {
		onClose?.();
	}
</script>

{#if open && reservation}
	<div class="fixed inset-0 z-40 bg-slate-950/35" role="presentation" onclick={handleClose}></div>
	<div class="fixed inset-0 z-50 flex items-center justify-center p-4" role="dialog" aria-modal="true" aria-labelledby="reservation-confirmation-title">
		<Card class="w-full max-w-lg border border-emerald-200 bg-base-100 shadow-xl">
			<CardHeader>
				<CardTitle class="text-xl text-emerald-800" id="reservation-confirmation-title">Reservation confirmed</CardTitle>
				<CardDescription>Your booking was saved successfully. Here are the reservation details.</CardDescription>
			</CardHeader>
			<CardContent>
				<div class="grid gap-3 text-sm">
					<div class="rounded-xl bg-emerald-50 p-3 text-emerald-900">
						<p class="font-semibold">Table {reservation.tableNumber}</p>
						<p class="text-xs opacity-80">{reservation.zoneName}</p>
					</div>
					<div class="grid grid-cols-2 gap-3">
						<div class="rounded-lg border border-base-300 p-3">
							<p class="text-xs text-muted-foreground">Start</p>
							<p class="font-medium">{formatDateTime(reservation.startTime)}</p>
						</div>
						<div class="rounded-lg border border-base-300 p-3">
							<p class="text-xs text-muted-foreground">End</p>
							<p class="font-medium">{formatDateTime(reservation.endTime)}</p>
						</div>
						<div class="rounded-lg border border-base-300 p-3">
							<p class="text-xs text-muted-foreground">Duration</p>
							<p class="font-medium">{reservation.durationMinutes} min</p>
						</div>
						<div class="rounded-lg border border-base-300 p-3">
							<p class="text-xs text-muted-foreground">Party size</p>
							<p class="font-medium">{reservation.numberOfPeople} guests</p>
						</div>
					</div>
					<div class="rounded-lg border border-base-300 p-3">
						<p class="text-xs text-muted-foreground">Customer</p>
						<p class="font-medium">{reservation.customerName}</p>
						<p class="text-sm text-muted-foreground">{reservation.customerPhone}</p>
					</div>
				</div>
			</CardContent>
			<CardFooter class="justify-end gap-2">
				<Button type="button" variant="outline" onclick={handleClose}>Close</Button>
			</CardFooter>
		</Card>
	</div>
{/if}