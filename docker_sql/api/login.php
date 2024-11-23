<?php
include 'connection.php';
if(!empty($_POST['username'])&& !empty($_POST['password'])){
    $username = $_POST['username'];
    $password = $_POST['password'];
    // echo json_encode($username);
    // echo json_encode($password);
}
if($conn){
    $result = array();
    $sql = "SELECT * FROM users WHERE username = '$username'"; 
    $res = mysqli_query($conn, $sql);
    if(mysqli_query($conn, $sql)){
        $row = mysqli_fetch_assoc($res);
        // echo json_encode($row);
        if ($username == $row['username'] && password_verify($password, $row['password'])){
            try {
                $apiKey = bin2hex(random_bytes(23));
            } catch (Exception $e){
                $apiKey = bin2hex(uniqid($username, true));
            }
            $sqlUpdate = "UPDATE users SET apiKey = '" . $apiKey . "' WHERE username = '" . $username . "'";     
            if(mysqli_query($conn, $sqlUpdate)){
                 $result = array("status" => "success", "message" => "Login successful", "username" => $row["username"], "apiKey" => $row["apiKey"] , "job" => $row["job"]);
            } else $result = array("status" => "failed", "message" => "Login failed try again");
        } else $result = array("status" => "failed", "message" => "Retry with correct email and password");
    }
    echo json_encode($result);
}
mysqli_close($conn);
?>