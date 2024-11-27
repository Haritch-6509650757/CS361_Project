<?php
    include 'connection.php';
    if (!empty($_POST['username']) && !empty($_POST['visa']) && !empty($_POST['cvv']) && !empty($_POST['profile_image']) && !empty($_POST['apiKey']) && !empty($_POST['email']) && !empty($_POST['phone'])) {
        $username = $_POST['username'];
        $visa = $_POST['visa'];
        $cvv = $_POST['cvv'];
        $profile_image = $_POST['profile_image'];
        $apiKey = $_POST['apiKey'];
        $email = $_POST['email'];
        $phone = $_POST['phone'];

        // แปลง Base64 เป็นไฟล์ภาพ
        $imageName = uniqid() . '.jpg';
        $imagePath = 'images/' . $imageName;

        if (!file_exists('uploads')) {
            mkdir('uploads', 0777, true);
        }
        $decodedImage = base64_decode($profile_image);
        file_put_contents($imagePath, $decodedImage);

        $sql = "UPDATE users SET username=?, visa=?, cvv=?, profile_image=?, email=?, phone=? WHERE apiKey=?";
        $stmt = $conn->prepare($sql);

        if ($stmt) {
            $stmt->bind_param('sssssss', $username, $visa, $cvv, $imageName, $email, $phone, $apiKey);

            if ($stmt->execute()) {
                echo json_encode(['success' => true, 'message' => 'Profile updated successfully', 'image_name' => $imageName, 'email' => $email, 'phone' => $phone]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Failed to update profile']);
            }

            $stmt->close();
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Failed to prepare statement']);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Missing required parameters."]);
    }
?>
