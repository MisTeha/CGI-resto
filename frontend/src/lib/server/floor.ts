import { combineDateAndTime, type FloorTable, type RecommendationBuckets, type RestoTable, type SearchParams, type TableState } from '$lib';
import { requestJson } from '$lib/request';

const API_BASE = 'http://localhost:8080/api';

export type FloorSearchResult = {
	tables: FloorTable[];
	recommendations: RecommendationBuckets | null;
};

function toFloorTables(sourceTables: RestoTable[], buckets: RecommendationBuckets | null): FloorTable[] {
	const states = new Map<number, TableState>();

	if (buckets?.mostRecommended) {
		states.set(buckets.mostRecommended.tableId, 'recommended');
	}

	for (const table of buckets?.otherAvailable ?? []) {
		states.set(table.tableId, 'available');
	}

	for (const table of buckets?.enoughSeatsAnotherTime ?? []) {
		states.set(table.tableId, 'unavailable');
	}

	for (const table of buckets?.tooFewSeats ?? []) {
		states.set(table.tableId, 'not-suitable');
	}

	return sourceTables.map((table) => ({
		...table,
		state: states.get(table.tableId) ?? 'available'
	}));
}

export async function searchFloorTablesServer(search: SearchParams): Promise<FloorSearchResult> {
	const params = new URLSearchParams({
		startTime: combineDateAndTime(search.date, search.time),
		durationMinutes: String(search.durationMinutes),
		partySize: String(search.partySize),
		zoneId: String(search.zoneId),
		windowPreferred: String(search.windowPreferred),
		privacyPreferred: String(search.privacyPreferred)
	});

	const [tables, recommendations] = await Promise.all([
		requestJson<RestoTable[]>(`${API_BASE}/tables`, { cache: 'no-store' }),
		requestJson<RecommendationBuckets>(`${API_BASE}/availability/recommendations?${params.toString()}`, {
			cache: 'no-store'
		})
	]);

	const zoneTables = tables.filter((table) => !search.zoneId || table.zoneId === search.zoneId);

	return {
		tables: toFloorTables(zoneTables, recommendations),
		recommendations
	};
}
