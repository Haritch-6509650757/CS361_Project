<?php
    include 'connection.php';

    if (isset($_POST['username'], $_POST['visa'], $_POST['cvv'])) {
        // รับข้อมูลจากคำขอ POST
        $username = $_POST['username'];
        $visa = $_POST['visa'];
        $cvv = $_POST['cvv'];
//         $apikey = bin2hex(random_bytes(23));

//         $sqlUpdate = "UPDATE users SET apiKey = '$apiKey' WHERE 1";
        // คำสั่ง SQL สำหรับอัปเดตข้อมูล
        $sql = "UPDATE users SET username='$username', visa='$visa', cvv='$cvv' WHERE apikey = 'f6aa83e29adffcb2d84a4aa6661e40e1a7eda139d9ec5f'
";

        // ส่งคำสั่ง SQL ไปที่ฐานข้อมูล
        if ($conn->query($sql) === TRUE) {
            echo "Profile updated successfully";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    } else {
        // ถ้าไม่พบพารามิเตอร์ที่จำเป็น
        echo json_encode(["success" => false, "message" => "Missing required parameters."]);
    }
?>