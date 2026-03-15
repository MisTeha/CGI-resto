import { env } from '$env/dynamic/private';

export const backendOrigin = env.BACKEND_ORIGIN ?? 'http://localhost:8080';
export const backendApiBase = env.BACKEND_API_BASE ?? `${backendOrigin}/api`;