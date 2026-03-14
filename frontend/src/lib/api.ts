import type {
	BookingPayload,
	BookingResult,
	RecommendationBuckets,
	SearchParams,
	RestoTable,
	Zone
} from '$lib';
import { combineDateAndTime } from '$lib';

// See on keelemudeli genereeritud fail, aga lisasin juurde kaks wrapperit.
const API_BASE = '/api';

export type ApiResult<T> = {
	statusCode: number;
	response: T | null;
};

async function genericApiRequest<T>(
	path: string,
	init?: RequestInit,
	fetcher: typeof fetch = fetch
): Promise<ApiResult<T>> {
	const response = await fetcher(`${API_BASE}${path}`, {
		headers: {
			Accept: 'application/json',
			...init?.headers
		},
		...init
	});

	if (!response.ok) {
		const message = await response.text();
		throw new Error(message || `Request failed with status ${response.status}`);
	}

	if (response.status === 204) {
		return {
			statusCode: response.status,
			response: null
		};
	}

	const responseText = await response.text();
	if (!responseText.trim()) {
		return {
			statusCode: response.status,
			response: null
		};
	}

	return {
		statusCode: response.status,
		response: JSON.parse(responseText) as T
	};
}

// need kaks wrapperit tegin juurde, kuna "undefined" kordamine ei meeldinud.
async function GETrequest<T>(path: string, fetcher: typeof fetch = fetch): Promise<ApiResult<T>> {
    return genericApiRequest<T>(path, undefined, fetcher);
}

async function POSTrequest<T>(path: string, fetcher: typeof fetch = fetch): Promise<ApiResult<T>> {
    return genericApiRequest<T>(path, {method: 'POST'}, fetcher);
}


export function fetchZones(fetcher?: typeof fetch) {
	return GETrequest<Zone[]>('/zones', fetcher);
}

export function fetchTables(fetcher?: typeof fetch) {
	return GETrequest<RestoTable[]>('/tables', fetcher);
}

export function fetchRecommendations(search: SearchParams, fetcher?: typeof fetch) {
	const params = new URLSearchParams({
		startTime: combineDateAndTime(search.date, search.time),
		durationMinutes: String(search.durationMinutes),
		partySize: String(search.partySize),
		zoneId: String(search.zoneId),
		windowPreferred: String(search.windowPreferred),
		privacyPreferred: String(search.privacyPreferred)
	});

	return GETrequest<RecommendationBuckets>(
		`/availability/recommendations?${params.toString()}`,
		fetcher
	);
}

export function postBooking(payload: BookingPayload, fetcher?: typeof fetch) {
	const params = new URLSearchParams({
		tableId: String(payload.tableId),
		startTime: payload.startTime,
		durationMinutes: String(payload.durationMinutes),
		numberOfPeople: String(payload.numberOfPeople),
		customerName: payload.customerName,
		customerPhone: payload.customerPhone
	});

	// TODO: switch this to a JSON body when the backend booking contract settles.
	return POSTrequest<BookingResult>(`/bookings?${params.toString()}`, fetcher);
}
