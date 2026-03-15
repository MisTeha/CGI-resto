import { requestJson } from '$lib/request';
import { backendApiBase } from '$lib/server/backend';

export type Reservation = {
	id: number;
	tableId: number;
	startTime: string;
	endTime: string;
	numberOfPeople: number;
	customerName: string;
	customerPhone: string;
};

export async function fetchReservationsByTableAndDayServer(
	tableId: number,
	day: string // YYYY-MM-DD format
): Promise<Reservation[]> {
	try {
		const params = new URLSearchParams({
			tableId: String(tableId),
			day
		});

		return await requestJson<Reservation[]>(`${backendApiBase}/bookings/by-table-and-day?${params.toString()}`, {
			cache: 'no-store'
		});
	} catch (error) {
		console.error('Error fetching reservations:', error);
		// TODO: Error handling
		return [];
	}
}
