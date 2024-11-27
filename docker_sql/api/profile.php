<?php
    include 'connection.php';

    if(!empty($_POST['apiKey'])){
        $apiKey = $_POST['apiKey'];
    }
    if($conn){
        $sql = "SELECT * FROM users WHERE apiKey = '$apiKey'"; 
        $res = mysqli_query($conn, $sql);
        $row = mysqli_fetch_assoc($res);
        $result = array("status" => "success","message" => "query success",
            "apiKey" => $apiKey,
            "id" => $row["id"],
            "username" => $row["username"],
            "profile_image" => $row["profile_image"],
            "email" => $row["email"],
            "phone" => $row["phone"]);
    } else {
        $result = array("status" => "failed", "message" => "lost connect to sql");
    }
    echo json_encode($result);
    mysqli_close($conn);
?>