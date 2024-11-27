<?php
include 'connection.php'
if($_SERVER['REQUEST_METHOD'] === 'PUT'){
    parse_str(file_get_contents("php://input"), $_PUT);

    $productId = $_PUT['Pid'];
    $reduceAmount = $_PUT['Pamount']
}
?>