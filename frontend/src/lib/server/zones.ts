const API_BASE = 'http://localhost:8080/api';

export type Zone = {
	id: number;
	name: string;
	description: string;
};

export async function fetchZonesServer(): Promise<Zone[]> {
	try {
		const response = await fetch(`${API_BASE}/zones`, {
			headers: { Accept: 'application/json' }
		});

		if (!response.ok) {
			throw new Error(`Failed to fetch zones: ${response.statusText}`);
		}

		return await response.json();
	} catch (error) {
		console.error('Error fetching zones:', error);
		// TODO: Error handling
		return [];
	}
}
