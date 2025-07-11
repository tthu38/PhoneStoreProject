<%-- 
    Document   : index
    Created on : Jun 5, 2025, 11:59:16 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="container-fluid" style="padding-top: 24px;">
            <div class="row g-4">
                <div class="col-lg-7 col-md-12">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h2 class="card-title text-center mb-4">Biểu đồ doanh thu theo tháng</h2>
                            <div style="overflow-x:auto;">
                                <canvas id="revenueChart" style="width:1200px;max-width:none;height:400px;max-height:400px;"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-5 col-md-12">
                    <div class="card shadow-sm h-100">
                        <div class="card-body">
                            <h2 class="card-title text-center mb-2">Biến thể sản phẩm sắp hết hàng</h2>
                            <div style="font-size:13px;color:#888;margin-bottom:8px; text-align:center;">(Các biến thể có tổng tồn kho &lt; 10)</div>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover align-middle mb-0" style="background:#fff; border-radius:8px;">
                                    <thead class="table-warning">
                                        <tr>
                                            <th scope="col">#</th>
                                            <th scope="col">Tên sản phẩm</th>
                                            <th scope="col">Màu</th>
                                            <th scope="col">ROM</th>
                                            <th scope="col">Số lượng tồn kho</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${lowStockVariants}" varStatus="loop">
                                            <tr <c:if test="${item.stock != null && item.stock <= 5}">style="background-color:#fff3cd;font-weight:bold;"</c:if>>
                                                <td>${loop.index + 1}</td>
                                                <td>${item.variant.product.name}</td>
                                                <td>${item.variant.color}</td>
                                                <td>${item.variant.rom}</td>
                                                <td>${item.stock}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <style>
            #revenueChart {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                margin-bottom: 0;
                padding: 8px;
                height: 400px !important;
                max-height: 400px;
                min-height: 400px;
            }
            .card {
                border-radius: 16px;
            }
            .table {
                font-size: 15px;
                border-radius: 12px;
                overflow: hidden;
                margin-bottom: 0;
            }
            .table thead th {
                background: linear-gradient(90deg, #ffe082 0%, #fffde7 100%);
                color: #333;
                font-weight: bold;
                border-top: none;
                border-bottom: 2px solid #ffe082;
                text-align: center;
                font-size: 16px;
            }
            .table tbody td {
                vertical-align: middle;
                padding: 10px 8px;
            }
            .table tbody tr {
                transition: background 0.2s;
            }
            .table tbody tr:hover {
                background: #f1f8e9;
            }
            .table-bordered > :not(caption) > * > * {
                border-color: #e0e0e0;
            }
            .table td:first-child, .table th:first-child {
                border-top-left-radius: 12px;
            }
            .table td:last-child, .table th:last-child {
                border-top-right-radius: 12px;
            }
            .table td, .table th {
                text-align: center;
            }
            .table-warning th {
                background: #ffe082 !important;
            }
            @media (max-width: 991px) {
                .card-title { font-size: 1.2rem; }
                .table { font-size: 14px; }
            }
        </style>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
        <script>
            // Lấy dữ liệu từ JSTL
            var labels = [];
            var data = [];
            <c:forEach var="entry" items="${monthlyRevenue}">
            labels.push("${entry.key}");
            data.push(${entry.value});
            </c:forEach>

            // Tạo màu sắc khác nhau cho từng cột
            var backgroundColors = [
                'rgba(54, 162, 235, 0.7)', // xanh dương
                'rgba(255, 99, 132, 0.7)', // đỏ
                'rgba(255, 206, 86, 0.7)', // vàng
                'rgba(75, 192, 192, 0.7)', // xanh ngọc
                'rgba(153, 102, 255, 0.7)', // tím
                'rgba(255, 159, 64, 0.7)', // cam
                'rgba(40, 167, 69, 0.7)', // xanh lá
                'rgba(255, 99, 71, 0.7)', // đỏ cam
                'rgba(0, 123, 255, 0.7)', // xanh biển
                'rgba(220, 53, 69, 0.7)', // đỏ đậm
                'rgba(23, 162, 184, 0.7)', // xanh cyan
                'rgba(108, 117, 125, 0.7)'  // xám
            ];
            // Lặp lại màu nếu số tháng > số màu
            var barColors = [];
            for (var i = 0; i < labels.length; i++) {
                barColors.push(backgroundColors[i % backgroundColors.length]);
            }

            var ctx = document.getElementById('revenueChart').getContext('2d');
            var revenueChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                            label: 'Doanh thu (VNĐ)',
                            data: data,
                            backgroundColor: barColors,
                            borderColor: barColors.map(c => c.replace('0.7', '1')),
                            borderWidth: 2
                        }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        },
                        title: {
                            display: true,
                            text: 'So sánh doanh thu các tháng',
                            font: {size: 20}
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    // Định dạng số tiền
                                    return context.dataset.label + ': ' + context.parsed.y.toLocaleString('vi-VN') + ' VNĐ';
                                }
                            }
                        },
                        datalabels: {
                            anchor: 'end',
                            align: 'top',
                            formatter: function (value) {
                                return value.toLocaleString('vi-VN');
                            },
                            font: {weight: 'bold'}
                        }
                    },
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: 'Tháng',
                                font: {size: 16}
                            },
                            grid: {display: false}
                        },
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Doanh thu (VNĐ)',
                                font: {size: 16}
                            },
                            ticks: {
                                stepSize: 5000000, // bước nhỏ hơn, điều chỉnh tùy doanh thu
                                callback: function (value) {
                                    return value.toLocaleString('vi-VN');
                                }
                            },
                            suggestedMax: Math.max(...data) * 1.3 // Giới hạn cao hơn 10% so với doanh thu lớn nhất
                        }
                    }
                },
                plugins: [ChartDataLabels]
            });
        </script>
    </body>
</html>
