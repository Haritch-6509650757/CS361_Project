<?php
    include 'connection.php';
    if (!empty($_POST['apiKey'])) {
        $apiKey = $_POST['apiKey'];
        //var_dump($_POST);

        $sql = "DELETE FROM users WHERE apiKey = ?";
        $stmt = $conn->prepare($sql);
        if ($stmt) {
            $stmt->bind_param("s", $apiKey);
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
    } else {
         echo json_encode(["success" => false, "message" => "Missing required parameters."]);
    }

    $conn->close();
?>