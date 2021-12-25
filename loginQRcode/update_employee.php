<?php
require "connect.php";
$maNV = $_POST['maNV'];
$tenNV = $_POST['hoTen'];
$gioiTinh = $_POST['gioiTinh'];
$email = $_POST['Email'];
$tenDN = $_POST['tenDN'];
$matKhau = $_POST['mK'];
$soDienThoai = $_POST['soDT'];
$maQuyen = $_POST['maQuyen'];
$sqlupdate =  "UPDATE `nhanvien` SET `tenNV` = '$tenNV', `Email` = '$email', `soDienThoai` = '$soDienThoai', `gioiTinh` = '$gioiTinh', `matKhau` = '$matKhau',`tenDangNhap`='$tenDN',`maquyen`= '$maQuyen' WHERE `nhanvien`.`maNV` = '$maNV';";
if(mysqli_query($conn, $sqlupdate)){
    echo "success";
}else{
    echo "error";
}
?>