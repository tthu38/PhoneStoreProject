// Cart JavaScript Functions
class CartManager {
    constructor() {
        this.init();
    }

    init() {
        this.bindEvents();
        this.updateCartBadge();
    }

    bindEvents() {
        // Quantity controls
        document.querySelectorAll('.quantity-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                const variantId = this.getVariantIdFromElement(btn);
                const change = btn.querySelector('.fa-minus') ? -1 : 1;
                this.updateQuantity(variantId, change);
            });
        });

        // Quantity input changes
        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', (e) => {
                const variantId = this.getVariantIdFromElement(input);
                const currentQuantity = parseInt(input.dataset.originalQuantity || input.value);
                const newQuantity = parseInt(input.value);
                const change = newQuantity - currentQuantity;
                this.updateQuantity(variantId, change);
            });
        });

        // Remove buttons
        document.querySelectorAll('.remove-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                const variantId = this.getVariantIdFromElement(btn);
                this.removeItem(variantId);
            });
        });

        // Clear cart button
        const clearBtn = document.querySelector('.clear-cart-btn');
        if (clearBtn) {
            clearBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.clearCart();
            });
        }

        // Checkout button
        const checkoutBtn = document.querySelector('.checkout-btn');
        if (checkoutBtn) {
            checkoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.proceedToCheckout();
            });
        }

        // Select all checkbox
        const selectAllCheckbox = document.getElementById('selectAll');
        if (selectAllCheckbox) {
            selectAllCheckbox.addEventListener('change', (e) => {
                this.toggleSelectAll(e.target.checked);
            });
        }

        // Individual item checkboxes
        document.querySelectorAll('.item-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', (e) => {
                const variantId = parseInt(e.target.dataset.variantId);
                this.toggleItemSelection(variantId, e.target.checked);
            });
        });
    }

    getVariantIdFromElement(element) {
        const cartItem = element.closest('.cart-item');
        if (cartItem) {
            return parseInt(cartItem.id.replace('cart-item-', ''));
        }
        return null;
    }

    async updateQuantity(variantId, change) {
        const input = document.querySelector(`#cart-item-${variantId} .quantity-input`);
        const currentQuantity = parseInt(input.value);
        const newQuantity = currentQuantity + change;

        if (newQuantity < 1) {
            this.showToast('Số lượng phải lớn hơn 0', 'warning');
            return;
        }

        try {
            this.setLoading(true);
            
            const response = await fetch(`${this.getContextPath()}/cart?action=update`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `variantId=${variantId}&quantity=${newQuantity}`
            });

            const data = await response.json();
            
            if (data.success) {
                input.value = newQuantity;
                this.showToast(data.message || 'Đã cập nhật giỏ hàng', 'success');
                this.updateCartBadge();
                this.updateTotalPrice();
            } else {
                this.showToast(data.message || 'Có lỗi xảy ra', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            this.showToast('Có lỗi xảy ra khi cập nhật giỏ hàng', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    async removeItem(variantId) {
        if (!confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
            return;
        }

        try {
            this.setLoading(true);
            
            const cartItem = document.getElementById(`cart-item-${variantId}`);
            cartItem.classList.add('removing');

            const response = await fetch(`${this.getContextPath()}/cart?action=remove`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `variantId=${variantId}`
            });

            const data = await response.json();
            
            if (data.success) {
                setTimeout(() => {
                    cartItem.remove();
                    this.updateCartBadge();
                    this.updateTotalPrice();
                    this.checkEmptyCart();
                }, 300);
                this.showToast(data.message || 'Đã xóa sản phẩm khỏi giỏ hàng', 'success');
            } else {
                cartItem.classList.remove('removing');
                this.showToast(data.message || 'Có lỗi xảy ra', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            this.showToast('Có lỗi xảy ra khi xóa sản phẩm', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    async clearCart() {
        if (!confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?')) {
            return;
        }

        try {
            this.setLoading(true);
            
            const response = await fetch(`${this.getContextPath()}/cart?action=clear`, {
                method: 'POST'
            });

            const data = await response.json();
            
            if (data.success) {
                this.showEmptyCart();
                this.showToast(data.message || 'Đã xóa toàn bộ giỏ hàng', 'success');
            } else {
                this.showToast(data.message || 'Có lỗi xảy ra', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            this.showToast('Có lỗi xảy ra khi xóa giỏ hàng', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    proceedToCheckout() {
        // Kiểm tra đăng nhập
        const currentUser = document.querySelector('[data-user-id]');
        if (!currentUser) {
            this.showToast('Vui lòng đăng nhập để tiếp tục thanh toán', 'warning');
            return;
        }

        // Chuyển đến trang thanh toán
        window.location.href = `${this.getContextPath()}/cart/confirm.jsp`;
    }

    async toggleSelectAll(selectAll) {
        try {
            this.setLoading(true);
            
            const response = await fetch(`${this.getContextPath()}/carts`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=selectAll&selectAll=${selectAll}`
            });

            const data = await response.json();
            
            if (data.success) {
                // Update all checkboxes
                document.querySelectorAll('.item-checkbox').forEach(checkbox => {
                    checkbox.checked = selectAll;
                });
                
                // Update visual feedback for all items
                document.querySelectorAll('.cart-item').forEach(item => {
                    if (selectAll) {
                        item.classList.add('selected');
                    } else {
                        item.classList.remove('selected');
                    }
                });
                
                // Update total price
                this.updateSelectedTotal(data.selectedTotal);
                this.showToast(data.message || 'Đã cập nhật lựa chọn tất cả', 'success');
            } else {
                this.showToast(data.message || 'Có lỗi xảy ra', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            this.showToast('Có lỗi xảy ra khi cập nhật lựa chọn', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    async toggleItemSelection(variantId, selected) {
        try {
            this.setLoading(true);
            
            const response = await fetch(`${this.getContextPath()}/carts`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=toggleSelection&variantId=${variantId}&selected=${selected}`
            });

            const data = await response.json();
            
            if (data.success) {
                // Update visual feedback
                const cartItem = document.getElementById(`cart-item-${variantId}`);
                if (cartItem) {
                    if (selected) {
                        cartItem.classList.add('selected');
                    } else {
                        cartItem.classList.remove('selected');
                    }
                }
                
                // Update total price
                this.updateSelectedTotal(data.selectedTotal);
                
                // Update select all checkbox
                this.updateSelectAllCheckbox();
                
                this.showToast(data.message || 'Đã cập nhật lựa chọn', 'success');
            } else {
                this.showToast(data.message || 'Có lỗi xảy ra', 'error');
            }
        } catch (error) {
            console.error('Error:', error);
            this.showToast('Có lỗi xảy ra khi cập nhật lựa chọn', 'error');
        } finally {
            this.setLoading(false);
        }
    }

    updateSelectedTotal(selectedTotal) {
        const subtotalElement = document.getElementById('subtotal');
        const totalElement = document.getElementById('total');
        
        if (subtotalElement && totalElement) {
            const formattedTotal = this.formatCurrency(selectedTotal);
            subtotalElement.textContent = formattedTotal;
            totalElement.textContent = formattedTotal;
        }
    }

    updateSelectAllCheckbox() {
        const selectAllCheckbox = document.getElementById('selectAll');
        const itemCheckboxes = document.querySelectorAll('.item-checkbox');
        
        if (selectAllCheckbox && itemCheckboxes.length > 0) {
            const checkedCount = Array.from(itemCheckboxes).filter(cb => cb.checked).length;
            const totalCount = itemCheckboxes.length;
            
            if (checkedCount === 0) {
                selectAllCheckbox.indeterminate = false;
                selectAllCheckbox.checked = false;
            } else if (checkedCount === totalCount) {
                selectAllCheckbox.indeterminate = false;
                selectAllCheckbox.checked = true;
            } else {
                selectAllCheckbox.indeterminate = true;
                selectAllCheckbox.checked = false;
            }
        }
    }

    updateCartBadge() {
        const cartItems = document.querySelectorAll('.cart-item');
        let totalItems = 0;
        
        cartItems.forEach(item => {
            const quantity = parseInt(item.querySelector('.quantity-input').value);
            totalItems += quantity;
        });
        
        const badge = document.getElementById('cartBadge');
        if (badge) {
            badge.textContent = totalItems;
            badge.style.display = totalItems > 0 ? 'block' : 'none';
        }
    }

    updateTotalPrice() {
        const cartItems = document.querySelectorAll('.cart-item');
        let totalPrice = 0;
        
        cartItems.forEach(item => {
            const quantity = parseInt(item.querySelector('.quantity-input').value);
            const priceElement = item.querySelector('.current-price');
            if (priceElement) {
                const priceText = priceElement.textContent.replace(/[^\d]/g, '');
                const price = parseInt(priceText);
                totalPrice += price;
            }
        });
        
        const totalElement = document.querySelector('.summary-total span:last-child');
        if (totalElement) {
            totalElement.textContent = this.formatCurrency(totalPrice);
        }
    }

    checkEmptyCart() {
        const cartItems = document.querySelectorAll('.cart-item');
        if (cartItems.length === 0) {
            this.showEmptyCart();
        }
    }

    showEmptyCart() {
        const cartContainer = document.querySelector('.col-lg-8');
        if (cartContainer) {
            cartContainer.innerHTML = `
                <div class="empty-cart">
                    <i class="fas fa-shopping-cart"></i>
                    <h3>Giỏ hàng trống</h3>
                    <p>Bạn chưa có sản phẩm nào trong giỏ hàng</p>
                    <a href="${this.getContextPath()}/" class="continue-shopping">
                        <i class="fas fa-arrow-left"></i> Tiếp tục mua sắm
                    </a>
                </div>
            `;
        }
    }

    setLoading(loading) {
        const cartContainer = document.querySelector('.cart-container');
        if (loading) {
            cartContainer.classList.add('loading');
        } else {
            cartContainer.classList.remove('loading');
        }
    }

    showToast(message, type = 'success') {
        // Tạo toast container nếu chưa có
        let toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container';
            document.body.appendChild(toastContainer);
        }

        // Tạo toast element
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-circle' : 'exclamation-triangle'} me-2"></i>
                <span>${message}</span>
            </div>
        `;

        // Thêm vào container
        toastContainer.appendChild(toast);

        // Tự động xóa sau 3 giây
        setTimeout(() => {
            toast.style.animation = 'slideOutRight 0.3s ease';
            setTimeout(() => {
                toast.remove();
                if (toastContainer.children.length === 0) {
                    toastContainer.remove();
                }
            }, 300);
        }, 3000);
    }

    formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 1));
    }
}

// Initialize cart manager when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    new CartManager();
});

// Add slideOutRight animation
const style = document.createElement('style');
style.textContent = `
    @keyframes slideOutRight {
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style); 