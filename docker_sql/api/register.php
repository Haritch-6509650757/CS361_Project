<?php
include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $job = $_POST['job'];

    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    $sql = "INSERT INTO users (username, password, job) VALUES ('$username', '$hashed_password', '$job')";

    if (mysqli_query($conn, $sql)) {
        $last_id = mysqli_insert_id($conn);
        echo json_encode(array("status" => "success", "id" => $last_id, "message" => "User added successfully"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Error adding user"));
    }

    mysqli_close($conn);
}
?>