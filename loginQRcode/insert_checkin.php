<?php
 if(isset($_POST['maNV'])){
    require_once "connect.php";
    $maNV = $_POST['maNV'];
    $sqlinsert =  "INSERT INTO `quanlyvao` (`id`, `maNV`, `ngayVao`, `giovao`) VALUES (NULL, '$maNV', current_timestamp(), now());";
    $execute = $conn -> query($sqlinsert);
    echo "success";  
 }
?>
