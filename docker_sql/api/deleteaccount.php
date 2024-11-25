<?php
    include 'connection.php';
    $id = isset($_POST['id']) ? $_POST['id'] : '';

    //var_dump($_POST);

    $sql = "DELETE FROM users WHERE id = ?";

    $stmt = $conn->prepare($sql);
    if ($stmt) {
        $stmt->bind_param("i", $id); // เดี๋ยวค่อยเปลี่ยนเป็น apikey
        if ($stmt->execute()) {
            echo json_encode([
                "status" => "success",
                "message" => "Account deleted successfully"
            ]);
        } else {
            echo json_encode([
                "status" => "error",
                "message" => "Failed to delete account"
            ]);
        }
        $stmt->close();
    } else {
        echo json_encode([
            "status" => "error",
            "message" => "SQL error: " . $conn->error
        ]);
    }

    $conn->close();
    ?>