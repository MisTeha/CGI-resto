export type { Zone } from '$lib/server/zones';

export type TableState = 'recommended' | 'available' | 'selected' | 'unavailable' | 'not-suitable';

export type RestoTable = {
	tableId: number;
	tableNumber: string;
	zoneId: number;
	zoneName: string;
	nSeats: number;
	nextToWindow: boolean;
	privacyScore: number | null;
	gridX: number | null;
	gridY: number | null;
};

export type RecommendationBuckets = {
	mostRecommended: RestoTable | null;
	otherAvailable: RestoTable[];
	enoughSeatsAnotherTime: RestoTable[];
	tooFewSeats: RestoTable[];
};

export type FloorTable = RestoTable & {
	state: TableState;
};

export type FloorSearchResult = {
	tables: FloorTable[];
	recommendations: RecommendationBuckets | null;
};

export type SearchParams = {
	date: string;
	time: string;
	durationMinutes: number;
	zoneId: number;
	partySize: number;
	windowPreferred: boolean;
	privacyPreferred: boolean;
};

export type BookingPayload = {
	tableId: number;
	startTime: string;
	durationMinutes: number;
	numberOfPeople: number;
	customerName: string;
	customerPhone: string;
};

export type BookingResult = {
	id: number;
	tableId: number;
	startTime: string;
	endTime: string;
	numberOfPeople: number;
	customerName: string;
	customerPhone: string;
};

export type Reservation = {
	id: number;
	tableId: number;
	startTime: string;
	endTime: string;
	numberOfPeople: number;
	customerName: string;
	customerPhone: string;
};

export const DURATION_OPTIONS = [
	{ value: 60, label: '60 min' },
	{ value: 90, label: '90 min' },
	{ value: 120, label: '120 min' },
	{ value: 150, label: '150 min' }
];

export const TIME_OPTIONS = createQuarterHourOptions();

export function createQuarterHourOptions() {
	const options: string[] = [];

	for (let hour = 0; hour < 24; hour += 1) {
		for (let minutes = 0; minutes < 60; minutes += 15) {
			options.push(`${String(hour).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`);
		}
	}

	return options;
}

export function roundToQuarterHour(date = new Date()) {
	const rounded = new Date(date);
	rounded.setSeconds(0, 0);
	const roundedMinutes = Math.ceil(rounded.getMinutes() / 15) * 15;
	const minuteOffset = roundedMinutes - rounded.getMinutes();
	rounded.setMinutes(rounded.getMinutes() + minuteOffset);

	return rounded;
}

export function toDateInputValue(date = new Date()) {
	return date.toISOString().slice(0, 10);
}

export function toTimeInputValue(date = new Date()) {
	return date.toTimeString().slice(0, 5);
}

export function combineDateAndTime(date: string, time: string) {
	return `${date}T${time}:00`;
}

export function createBookingTimeOptions(baseTime: string, count = 6) {
	const [hours, minutes] = baseTime.split(':').map(Number);
	const baseMinutes = hours * 60 + minutes;

	return Array.from({ length: count }, (_, index) => {
		const totalMinutes = baseMinutes + index * 15;
		const safeMinutes = ((totalMinutes % (24 * 60)) + 24 * 60) % (24 * 60);
		const nextHour = Math.floor(safeMinutes / 60);
		const nextMinutes = safeMinutes % 60;
		return `${String(nextHour).padStart(2, '0')}:${String(nextMinutes).padStart(2, '0')}`;
	});
}
