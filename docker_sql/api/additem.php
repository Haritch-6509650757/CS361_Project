<?php
include 'connection.php';
if(!empty($_POST['image'])){
    // ../db_data/uploads/
    $path = 'images/'.date("d-m-Y").'-'.time().'-'.rand(10000, 100000) . '.jpg';
    if(file_put_contents($path,
        base64_decode($_POST['image']))){
        $sql = "insert into images (path) values ('".$path."')";

        if(mysqli_query($conn, $sql)){
            echo 'success';

        } else {
            echo 'Failed to insert database';
        } 
    } else {
        echo 'Failed to upload image';
    }
} else {
    echo 'No image found';
}


?>