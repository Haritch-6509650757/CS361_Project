<?php
    include 'connection.php';
    // Query ข้อมูลจากตาราง users
    $query = "SELECT id, username FROM users";
    $result = $conn->query($query);

    if ($result->num_rows > 0) {
        $users = [];
        while ($row = $result->fetch_assoc()) {
            $users[] = $row;
        }
        echo json_encode(["success" => true, "data" => $users]);
    } else {
        echo json_encode(["success" => false, "message" => "No users found"]);
    }
    $conn->close();
?>