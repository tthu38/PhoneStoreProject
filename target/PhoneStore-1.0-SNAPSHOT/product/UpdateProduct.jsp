<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Update Product</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

    <style>
        :root {
            --primary-color: #e74c3c;
            --primary-light: #ff6b6b;
            --primary-dark: #c0392b;
            --accent-color: #e67e22;
            --accent-dark: #d35400;
            --text-color: #2d3436;
            --light-gray: #f7f9fb;
            --white: #ffffff;
            --border-color: #dfe6e9;
            --shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
            --shadow-hover: 0 10px 20px rgba(0, 0, 0, 0.15);
            --radius: 12px;
            --danger: #e74c3c;
            --danger-hover: #c0392b;
            --cancel-color: #7f8c8d;
            --cancel-dark: #636e72;
            --transition: all 0.3s ease;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--light-gray);
            color: var(--text-color);
            line-height: 1.7;
            padding: 20px;
            margin: 0;
            overflow-x: hidden;
        }

        h2 {
            color: var(--primary-color);
            margin: 40px 0;
            font-weight: 700;
            font-size: 2.2rem;
            text-align: center;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
            position: relative;
            padding-bottom: 15px;
            animation: fadeInDown 0.5s ease;
        }

        h2::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 150px;
            height: 4px;
            background: linear-gradient(to right, var(--primary-light), var(--primary-dark));
            border-radius: 4px;
        }

        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        form {
            background: linear-gradient(135deg, var(--white) 0%, #fff5f5 100%);
            padding: 40px;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            animation: fadeIn 0.6s ease;
        }

        .form-group {
            margin-bottom: 25px;
        }

        label {
            font-weight: 500;
            display: block;
            margin: 10px 0 8px;
            color: var(--primary-dark);
            font-size: 1.1rem;
            display: flex;
            align-items: center;
        }

        label i {
            margin-right: 10px;
            color: var(--primary-color);
            font-size: 1.2rem;
            transition: var(--transition);
        }

        label i:hover {
            color: var(--primary-dark);
            transform: scale(1.1);
        }

        input, textarea {
            width: 100%;
            padding: 14px;
            border: 1px solid var(--border-color);
            border-radius: var(--radius);
            font-size: 1rem;
            font-family: 'Inter', sans-serif;
            background: #fff;
            transition: var(--transition);
        }

        input:focus, textarea:focus {
            outline: none;
            border-color: var(--primary-light);
            box-shadow: 0 0 0 4px rgba(231, 76, 60, 0.2);
            background: #fff9f9;
        }

        textarea {
            min-height: 120px;
            resize: vertical;
        }

        a {
            color: var(--primary-color);
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            margin: 10px 0;
            font-weight: 500;
            transition: var(--transition);
        }

        a i {
            margin-right: 8px;
            font-size: 1.1rem;
        }

        a:hover {
            color: var(--primary-dark);
            text-decoration: underline;
            transform: translateX(3px);
        }

        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin: 20px 0;
            border-radius: var(--radius);
            overflow: hidden;
            box-shadow: var(--shadow);
            background: var(--white);
        }

        th, td {
            padding: 16px;
            text-align: left;
            border: 1px solid var(--border-color);
        }

        th {
            background: linear-gradient(to right, var(--primary-color), var(--primary-dark));
            color: var(--white);
            font-weight: 600;
            font-size: 1.1rem;
        }

        tr:nth-child(even) {
            background: #f9fbfc;
        }

        tr:hover {
            background: #ffe6e6;
            transition: var(--transition);
        }

        .btn, button, input[type="submit"] {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 14px 24px;
            background: linear-gradient(to right, var(--primary-color), var(--primary-light));
            color: var(--white);
            border: none;
            border-radius: var(--radius);
            cursor: pointer;
            font-size: 1rem;
            font-weight: 600;
            font-family: 'Inter', sans-serif;
            transition: var(--transition);
            box-shadow: var(--shadow);
        }

        .btn:hover, button:hover, input[type="submit"]:hover {
            background: linear-gradient(to right, var(--primary-dark), var(--primary-color));
            transform: scale(1.05);
            box-shadow: var(--shadow-hover);
        }

        .btn i, button i, input[type="submit"] i {
            margin-right: 10px;
            font-size: 1.2rem;
        }

        input[type="submit"] {
            background: linear-gradient(to right, var(--accent-color), var(--accent-dark));
            width: 100%;
            margin-top: 30px;
            font-weight: 700;
        }

        input[type="submit"]:hover {
            background: linear-gradient(to right, var(--accent-dark), var(--accent-color));
        }

        button[onclick="removeRow(this)"] {
            background: linear-gradient(to right, var(--danger), var(--danger-hover));
            padding: 10px 14px;
            font-size: 0.9rem;
        }

        button[onclick="removeRow(this)"]:hover {
            background: linear-gradient(to right, var(--danger-hover), var(--danger));
            transform: scale(1.1);
        }

        button[onclick="addRow()"] {
            padding: 12px 20px;
        }

        .image-preview {
            margin-top: 12px;
            display: flex;
            align-items: center;
        }

        .image-preview a {
            margin-left: 12px;
            font-size: 0.95rem;
        }

        .form-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 25px;
            padding-bottom: 20px;
            border-bottom: 2px solid var(--border-color);
        }

        .form-header h3 {
            color: var(--primary-color);
            font-weight: 600;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
        }

        .form-header h3 i {
            margin-right: 10px;
        }

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 1000;
            align-items: center;
            justify-content: center;
        }

        .modal-content {
            background: var(--white);
            padding: 30px;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            max-width: 700px;
            width: 90%;
            max-height: 80vh;
            overflow-y: auto;
            animation: fadeIn 0.3s ease;
        }

        .modal-content h3 {
            color: var(--primary-color);
            font-size: 1.5rem;
            margin-bottom: 20px;
            text-align: center;
        }

        .modal-content table {
            margin: 15px 0;
        }

        .modal-content .btn-group {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .modal-content .btn-confirm {
            background: linear-gradient(to right, var(--primary-color), var(--primary-light));
        }

        .modal-content .btn-confirm:hover {
            background: linear-gradient(to right, var(--primary-dark), var(--primary-color));
        }

        .modal-content .btn-cancel {
            background: linear-gradient(to right, var(--cancel-color), var(--cancel-dark));
        }

        .modal-content .btn-cancel:hover {
            background: linear-gradient(to right, var(--cancel-dark), var(--cancel-color));
        }

        /* Animations */
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            body {
                padding: 10px;
            }

            form {
                padding: 25px 15px;
            }

            h2 {
                font-size: 1.8rem;
            }

            input, textarea, button, .btn, input[type="submit"] {
                font-size: 0.95rem;
                padding: 12px;
            }

            table {
                display: block;
                overflow-x: auto;
                white-space: nowrap;
            }

            th, td {
                min-width: 120px;
            }

            .modal-content {
                width: 95%;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <form id="productForm" action="products" method="post" enctype="multipart/form-data">
        <div class="form-header">
            <h3><i class="fas fa-tag"></i> ${product.name}</h3>
            <a href="${pageContext.request.contextPath}/products" class="btn">
                <i class="fas fa-arrow-left"></i> Back to List
            </a>
        </div>

        <input type="hidden" name="action" value="update" />
        <input type="hidden" name="productId" value="${product.id}" />

        <div class="form-group">
            <label for="name"><i class="fas fa-shopping-bag"></i> Name:</label>
            <input type="text" id="name" name="name" value="${product.name}" required />
        </div>

        <div class="form-group">
            <label for="description"><i class="fas fa-align-left"></i> Description:</label>
            <textarea id="description" name="description" required>${product.description}</textarea>
        </div>

        <div class="form-group">
            <label for="brand"><i class="fas fa-building"></i> Brand:</label>
            <select id="brand" name="brandId" required>
                <c:forEach var="b" items="${brands}">
                    <option value="${b.id}" ${b.id == product.brand.id ? 'selected' : ''}>${b.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="thumbnailImage"><i class="fas fa-image"></i> Thumbnail Image:</label>
            <input type="file" id="thumbnailImage" name="thumbnailImage" />
            <div class="image-preview">
                <a href="${product.thumbnailImage}" target="_blank">
                    <i class="fas fa-external-link-alt"></i> View Image
                </a>
            </div>
        </div>

        <div class="form-group">
            <label for="createAt"><i class="fas fa-calendar-alt"></i> Create Date:</label>
            <input type="datetime-local" id="createAt" name="createAt" value="${formattedCreateAt}" required />
        </div>

        <div class="form-group">
            <label><i class="fas fa-list-alt"></i> ROM, Color, Price & Quantity:</label>
            <table id="variantsTable">
                <tr>
                    <th>ROM</th>
                    <th>Color</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="entry" items="${productVariantEntries}">
                    <tr>
                        <td><input type="text" name="rom[]" value="${entry.key.rom}" required /></td>
                        <td><input type="text" name="color[]" value="${entry.key.color}" required /></td>
                        <td><input type="number" name="prices[]" value="${entry.key.price}" required /></td>
                        <td><input type="number" name="quantities[]" value="${entry.value}" required /></td>
                        <td>
                            <button type="button" onclick="removeRow(this)">
                                <i class="fas fa-trash"></i> Remove
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <a class="btn" href="javascript:void(0)" onclick="addRow()"><i class="fas fa-plus"></i> Add Variant</a>
        </div>

        <input type="submit" value="Update Product" onclick="showConfirmationModal(event)" />
    </form>

    <div id="confirmationModal" class="modal">
        <div class="modal-content">
            <h3>Confirm Product Update</h3>
            <div id="confirmDetails">
                <p><strong>Name:</strong> <span id="confirmName"></span></p>
                <p><strong>Description:</strong> <span id="confirmDescription"></span></p>
                <p><strong>Image URL:</strong> <a id="confirmImageURL" href="#" target="_blank"></a></p>
                <p><strong>Create Date:</strong> <span id="confirmImportDate"></span></p>
                <h4>Variants:</h4>
                <table id="confirmVariantsTable">
                    <tr>
                        <th>ROM</th>
                        <th>Color</th>
                        <th>Price</th>
                        <th>Quantity</th>
                    </tr>
                </table>
            </div>
            <div class="btn-group">
                <button class="btn btn-confirm" onclick="confirmUpdate()"><i class="fas fa-check"></i> Confirm</button>
                <button class="btn btn-cancel" onclick="closeModal()"><i class="fas fa-times"></i> Cancel</button>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        function showConfirmationModal(event) {
            event.preventDefault();

            const form = document.getElementById("productForm");
            const modal = document.getElementById("confirmationModal");

            document.getElementById("confirmName").textContent = form.querySelector("#name").value || "N/A";
            document.getElementById("confirmDescription").textContent = form.querySelector("#description").value || "N/A";

            const imageInput = form.querySelector("#thumbnailImage");
            const imageURL = imageInput && imageInput.files.length > 0
                ? URL.createObjectURL(imageInput.files[0])
                : "${product.thumbnailImage}" || "No image";
            const confirmImageURL = document.getElementById("confirmImageURL");
            confirmImageURL.href = imageURL;
            confirmImageURL.textContent = imageURL;

            document.getElementById("confirmImportDate").textContent = form.querySelector("#createAt").value || "N/A";

            const confirmTable = document.getElementById("confirmVariantsTable");
            while (confirmTable.rows.length > 1) confirmTable.deleteRow(1);

            const variantRows = document.querySelectorAll("#variantsTable tr:not(:first-child)");
            variantRows.forEach((row) => {
                const inputs = row.getElementsByTagName("input");
                if (inputs.length === 4) {
                    const rom = inputs[0].value.trim() || "N/A";
                    const color = inputs[1].value.trim() || "N/A";
                    const price = inputs[2].value.trim() || "0";
                    const quantity = inputs[3].value.trim() || "0";

                    const newRow = confirmTable.insertRow();
                    const cell1 = newRow.insertCell(0);
                    const cell2 = newRow.insertCell(1);
                    const cell3 = newRow.insertCell(2);
                    const cell4 = newRow.insertCell(3);
                    cell1.textContent = rom;
                    cell2.textContent = color;
                    cell3.textContent = price;
                    cell4.textContent = quantity;
                }
            });

            modal.style.display = "flex";
        }

        function confirmUpdate() {
            const form = document.getElementById("productForm");
            form.action = "${pageContext.request.contextPath}/products";
            form.method = "post";
            form.querySelector('input[name="action"]').value = "update";
            form.submit();
        }

        function closeModal() {
            document.getElementById("confirmationModal").style.display = "none";
        }

        function addRow() {
            const table = document.getElementById("variantsTable");
            const newRow = table.insertRow();
            newRow.innerHTML = `
                <td><input type="text" name="rom[]" required /></td>
                <td><input type="text" name="color[]" required /></td>
                <td><input type="number" name="prices[]" required /></td>
                <td><input type="number" name="quantities[]" required /></td>
                <td>
                    <button type="button" onclick="removeRow(this)">
                        <i class="fas fa-trash"></i> Remove
                    </button>
                </td>
            `;
        }

        function removeRow(button) {
            let row = button.parentElement.parentElement;
            let quantityInput = row.querySelector('input[name="quantities[]"]');
            if (quantityInput) {
                row.dataset.originalQuantity = quantityInput.value;
                quantityInput.value = 0;
            }
            row.style.backgroundColor = "#ffe6e6";
            button.innerHTML = '<i class="fas fa-undo"></i> Restore';
            button.onclick = () => restoreRow(button);
        }

        function restoreRow(button) {
            let row = button.parentElement.parentElement;
            let quantityInput = row.querySelector('input[name="quantities[]"]');
            if (quantityInput) {
                quantityInput.value = row.dataset.originalQuantity || "";
            }
            row.style.backgroundColor = "";
            button.innerHTML = '<i class="fas fa-trash"></i> Remove';
            button.onclick = () => removeRow(button);
        }
    </script>
</body>
</html>