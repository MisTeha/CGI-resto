
// AGENDI GENEREERITUD KOOD

import { TIME_OPTIONS, type Reservation, type Zone } from '$lib';

type ZoneWindow = {
	openMinutes: number;
	closeMinutes: number;
};

export function normalizeTime(timeValue: string | null | undefined): string | null {
	if (!timeValue) return null;
	const [hours, minutes] = timeValue.split(':');
	if (hours === undefined || minutes === undefined) return null;
	return `${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}`;
}

export function toMinutes(timeValue: string): number {
	const [hours, minutes] = timeValue.split(':').map(Number);
	return hours * 60 + minutes;
}

export function fromMinutes(totalMinutes: number): string {
	const hours = Math.floor(totalMinutes / 60);
	const minutes = totalMinutes % 60;
	return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}`;
}

export function extractHHmm(dateTimeString: string): string | null {
	if (!dateTimeString) return null;
	const timePart = dateTimeString.includes('T') ? dateTimeString.split('T')[1] : dateTimeString;
	if (!timePart) return null;
	return normalizeTime(timePart);
}

function resolveZoneWindow(zone: Zone | null, durationMinutes: number): ZoneWindow | null {
	const openingTime = normalizeTime(zone?.openingTime);
	const closingTime = normalizeTime(zone?.closingTime);

	if (!openingTime || !closingTime) {
		return null;
	}

	const openMinutes = toMinutes(openingTime);
	const closeMinutes = toMinutes(closingTime);
	if (closeMinutes <= openMinutes || durationMinutes <= 0) {
		return null;
	}

	return {
		openMinutes,
		closeMinutes
	};
}

function quarterHourStartsWithinWindow(window: ZoneWindow, durationMinutes: number): number[] {
	const starts: number[] = [];
	for (
		let startMinutes = window.openMinutes;
		startMinutes + durationMinutes <= window.closeMinutes;
		startMinutes += 15
	) {
		starts.push(startMinutes);
	}
	return starts;
}

export function getSuitableSearchTimes(zone: Zone | null, durationMinutes: number): string[] {
	const window = resolveZoneWindow(zone, durationMinutes);
	if (!window) {
		return TIME_OPTIONS;
	}

	return quarterHourStartsWithinWindow(window, durationMinutes).map(fromMinutes);
}

export function computeAvailableSlots(
	zone: Zone | null,
	reservations: Reservation[],
	durationMinutes: number
): string[] {
	const window = resolveZoneWindow(zone, durationMinutes);
	if (!window) {
		return [];
	}

	const reservationWindows = reservations
		.map((reservation) => {
			const start = extractHHmm(reservation.startTime);
			const end = extractHHmm(reservation.endTime);
			if (!start || !end) return null;
			return {
				startMinutes: toMinutes(start),
				endMinutes: toMinutes(end)
			};
		})
		.filter((window): window is { startMinutes: number; endMinutes: number } => Boolean(window));

	const slots: string[] = [];
	for (const slotStart of quarterHourStartsWithinWindow(window, durationMinutes)) {
		const slotEnd = slotStart + durationMinutes;
		const overlapsReservation = reservationWindows.some(
			(window) => slotStart < window.endMinutes && window.startMinutes < slotEnd
		);

		if (!overlapsReservation) {
			slots.push(fromMinutes(slotStart));
		}
	}

	return slots;
}