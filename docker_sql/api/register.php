<?php
include 'connection.php';
//ALTER TABLE users AUTO_INCREMENT = <next_id>;
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $job = $_POST['job'];
    $result = array();

    $check_sql = "SELECT id FROM users WHERE username = '$username'";
    $check_result = mysqli_query($conn, $check_sql);

    if (mysqli_num_rows($check_result) > 0) {
        $result = array( "status" => "error", "id"=> "error", "message" => "Username already exists");
    } else {
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        $sql = "INSERT INTO users (username, password, job, apiKey, visa, cvv, profile_image, email, phone) VALUES ('$username', '$hashed_password', '$job', '', '', '', '', '', '')";

        if (mysqli_query($conn, $sql)) {
            $last_id = mysqli_insert_id($conn);
            //echo json_encode(array("status" => "success", "id" => $last_id, "message" => "User added successfully"));
            $result = (array("status" => "success", "id" => $last_id, "message" => "User added successfully"));
        } else {
            //echo json_encode(array("status" => "error", "message" => "Error adding user"));
            $result = (array("status" => "error", "id"=> "error", "message" => "Error adding user"));
        }
    }

    echo json_encode($result);
    mysqli_close($conn);
}
?>