package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "Orders")
@NamedQueries({
    @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o"),
    @NamedQuery(name = "Order.findByOrderStatus", query = "SELECT o FROM Order o WHERE o.status = :orderStatus"),
    @NamedQuery(name = "Order.findByUserID", query = "SELECT o FROM Order o WHERE o.user.id = :userID")
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "UserID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "OrderDate", nullable = false)
    private Instant orderDate;

    @Column(name = "TotalAmount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Nationalized
    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @Nationalized
    @Column(name = "ShippingAddress", nullable = false, length = 255)
    private String shippingAddress;

    @Nationalized
    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Nationalized
    @Column(name = "Note", length = 500)
    private String note;

    // === Constructor mặc định ===
    public Order() {
        this.status = "Pending";
    }

    // === Set mặc định ngày khi lưu ===
    @PrePersist
    protected void onCreate() {
        this.orderDate = Instant.now();
    }

    // === Getter & Setter ===

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Backward compatibility method
    public String getOrderStatus() {
        return status;
    }

    public void setOrderStatus(String orderStatus) {
        this.status = orderStatus;
    }

    public String getOrderDateFormatted() {
        if (orderDate == null) return "";
        java.time.LocalDateTime ldt = java.time.LocalDateTime.ofInstant(orderDate, java.time.ZoneId.systemDefault());
        return ldt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
