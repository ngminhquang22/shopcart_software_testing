export interface Product {
    id: string;
    name: string;
    price: number;
    stock: number;
}

export interface CartItem extends Product {
    quantity: number;
}

export interface OrderResponse {
    orderId: string;
    status: string;
    totalPrice: number;
}
