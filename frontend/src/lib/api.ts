import type {
	BookingPayload,
	BookingResult,
	Zone
} from '$lib';
import { requestJsonWithStatus } from '$lib/request';

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
	const { statusCode, data } = await requestJsonWithStatus<T>(`${API_BASE}${path}`, init, fetcher);

	return {
		statusCode,
		response: data
	};
}

// selle ja järgneva wrapperi tegin juurde, kuna "undefined" kordamine ei meeldinud.
async function GETrequest<T>(path: string, fetcher: typeof fetch = fetch): Promise<ApiResult<T>> {
    return genericApiRequest<T>(path, undefined, fetcher);
}

async function POSTrequest<T>(path: string, fetcher: typeof fetch = fetch): Promise<ApiResult<T>> {
    return genericApiRequest<T>(path, {method: 'POST'}, fetcher);
}


export function fetchZones(fetcher?: typeof fetch) {
	return GETrequest<Zone[]>('/zones', fetcher);
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
