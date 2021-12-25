<?php
 if(isset($_POST['maNV'])){
    require_once "connect.php";
    $maNV = $_POST['maNV'];
    $tenNV = $_POST['tenNV'];
    $email = $_POST['email'];
    $soDienThoai = $_POST['soDienThoai'];
    $gioiTinh = $_POST['gioiTinh'];
    $matKhau = $_POST['matKhau'];
    $sqlupdate =  "UPDATE `nhanvien` SET `tenNV` = '$tenNV', `Email` = '$email', `soDienThoai` = '$soDienThoai', `gioiTinh` = '$gioiTinh', `matKhau` = '$matKhau' WHERE `nhanvien`.`maNV` = '$maNV';";
    $execute = $conn -> query($sqlupdate);
    echo "success";  
 }
?>