<script lang="ts">
	import { onMount } from 'svelte';

	import { fetchRecommendations, fetchTables, fetchZones, postBooking } from '$lib/api';
	import FloorGrid from '$lib/components/FloorGrid.svelte';
	import SearchBar from '$lib/components/SearchBar.svelte';
	import TablePanel from '$lib/components/TablePanel.svelte';
	import {
		createBookingTimeOptions,
		deriveFloorTables,
		roundToQuarterHour,
		toDateInputValue,
		toTimeInputValue,
		type RecommendationBuckets,
		type SearchParams,
		type RestoTable,
		type Zone
	} from '$lib';

	const roundedNow = roundToQuarterHour();

	let zones: Zone[] = [];
	let allTables: RestoTable[] = [];
	let recommendations: RecommendationBuckets | null = null;
	let loading = true;
	let searching = false;
	let booking = false;
	let selectedTableId: number | null = null;
	let pageMessage = '';
	let bookingMessage = '';
	let customerName = '';
	let customerPhone = '';
	let bookingTime = toTimeInputValue(roundedNow);

	let search: SearchParams = {
		date: toDateInputValue(roundedNow),
		time: toTimeInputValue(roundedNow),
		durationMinutes: 90,
		zoneId: 0,
		partySize: 2,
		windowPreferred: false,
		privacyPreferred: false
	};

	async function loadBootstrapData() {
		loading = true;
		pageMessage = '';

		try {
			const [zoneResponse, tableResponse] = await Promise.all([fetchZones(), fetchTables()]);
			zones = zoneResponse.response ?? [];
			allTables = tableResponse.response ?? [];

			if (!search.zoneId && zones.length > 0) {
				search.zoneId = zones[0].id;
			}
		} catch (error) {
			pageMessage = error instanceof Error ? error.message : 'Could not load booking data.';
		} finally {
			loading = false;
		}
	}

	onMount(loadBootstrapData);

	$: zoneTables = allTables.filter((table) => !search.zoneId || table.zoneId === search.zoneId);
	$: floorTables = deriveFloorTables(zoneTables, recommendations, selectedTableId);
	$: selectedTable =
		selectedTableId && zoneTables.some((table) => table.tableId === selectedTableId)
			? (floorTables.find((table) => table.tableId === selectedTableId) ?? null)
			: null;
	$: availableTimes = createBookingTimeOptions(search.time);
	$: if (!availableTimes.includes(bookingTime)) {
		bookingTime = availableTimes[0] ?? search.time;
	}

	async function handleSearch() {
		if (!search.zoneId) {
			pageMessage = 'Select a zone before searching.';
			return;
		}

		searching = true;
		pageMessage = '';
		bookingMessage = '';
		selectedTableId = null;

		try {
			const recommendationResult = await fetchRecommendations(search);
			recommendations = recommendationResult.response;
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

			bookingMessage = `Booked table ${selectedTable.tableNumber} for ${customerName} (status ${bookingResult.statusCode}).`;
			customerName = '';
			customerPhone = '';
			await handleSearch();
		} catch (error) {
			bookingMessage = error instanceof Error ? error.message : 'Booking failed.';
		} finally {
			booking = false;
		}
	}

	// TODO: move page state into a dedicated store/load function once the interaction model feels stable.
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

	<SearchBar {search} {zones} loading={loading || searching} on:submit={handleSearch} />

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

			<FloorGrid bind:selectedTableId tables={floorTables} />
		</div>

		<div class="hidden lg:block">
			<TablePanel
				availableTimes={availableTimes}
				bind:customerName
				bind:customerPhone
				bind:selectedTime={bookingTime}
				canBook={selectedTable?.state !== 'not-suitable'}
				message={bookingMessage}
				selectedDate={search.date}
				submitting={booking}
				table={selectedTable}
				on:book={handleBooking}
			/>
		</div>
	</section>

	{#if selectedTable}
		<div class="lg:hidden">
			<div class="fixed inset-0 z-20 bg-slate-950/30" role="presentation" on:click={handleTableSelectionReset}></div>
			<div class="fixed inset-x-0 bottom-0 z-30 max-h-[85vh] rounded-t-4xl px-3 pb-3">
				<TablePanel
					availableTimes={availableTimes}
					bind:customerName
					bind:customerPhone
					bind:selectedTime={bookingTime}
					canBook={selectedTable?.state !== 'not-suitable'}
					message={bookingMessage}
					mobile={true}
					selectedDate={search.date}
					submitting={booking}
					table={selectedTable}
					on:book={handleBooking}
					on:close={handleTableSelectionReset}
				/>
			</div>
		</div>
	{/if}
</div>
