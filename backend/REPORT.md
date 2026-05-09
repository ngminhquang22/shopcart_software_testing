# Bao cao kiem thu CartService

## A) addToCart() - cac scenario da test
- testAddToCart_Success: them moi san pham vao gio hang thanh cong.
- testAddToCart_ProductExists_ShouldIncreaseQuantity: san pham da ton tai thi tang so luong.
- testAddToCart_InsufficientStock_ShouldThrowException: vuot qua ton kho thi nem OutOfStockException.
- testAddToCart_ProductNotFound_ShouldThrowException: khong tim thay san pham thi nem ResourceNotFoundException.
- testAddToCart_InventoryNotFound_ShouldThrowException: khong tim thay ton kho thi nem ResourceNotFoundException.
- testAddToCart_UserNotFound_ShouldThrowException: khong tim thay nguoi dung thi nem ResourceNotFoundException.

## B) removeFromCart() va updateQuantity()
### updateQuantity()
- testUpdateQuantity_Success: cap nhat so luong thanh cong.
- testUpdateQuantity_InsufficientStock_ShouldThrowException: vuot qua ton kho thi nem OutOfStockException.
- testUpdateQuantity_CartItemNotFound_ShouldThrowException: khong tim thay cart item thi nem ResourceNotFoundException.

### removeFromCart()
- testRemoveFromCart_Success: xoa cart item thanh cong.
- testRemoveFromCart_CartItemNotFound_ShouldThrowException: khong tim thay cart item thi nem ResourceNotFoundException.
