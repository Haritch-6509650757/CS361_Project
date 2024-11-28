<?php
include 'connection.php';
if(!empty($_POST['image'] && $_POST['item'] && $_POST['price'] && $_POST['amount'])){
    // ../db_data/uploads/

    $item = $_POST['item'];
    $price = $_POST['price'];
    $amount = $_POST['amount'];

    $path = 'images/'.date("d-m-Y").'-'.time().'-'.rand(10000, 100000) . '.jpg';
    if(file_put_contents($path,
        base64_decode($_POST['image']))){
        $sql = "INSERT into merchant_products (Mitem, Mprice, Mamount, Mimage) values ('".$item."', '".$price."', '".$amount."', '".$path."')";

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
mysqli_close($conn);

?>