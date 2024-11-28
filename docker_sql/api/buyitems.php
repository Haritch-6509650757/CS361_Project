<?php
include 'connection.php';

if ($conn->connect_error){
    die("Connection failed: " . $conn->connect_error);
}

// รับค่าจาก HTTP POST Request
$itemName = $_POST['Pname']; // ชื่อสินค้า

if (isset($itemName)) {
    // ตรวจสอบจำนวน stock ปัจจุบัน
    $query = "SELECT Pamount FROM products WHERE Pname = ?";
    $stmt = $conn->prepare($query);
    $stmt->bind_param("s", $itemName);
    $stmt->execute();
    $stmt->bind_result($currentStock);
    $stmt->fetch();
    $stmt->close();

    if ($currentStock > 0) {
        // ลด stock ลง 1
        $newStock = $currentStock - 1;

        // อัปเดต stock ในฐานข้อมูล
        $updateQuery = "UPDATE products SET Pamount = ? WHERE Pname = ?";
        $updateStmt = $conn->prepare($updateQuery);
        $updateStmt->bind_param("is", $newStock, $itemName);

        if ($updateStmt->execute()) {
            echo json_encode(["message" => "Stock updated successfully!", "new_stock" => $newStock]);
        } else {
            echo json_encode(["error" => "Error updating stock: " . $updateStmt->error]);
        }

        $updateStmt->close();
    } else {
        echo json_encode(["error" => "Not enough stock!"]);
    }
} else {
    echo json_encode(["error" => "Invalid input!"]);
}

$conn->close();
?>