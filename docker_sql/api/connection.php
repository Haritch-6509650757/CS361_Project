<?php

$servername = 'db';
$username =  'root';
$password = '1234';
$dbname = 'cs361project';

$conn = mysqli_connect($servername, $username, $password, $dbname);

//mysqli_connect($conn, 'utf8');
// if ($conn) {
//     echo "connected";
// } else {
//     echo "not connected: " . mysqli_connect_error();
// }
?>
