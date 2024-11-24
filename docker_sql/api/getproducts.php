/*<?php
include 'connection.php';

if ($conn) {
    $sql = "SELECT * FROM products";
    $result = mysqli_query($conn, $sql);

    if ($result) {
        $response = [];
        while ($row = mysqli_fetch_assoc($result)) {
            if(isset($row['Pimage']) && !empty($row['Pimage'])){
                $row['Pimage'] = base64_encode($row['Pimage']);
            }else{
                $row['Pimage'] = null;
            }
            $response[] = $row;
        }
        echo json_encode($response, JSON_PRETTY_PRINT);
    } else {
        echo "Query error: " . mysqli_error($conn);
    }
} else {
    echo "Connection failed: " . mysqli_connect_error();
}
?>*/