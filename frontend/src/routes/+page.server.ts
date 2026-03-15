import { fetchZonesServer } from '$lib/server/zones';
import { searchFloorTablesServer } from '$lib/server/floor';
import type { PageServerLoad } from './$types';
import type { FloorSearchResult, SearchParams, Zone } from '$lib';

type PageServerData = {
	zones: Zone[];
	floor: FloorSearchResult | null;
	search: SearchParams | null;
	errorMessage: string;
};

function parsePositiveInt(value: string | null): number | null {
	const parsed = Number(value);
	return Number.isInteger(parsed) && parsed > 0 ? parsed : null;
}

export const load: PageServerLoad = async ({ url }): Promise<PageServerData> => {
	try {
		const zones = await fetchZonesServer();

		const date = url.searchParams.get('date');
		const time = url.searchParams.get('time');
		const durationMinutes = parsePositiveInt(url.searchParams.get('durationMinutes'));
		const zoneId = parsePositiveInt(url.searchParams.get('zoneId'));
		const partySize = parsePositiveInt(url.searchParams.get('partySize'));
		const windowPreferred = (url.searchParams.get('windowPreferred') ?? 'false') === 'true';
		const privacyPreferred = (url.searchParams.get('privacyPreferred') ?? 'false') === 'true';

		const hasSearchParams =
			Boolean(date) &&
			Boolean(time) &&
			durationMinutes !== null &&
			zoneId !== null &&
			partySize !== null;

		if (!hasSearchParams) {
			return {
				zones,
				floor: null,
				search: null,
				errorMessage: ''
			};
		}

		const search: SearchParams = {
			date: date ?? '',
			time: time ?? '',
			durationMinutes: durationMinutes ?? 0,
			zoneId: zoneId ?? 0,
			partySize: partySize ?? 0,
			windowPreferred,
			privacyPreferred
		};

		const floor = await searchFloorTablesServer(search);

		return {
			zones,
			floor,
			search,
			errorMessage: ''
		};
	} catch (error) {
		console.error('Failed to load zones on server:', error);
		return {
			zones: [],
			floor: null,
			search: null,
			errorMessage: error instanceof Error ? error.message : 'Could not load booking data.'
		};
	}
};
