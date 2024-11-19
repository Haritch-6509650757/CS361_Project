<?php

$servername = "db";
$username = "root";
$password = "1234";
$dbname = "users";

$conn = mysqli_connect("db", "root", "1234", "users");

if ($conn) {
    echo "connected";
} else {
    echo "not connected: " . mysqli_connect_error();
}
?>
