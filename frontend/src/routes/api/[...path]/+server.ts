import { backendOrigin } from '$lib/server/backend';
import type { RequestHandler } from './$types';

const BODY_METHODS = new Set(['POST', 'PUT', 'PATCH', 'DELETE']);

function buildTargetUrl(url: URL, path: string | undefined): URL {
	const normalizedPath = path ? `/api/${path}` : '/api';
	const target = new URL(normalizedPath, backendOrigin);
	target.search = url.search;
	return target;
}

const proxy: RequestHandler = async ({ request, params, url }) => {
	const target = buildTargetUrl(url, params.path);
	const headers = new Headers(request.headers);

	headers.delete('host');
	headers.delete('connection');
	headers.delete('content-length');

	const upstreamResponse = await fetch(target, {
		method: request.method,
		headers,
		body: BODY_METHODS.has(request.method) ? request.body : undefined,
		redirect: 'manual'
	});

	return new Response(upstreamResponse.body, {
		status: upstreamResponse.status,
		statusText: upstreamResponse.statusText,
		headers: upstreamResponse.headers
	});
};

export const GET = proxy;
export const POST = proxy;
export const PUT = proxy;
export const PATCH = proxy;
export const DELETE = proxy;
export const OPTIONS = proxy;
export const HEAD = proxy;