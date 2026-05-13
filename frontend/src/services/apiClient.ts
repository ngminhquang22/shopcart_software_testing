// API Client for backend communication
const API_BASE_URL = 'http://localhost:8080/api';

export interface ApiClientConfig {
    baseUrl?: string;
    userId?: string;
}

let config: ApiClientConfig = {
    baseUrl: API_BASE_URL,
    userId: 'user-1' // default user ID
};

export const setApiConfig = (newConfig: Partial<ApiClientConfig>) => {
    config = { ...config, ...newConfig };
};

export const getApiConfig = () => config;

const getHeaders = () => {
    const headers: HeadersInit = {
        'Content-Type': 'application/json',
    };

    if (config.userId) {
        headers['Authorization'] = `Bearer ${config.userId}`;
    }

    return headers;
};

export const apiRequest = async <T>(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE',
    endpoint: string,
    body?: unknown
): Promise<T> => {
    const url = `${config.baseUrl}${endpoint}`;
    const options: RequestInit = {
        method,
        headers: getHeaders(),
    };

    if (body && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(url, options);

        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(`API Error: ${response.status} - ${errorData}`);
        }

        if (response.status === 204) {
            return {} as T;
        }

        const data = await response.json();
        return data as T;
    } catch (error) {
        console.error('API Request Error:', error);
        throw error;
    }
};
