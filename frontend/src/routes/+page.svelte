

<!-- AGENDI GENEREERITUD KOOD-->
<script lang="ts">
	import { goto } from '$app/navigation';

	import { fetchReservationsByTableAndDay, postBooking } from '$lib/api';
	import BookingConfirmationDialog from '$lib/components/BookingConfirmationDialog.svelte';
	import FloorGrid from '$lib/components/FloorGrid.svelte';
	import SearchBar from '$lib/components/SearchBar.svelte';
	import TablePanel from '$lib/components/TablePanel.svelte';
	import { computeAvailableSlots, normalizeTime } from '$lib/time-slots';
	import {
		type BookingResult,
		type Reservation,
		roundToQuarterHour,
		toDateInputValue,
		toTimeInputValue,
		type FloorTable,
		type RecommendationBuckets,
		type SearchParams,
		type Zone
	} from '$lib';
	import type { PageData } from './$types';

	type BookingConfirmation = BookingResult & {
		tableNumber: string;
		zoneName: string;
		durationMinutes: number;
	};

	let { data }: { data: PageData } = $props();

	const roundedNow = roundToQuarterHour();
	const defaultSearch = {
		date: toDateInputValue(roundedNow),
		time: toTimeInputValue(roundedNow),
		durationMinutes: 90,
		zoneId: 0,
		partySize: 2,
		windowPreferred: false,
		privacyPreferred: false
	} satisfies SearchParams;

	let zones = $state<Zone[]>([]);
	let floorTables = $state<FloorTable[]>([]);
	let recommendations = $state<RecommendationBuckets | null>(null);
	let searching = $state(false);
	let booking = $state(false);
	let selectedTableId = $state<number | null>(null);
	let pageMessage = $state('');
	let bookingMessage = $state('');
	let bookingConfirmation = $state<BookingConfirmation | null>(null);
	let bookingConfirmationOpen = $state(false);
	let tableDayReservations = $state<Reservation[]>([]);
	let customerName = $state('');
	let customerPhone = $state('');
	let bookingTime = $state(toTimeInputValue(roundedNow));
	let search = $state<SearchParams>(defaultSearch);

	$effect(() => {
		zones = data.zones ?? [];
		floorTables = data.floor?.tables ?? [];
		recommendations = data.floor?.recommendations ?? null;
		pageMessage = data.errorMessage ?? '';

		if (data.search) {
			search = { ...data.search };
		} else if (!search.zoneId && zones.length > 0) {
			search = { ...search, zoneId: zones[0].id };
		}
	});

	const selectedTable = $derived(floorTables.find((table) => table.tableId === selectedTableId) ?? null);
	const selectedZone = $derived(zones.find((zone) => zone.id === search.zoneId) ?? null);
	const selectedTableZone = $derived(
		selectedTable ? zones.find((zone) => zone.id === selectedTable.zoneId) ?? null : null
	);
	const selectedZoneHours = $derived.by(() => {
		const opening = normalizeTime(selectedZone?.openingTime);
		const closing = normalizeTime(selectedZone?.closingTime);
		if (!opening || !closing) return 'Not configured';
		return `${opening}–${closing}`;
	});
	const availableTimes = $derived(
		computeAvailableSlots(selectedTableZone, tableDayReservations, search.durationMinutes)
	);

	$effect(() => {
		if (!selectedTable) {
			tableDayReservations = [];
			return;
		}

		let cancelled = false;

		fetchReservationsByTableAndDay(selectedTable.tableId, search.date)
			.then((result) => {
				if (!cancelled) {
					tableDayReservations = result.response ?? [];
				}
			})
			.catch((error) => {
				console.error('Failed to load table reservations for slots:', error);
				if (!cancelled) {
					tableDayReservations = [];
				}
			});

		return () => {
			cancelled = true;
		};
	});

	$effect(() => {
		if (!availableTimes.includes(bookingTime)) {
			bookingTime = availableTimes[0] ?? search.time;
		}
	});

	async function handleSearch(options: { resetSelection?: boolean; clearBookingMessage?: boolean } = {}) {
		const { resetSelection = true, clearBookingMessage = true } = options;

		if (!search.zoneId) {
			pageMessage = 'Select a zone before searching.';
			return;
		}

		searching = true;
		pageMessage = '';
		if (clearBookingMessage) {
			bookingMessage = '';
		}
		if (resetSelection) {
			selectedTableId = null;
		}

		try {
			const params = new URLSearchParams({
				date: search.date,
				time: search.time,
				durationMinutes: String(search.durationMinutes),
				zoneId: String(search.zoneId),
				partySize: String(search.partySize),
				windowPreferred: String(search.windowPreferred),
				privacyPreferred: String(search.privacyPreferred)
			});

			await goto(`?${params.toString()}`, {
				replaceState: false,
				keepFocus: true,
				noScroll: true,
				invalidateAll: true
			});
		} catch (error) {
			pageMessage = error instanceof Error ? error.message : 'Search failed.';
		} finally {
			searching = false;
		}
	}

	function handleTableSelectionReset() {
		selectedTableId = null;
		bookingMessage = '';
	}

	function handleBookingConfirmationClose() {
		bookingConfirmationOpen = false;
		bookingConfirmation = null;
		selectedTableId = null;
		bookingMessage = '';
	}

	async function handleBooking() {
		if (!selectedTable) {
			return;
		}

		booking = true;
		bookingMessage = '';

		try {
			const bookingResult = await postBooking({
				tableId: selectedTable.tableId,
				startTime: `${search.date}T${bookingTime}:00`,
				durationMinutes: search.durationMinutes,
				numberOfPeople: search.partySize,
				customerName,
				customerPhone
			});

			if (!bookingResult.response) {
				throw new Error('Booking created but no reservation details were returned.');
			}

			bookingConfirmation = {
				...bookingResult.response,
				tableNumber: selectedTable.tableNumber,
				zoneName: selectedTable.zoneName,
				durationMinutes: search.durationMinutes
			};
			bookingConfirmationOpen = true;
			bookingMessage = '';
			customerName = '';
			customerPhone = '';
			await handleSearch({ resetSelection: false, clearBookingMessage: false });
		} catch (error) {
			bookingMessage = error instanceof Error ? error.message : 'Booking failed.';
		} finally {
			booking = false;
		}
	}
</script>

<svelte:head>
	<title>CGI resto booking skeleton</title>
	<meta content="Restaurant booking skeleton with filters, floor grid, and booking panel." name="description" />
</svelte:head>

<div class="mx-auto flex min-h-screen w-full max-w-7xl flex-col gap-6 px-4 py-6 md:px-6 xl:px-8">
	<header class="space-y-2">
		<p class="text-sm font-medium uppercase tracking-[0.24em] text-slate-500">CGI resto</p>
		<div class="flex flex-col gap-2 lg:flex-row lg:items-end lg:justify-between">
			<div>
				<h1 class="text-3xl font-semibold tracking-tight text-slate-950 md:text-4xl">Booking landing page skeleton</h1>
				<p class="mt-2 max-w-3xl text-sm text-slate-600 md:text-base">
					Incremental first pass: load zones and tables, search for recommendations, and keep the booking surface intentionally simple.
				</p>
			</div>
			{#if pageMessage}
				<p class="max-w-lg rounded-2xl border border-amber-300 bg-amber-50 px-4 py-3 text-sm text-amber-900">{pageMessage}</p>
			{/if}
		</div>
	</header>

	<SearchBar {search} {zones} loading={searching} onSubmit={handleSearch} />

	<section class="grid gap-6 lg:grid-cols-[minmax(0,2fr)_minmax(22rem,1fr)] lg:items-start">
		<div class="space-y-4">
			<div class="rounded-3xl border border-slate-200 bg-white/80 p-4 text-sm text-slate-600 shadow-sm">
				<div class="flex flex-wrap items-center justify-between gap-3">
					<div>
						<p class="font-medium text-slate-900">Current search</p>
						<p class="mt-1 text-sm text-slate-500">
							{search.partySize} guests • {search.date} at {search.time} • {search.durationMinutes} min
						</p>
					</div>
					{#if recommendations?.mostRecommended}
						<p class="rounded-full bg-emerald-100 px-3 py-1 text-xs font-medium text-emerald-900">
							Top pick: table {recommendations.mostRecommended.tableNumber}
						</p>
					{:else if recommendations}
						<p class="rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-700">No recommendation yet</p>
					{/if}
				</div>
			</div>

			<FloorGrid
				bind:selectedTableId
				tables={floorTables}
				zoneHours={selectedZoneHours}
				zoneName={selectedZone?.name ?? ''}
			/>
		</div>

		<div class="hidden lg:block">
			<TablePanel
				availableTimes={availableTimes}
				bind:customerName
				bind:customerPhone
				bind:selectedTime={bookingTime}
				canBook={selectedTable?.state !== 'not-suitable' && availableTimes.length > 0}
				message={bookingMessage}
				selectedDate={search.date}
				submitting={booking}
				table={selectedTable}
				onBook={handleBooking}
			/>
		</div>
	</section>

	{#if selectedTable}
		<div class="lg:hidden">
			<div class="fixed inset-0 z-20 bg-slate-950/30" role="presentation" onclick={handleTableSelectionReset}></div>
			<div class="fixed inset-x-0 bottom-0 z-30 max-h-[85vh] rounded-t-4xl px-3 pb-3">
				<TablePanel
					availableTimes={availableTimes}
					bind:customerName
					bind:customerPhone
					bind:selectedTime={bookingTime}
					canBook={selectedTable?.state !== 'not-suitable' && availableTimes.length > 0}
					message={bookingMessage}
					mobile={true}
					selectedDate={search.date}
					submitting={booking}
					table={selectedTable}
					onBook={handleBooking}
					onClose={handleTableSelectionReset}
				/>
			</div>
		</div>
	{/if}

	<BookingConfirmationDialog
		onClose={handleBookingConfirmationClose}
		open={bookingConfirmationOpen}
		reservation={bookingConfirmation}
	/>
</div>
