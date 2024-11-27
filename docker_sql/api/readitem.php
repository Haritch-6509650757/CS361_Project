<?php
include 'connection.php';
if($conn){
    $sql = "select * from images";
    $result = mysqli_query($conn, $sql);
    while ($row = mysqli_fetch_assoc($result)){
        echo "<img src='".$row['path']."'>";
    }
}

?>