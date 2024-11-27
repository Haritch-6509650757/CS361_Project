<?php
include 'connection.php';
if(!empty($_POST['username']) && !empty($_POST['apiKey'])){
    $username = $_POST['username'];
    $apiKey = $_POST['apiKey'];
    if($conn){
        $sql = "SELECT * from users where username = '" . $username . "' and apiKey = '" . $apiKey . "'";
        $res = mysqli_query($conn, $sql);
        if(mysqli_num_rows($res) != 0 ){
            $row = mysqli_fetch_assoc($res);
            $sqlUpdate = "UPDATE users SET apiKey = '' WHERE username = '" . $username . "'";
            if(mysqli_query($conn, $sqlUpdate)){
                echo json_encode(["status" => "success", "message" => "Logged out successfully"]);
            } else echo json_encode("Logout failed");
        } else echo json_encode(["status" => "error", "message" => "Logout failed"]);
    } else echo json_encode(["status" => "error", "message" => "Database connection failed"]); 
} else echo json_encode(["status" => "error", "message" => "All fields are required"]);
?>