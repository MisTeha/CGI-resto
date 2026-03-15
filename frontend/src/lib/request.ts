export type RequestJsonResult<T> = {
	statusCode: number;
	data: T | null;
};

export async function requestJsonWithStatus<T>(
	url: string,
	init?: RequestInit,
	fetcher: typeof fetch = fetch
): Promise<RequestJsonResult<T>> {
	const response = await fetcher(url, {
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
			data: null
		};
	}

	const responseText = await response.text();
	if (!responseText.trim()) {
		return {
			statusCode: response.status,
			data: null
		};
	}

	return {
		statusCode: response.status,
		data: JSON.parse(responseText) as T
	};
}

export async function requestJson<T>(
	url: string,
	init?: RequestInit,
	fetcher: typeof fetch = fetch
): Promise<T> {
	const result = await requestJsonWithStatus<T>(url, init, fetcher);
	return result.data as T;
}
