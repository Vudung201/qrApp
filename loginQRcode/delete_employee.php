<?php
require "connect.php";
$maNV = $_POST['maNV'];

$sqlupdate = "DELETE FROM `nhanvien` WHERE `nhanvien`.`maNV` = '$maNV';";
if(mysqli_query($conn, $sqlupdate)){
    echo "success";
}else{
    echo "error";
}
?>