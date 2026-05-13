// Hardcoded products matching backend seed data
import type { Product } from '../types';

// Since backend doesn't have GET /api/products endpoint, we return hardcoded data
// matching the seed data from database.sql
export const getProducts = (): Product[] => {
    return [
        {
            id: 'nike-air-force-1-white',
            productId: 'nike-air-force-1-white',
            name: 'Nike Air Force 1 Low White',
            price: 2590000,
            stock: 15,
            status: 'ACTIVE',
        },
        {
            id: 'nike-air-force-1-black',
            productId: 'nike-air-force-1-black',
            name: 'Nike Air Force 1 Low Black',
            price: 2590000,
            stock: 14,
            status: 'ACTIVE',
        },
        {
            id: 'jordan-1-chicago',
            productId: 'jordan-1-chicago',
            name: 'Air Jordan 1 Retro High OG Chicago',
            price: 5890000,
            stock: 8,
            status: 'ACTIVE',
        },
        {
            id: 'jordan-4-white-cement',
            productId: 'jordan-4-white-cement',
            name: 'Air Jordan 4 Retro White Cement',
            price: 6790000,
            stock: 6,
            status: 'ACTIVE',
        },
        {
            id: 'yeezy-boost-350-v2',
            productId: 'yeezy-boost-350-v2',
            name: 'Adidas Yeezy Boost 350 V2 Bone',
            price: 7390000,
            stock: 12,
            status: 'ACTIVE',
        },
        {
            id: 'new-balance-550-white',
            productId: 'new-balance-550-white',
            name: 'New Balance 550 White Grey',
            price: 3290000,
            stock: 20,
            status: 'ACTIVE',
        },
        {
            id: 'asics-gel-nyc-cream',
            productId: 'asics-gel-nyc-cream',
            name: 'ASICS GEL-NYC Cream',
            price: 4190000,
            stock: 10,
            status: 'ACTIVE',
        },
        {
            id: 'converse-chuck-70-high',
            productId: 'converse-chuck-70-high',
            name: 'Converse Chuck 70 High Black',
            price: 1890000,
            stock: 25,
            status: 'ACTIVE',
        },
    ];
};

export const getProductById = (productId: string): Product | undefined => {
    return getProducts().find((p) => p.productId === productId);
};
