<?php
include 'connection.php';

if ($conn) {
    $sql = "SELECT * FROM merchant_products";
    $result = mysqli_query($conn, $sql);

    if ($result) {
        $response = [];
        while ($row = mysqli_fetch_assoc($result)) {
            $response[] = $row;
        }
        echo json_encode($response, JSON_PRETTY_PRINT);
    } else {
        echo "Query error: " . mysqli_error($conn);
    }
} else {
    echo "Connection failed: " . mysqli_connect_error();
}
?>