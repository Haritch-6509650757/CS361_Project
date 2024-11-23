<?php
include 'connection.php';
if(!empty($_POST['username'])&& !empty($_POST['password'])){
    $username = $_POST['username'];
    $password = $_POST['password'];
}
if($con){
    $result = array();
    $sql = "select * from users where username = '".$username."'"; 
    $res = mysqli_query($con, $sql);
    if(mysqli_query($con, $sql)){
        $row = mysqli_fetch_assoc($res);
        if ($email == $row['username'] && password_verify($password, $row['password'])){
            try {
                $apiKey = bin2hex(random_bytes(23));
            } catch (Exception $e){
                $apiKey = bin2hex(uniqid($username, true));
            }
            $sqlUpdate = "update users set apiKey = '" . $apiKey . "' where username = '" . $username . "'";     
            if(mysqli_query($con, $sqlUpdate)){
                 $result = array("status" => "success", "message" => "Login successful");
            } else $result = array("status" => "failed", "message" => "Login failed try again")
        } else $result = array array("status" => "failed", "message" => "Retry with correct email and password")
    }
}
cho json_encode($result);
?>