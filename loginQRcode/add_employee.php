<?php
require "connect.php";
$maNV = $_POST['maNV'];
$hoTen = $_POST['hoTen'];
$gioiTinh = $_POST['gioiTinh'];
$Email = $_POST['Email'];
$tenDN = $_POST['tenDN'];
$mK = $_POST['mK'];
$soDT = $_POST['soDT'];
$maQuyen = $_POST['maQuyen'];
$query = "INSERT INTO nhanvien VALUES('$maNV', '$hoTen', '$gioiTinh', '$Email', '$tenDN', '$mK', '$soDT', '$maQuyen')";
if(mysqli_query($conn, $query)){
   // thành công
    echo "success";
}else{
    echo "error";
}
?>